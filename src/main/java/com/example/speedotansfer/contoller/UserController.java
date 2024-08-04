package com.example.speedotansfer.contoller;


import com.example.speedotansfer.dto.accountDTO.AccountDTO;
import com.example.speedotansfer.dto.userDTOs.UpdateUserDTO;
import com.example.speedotansfer.dto.userDTOs.UpdateUserResponseDTO;
import com.example.speedotansfer.dto.userDTOs.UserDTO;
import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.service.impl.UserService;
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
    public UserDTO getUserById(@RequestHeader("Authorization") String token)
            throws UserNotFoundException {
        return userService.getUserById(token);
    }

    @PutMapping("/update")
    public UpdateUserResponseDTO updateUser(@RequestHeader("Authorization") String token, @RequestBody UpdateUserDTO userDTO)
            throws UserNotFoundException, InvalidJwtTokenException {
        return userService.updateUser(token, userDTO);
    }


    @GetMapping("/cards")
    public List<AccountDTO> getCards(@RequestHeader("Authorization") String token) {
        return userService.getAccounts(token);
    }
}
