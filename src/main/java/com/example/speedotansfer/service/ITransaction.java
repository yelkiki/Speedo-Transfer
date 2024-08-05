package com.example.speedotansfer.service;

import com.example.speedotansfer.dto.transactionDTOs.AllTransactionsDTO;
import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import org.springframework.security.core.AuthenticationException;

public interface ITransaction {

//    TransferResponseDTO transferUsingUsername(String token, SendMoneyWithUsernameDTO sendMoneyWithUsernameDTO) throws InsufficientAmountException, UserNotFoundException;

    AllTransactionsDTO getHistory(String token) throws UserNotFoundException, InvalidJwtTokenException, AuthenticationException;

}
