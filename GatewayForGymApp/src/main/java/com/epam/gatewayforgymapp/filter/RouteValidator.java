package com.epam.gatewayforgymapp.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/eureka",
            "/gym/trainer/register",
            "/gym/trainee/register",
            "/auth/login"
    );


    public static final List<String> loginEndPoint = List.of(
            "/auth/login"
    );

    public final Predicate<ServerHttpRequest> isLogin =
            request -> loginEndPoint
                    .stream()
                    .anyMatch(uri -> request.getURI().getPath().contains(uri));

    public final Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}