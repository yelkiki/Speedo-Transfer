package com.example.speedotansfer.service;


import com.example.speedotansfer.dto.userDTOs.UpdateUserDTO;
import com.example.speedotansfer.dto.userDTOs.UserDTO;
import com.example.speedotansfer.exception.custom.UserNotFoundException;

public interface IUser {
    public UserDTO updateCustomer(String token, UpdateUserDTO updateCustomerDTO) throws UserNotFoundException;

    public UserDTO getCustomerById(String token) throws UserNotFoundException;

}
