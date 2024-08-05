package com.example.speedotansfer.contoller;


import com.example.speedotansfer.dto.accountDTO.AccountDTO;
import com.example.speedotansfer.dto.userDTOs.UpdateUserDTO;
import com.example.speedotansfer.dto.userDTOs.UpdateUserResponseDTO;
import com.example.speedotansfer.dto.userDTOs.UserDTO;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.exception.response.ErrorDetails;
import com.example.speedotansfer.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Get User Details")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", description = "User Not Found", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "Unauthenticated", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})

    @GetMapping("/details")
    public UserDTO getUserById(@RequestHeader("Authorization") String token)
            throws UserNotFoundException {
        return userService.getUserById(token);
    }

    @Operation(summary = "Update User Details")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UpdateUserResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", description = "User Not Found", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "Unauthenticated", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PutMapping("/update")
    public UpdateUserResponseDTO updateUser(@RequestHeader("Authorization") String token, @RequestBody UpdateUserDTO userDTO)
            throws UserNotFoundException {
        return userService.updateUser(token, userDTO);
    }

    @Operation(summary = "Get All Cards Of User")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "Unauthenticated", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/cards")
    public List<AccountDTO> getCards(@RequestHeader("Authorization") String token) {
        return userService.getAccounts(token);
    }
}
