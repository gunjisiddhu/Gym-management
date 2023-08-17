package com.epam.gymmanagementapplication.service;

import com.epam.gymmanagementapplication.dto.UserDTO;
import com.epam.gymmanagementapplication.dto.request.ModifyPassword;
import com.epam.gymmanagementapplication.dto.response.Credential;
import com.epam.gymmanagementapplication.exception.UserException;
import com.epam.gymmanagementapplication.model.User;
import jakarta.transaction.Transactional;

public interface UserService {
    String SAVE_USER = "saveUser";
    String LOGIN = "loginUser";
    String CHANGE_LOGIN = "changeLogin";
    String GET = "getUser";
    String REMOVE = "removeUser";

    User saveUser(UserDTO userDTO) throws UserException;
    boolean loginUser(Credential credential) throws UserException;
    @Transactional
    boolean changeLogin(ModifyPassword modifyPassword) throws UserException;
    User getUser(String username) throws UserException;
    void removeUser(User user);
}
