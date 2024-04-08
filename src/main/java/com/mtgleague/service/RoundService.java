package com.mtgleague.service;

import com.mtgleague.dto.request.RoundRequestDTO;
import com.mtgleague.dto.response.RoundResponseDTO;
import com.mtgleague.model.Event;
import com.mtgleague.model.Round;
import com.mtgleague.repo.RoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RoundService {

    private final RoundRepository roundRepository;

    public Round findById(Long id){
        return roundRepository.getReferenceById(id);
    }

    public Round createRound(Long idPlayer1, String nameP1, String surnameP1, Long idPlayer2, String nameP2, String surnameP2, int p1wins, int p2Wins, Event event, int turn, boolean ended){
        return roundRepository.save(
                Round.builder()
                        .idP1(idPlayer1)
                        .nameP1(nameP1)
                        .surnameP1(surnameP1)
                        .idP2(idPlayer2)
                        .nameP2(nameP2)
                        .surnameP2(surnameP2)
                        .p1Wins(p1wins)
                        .p2Wins(p2Wins)
                        .event(event)
                        .turn(turn)
                        .ended(ended)
                .build()
        );
    }

    public Long confirmScore(RoundRequestDTO round){
        Round roundToSave= findById(round.getRoundId());

        //only the players playing this match can save the score
        if((round.getPlayerId()==roundToSave.getIdP1() || round.getPlayerId()==roundToSave.getIdP2()) && !roundToSave.isEnded()){
            roundToSave.setP1Wins(round.getP1Wins());
            roundToSave.setP2Wins(round.getP2Wins());
            roundToSave.setEnded(true);

            return roundRepository.save(roundToSave).getEvent().getId();
        }
        return null;
    }

    public boolean isRoundEnded(Long eventId){
        List<Round> currentRounds= roundRepository.findCurrentByEventId(eventId);
        return currentRounds.size()==0;
    }

    public Round getCurrentRound(Long playerId){
        Optional<Round> currentRound= roundRepository.findCurrentByPlayerId(playerId);
        return currentRound.get();
    }

    public List<Round> getAllPlayerRounds(Long playerId){
        return roundRepository.findByPlayerId(playerId);
    }

}
