package com.mtgleague.controller;

import com.mtgleague.dto.request.RoundRequestDTO;
import com.mtgleague.service.RoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rounds")
@RequiredArgsConstructor
public class RoundController {

    private final RoundService roundService;

    @PostMapping("/save")
    public ResponseEntity confirmScore(@RequestBody RoundRequestDTO request){
        roundService.confirmScore(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
