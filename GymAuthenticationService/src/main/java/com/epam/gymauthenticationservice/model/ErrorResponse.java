package com.epam.gymauthenticationservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private String time;
    private String errorMessage;
    private String path;
}
