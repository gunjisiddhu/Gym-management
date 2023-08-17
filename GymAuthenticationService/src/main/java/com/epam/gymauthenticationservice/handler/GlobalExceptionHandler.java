package com.epam.gymauthenticationservice.handler;

import com.epam.gymauthenticationservice.Exception.AuthorizationException;
import com.epam.gymauthenticationservice.model.ErrorResponse;
import com.epam.gymauthenticationservice.model.StringConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse invalidArgumentResponse(MethodArgumentNotValidException e, WebRequest webRequest){
        log.info(StringConstants.ENTERED_EXCEPTION_HANDLER.getValue(), "invalidArgumentResponse","MethodArgumentNotValidException");
        printToLog(e);
        List<String> errorList = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(objectError -> errorList.add(objectError.getDefaultMessage()));
        return new ErrorResponse(LocalTime.now().toString(), errorList.toString(), webRequest.getDescription(false));
    }





    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.OK)
    ErrorResponse authorizationExceptionResponse(AuthorizationException userException, WebRequest webRequest){
        log.info(StringConstants.ENTERED_EXCEPTION_HANDLER.getValue(), "authorizationExceptionResponse","AuthorizationException");
        printToLog(userException);
        return new ErrorResponse(LocalTime.now().toString(), userException.getMessage(),webRequest.getDescription(false));
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse runTimeExceptionResponse(RuntimeException runtimeException, WebRequest webRequest){
        log.info(StringConstants.ENTERED_EXCEPTION_HANDLER.getValue(), "runTimeExceptionResponse","RuntimeException");
        printToLog(runtimeException);
        return new ErrorResponse(LocalTime.now().toString(), "Internal Server Issue, Check if all values are valid and try again", webRequest.getDescription(false));
    }

    private void printToLog(Exception e) {
        log.info(ExceptionUtils.getStackTrace(e));
    }
}
