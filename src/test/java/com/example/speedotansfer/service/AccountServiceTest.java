package com.example.speedotansfer.service;


import com.example.speedotansfer.repository.AccountRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.service.impl.AccountService;
import com.example.speedotansfer.service.impl.RedisService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private AccountService accountService;


}
