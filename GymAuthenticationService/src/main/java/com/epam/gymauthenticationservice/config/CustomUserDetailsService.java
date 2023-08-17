package com.epam.gymauthenticationservice.config;

import com.epam.gymauthenticationservice.model.CustomUserDetails;
import com.epam.gymauthenticationservice.model.StringConstants;
import com.epam.gymauthenticationservice.model.User;
import com.epam.gymauthenticationservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(),"loadUserByUsername", this.getClass().getName(), username);
        Optional<User> credential = repository.findByUsername(username);
        UserDetails userDetails =  credential.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + username));
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(),"loadUserByUsername", this.getClass().getName());
        return userDetails;
    }
}