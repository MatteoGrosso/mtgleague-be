package com.mtgleague.controller;

import com.mtgleague.dto.request.PlayerRequestDTO;
import com.mtgleague.dto.response.GenericEntityListDTO;
import com.mtgleague.dto.response.PlayerResponseDTO;
import com.mtgleague.model.Player;
import com.mtgleague.service.PlayersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayersController {

    private final PlayersService playersService;

    @GetMapping()
    public ResponseEntity<GenericEntityListDTO<PlayerResponseDTO>> getPlayers(){
        GenericEntityListDTO<PlayerResponseDTO> players= new GenericEntityListDTO(playersService.findAll());
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<Player> createPlayer(@RequestBody PlayerRequestDTO playerRequestDTO){
        Player newPlayer= playersService.addPlayer(playerRequestDTO);
        return new ResponseEntity<>(newPlayer, HttpStatus.CREATED);
    }

}
