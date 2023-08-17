package com.epam.gymmanagementapplication.service;

import com.epam.gymmanagementapplication.dto.UserDTO;
import com.epam.gymmanagementapplication.dto.request.ModifyPassword;
import com.epam.gymmanagementapplication.dto.response.Credential;
import com.epam.gymmanagementapplication.exception.UserException;
import com.epam.gymmanagementapplication.model.User;
import com.epam.gymmanagementapplication.repository.UserRepository;
import com.epam.gymmanagementapplication.util.StringConstants;
import com.epam.gymmanagementapplication.util.ValueMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User saveUser(UserDTO userDTO) throws UserException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), SAVE_USER, this.getClass().getName(), userDTO.toString());
        User user = ValueMapper.userDtoToUserMapper(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), SAVE_USER, this.getClass().getName());
        return user;
    }


    public boolean loginUser(Credential credential) throws UserException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), LOGIN, this.getClass().getName(), credential.toString());
        boolean isValid = userRepository.findByUsername(credential.getUsername()).map(user -> user.getPassword().equals(credential.getPassword())).orElseThrow(() -> new UserException("Invalid Username"));
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), LOGIN, this.getClass().getName());
        return isValid;
    }

    @Transactional
    public boolean changeLogin(ModifyPassword modifyPassword) throws UserException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), CHANGE_LOGIN, this.getClass().getName(), modifyPassword.toString());
        if(modifyPassword.getOldPassword().equalsIgnoreCase( modifyPassword.getNewPassword()))
            throw new UserException("old and new Passwords should not match");
        boolean isValid;
        isValid = userRepository.findByUsername(modifyPassword.getUsername()).map(user -> {
            if(passwordEncoder.matches(user.getPassword(), modifyPassword.getOldPassword())){
                user.setPassword(passwordEncoder.encode(modifyPassword.getNewPassword()));
                return true;
            }
            else
               return false;
        }).orElseThrow(() -> new UserException("Invalid Username"));
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), CHANGE_LOGIN, this.getClass().getName());
        return isValid;
    }

    public User getUser(String username) throws UserException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), GET, this.getClass().getName(), username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserException("No User found"));
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), GET, this.getClass().getName());
        return user;
    }

    public void removeUser(User user){
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), REMOVE, this.getClass().getName(), user.toString());
        userRepository.delete(user);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), REMOVE, this.getClass().getName());
    }
}
