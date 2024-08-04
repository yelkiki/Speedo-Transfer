package com.example.speedotansfer.service;

import com.example.speedotansfer.dto.userDTOs.AccountDTO;
import com.example.speedotansfer.dto.userDTOs.BalanceDTO;
import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;

import javax.naming.AuthenticationException;

public interface IAccount {

    public BalanceDTO getBalance(String token) throws UserNotFoundException, InvalidJwtTokenException;

    public BalanceDTO getBalanceUsingAccountNumber(String token,String accountNumber) throws UserNotFoundException, InvalidJwtTokenException, com.example.speedotansfer.exception.custom.AccountNotFoundException, AuthenticationException;

    public AccountDTO addAccount(String token, AccountDTO acc) throws UserNotFoundException, InvalidJwtTokenException;
}
