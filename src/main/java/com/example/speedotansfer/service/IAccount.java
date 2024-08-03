package com.example.speedotansfer.service;

import com.example.speedotansfer.dto.userDTOs.AccountDTO;
import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;

public interface IAccount {

    public AccountDTO addAccount(String token, AccountDTO acc) throws UserNotFoundException, InvalidJwtTokenException;
}
