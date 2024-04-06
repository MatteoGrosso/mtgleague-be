package com.mtgleague.controller;

import com.mtgleague.dto.request.PlayerRequestDTO;
import com.mtgleague.dto.response.GenericEntityListDTO;
import com.mtgleague.dto.response.PlayerResponseDTO;
import com.mtgleague.model.Player;
import com.mtgleague.service.PlayersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayersController {

    private final PlayersService playersService;

    @GetMapping()
    public ResponseEntity<GenericEntityListDTO<PlayerResponseDTO>> getPlayersRanked(){
        GenericEntityListDTO<PlayerResponseDTO> players= new GenericEntityListDTO(playersService.findAll());
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @PostMapping("/role")
    public ResponseEntity<PlayerResponseDTO> getPlayerRole(@RequestBody PlayerRequestDTO playerRequestDTO) {
        Player player = playersService.findById(playerRequestDTO.getPlayerId());
        PlayerResponseDTO response= PlayerResponseDTO.builder().role(player.getRole().name()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
