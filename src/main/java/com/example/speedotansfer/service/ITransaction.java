package com.example.speedotansfer.service;

import com.example.speedotansfer.dto.userDTOs.AccountDTO;
import com.example.speedotansfer.dto.transactionDTOs.AllTransactionsDTO;
import com.example.speedotansfer.dto.transactionDTOs.SendMoneyWithAccNumberDTO;
import com.example.speedotansfer.dto.transactionDTOs.SendMoneyWithUsernameDTO;
import com.example.speedotansfer.dto.transactionDTOs.TransferResponseDTO;
import com.example.speedotansfer.exception.custom.InsufficientAmountException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;

public interface ITransaction {

    public AccountDTO getBalance(String token) throws UserNotFoundException;

    TransferResponseDTO transferUsingAccNumber(String token, SendMoneyWithAccNumberDTO sendMoneyWithAccNumberDTO) throws InsufficientAmountException, UserNotFoundException;

//    TransferResponseDTO transferUsingUsername(String token, SendMoneyWithUsernameDTO sendMoneyWithUsernameDTO) throws InsufficientAmountException, UserNotFoundException;

    AllTransactionsDTO getHistory(String token) throws UserNotFoundException;
}
