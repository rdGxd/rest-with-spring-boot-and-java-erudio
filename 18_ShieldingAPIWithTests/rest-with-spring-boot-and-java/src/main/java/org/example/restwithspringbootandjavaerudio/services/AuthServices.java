package org.example.restwithspringbootandjavaerudio.services;

import org.example.restwithspringbootandjavaerudio.data.vo.v1.security.AccountCredentialsVO;
import org.example.restwithspringbootandjavaerudio.data.vo.v1.security.TokenVO;
import org.example.restwithspringbootandjavaerudio.model.User;
import org.example.restwithspringbootandjavaerudio.repositories.UserRepository;
import org.example.restwithspringbootandjavaerudio.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    private static boolean checkIfParamsIsNotNull(String username, String refreshToken) {
        return refreshToken == null || refreshToken.isBlank() || username == null || username.isBlank();
    }

    private static boolean checkIfParamsNotNull(AccountCredentialsVO data) {
        return data == null || data.getUsername() == null || data.getUsername().isBlank() || data.getPassword() == null || data.getPassword().isBlank();
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity signin(AccountCredentialsVO data) {
        if (checkIfParamsNotNull(data)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }

        try {
            String username = data.getUsername();
            String password = data.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = repository.findByUsername(username);

            if (user != null) {
                TokenVO tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
                return ResponseEntity.ok(tokenResponse);
            } else {
                throw new UsernameNotFoundException("Username " + username + " not found!");
            }

        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity refreshToken(String username, String refreshToken) {
        User user = repository.findByUsername(username);

        if (checkIfParamsIsNotNull(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }

        if (user != null) {
            TokenVO tokenResponse = tokenProvider.refreshToken(refreshToken);
            return ResponseEntity.ok(tokenResponse);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
    }
}
