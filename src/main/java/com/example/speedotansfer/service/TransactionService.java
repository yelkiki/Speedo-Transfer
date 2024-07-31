package com.example.speedotansfer.service;

import com.example.speedotansfer.dto.transactionDTOs.SendMoneyWithAccNumberDTO;
import com.example.speedotansfer.dto.transactionDTOs.SendMoneyWithUsernameDTO;
import com.example.speedotansfer.dto.transactionDTOs.TransferResponseDTO;
import com.example.speedotansfer.exception.custom.InsufficientAmountException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Account;
import com.example.speedotansfer.model.Transaction;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.AccountRepository;
import com.example.speedotansfer.repository.TransactionRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.security.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransaction{

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;


    @Override
    public boolean deposit(String accountNumber,double amount) throws UserNotFoundException {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber).orElse(null);
        if (account == null) {
            throw new UserNotFoundException("Could not find your account");
        }
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        return true;
    }

    @Override
    public boolean withdraw(String accountNumber,double amount) throws InsufficientAmountException, UserNotFoundException {

        Account account = accountRepository.findAccountByAccountNumber(accountNumber).orElse(null);
        if (account == null) {
            throw new UserNotFoundException("Could not find your account");
        }
        if (account.getBalance() < amount){
            throw new InsufficientAmountException("Insufficient Amount to Withdraw");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
        return true;

    }

    @Override
    public double getBalance(String token) throws UserNotFoundException {
        String email = jwtUtils.getUserNameFromJwtToken(token);
        String accountNumber = userRepository.findAccountNumberByEmail(email);
        if (accountNumber == null) {
            throw new UserNotFoundException("Could not find Account");
        }
        Account account = accountRepository.findAccountByAccountNumber(accountNumber).orElse(null);
        if (account == null) {
            throw new UserNotFoundException("Invalid Account Number");
        }
        return account.getBalance();
    }



    @Override
    @Transactional
    public TransferResponseDTO transferUsingAccNumber(String token, SendMoneyWithAccNumberDTO sendMoneyWithAccNumberDTO) throws InsufficientAmountException, UserNotFoundException {

        // Extracting accountNumber from Token
        String email = jwtUtils.getUserNameFromJwtToken(token);
        String accountNumber = userRepository.findAccountNumberByEmail(email);

        // Making sure both sender and receiver Do EXIST
        User sender = userRepository.findUserByAccountNumber(accountNumber).orElseThrow(()-> new UserNotFoundException("Could not find Sender"));
        User receiver = userRepository.findUserByAccountNumber(sendMoneyWithAccNumberDTO.getAccountNumber()).orElseThrow(()-> new UserNotFoundException("Could not find Reciever"));
        if (withdraw(accountNumber,sendMoneyWithAccNumberDTO.getAmount())){
            if (deposit(sendMoneyWithAccNumberDTO.getAccountNumber(),sendMoneyWithAccNumberDTO.getAmount())){

                Transaction transaction = transactionRepository.save(Transaction.builder()
                                .amount(sendMoneyWithAccNumberDTO.getAmount())
                                .sender(sender)
                                .receiver(receiver)
                                .status(true)
                        .build());
                return transaction.toDto();
            }
            // failed to deposit but withdrawn already
            deposit(accountNumber,sendMoneyWithAccNumberDTO.getAmount());
            Transaction transaction = transactionRepository.save(Transaction.builder()
                    .amount(sendMoneyWithAccNumberDTO.getAmount())
                    .sender(sender)
                    .receiver(receiver)
                    .status(false)
                    .build());
            return transaction.toDto();
        }else {
            throw new InsufficientAmountException("Could Not Do Transaction");
        }

    }

    @Override
    @Transactional
    public TransferResponseDTO transferUsingUsername(String token, SendMoneyWithUsernameDTO sendMoneyWithUsernameDTO) throws InsufficientAmountException, UserNotFoundException {
        // Extracting accountNumber from Token
        String email = jwtUtils.getUserNameFromJwtToken(token);
        String accountNumber = userRepository.findAccountNumberByEmail(email);

        // Making sure both sender and receiver Do EXIST
        User sender = userRepository.findUserByEmail(email).orElseThrow(()-> new UserNotFoundException("Could not find Sender"));
        User receiver = userRepository.findUserByUsername(sendMoneyWithUsernameDTO.getUsername()).orElseThrow(()-> new UserNotFoundException("Could not find Reciever"));
        if (withdraw(sender.getAccount().getAccountNumber(),sendMoneyWithUsernameDTO.getAmount())){
            if (deposit(receiver.getAccount().getAccountNumber(),sendMoneyWithUsernameDTO.getAmount())){

                Transaction transaction = transactionRepository.save(Transaction.builder()
                        .amount(sendMoneyWithUsernameDTO.getAmount())
                        .sender(sender)
                        .receiver(receiver)
                        .status(true)
                        .build());
                return transaction.toDto();
            }
            // failed to deposit but withdrawn already
            deposit(accountNumber,sendMoneyWithUsernameDTO.getAmount());
            Transaction transaction = transactionRepository.save(Transaction.builder()
                    .amount(sendMoneyWithUsernameDTO.getAmount())
                    .sender(sender)
                    .receiver(receiver)
                    .status(false)
                    .build());
            return transaction.toDto();
        }else {
            throw new InsufficientAmountException("Could Not Do Transaction");
        }
    }

    @Override
    //////////////////////// to be done
    public List<TransferResponseDTO> getHistory(String token) {
        return List.of();
    }
}
