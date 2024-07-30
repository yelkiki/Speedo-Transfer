package com.example.speedotansfer.service;


import com.example.speedotansfer.dto.customerDTOs.UpdateUserDTO;
import com.example.speedotansfer.dto.customerDTOs.UserDTO;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.security.JwtUtils;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService implements IUser {

    private final UserRepository customerRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDTO updateCustomer(UUID id, UpdateUserDTO updateCustomerDTO) throws UserNotFoundException {

        User customer = customerRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));


        customer.setUsername(updateCustomerDTO.getUsername());
        customer.setFirstName(updateCustomerDTO.getFirstName());
        customer.setLastname(updateCustomerDTO.getLastName());
        customer.setEmail(updateCustomerDTO.getEmail());
        customer.setPhoneNumber(updateCustomerDTO.getPhoneNumber());

        return customer.toDTO();

    }


    @Override
//    @Cacheable("customer")
    public UserDTO getCustomerById(String token) throws UserNotFoundException {


        String email = jwtUtils.getUserNameFromJwtToken(token);
        User user = userRepository.findUserByEmail(email).orElseThrow(()-> new UserNotFoundException("User not found"));

        return user.toDTO();
    }



}
