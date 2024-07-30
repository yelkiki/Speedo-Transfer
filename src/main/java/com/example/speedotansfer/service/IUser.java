package com.example.speedotansfer.service;


import com.example.speedotansfer.dto.customerDTOs.UpdateUserDTO;
import com.example.speedotansfer.dto.customerDTOs.UserDTO;
import com.example.speedotansfer.exception.custom.UserNotFoundException;

public interface IUser {
    public UserDTO updateCustomer(long id, UpdateUserDTO updateCustomerDTO) throws UserNotFoundException;

    public UserDTO getCustomerById(long id) throws UserNotFoundException;

}
