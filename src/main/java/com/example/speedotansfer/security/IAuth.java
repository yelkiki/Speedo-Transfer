package com.example.speedotansfer.security;


import com.example.speedotansfer.dto.authDTOs.LoginRequestDTO;
import com.example.speedotansfer.dto.authDTOs.LoginResponseDTO;
import com.example.speedotansfer.dto.authDTOs.RegisterDTO;
import com.example.speedotansfer.dto.customerDTOs.UserDTO;
import com.example.speedotansfer.exception.custom.UserAlreadyExistsException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;

public interface IAuth {

    UserDTO register(RegisterDTO registerDTO) throws UserAlreadyExistsException, UserNotFoundException;

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws UserNotFoundException;

}
