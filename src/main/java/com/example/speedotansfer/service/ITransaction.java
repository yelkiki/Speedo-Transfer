package com.example.speedotansfer.service;

import com.example.speedotansfer.dto.transactionDTOs.SendMoneyWithAccNumberDTO;
import com.example.speedotansfer.dto.transactionDTOs.SendMoneyWithUsernameDTO;
import com.example.speedotansfer.dto.transactionDTOs.TransferResponseDTO;
import com.example.speedotansfer.exception.custom.InsufficientAmountException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Transaction;

import java.util.List;

public interface ITransaction {

    public boolean deposit(String accountNumber,double amount) throws UserNotFoundException; // either successfull or failed

    public boolean withdraw(String accountNumber,double amount) throws InsufficientAmountException, UserNotFoundException; // either successfull or failed

    public double getBalance(String token) throws UserNotFoundException;

    TransferResponseDTO transferUsingAccNumber(String token, SendMoneyWithAccNumberDTO sendMoneyWithAccNumberDTO) throws InsufficientAmountException, UserNotFoundException;

    TransferResponseDTO transferUsingUsername(String token, SendMoneyWithUsernameDTO sendMoneyWithUsernameDTO) throws InsufficientAmountException, UserNotFoundException;

    List<TransferResponseDTO> getHistory(String token);
}
