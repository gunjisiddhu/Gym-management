package com.epam.gymauthenticationservice.controller;

import com.epam.gymauthenticationservice.Exception.AuthorizationException;
import com.epam.gymauthenticationservice.model.AuthRequest;
import com.epam.gymauthenticationservice.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService service;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String getToken(@RequestBody AuthRequest authRequest) throws AuthorizationException {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return service.generateToken(authRequest.getUsername());
        } else {
            throw new AuthorizationException("invalid access");
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        log.info("Entered validate token method at "+ LocalDateTime.now());
        try {
            service.validateToken(token);
        } catch ( ExpiredJwtException e ) {
            log.info("Invalid token received, Exiting validateToken method");
            return "token invalid";
        }
        log.info("Exiting validate token method");
        return "Token is valid";
    }
}