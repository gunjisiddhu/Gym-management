package com.epam.gatewayforgymapp.proxy;

import com.epam.gatewayforgymapp.dto.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class WebFluxAuthenticationProxy  {


    private WebClient.Builder webClientBuilder;

    @Autowired
    public WebFluxAuthenticationProxy(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }
    public Mono<String> getToken(AuthRequest authRequest) {
        return webClientBuilder
                .build()
                .post() // Use POST method
                .uri("http://gym-authentication-service/auth/login")
                .contentType(MediaType.APPLICATION_JSON) // Set content type
                .bodyValue(authRequest) // Add your request body object
                .retrieve()
                .bodyToMono(String.class)
                .subscribeOn(Schedulers.boundedElastic());
    }


    public Mono<String> validateToken(String token) {
        return webClientBuilder.build().get()
                .uri("http://gym-authentication-service/auth/validate?token=" + token)
                .retrieve()
                .bodyToMono(String.class)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
