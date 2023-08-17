package com.epam.gatewayforgymapp.filter;

import com.epam.gatewayforgymapp.dto.AuthRequest;
import com.epam.gatewayforgymapp.exception.UnauthorizedException;
import com.epam.gatewayforgymapp.proxy.WebFluxAuthenticationProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final WebFluxAuthenticationProxy authenticationProxy;

    public AuthenticationFilter(RouteValidator validator,  WebFluxAuthenticationProxy authenticationProxy) {
        super(Config.class);
        this.validator = validator;
        this.authenticationProxy = authenticationProxy;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info("Received request " + exchange.getRequest().toString());

            /*if (validator.isLogin.test(exchange.getRequest())) {
                log.info("Received login request");

                return exchange.getRequest().getBody()
                        .collect(Collectors.toMap(key -> "body", DataBuffer::asInputStream))
                        .flatMap(bodyMap -> {
                            AuthRequest authRequest = readRequestBody(bodyMap.get("body"));
                            return getLoginToken(authRequest)
                                    .flatMap(loginToken -> {
                                        return chain.filter(exchange);
                                        //write code here
                                    });
                        });
            } else */if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new UnauthorizedException("Missing authorization header");
                }

                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                log.info("Received token : " + authHeader);

                return validateToken(authHeader)
                        .flatMap(valid -> {
                            if (valid) {
                                log.info("Valid token received");
                                return chain.filter(exchange);
                            } else {
                                log.info("Token validation failed");
                                ServerHttpResponse response = exchange.getResponse();
                                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                                return response.setComplete();
                            }
                        });
            }

            return chain.filter(exchange);
        };
    }

    private Mono<String> getLoginToken(AuthRequest authRequest) {
        log.info("Entered getLoginToken Method");
        return authenticationProxy.getToken(authRequest)
                .doOnNext(token -> log.info("Token Generated : " + token));
    }

    private Mono<Boolean> validateToken(String token) {
        log.info("Validating token : " + token);
        return authenticationProxy.validateToken(token)
                .map(response -> response.equals("Token is valid")); // Adapt this based on your authentication response
    }

    private AuthRequest readRequestBody(InputStream inputStream) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputStream, AuthRequest.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read request body", e);
        }
    }


    public static class Config {
        // Empty class for configuration if needed
    }
}
