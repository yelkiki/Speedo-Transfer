package com.example.speedotansfer.service.impl;


import com.example.speedotansfer.dto.authDTOs.LoginRequestDTO;
import com.example.speedotansfer.dto.authDTOs.LoginResponseDTO;
import com.example.speedotansfer.dto.authDTOs.RegisterDTO;
import com.example.speedotansfer.dto.authDTOs.RegisterReponseDTO;
import com.example.speedotansfer.dto.userDTOs.UserDTO;
import com.example.speedotansfer.enums.TokenType;
import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.exception.custom.PasswordNotMatchException;
import com.example.speedotansfer.exception.custom.UserAlreadyExistsException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Token;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.TokenRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.security.JwtUtils;
import com.example.speedotansfer.security.UserDetailsImpl;
import com.example.speedotansfer.service.IAuth;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuth {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final TokenRepository tokenRepository;
    private final RedisService redisService;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RegisterReponseDTO register(RegisterDTO registerDTO)
            throws UserAlreadyExistsException, PasswordNotMatchException{

        if (userRepository.existsByEmail(registerDTO.getEmail()))
            throw new UserAlreadyExistsException("User Email Already Exists");

        if (userRepository.existsByUsername(registerDTO.getUsername()))
            throw new UserAlreadyExistsException("User Username Already Exists");

        if (userRepository.existsByPhoneNumber(registerDTO.getPhoneNumber()))
            throw new UserAlreadyExistsException("User Phone Number Already Exists");

        if (!Objects.equals(registerDTO.getPassword(), registerDTO.getConfirmPassword()))
            throw new PasswordNotMatchException("User Password Not Matched");


        User user = User.builder().
                    externalId(UUID.randomUUID()).
                    fullName(registerDTO.getFullName()).
                    username(registerDTO.getUsername()).
                    password(encoder.encode(registerDTO.getPassword())).
                    email(registerDTO.getEmail()).
                    gender(registerDTO.getGender()).
                    phoneNumber(registerDTO.getPhoneNumber()).
                    country(registerDTO.getCountry()).
                    birthdate(registerDTO.getBirthDate()).
                    build();


        userRepository.save(user);


        return RegisterReponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .message("User Registered Successfully")
                .details("User Registered Successfully")
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws AuthenticationException {

        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
        }catch (AuthenticationException e){
            throw new AuthenticationException("Password or Email is incorrect {}: "){};
        }

        // Authenticate the user across the spring security
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        // Get Current Authenticated User
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepository.findUserByInternalId(userDetails.getId()).
                orElseThrow();

        redisService.storeToken(jwt, user.getInternalId(), 30*60);

        return LoginResponseDTO.builder()
                .token(jwt)
                .email(userDetails.getEmail())
                .tokenType("Bearer")
                .massage("Login Successful")
                .status(HttpStatus.ACCEPTED)
                .build();
    }
    
}
