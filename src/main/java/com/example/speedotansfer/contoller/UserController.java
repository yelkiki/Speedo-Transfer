package com.example.speedotansfer.contoller;


import com.example.speedotansfer.dto.userDTOs.AccountDTO;
import com.example.speedotansfer.dto.userDTOs.UpdateUserDTO;
import com.example.speedotansfer.dto.userDTOs.UserDTO;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.repository.AccountRepository;
import com.example.speedotansfer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/details")
    public UserDTO getUserById(@RequestHeader("Authorization") String token) throws UserNotFoundException {
        return userService.getCustomerById(token);
    }

    @PutMapping("/update")
    public UserDTO updateUser(@RequestHeader("Authorization") String token,@RequestBody UpdateUserDTO userDTO) throws UserNotFoundException {
        return userService.updateCustomer(token,userDTO);
    }

    @PostMapping("/addAccount")
    public AccountDTO addCard(@RequestHeader("Authorization") String token,@RequestBody AccountDTO acc) throws UserNotFoundException {
        return userService.addAccount(token,acc);
    }

    @GetMapping("/cards")
    public List<AccountDTO> getCards(@RequestHeader("Authorization") String token) throws UserNotFoundException {
        return userService.getAccounts(token);
    }
}
