package com.mtgleague.logic;

import com.mtgleague.model.Round;
import com.mtgleague.service.EventsService;
import com.mtgleague.service.RoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Component
public class Pairing {

    private final EventsService eventsService;
    private final RoundService roundService;

    private void calculatePairings(Long eventId) throws Exception {
        List<PlayerScore> activePlayers = calculatePlayersScores(eventId);

        // Sort the list of active players based on the actual score + the 3-step rules to break a tie
        quickSort(activePlayers, 0, activePlayers.size() - 1);

        doPairings(activePlayers);
    }

    // QuickSort algorithm implementation
    private void quickSort(List<PlayerScore> players, int low, int high) {
        if (low < high) {
            int pi = partition(players, low, high);
            quickSort(players, low, pi - 1);
            quickSort(players, pi + 1, high);
        }
    }

    /*
    this partition follows the rules magic the gathering use to do the pairings and the 3-step rules to break a tie:
        1- players points;
        2- omw (value that refers to the winRate (of the matches) of the opponents / the number of opponents)
        3- gw (value that refers to the winRate (of the single games) of the player)
        4- ogw (value that refers to the winRate (of the single games) of the opponents / the number of opponents)

    !!!Be careful: magic the gathering uses this rules "when calculating omw and ogw, the single winRates are set to 33 if they are below"!!!
     */
    private int partition(List<PlayerScore> players, int low, int high) {
        PlayerScore pivot = players.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            PlayerScore player = players.get(j);
            if (player.getScore() < pivot.getScore()) {
                i++;
                swap(players, i, j);
            }else if(player.getScore() == pivot.getScore()){
                int omwP= omwCalc(player, players);
                int omwPivot= omwCalc(pivot, players);
                if (omwP < omwPivot) {
                    i++;
                    swap(players, i, j);
                }else if(omwP == omwPivot){
                    int gwP= gwCalc(player);
                    int gwPivot= gwCalc(pivot);
                    if (gwP < gwPivot) {
                        i++;
                        swap(players, i, j);
                    }else if(gwP == gwPivot){
                        int ogwP= ogwCalc(player, players);
                        int ogwPivot= ogwCalc(pivot, players);
                        if (ogwP < ogwPivot) {
                            i++;
                            swap(players, i, j);
                        }
                    }
                }
            }
        }
        swap(players, i + 1, high);
        return i + 1;
    }

    private void swap(List<PlayerScore> players, int i, int j) {
        PlayerScore temp = players.get(i);
        players.set(i, players.get(j));
        players.set(j, temp);
    }

    //END QuickSort algorithm implementation

    //omw is the value that refers to the winRate (of the matches) of the opponents / the number of opponents
    //!!!Be careful: magic the gathering uses this rules "when calculating omw and ogw, the single winRates are set to 33 if they are below"!!!
    private int omwCalc(PlayerScore player, List<PlayerScore> players){
        AtomicInteger omwPlayer= new AtomicInteger();
        player.getOpponentsIds().forEach(
                opponentId -> {
                    Optional<PlayerScore> ps= players.stream().filter(
                            p -> p.getId() == opponentId
                    ).findFirst();
                    if(ps.isPresent()){
                        omwPlayer.addAndGet( ps.get().getFixedMatchWinRateWithoutBye());
                    }
                }
        );
        return omwPlayer.get()/player.getOpponentsIds().toArray().length;
    }

    //omw is the value that refers to the winRate (of the single games) of the player
    private int gwCalc(PlayerScore player){
        return player.getGameWinRateWithoutBye();
    }

    //omw is the value that refers to the winRate (of the single games) of the opponents / the number of opponents
    //!!!Be careful: magic the gathering uses this rules "when calculating omw and ogw, the single winRates are set to 33 if they are below"!!!
    private int ogwCalc(PlayerScore player, List<PlayerScore> players){
        AtomicInteger ogwPlayer= new AtomicInteger();
        player.getOpponentsIds().forEach(
                opponentId -> {
                    Optional<PlayerScore> ps= players.stream().filter(
                            p -> p.getId() == opponentId
                    ).findFirst();
                    if(ps.isPresent()){
                        ogwPlayer.addAndGet(ps.get().getFixedGameWinRateWithoutBye());
                    }
                }
        );
        return ogwPlayer.get()/player.getOpponentsIds().toArray().length;
    }

    private void doPairings(List<PlayerScore> players){
        Boolean byeIsNeeded= players.size()%2 == 1;

        for(int index=0; index < players.size()-1; index++){
            roundService.createRound(players.get(index).getId(), players.get(++index).getId());
        }
        if(byeIsNeeded){
            roundService.createRound(players.get(players.size()-1).getId(), null);
        }
    }

    private List<PlayerScore> calculatePlayersScores(Long eventId) throws Exception{

        List<PlayerScore> playersScores= new ArrayList<>();

        eventsService.findAllPlayersPlaying(eventId).forEach(
                player -> {
                    try {
                        playersScores.add(
                                calculatePastRounds(
                                        PlayerScore
                                                .builder()
                                                .id(player.getId())
                                                .build()
                                )
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        return playersScores;
    }

    private PlayerScore calculatePastRounds(PlayerScore playerScore){

        List<Round> playerRounds=  roundService.getAllPlayerRounds(playerScore.getId());
        Set<Long> opponents= playerScore.getOpponentsIds();

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
                        opponents.add(round.getIdP2());
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

}