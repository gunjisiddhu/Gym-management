package com.epam.gymauthenticationservice.service;

import com.epam.gymauthenticationservice.model.StringConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JWTService {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";


    public void validateToken(final String token) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(),"validateToken", this.getClass().getName(), token);
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(),"validateToken", this.getClass().getName());
    }


    public String generateToken(String userName) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(),"generateToken", this.getClass().getName());
        Map<String, Object> claims = new HashMap<>();
        String token =  createToken(claims, userName);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(),"generateToken", this.getClass().getName());
        return token;
    }

    private String createToken(Map<String, Object> claims, String userName) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(),"createToken", this.getClass().getName());
        String token =  Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(),"createToken", this.getClass().getName());
        return token;
    }

    private Key getSignKey() {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(),"getSignKey", this.getClass().getName());
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        Key key =  Keys.hmacShaKeyFor(keyBytes);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(),"getSignKey", this.getClass().getName());
        return key;
    }
}
