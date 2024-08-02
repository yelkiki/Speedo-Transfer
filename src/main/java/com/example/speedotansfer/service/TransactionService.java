package com.example.speedotansfer.service;

import com.example.speedotansfer.dto.userDTOs.AccountDTO;
import com.example.speedotansfer.dto.transactionDTOs.AllTransactionsDTO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransaction{

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;


    @Override
    public AccountDTO getBalance(String token) throws UserNotFoundException {
        token = token.substring(7);
        long id = jwtUtils.getIdFromJwtToken(token);
        User user = userRepository.findUserByInternalId(id).orElseThrow(()-> new UserNotFoundException("User not found"));

        return user.getAccount().toDTO();
    }


    @Override
    @Transactional
    public TransferResponseDTO transferUsingAccNumber(String token, SendMoneyWithAccNumberDTO sendMoneyWithAccNumberDTO) throws InsufficientAmountException, UserNotFoundException {
        token = token.substring(7);
        long id = jwtUtils.getIdFromJwtToken(token);
        User sender = userRepository.findUserByInternalId(id).orElseThrow(()-> new UserNotFoundException("User not found"));

        Account receiverAccount = accountRepository.findAccountByAccountNumber(sendMoneyWithAccNumberDTO.getAccountNumber()).orElseThrow(() -> new UserNotFoundException("Could not find receiver's account 1"));
        Account senderAccount = sender.getAccount();

        User receiver = userRepository.findUserByAccount(receiverAccount).orElseThrow(() -> new UserNotFoundException("Could not find receiver's account 2"));

        if (senderAccount.getBalance() < sendMoneyWithAccNumberDTO.getAmount()) {
            throw new InsufficientAmountException("Insufficient funds");
        }else {
            senderAccount.setBalance(senderAccount.getBalance()-sendMoneyWithAccNumberDTO.getAmount());
            receiverAccount.setBalance(receiverAccount.getBalance()+sendMoneyWithAccNumberDTO.getAmount());
            Transaction transaction = Transaction.builder()
                    .status(true)
                    .receiver(receiver)
                    .sender(sender)
                    .amount(sendMoneyWithAccNumberDTO.getAmount())
                    .build();
            transactionRepository.save(transaction);
            accountRepository.save(senderAccount);
            accountRepository.save(receiverAccount);
            return transaction.toDto();
        }


    }

//    @Override
//    @Transactional
//    public TransferResponseDTO transferUsingUsername(String token, SendMoneyWithUsernameDTO sendMoneyWithUsernameDTO) throws InsufficientAmountException, UserNotFoundException {
//
//        token = token.substring(7);
//        long id = jwtUtils.getIdFromJwtToken(token);
//        User sender = userRepository.findUserByInternalId(id).orElseThrow(()-> new UserNotFoundException("User not found"));
//        User receiver = userRepository.findUserByUsername(sendMoneyWithUsernameDTO.getUsername()).orElseThrow(() -> new UserNotFoundException("Could not find receiver account"));
//
//        Account senderAccount = sender.getAccount();
//        Account receiverAccount = receiver.getAccount();
//
//        if (senderAccount.getBalance() < sendMoneyWithUsernameDTO.getAmount()) {
//            throw new InsufficientAmountException("Insufficient funds");
//        }else {
//            senderAccount.setBalance(senderAccount.getBalance()-sendMoneyWithUsernameDTO.getAmount());
//            receiverAccount.setBalance(receiverAccount.getBalance()+sendMoneyWithUsernameDTO.getAmount());
//            Transaction transaction = Transaction.builder()
//                    .status(true)
//                    .receiver(receiver)
//                    .sender(sender)
//                    .amount(sendMoneyWithUsernameDTO.getAmount())
//                    .build();
//            transactionRepository.save(transaction);
//            accountRepository.save(senderAccount);
//            accountRepository.save(receiverAccount);
//            return transaction.toDto();
//        }
//
//    }

    @Override
    public AllTransactionsDTO getHistory(String token) throws UserNotFoundException {
        token = token.substring(7);
        long id = jwtUtils.getIdFromJwtToken(token);
        User user = userRepository.findUserByInternalId(id).orElseThrow(()-> new UserNotFoundException("User not found"));

        List<Transaction> lst = transactionRepository.findAllBySenderInternalId(user.getInternalId());
        lst.addAll(transactionRepository.findAllByReceiverInternalId(user.getInternalId()));


        return new AllTransactionsDTO(lst.stream().map(Transaction::toDto).collect(Collectors.toList()));
    }
}
