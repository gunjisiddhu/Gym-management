package com.epam.gymmanagementapplication.controller;

import com.epam.gymmanagementapplication.dto.request.ModifyPassword;
import com.epam.gymmanagementapplication.dto.response.Credential;
import com.epam.gymmanagementapplication.exception.UserException;
import com.epam.gymmanagementapplication.service.UserService;
import com.epam.gymmanagementapplication.util.StringConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gym/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private static final String LOGIN_USER = "loginUser";
    private static final String MODIFY_PASSWORD = "changePassword";
    private final UserService userService;
    @PostMapping("/login")
    ResponseEntity<Boolean> loginUser(@RequestBody @Valid Credential credential) throws UserException {
        log.info(StringConstants.ENTERED_CONTROLLER_MESSAGE.getValue(), LOGIN_USER, this.getClass());
        boolean isValid = userService.loginUser(credential);
        ResponseEntity<Boolean> response = new ResponseEntity<>(isValid, HttpStatus.ACCEPTED);
        log.info(StringConstants.EXITING_CONTROLLER_MESSAGE.getValue(), LOGIN_USER, this.getClass());
        return response;
    }
    @PutMapping("/modifyPassword")
    ResponseEntity<Boolean> changePassword(@RequestBody @Valid ModifyPassword modifyPassword) throws UserException {
        log.info(StringConstants.ENTERED_CONTROLLER_MESSAGE.getValue(), MODIFY_PASSWORD, this.getClass());
        boolean isValid = userService.changeLogin(modifyPassword);
        ResponseEntity<Boolean> response = new ResponseEntity<>(isValid, HttpStatus.ACCEPTED);
        log.info(StringConstants.EXITING_CONTROLLER_MESSAGE.getValue(), MODIFY_PASSWORD, this.getClass());
        return response;
    }
}
