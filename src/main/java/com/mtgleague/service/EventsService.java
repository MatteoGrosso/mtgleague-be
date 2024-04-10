package com.mtgleague.service;

import com.mtgleague.dto.request.EventRequestDTO;
import com.mtgleague.dto.response.EventPlayerResponseDTO;
import com.mtgleague.dto.response.EventResponseDTO;
import com.mtgleague.logic.Pairing;
import com.mtgleague.logic.PlayerScore;
import com.mtgleague.model.Event;
import com.mtgleague.model.Player;
import com.mtgleague.model.Round;
import com.mtgleague.repo.EventsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventsService {

    private final EventsRepository eventsRepository;

    private final RoundService roundService;

    private final Pairing pairing;

    public List<EventResponseDTO> findAll(){
        List<Event> results= eventsRepository.findAll();
        List<EventResponseDTO> dtos= new ArrayList<>();
        results.forEach(event -> {
            dtos.add(toDto(event));
        });
        return dtos;
    }

    public Event addEvent(EventRequestDTO event){
        return eventsRepository.save(toEntity(event));
    }

    public Event findById(Long id){
        return eventsRepository.getReferenceById(id);
    }

    public EventResponseDTO findByIdDTO(Long id){
        return toDto(findById(id));
    }

    private Event toEntity(EventRequestDTO eventRequestDTO){
        return new Event(eventRequestDTO.getName(), eventRequestDTO.getDate(), eventRequestDTO.getCap(), eventRequestDTO.getDescription());
    }

    private EventResponseDTO toDto(Event entity){
        return new EventResponseDTO(entity.getId(), entity.getName(), entity.getDate(), entity.getCap(), entity.getDescription(), entity.isStarted(), entity.getPlayers());
    }

    public Event registerPlayer(Long eventId, Player playerToSubscribe) {

        Event selectedEvent= findById(eventId);
        Set<Player> playersSubscribed= selectedEvent.getPlayers();

        if(playersSubscribed.size()<selectedEvent.getCap() && !selectedEvent.isStarted()){
            playersSubscribed.add(playerToSubscribe);
            selectedEvent.setPlayers(playersSubscribed);
            return eventsRepository.save(selectedEvent);
        }
        return null;
    }

    public Event unsubscribePlayer(Long eventId, Player playerToUnsubscribe) {

        Event selectedEvent= findById(eventId);

        if(!selectedEvent.isStarted()){
            Set<Player> playersSubscribed= selectedEvent.getPlayers();

            Set<Player> filteredPlayers = playersSubscribed.stream()
                    .filter(player -> player.getId() != playerToUnsubscribe.getId())
                    .collect(Collectors.toSet());

            selectedEvent.setPlayers(filteredPlayers);
        }

        return eventsRepository.save(selectedEvent);
    }

    public void calculatePairings(Long eventId) throws Exception {

        Event event= findById(eventId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if(dateFormat.format(event.getDate()).equals(dateFormat.format(new Date()))){
            if(event.getMaxTurn()==0){
                event.setMaxTurn(getMaxTurn(event));
            }
            event.setCurrentTurn(event.getCurrentTurn()+1);
            calculatePairings(event);
            event.setStarted(true); //redundant after the first start, but I'll leave it as is as it is faster than a condition and I have to save the entity anyway
            eventsRepository.save(event);
        }
    }

    private int getMaxTurn(Event event){
        int numberOfParticipants= event.getPlayers().size();
        return (int) Math.ceil(Math.log(numberOfParticipants) / Math.log(2));
    }

    private void calculatePairings(Event event) throws Exception {
        if(event.getCurrentTurn()<=event.getMaxTurn()){
            boolean eventStarted= event.isStarted();
            List<Player> players = new ArrayList<>(event.getPlayers());
            Collections.shuffle(players);

            List<PlayerScore> activePlayers = calculatePlayersScores(eventStarted, players, event.getId());
            if(eventStarted){
                // Sort the list of active players based on the actual score + the 3-step rules to break a tie
                pairing.quickSort(activePlayers, 0, activePlayers.size() - 1);
            }
            pairing.doPairings(activePlayers, event, event.getCurrentTurn());
        }
    }

    //public because I'm using it in PlayersService for the rankList
    public List<PlayerScore> calculatePlayersScores(boolean started, List<Player> players, Long eventId) throws Exception{

        List<PlayerScore> playersScores= new ArrayList<>();

        List<Player> playersShuffled = new ArrayList<>(players);
        Collections.shuffle(playersShuffled);
        if(started){
            playersShuffled.forEach(
                    player -> playersScores.add(
                            calculatePastRounds(
                                    PlayerScore
                                            .builder()
                                            .id(player.getId())
                                            .name(player.getName()) //im using the name/surname for the "current round" in the fe page. doing that here will save memory
                                            .surname(player.getSurname())
                                            .build()
                                    ,eventId
                            )
                    )
            );
        }else{
            playersShuffled.forEach(
                    player -> playersScores.add(
                            PlayerScore
                                    .builder()
                                    .id(player.getId())
                                    .name(player.getName()) //im using these in the name/surname for the current round in the fe page. doing that here will save memory
                                    .surname(player.getSurname())
                                    .build()
                    )
            );
        }
        return playersScores;
    }

    private PlayerScore calculatePastRounds(PlayerScore playerScore, Long eventId){
        List<Round> playerRounds;
        if(eventId != null){
            playerRounds= roundService.getAllEventPlayerRounds(playerScore.getId(), eventId);
        }else{
            playerRounds= roundService.getAllPlayerRounds(playerScore.getId());
        }

        Set<Long> opponents = new HashSet<>();

        playerRounds.forEach(
                round -> {
                    if(!round.isBye()){
                        playerScore.setMatchPlayedWithoutBye(playerScore.getMatchPlayedWithoutBye()+1);
                    }
                    playerScore.setMatchPlayed(playerScore.getMatchPlayed()+1);
                    if(round.getIdP1().equals(playerScore.getId())){
                        playerScore.setGameWin(playerScore.getGameWin()+round.getP1Wins());
                        playerScore.setGamePlayed(playerScore.getGamePlayed()+round.getP2Wins()+round.getP1Wins());
                        if(!round.isBye()){
                            playerScore.setGameWinWithoutBye(playerScore.getGameWinWithoutBye()+round.getP1Wins());
                            playerScore.setGamePlayedWithoutBye(playerScore.getGamePlayedWithoutBye()+round.getP2Wins()+round.getP1Wins());
                        }
                        if(round.getP1Wins() > round.getP2Wins()){
                            if(!round.isBye()){
                                playerScore.setMatchWinWithoutBye(playerScore.getMatchWinWithoutBye()+1);
                            }
                            playerScore.setMatchWin(playerScore.getMatchWin()+1);
                        } else if(round.getP1Wins() == round.getP2Wins()){
                            playerScore.setMatchDraw(playerScore.getMatchDraw()+1);
                        }
                        if(!round.isBye()){
                            opponents.add(round.getIdP2());
                        }
                    } else {
                        playerScore.setGameWin(playerScore.getGameWin()+round.getP2Wins());
                        playerScore.setGamePlayed(playerScore.getGamePlayed()+round.getP2Wins()+round.getP1Wins());
                        if(round.getP2Wins() > round.getP1Wins()){
                            playerScore.setMatchWin(playerScore.getMatchWin()+1);
                        }else if(round.getP1Wins() == round.getP2Wins()){
                            playerScore.setMatchDraw(playerScore.getMatchDraw()+1);
                        }
                        opponents.add(round.getIdP1());
                    }
                }
        );
        playerScore.setOpponentsIds(opponents);
        return playerScore;
    }

    public List<EventPlayerResponseDTO> findEventRanks(Long eventId) throws Exception {
        List<Player> players= eventsRepository.getReferenceById(eventId).getPlayers().stream().toList();
        List<PlayerScore> playerScores= calculatePlayersScores(true, players, eventId);
        pairing.quickSort(playerScores, 0, playerScores.size() - 1);

        List<EventPlayerResponseDTO> result= new ArrayList<>();
        playerScores.forEach(
                player -> {
                    player.setEventsPlayed(
                            players.stream().filter(
                                    r -> r.getId()==player.getId()
                            ).collect(Collectors.toList()).get(0).getEvents().size());
                    result.add(toDto(player));
                }
        );
        return result;
    }

    private EventPlayerResponseDTO toDto(PlayerScore playerScore){
        return EventPlayerResponseDTO.builder()
                .name(playerScore.getName())
                .surname(playerScore.getSurname())
                .score(playerScore.getScore())
                .omw(playerScore.getOmw())
                .gw(playerScore.getGw())
                .ogw(playerScore.getOgw())
                .build();
    }
}
