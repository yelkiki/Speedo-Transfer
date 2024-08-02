package com.example.speedotansfer.service;


import com.example.speedotansfer.dto.userDTOs.AccountDTO;
import com.example.speedotansfer.dto.userDTOs.UpdateUserDTO;
import com.example.speedotansfer.dto.userDTOs.UserDTO;
import com.example.speedotansfer.exception.custom.UserNotFoundException;

import java.util.List;

public interface IUser {
    public UserDTO updateCustomer(String token, UpdateUserDTO updateCustomerDTO) throws UserNotFoundException;

    public UserDTO getCustomerById(String token) throws UserNotFoundException;

    public AccountDTO addAccount(String token, AccountDTO acc) throws UserNotFoundException;

    public List<AccountDTO> getAccounts(String token) throws UserNotFoundException;

}
