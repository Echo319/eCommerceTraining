package com.rdavies.authservice.service.impl;

import com.rdavies.authservice.exceptions.InactiveAccountException;
import com.rdavies.authservice.exceptions.NoSuchUserException;
import com.rdavies.authservice.exceptions.NotMatchingPasswordException;
import com.rdavies.authservice.exceptions.NotUniqueException;
import com.rdavies.authservice.model.constants.RoleNames;
import com.rdavies.authservice.model.constants.TokenTypes;
import com.rdavies.authservice.model.dao.Role;
import com.rdavies.authservice.model.dao.User;
import com.rdavies.authservice.model.dto.AuthRequest;
import com.rdavies.authservice.model.dto.AuthResponse;
import com.rdavies.authservice.model.dto.RegisterRequest;
import com.rdavies.authservice.repositories.RoleRepository;
import com.rdavies.authservice.repositories.UserRepository;
import com.rdavies.authservice.security.JwtProvider;
import com.rdavies.authservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request){
        // validate uniqueness
        if(userRepository.existsByUsername(request.username())){
            throw new NotUniqueException("Username is in use");
        }
        if(userRepository.existsByEmail(request.email())){
            throw new NotUniqueException("This email is in use");
        }

        // load default role
        Role defaultRole = roleRepository.findByName(RoleNames.USER)
                .orElseThrow(() -> new IllegalStateException("Default user role is not configured"));
        // hash password
        String hashedPassword = passwordEncoder.encode(request.password());
        // save user
        User newUser = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(hashedPassword)
                .role(defaultRole)
                .build();
        userRepository.save(newUser);

        // return JWT response
        return new AuthResponse(jwtProvider.generateToken(newUser),
                TokenTypes.BEARER,
                jwtProvider.getExpirationMs(),
                newUser.getUsername(),
                newUser.getRole().getName());
    }


    // Question. for login purposes should we obfuscate the exception even to developers to prevent data knowledge. i.e saying wrong password tells the person making the attempt that the username is correct?
    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request){
        // find active user
        User user = userRepository.findByUsername(request.username()).orElseThrow(NoSuchUserException::new);
        // ensure password matches
        if(!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new NotMatchingPasswordException();
        }

        if(user.getIsActive() == false) {
            throw new InactiveAccountException();
        }

        // return JWT response
        return new AuthResponse(jwtProvider.generateToken(user),
                TokenTypes.BEARER,
                jwtProvider.getExpirationMs(),
                user.getUsername(),
                user.getRole().getName());
    }
}
