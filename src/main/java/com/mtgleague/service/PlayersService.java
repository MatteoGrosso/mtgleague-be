package com.mtgleague.service;

import com.mtgleague.dto.response.PlayerResponseDTO;
import com.mtgleague.model.Event;
import com.mtgleague.model.Player;
import com.mtgleague.repo.PlayersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayersService {

    private final PlayersRepository playersRepository;

    public List<PlayerResponseDTO> findAll(){
        List<Player> results= playersRepository.findAll();
        List<PlayerResponseDTO> dtos= new ArrayList<>();
        results.forEach(player -> {
            dtos.add(toDto(player));
        });
        return dtos;
    }

    public Player findById(Long id){
        return playersRepository.getReferenceById(id);
    }

    private PlayerResponseDTO toDto(Player entity){
        PlayerResponseDTO dto= new PlayerResponseDTO(entity.getId(), entity.getName(), entity.getSurname(), entity.getEmail(), entity.getEvents(), entity.getScore(), entity.getWinRate(), entity.getRole().name());
        return dto;
    }
}
