package com.mtgleague.controller;

import com.mtgleague.dto.response.GenericEntityListDTO;
import com.mtgleague.dto.response.PlayerResponseDTO;
import com.mtgleague.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping()
    public ResponseEntity<GenericEntityListDTO<PlayerResponseDTO>> getGeneralRanking() throws Exception {
        GenericEntityListDTO<PlayerResponseDTO> players= new GenericEntityListDTO(rankingService.getLatestGeneralRanking());
        return new ResponseEntity<>(players, HttpStatus.OK);
    }
}
