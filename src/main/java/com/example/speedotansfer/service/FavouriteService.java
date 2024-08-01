package com.example.speedotansfer.service;

import com.example.speedotansfer.dto.favoriteDTOs.CreateFavouriteDTO;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Favourite;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.FavouriteRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouriteService implements IFavourite{

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final FavouriteRepository favouriteRepository;

    @Override
    public Favourite addToFavourites(String token, CreateFavouriteDTO createFavouriteDTO) throws UserNotFoundException {

        String email = jwtUtils.getUserNameFromJwtToken(token);
        System.out.println(email);
        User user = userRepository.findUserByEmail(email).
                orElseThrow(()-> new UserNotFoundException("User not found"));
        System.out.println(user);
        // I need to get user id from account Number
//        User favUser = userRepository.getUserFromAccountNumber(createFavouriteDTO.getAccountNumber())
//                .orElseThrow(()-> new UserNotFoundException("User With This Account Number does not exist "));
        User favUser = user;

        System.out.println(favUser);
        Favourite favourite = Favourite.builder()
                .user(user)
                .favouriteUser(favUser)
                .build();

        return favouriteRepository.save(favourite);


    }

    @Override
    public List<Favourite> getAllFavourites(String token) {
        return null;
    }

    @Override
    public void removeFromFavourites(String token, CreateFavouriteDTO createFavouriteDTO) {

    }

}
