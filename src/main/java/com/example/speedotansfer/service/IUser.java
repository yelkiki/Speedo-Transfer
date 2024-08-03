package com.example.speedotansfer.service;


import com.example.speedotansfer.dto.userDTOs.AccountDTO;
import com.example.speedotansfer.dto.userDTOs.UpdateUserDTO;
import com.example.speedotansfer.dto.userDTOs.UserDTO;
import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;

import java.util.List;

public interface IUser {
    public UserDTO updateUser(String token, UpdateUserDTO updateCustomerDTO) throws UserNotFoundException, InvalidJwtTokenException;

    public UserDTO getUserById(String token) throws UserNotFoundException, InvalidJwtTokenException;

    public List<AccountDTO> getAccounts(String token) throws UserNotFoundException, InvalidJwtTokenException;

}
