package com.example.speedotansfer.service;


import com.example.speedotansfer.dto.userDTOs.AccountDTO;
import com.example.speedotansfer.dto.userDTOs.UpdateUserDTO;
import com.example.speedotansfer.dto.userDTOs.UserDTO;
import com.example.speedotansfer.enums.Currency;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Account;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.AccountRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService implements IUser {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDTO updateCustomer(String token, UpdateUserDTO updateCustomerDTO) throws UserNotFoundException {

        token = token.substring(7);
        long id = jwtUtils.getIdFromJwtToken(token);
        User user = userRepository.findUserByInternalId(id).orElseThrow(()-> new UserNotFoundException("User not found"));

        if (updateCustomerDTO.getEmail() != null) {
            user.setEmail(updateCustomerDTO.getEmail());
        }
        if (updateCustomerDTO.getFullName() != null) {
            user.setFullName(updateCustomerDTO.getFullName());
        }


        if (updateCustomerDTO.getUsername() != null){
            user.setUsername(updateCustomerDTO.getUsername());
        }
        if (updateCustomerDTO.getPhoneNumber() != null){
            user.setPhoneNumber(updateCustomerDTO.getPhoneNumber());
        }

        return user.toDTO();

    }


    @Override
//    @Cacheable("customer")
    public UserDTO getCustomerById(String token) throws UserNotFoundException {
        token = token.substring(7);
        long id = jwtUtils.getIdFromJwtToken(token);
        User user = userRepository.findUserByInternalId(id).orElseThrow(()-> new UserNotFoundException("User not found"));

        return user.toDTO();
    }

    @Override
    public AccountDTO addAccount(String token, AccountDTO acc) throws UserNotFoundException {

        token = token.substring(7);
        long id = jwtUtils.getIdFromJwtToken(token);
        User user = userRepository.findUserByInternalId(id).orElseThrow(()-> new UserNotFoundException("User not found"));
        Optional<Account> res = accountRepository.findAccountByCurrencyAndUserid(acc.getCurrency().toString(),id);
        if(res.isPresent()){
            throw new UserNotFoundException("You Already have an account with this Currency");
        }

        Account account = Account.builder()
                .currency(acc.getCurrency())
                .balance(100)
                .accountNumber(new SecureRandom().nextInt(1000000000) + "")
                .cardholderName(acc.getCardholderName())
                .cardNumber(acc.getCardNumber())
                .cvv(acc.getCvv())
                .expirationDate(acc.getExpirationDate())
                .user(user)
                .build();

        accountRepository.save(account);


        return account.toDTO();
    }

    @Override
    public List<AccountDTO> getAccounts(String token) throws UserNotFoundException {
        token = token.substring(7);
        long id = jwtUtils.getIdFromJwtToken(token);
        accountRepository.findAllByUserid(id);
        return accountRepository.findAllByUserid(id).stream().map(Account::toDTO).collect(Collectors.toList());
    }


}
