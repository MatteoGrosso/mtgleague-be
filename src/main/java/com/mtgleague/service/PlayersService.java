package com.mtgleague.service;

import com.mtgleague.dto.request.PlayerRequestDTO;
import com.mtgleague.dto.response.PlayerResponseDTO;
import com.mtgleague.model.Player;
import com.mtgleague.repo.PlayersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayersService {
    @Autowired
    private PlayersRepository playersRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<PlayerResponseDTO> findAll(){
        List<Player> results= playersRepository.findAll();
        List<PlayerResponseDTO> dtos= new ArrayList<>();
        results.forEach(player -> {
            dtos.add(toDto(player));
        });
        return dtos;
    }

    public Player addPlayer(PlayerRequestDTO player){
        return playersRepository.save(toEntity(player));
    }

    private Player toEntity(PlayerRequestDTO playerRequestDTO){
        playerRequestDTO.setPassword(bCryptPasswordEncoder
                .encode(playerRequestDTO.getPassword()));
        return new Player(playerRequestDTO.getName(), playerRequestDTO.getSurname(), playerRequestDTO.getEmail(), playerRequestDTO.getPassword());
    }

    private PlayerResponseDTO toDto(Player entity){
        PlayerResponseDTO dto= new PlayerResponseDTO(entity.getId(), entity.getName(), entity.getSurname(), entity.getEmail(), entity.getEvents(), 0, 0, 0.0);
        return dto;
    }
}
