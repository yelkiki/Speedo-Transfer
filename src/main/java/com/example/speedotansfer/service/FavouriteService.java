package com.example.speedotansfer.service;

import com.example.speedotansfer.dto.favoriteDTOs.CreateFavouriteDTO;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Favourite;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.FavouriteRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouriteService implements IFavourite{

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final FavouriteRepository favouriteRepository;

    @Override
    public Favourite addToFavourites(String token, CreateFavouriteDTO createFavouriteDTO) throws UserNotFoundException {

        token = token.substring(7);
        String email = jwtUtils.getUserNameFromJwtToken(token);
        User user = userRepository.findUserByEmail(email).
                orElseThrow(()-> new UserNotFoundException("User not found"));


        User favUser = userRepository.getUserFromAccountNumber(createFavouriteDTO.getAccountNumber())
                .orElseThrow(()-> new UserNotFoundException("User With This Account Number does not exist "));


        Favourite favourite = Favourite
                .builder()
                .user(user)
                .favouriteUser(favUser)
                .addedAt(LocalDateTime.now())
                .build();
        try {
            return favouriteRepository.save(favourite);
        }catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Favourite User already exists");
        }
    }

    @Override
    public List<Favourite> getAllFavourites(String token) throws UserNotFoundException {
        token = token.substring(7);
        User user = userRepository.findUserByEmail(jwtUtils.getUserNameFromJwtToken(token))
                .orElseThrow(()-> new UserNotFoundException("User not found"));
        return favouriteRepository.getAllByUser(user);
    }

    public List<Favourite>getAllFavourites(String token, int page, int size) throws UserNotFoundException {
        token = token.substring(7);
        User user = userRepository.findUserByEmail(jwtUtils.getUserNameFromJwtToken(token))
                .orElseThrow(()-> new UserNotFoundException("User not found"));
        return favouriteRepository.getAllByUser(user, PageRequest.of(page, size, Sort.by("addedAt").descending()));
    }

    @Override
    public void removeFromFavourites(String token, Long favouriteId) throws UserNotFoundException, AuthenticationException {
        // Make sure the token user is same as the user who owns the relationship of favourite
        token = token.substring(7);
        User user = userRepository.findUserByEmail(jwtUtils.getUserNameFromJwtToken(token))
                .orElseThrow(()-> new UserNotFoundException("User not found"));
        Favourite favourite = favouriteRepository.findById(favouriteId)
                .orElseThrow(()-> new DataIntegrityViolationException("Favourite does not exist"));
        if(favourite.getUser().getId().equals(user.getId())){
            favouriteRepository.delete(favourite);
        }
        else{
            throw new AuthenticationException("You are not authorized to delete this favourite");
        }
    }



}
