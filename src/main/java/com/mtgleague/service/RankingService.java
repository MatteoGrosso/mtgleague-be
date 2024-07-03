package com.mtgleague.service;

import com.mtgleague.dto.response.PlayerResponseDTO;
import com.mtgleague.model.Ranking;
import com.mtgleague.repo.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;

    public void saveRankings(Ranking ranking){

        rankingRepository.save(ranking);
    }

    public List<PlayerResponseDTO> getEventRank(Long eventId){
        List<Ranking> rankings = rankingRepository.findByEventId(eventId);
        return rankings.isEmpty() ? null : rankings.get(0).getPlayers();
    }

    public List<PlayerResponseDTO> getLatestGeneralRanking(){
        List<Ranking> latestGeneralRanking = rankingRepository.getLatestGeneralRanking();
        return latestGeneralRanking.isEmpty() ? null : latestGeneralRanking.get(0).getPlayers();

    }

    public void invalidateRankings(){
        //called right before creating new rankings

        rankingRepository.updateIsValidField();
    }

}
