package com.example.speedotansfer.service;


import com.example.speedotansfer.dto.customerDTOs.UpdateUserDTO;
import com.example.speedotansfer.dto.customerDTOs.UserDTO;
import com.example.speedotansfer.exception.custom.UserNotFoundException;

import java.util.UUID;

public interface IUser {
    public UserDTO updateCustomer(String token, UpdateUserDTO updateCustomerDTO) throws UserNotFoundException;

    public UserDTO getCustomerById(String token) throws UserNotFoundException;

}
