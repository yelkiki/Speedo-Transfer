package com.example.speedotansfer.service;


import com.example.speedotansfer.dto.authDTOs.LoginRequestDTO;
import com.example.speedotansfer.dto.authDTOs.LoginResponseDTO;
import com.example.speedotansfer.dto.authDTOs.RegisterDTO;
import com.example.speedotansfer.dto.userDTOs.UserDTO;
import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.exception.custom.PasswordNotMatchException;
import com.example.speedotansfer.exception.custom.UserAlreadyExistsException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;

public interface IAuth {

    UserDTO register(RegisterDTO registerDTO) throws UserAlreadyExistsException, PasswordNotMatchException, MethodArgumentNotValidException;

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws UserNotFoundException;

}
