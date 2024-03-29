package com.mtgleague.controller;

import com.mtgleague.dto.auth.request.LoginRequest;
import com.mtgleague.dto.auth.request.SignupRequest;
import com.mtgleague.dto.auth.response.AuthenticationResponse;
import com.mtgleague.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup (@RequestBody SignupRequest request){
        return ResponseEntity.ok(service.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (@RequestBody LoginRequest request){
        return ResponseEntity.ok(service.login(request));
    }

}
