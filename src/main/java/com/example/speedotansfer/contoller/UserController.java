package com.example.speedotansfer.contoller;


import com.example.speedotansfer.dto.customerDTOs.UpdateUserDTO;
import com.example.speedotansfer.dto.customerDTOs.UserDTO;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/details")
    public UserDTO getUserById(@RequestHeader("Authorization") String token) throws UserNotFoundException {
        String trimmedToken = token.substring(7).trim();
        return userService.getCustomerById(trimmedToken);
    }

    @PutMapping("/update")
    public UserDTO updateUser(@RequestHeader("Authorization") String token,@RequestBody UpdateUserDTO userDTO) throws UserNotFoundException {
        String trimmedToken = token.substring(7).trim();
        return userService.updateCustomer(trimmedToken,userDTO);
    }
}
