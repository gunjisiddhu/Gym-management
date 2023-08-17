package com.epam.gymauthenticationservice.service;

import com.epam.gymauthenticationservice.model.StringConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JWTService jwtService;

    public String generateToken(String username) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(),"generateToken", this.getClass().getName(), username);
        String token =  jwtService.generateToken(username);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(),"generateToken", this.getClass().getName());
        return token;
    }

    public void validateToken(String token) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(),"validateToken", this.getClass().getName(), token);
        jwtService.validateToken(token);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(),"validateToken", this.getClass().getName());
    }
}
