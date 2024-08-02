package com.example.speedotansfer.security;


import com.example.speedotansfer.dto.authDTOs.LoginRequestDTO;
import com.example.speedotansfer.dto.authDTOs.LoginResponseDTO;
import com.example.speedotansfer.dto.authDTOs.RegisterDTO;
import com.example.speedotansfer.dto.userDTOs.UserDTO;
import com.example.speedotansfer.enums.Currency;
import com.example.speedotansfer.exception.custom.UserAlreadyExistsException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Account;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.AccountRepository;
import com.example.speedotansfer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuth {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AccountRepository accountRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserDTO register(RegisterDTO registerDTO) throws UserAlreadyExistsException, UserNotFoundException {
        if (userRepository.existsByEmail(registerDTO.getEmail()))
            throw new UserNotFoundException("Customer Email Already Exists");

        if (userRepository.existsByUsername(registerDTO.getUsername()))
            throw new UserNotFoundException("Customer Username Already Exists");

        if (userRepository.existsByPhoneNumber(registerDTO.getPhoneNumber()))
            throw new UserNotFoundException("Customer Phone Number Already Exists");

        if (!Objects.equals(registerDTO.getPassword(), registerDTO.getConfirmPassword()))
            throw new UserNotFoundException("Customer Password Not Matched");

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

//        Account account = Account.builder()
//                .currency(Currency.EGY)
//                .balance(100)
//                .accountNumber(new SecureRandom().nextInt(1000000000) + "")
//                .build();
//
//        account.setUser(user);
//
//        accountRepository.save(account);


        return user.toDTO();
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws UserNotFoundException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
        // Authenticate the user across the spring security
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        // Get Current Authenticated User
        UserDetailsImpl customerDetails = (UserDetailsImpl) authentication.getPrincipal();

        return LoginResponseDTO.builder()
                .token(jwt)
                .email(customerDetails.getEmail())
                .tokenType("Bearer")
                .massage("Login Successful")
                .status(HttpStatus.ACCEPTED)
                .build();
    }
}
