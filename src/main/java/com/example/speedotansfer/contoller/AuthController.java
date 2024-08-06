package com.example.speedotansfer.contoller;

import com.example.speedotansfer.dto.authDTOs.LoginRequestDTO;
import com.example.speedotansfer.dto.authDTOs.LoginResponseDTO;
import com.example.speedotansfer.dto.authDTOs.RegisterDTO;
import com.example.speedotansfer.dto.authDTOs.RegisterReponseDTO;
import com.example.speedotansfer.exception.custom.PasswordNotMatchException;
import com.example.speedotansfer.exception.custom.UserAlreadyExistsException;
import com.example.speedotansfer.exception.response.ErrorDetails;
import com.example.speedotansfer.service.impl.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Validated
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register new user")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = RegisterDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "User Already Exist ", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Password do not match ", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Constraint Violation", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/register")
    public RegisterReponseDTO register(@RequestBody @Valid RegisterDTO registerDTO)
            throws UserAlreadyExistsException, PasswordNotMatchException, ConstraintViolationException {
        return authService.register(registerDTO);
    }

    @Operation(summary = "Login user and generate JWT")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = LoginResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO loginRequestDTO)
            throws AuthenticationException {
        return authService.login(loginRequestDTO);
    }

}
