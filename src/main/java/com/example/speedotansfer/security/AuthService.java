package com.example.speedotansfer.security;


import com.example.speedotansfer.dto.authDTOs.LoginRequestDTO;
import com.example.speedotansfer.dto.authDTOs.LoginResponseDTO;
import com.example.speedotansfer.dto.authDTOs.RegisterDTO;
import com.example.speedotansfer.dto.customerDTOs.UserDTO;
import com.example.speedotansfer.enums.Currency;
import com.example.speedotansfer.exception.custom.UserAlreadyExistsException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Account;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.AccountRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.service.AccountService;
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

@Service
@RequiredArgsConstructor
public class AuthService implements IAuth {

    private final UserRepository userRepository;
    private final AccountService accountService;
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

        // Create an account -> To be replaced
        Account account = Account.builder()
                .currency(Currency.EGY)
                .balance(0)
                .accountNumber("78888987987")
                .build();
        accountRepository.save(account);

        User user = User.builder().
                firstName(registerDTO.getFirstName()).
                lastname(registerDTO.getLastName()).
                username(registerDTO.getUsername()).
                password(encoder.encode(registerDTO.getPassword())).
                email(registerDTO.getEmail()).
                gender(registerDTO.getGender()).
                phoneNumber(registerDTO.getPhoneNumber()).
                country(registerDTO.getCountry()).
                birthdate(registerDTO.getBirthDate()).
                account(account).
                build();


        user.setAccount(account);

        userRepository.save(user);

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
