package com.mtgleague.service;

import com.mtgleague.dto.response.PlayerResponseDTO;
import com.mtgleague.logic.PlayerScore;
import com.mtgleague.model.Player;
import com.mtgleague.repo.PlayersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayersService {

    private final PlayersRepository playersRepository;

    public List<Player> findAll() {
        List<Player> results= playersRepository.findAll();
        return results;
    }

    public Player findById(Long id){
        return playersRepository.getReferenceById(id);
    }

    public Optional<Player> findByIdString(String id){
        return playersRepository.getByIdString(id);
    }
}
