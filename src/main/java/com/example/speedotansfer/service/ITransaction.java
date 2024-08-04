package com.example.speedotansfer.service;

import com.example.speedotansfer.dto.transactionDTOs.AllTransactionsDTO;
import com.example.speedotansfer.dto.transactionDTOs.SendMoneyWithAccNumberDTO;
import com.example.speedotansfer.dto.transactionDTOs.TransferResponseDTO;
import com.example.speedotansfer.enums.Currency;
import com.example.speedotansfer.exception.custom.InsufficientAmountException;
import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.exception.custom.InvalidTransferException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;

import javax.security.auth.login.AccountNotFoundException;

public interface ITransaction {

    TransferResponseDTO transferUsingAccNumber(String token, SendMoneyWithAccNumberDTO sendMoneyWithAccNumberDTO) throws InsufficientAmountException, UserNotFoundException, AccountNotFoundException, com.example.speedotansfer.exception.custom.AccountNotFoundException, InvalidTransferException, InvalidJwtTokenException, AuthenticationException;

//    TransferResponseDTO transferUsingUsername(String token, SendMoneyWithUsernameDTO sendMoneyWithUsernameDTO) throws InsufficientAmountException, UserNotFoundException;

    AllTransactionsDTO getHistory(String token) throws UserNotFoundException, InvalidJwtTokenException, AuthenticationException;

    double getExchangeRate(Currency from, Currency to);
}
