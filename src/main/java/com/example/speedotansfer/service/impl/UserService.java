package com.example.speedotansfer.service.impl;


import com.example.speedotansfer.dto.userDTOs.AccountDTO;
import com.example.speedotansfer.dto.userDTOs.UpdateUserDTO;
import com.example.speedotansfer.dto.userDTOs.UserDTO;
import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Account;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.AccountRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.security.JwtUtils;
import com.example.speedotansfer.service.IUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService implements IUser {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDTO updateUser(String token, UpdateUserDTO updateCustomerDTO) throws UserNotFoundException, InvalidJwtTokenException {

        token = token.substring(7);
        long id = jwtUtils.getIdFromJwtToken(token);
        User user = userRepository.findUserByInternalId(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (updateCustomerDTO.getEmail() != null) {
            user.setEmail(updateCustomerDTO.getEmail());
        }
        if (updateCustomerDTO.getFullName() != null) {
            user.setFullName(updateCustomerDTO.getFullName());
        }
        if (updateCustomerDTO.getUsername() != null) {
            user.setUsername(updateCustomerDTO.getUsername());
        }
        if (updateCustomerDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(updateCustomerDTO.getPhoneNumber());
        }

        userRepository.save(user);

        return user.toDTO();

    }


    @Override
//    @Cacheable("customer")
    public UserDTO getUserById(String token) throws UserNotFoundException, InvalidJwtTokenException {
        token = token.substring(7);
        long id = jwtUtils.getIdFromJwtToken(token);
        User user = userRepository.findUserByInternalId(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.toDTO();
    }



    @Override
    public List<AccountDTO> getAccounts(String token) throws UserNotFoundException, InvalidJwtTokenException {
        token = token.substring(7);
        long id = jwtUtils.getIdFromJwtToken(token);
        accountRepository.findAllByUserid(id);
        return accountRepository.findAllByUserid(id).stream().map(Account::toDTO).collect(Collectors.toList());
    }


}
