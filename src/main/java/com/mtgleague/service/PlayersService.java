package com.mtgleague.service;

import com.mtgleague.dto.response.PlayerResponseDTO;
import com.mtgleague.logic.PlayerScore;
import com.mtgleague.model.Player;
import com.mtgleague.repo.PlayersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayersService {

    private final PlayersRepository playersRepository;
    private final EventsService eventsService;

    public List<PlayerResponseDTO> findAll() throws Exception {
        List<Player> results= playersRepository.findAll();
        List<PlayerScore> playerScores= eventsService.calculatePlayersScores(true, results);
        Collections.sort(playerScores, Comparator.comparingInt(PlayerScore::getScore));

        List<PlayerResponseDTO> result= new ArrayList<>();
        playerScores.forEach(
                player -> {
                    player.setEventsPlayed(results.stream().filter(r -> r.getId()==player.getId()).collect(Collectors.toList()).get(0).getEvents().size());
                    result.add(toDto(player));
                }
        );
        return result;
    }

    public Player findById(Long id){
        return playersRepository.getReferenceById(id);
    }

    public Optional<Player> findByIdString(String id){
        return playersRepository.getByIdString(id);
    }

    private PlayerResponseDTO toDto(PlayerScore entity){
        PlayerResponseDTO dto= PlayerResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .surname(entity.getSurname())
                .matchWinRate(entity.getMatchWinRate())
                .score(entity.getScore())
                .eventsPlayed(entity.getEventsPlayed())
                .build();
        return dto;
    }
}
