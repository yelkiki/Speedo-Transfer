package com.example.speedotansfer.service;


import com.example.speedotansfer.dto.accountDTO.AccountDTO;
import com.example.speedotansfer.dto.userDTOs.UpdateUserDTO;
import com.example.speedotansfer.dto.userDTOs.UpdateUserResponseDTO;
import com.example.speedotansfer.dto.userDTOs.UserDTO;
import com.example.speedotansfer.enums.Currency;
import com.example.speedotansfer.exception.custom.AuthenticationErrorException;
import com.example.speedotansfer.exception.custom.UserAlreadyExistsException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Account;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.AccountRepository;
import com.example.speedotansfer.repository.UserRepository;

import com.example.speedotansfer.service.impl.AuthService;
import com.example.speedotansfer.service.impl.RedisService;
import com.example.speedotansfer.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RedisService redisService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserService userService;


    @Test
    void updateUser_EmailAlreadyExists_ThrowsUserAlreadyExistsException() {
        // Arrange
        String token = "Bearer token";
        UpdateUserDTO updateUserDTO = UpdateUserDTO.builder()
                .email("newEmail@example.com")
                .build();

        User existingUser = new User();
        existingUser.setInternalId(1L);

        when(redisService.exists(anyString())).thenReturn(true);
        when(redisService.getUserIdByToken(anyString())).thenReturn(1L);
        when(userRepository.findUserByInternalId(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(updateUserDTO.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.updateUser(token, updateUserDTO);
        });
    }

    @Test
    void updateUser_Success() throws UserNotFoundException, UserAlreadyExistsException {
        // Arrange
        String token = "Bearer token";
        UpdateUserDTO updateUserDTO = UpdateUserDTO.builder()
                .email("newemail@example.com")
                .fullName("new Name")
                .phoneNumber("01151816728")
                .username("newUsername")
                .build();

        User existingUser = new User();
        existingUser.setInternalId(1L);
        existingUser.setEmail("oldemail@example.com");
        existingUser.setFullName("Old Name");
        existingUser.setUsername("oldusername");
        existingUser.setPhoneNumber("987654321");

        when(redisService.exists(anyString())).thenReturn(true);
        when(redisService.getUserIdByToken(anyString())).thenReturn(1L);
        when(userRepository.findUserByInternalId(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(updateUserDTO.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(updateUserDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(updateUserDTO.getPhoneNumber())).thenReturn(false);
        when(authService.generateToken(any(User.class))).thenReturn("newToken");

        // Act
        UpdateUserResponseDTO response = userService.updateUser(token, updateUserDTO);

        // Assert
        assertNotNull(response);
        assertEquals("newToken", response.getNewToken());
        assertEquals(HttpStatus.OK, response.getHttpStatusCode());
        assertTrue(response.getDetails().contains("Email updated successfully"));
        assertTrue(response.getDetails().contains("Full name updated successfully"));
        assertTrue(response.getDetails().contains("Username updated successfully"));
        assertTrue(response.getDetails().contains("Phone number updated successfully"));

        verify(userRepository, times(1)).save(existingUser);
        verify(redisService, times(1)).deleteToken(anyString());
        verify(redisService, times(1)).storeToken(anyString(), anyLong());
    }


    @Test
    public void GetuserByIdTest() throws UserNotFoundException {
        User user = User.builder()
                .internalId(1L)
                .externalId(UUID.randomUUID())
                .email("newEmail@example.com")
                .fullName("new Name")
                .phoneNumber("01151816728")
                .username("newUsername")
                .build();

        when(redisService.exists(anyString())).thenReturn(true);
        when(redisService.getUserIdByToken(anyString())).thenReturn(1L);
        when(userRepository.findUserByInternalId(1L)).thenReturn(Optional.of(user));

        UserDTO response = userService.getUserById("blablablatoken");

        assertEquals(user.getExternalId(), response.getUserId());
        assertEquals("newEmail@example.com", response.getEmail());
        assertEquals("newUsername", response.getUsername());

    }

    @Test
    void getAccounts_Success() {
        // Arrange
        String token = "Bearer validtoken";
        User user = User.builder()
                .internalId(1L)
                .externalId(UUID.randomUUID())
                .email("newEmail@example.com")
                .fullName("new Name")
                .phoneNumber("01151816728")
                .username("newUsername")
                .build();

        Account account1 = Account.builder()
                .id(1L)
                .cvv(132)
                .cardNumber("21321412515")
                .accountNumber("213124124")
                .cardholderName("gvsgfbd")
                .expirationDate("12/26")
                .currency(Currency.EGY)
                .balance(100)
                .user(user)
                .build();

        Account account2 = Account.builder()
                .id(1L)
                .cvv(132)
                .cardNumber("213214125315")
                .accountNumber("2132124124")
                .cardholderName("gvsgfbd")
                .expirationDate("12/26")
                .currency(Currency.EUR)
                .balance(200)
                .user(user)
                .build();

        List<Account> accounts = Arrays.asList(account1, account2);
        when(redisService.exists(anyString())).thenReturn(true);
        when(redisService.getUserIdByToken(anyString())).thenReturn(1L);
        when(accountRepository.findAllByUserid(anyLong())).thenReturn(accounts);

        // Act
        List<AccountDTO> accountDTOList = userService.getAccounts(token);

        // Assert
        assertNotNull(accountDTOList);
        assertEquals(2, accountDTOList.size());
        assertEquals(1L, accountDTOList.get(0).getUserId());
        assertEquals(100.0, accountDTOList.get(0).getBalance());
        assertEquals(1L, accountDTOList.get(1).getUserId());
        assertEquals(200.0, accountDTOList.get(1).getBalance());
    }

}
