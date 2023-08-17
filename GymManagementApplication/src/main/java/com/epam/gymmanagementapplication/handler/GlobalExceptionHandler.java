package com.epam.gymmanagementapplication.handler;


import com.epam.gymmanagementapplication.dto.response.ErrorResponse;
import com.epam.gymmanagementapplication.exception.*;
import com.epam.gymmanagementapplication.util.StringConstants;
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


    @ExceptionHandler(TraineeException.class)
    @ResponseStatus(HttpStatus.OK)
    ErrorResponse traineeExceptionResponse(TraineeException traineeException, WebRequest webRequest){
        log.info(StringConstants.ENTERED_EXCEPTION_HANDLER.getValue(), "traineeExceptionResponse","TraineeException");
        printToLog(traineeException);
        return new ErrorResponse(LocalTime.now().toString(), traineeException.getMessage(),webRequest.getDescription(false));
    }

    @ExceptionHandler(TrainerException.class)
    @ResponseStatus(HttpStatus.OK)
    ErrorResponse trainerExceptionResponse(TrainerException trainerException, WebRequest webRequest){
        log.info(StringConstants.ENTERED_EXCEPTION_HANDLER.getValue(), "trainerExceptionResponse","TrainerException");
        printToLog(trainerException);
        return new ErrorResponse(LocalTime.now().toString(), trainerException.getMessage(),webRequest.getDescription(false));
    }

    @ExceptionHandler(TrainingException.class)
    @ResponseStatus(HttpStatus.OK)
    ErrorResponse trainingExceptionResponse(TrainingException trainingException, WebRequest webRequest){
        log.info(StringConstants.ENTERED_EXCEPTION_HANDLER.getValue(), "trainingExceptionResponse","TrainingException");
        printToLog(trainingException);
        return new ErrorResponse(LocalTime.now().toString(), trainingException.getMessage(),webRequest.getDescription(false));
    }

    @ExceptionHandler(TrainingTypeException.class)
    @ResponseStatus(HttpStatus.OK)
    ErrorResponse trainingTypeExceptionResponse(TrainingTypeException trainingTypeException, WebRequest webRequest){
        log.info(StringConstants.ENTERED_EXCEPTION_HANDLER.getValue(), "trainingTypeExceptionResponse","TrainingTypeException");
        printToLog(trainingTypeException);
        return new ErrorResponse(LocalTime.now().toString(), trainingTypeException.getMessage(),webRequest.getDescription(false));
    }

    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.OK)
    ErrorResponse userExceptionResponse(UserException userException, WebRequest webRequest){
        log.info(StringConstants.ENTERED_EXCEPTION_HANDLER.getValue(), "userExceptionResponse","UserException");
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
