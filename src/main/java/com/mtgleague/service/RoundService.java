package com.mtgleague.service;

import com.mtgleague.dto.request.RoundRequestDTO;
import com.mtgleague.model.Round;
import com.mtgleague.repo.RoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RoundService {

    private final RoundRepository roundRepository;

    public Round findById(Long id){
        return roundRepository.getReferenceById(id);
    }

    public void confirmScore(RoundRequestDTO round){
        Round roundToSave= findById(round.getRoundId());

        roundToSave.setP1Wins(round.getP1Wins());
        roundToSave.setP2Wins(round.getP2Wins());

        roundRepository.save(roundToSave);
    }

    public List<Round> getAllPlayerRounds(Long playerId) throws Exception {
        return roundRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new Exception("Rounds not found with playerId: " + playerId));
    }



}
