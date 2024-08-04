package com.example.speedotansfer.service.impl;


import com.example.speedotansfer.dto.accountDTO.AccountDTO;
import com.example.speedotansfer.dto.userDTOs.UpdateUserDTO;
import com.example.speedotansfer.dto.userDTOs.UpdateUserResponseDTO;
import com.example.speedotansfer.dto.userDTOs.UserDTO;
import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.exception.custom.UserAlreadyExistsException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Account;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.AccountRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.service.IUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService implements IUser {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final RedisService redisService;
    private final AuthService authService;


    @Override
    @Transactional
    public UpdateUserResponseDTO updateUser(String token, UpdateUserDTO updateCustomerDTO)
            throws UserNotFoundException, InvalidJwtTokenException {


        token = token.substring(7);

        if (!redisService.exists(token))
            throw new AuthenticationException("Unauthorized") {
            };


        long id = redisService.getUserIdByToken(token);

        User user = userRepository.findUserByInternalId(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String details = "";

        // We have to check before updating
        if (userRepository.existsByEmail(updateCustomerDTO.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        if (userRepository.existsByUsername(updateCustomerDTO.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByPhoneNumber(updateCustomerDTO.getPhoneNumber())) {
            throw new UserAlreadyExistsException("Phone number already exists");
        }

        if (updateCustomerDTO.getEmail() != null) {
            user.setEmail(updateCustomerDTO.getEmail());
            details += "Email updated successfully, ";
        }
        if (updateCustomerDTO.getFullName() != null) {
            user.setFullName(updateCustomerDTO.getFullName());
            details += "Full name updated successfully, ";
        }
        if (updateCustomerDTO.getUsername() != null) {
            user.setUsername(updateCustomerDTO.getUsername());
            details += "Username updated successfully, ";
        }
        if (updateCustomerDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(updateCustomerDTO.getPhoneNumber());
            details += "Phone number updated successfully, ";
        }

        userRepository.save(user);

        // Delete Current Token
        redisService.deleteToken(token);

        String newToken = authService.generateToken(user);

        // Generate new token
        redisService.storeToken(newToken, user.getInternalId());

        return UpdateUserResponseDTO
                .builder()
                .newToken(newToken)
                .updatedAt(LocalDateTime.now())
                .massage("User updated successfully")
                .details(details)
                .httpStatusCode(HttpStatus.OK)
                .build();

    }

    @Override
    public UserDTO getUserById(String token) throws UserNotFoundException {
        token = token.substring(7);


        if (!redisService.exists(token))
            throw new AuthenticationException("Unauthorized") {
            };

        long id = redisService.getUserIdByToken(token);

        User user = userRepository.findUserByInternalId(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return user.toDTO();
    }

    @Override
    public List<AccountDTO> getAccounts(String token) {
        token = token.substring(7);

        if (!redisService.exists(token))
            throw new AuthenticationException("Unauthorized") {
            };

        long id = redisService.getUserIdByToken(token);

        accountRepository.findAllByUserid(id);
        return accountRepository.findAllByUserid(id).stream().map(Account::toDTO).collect(Collectors.toList());
    }


}
