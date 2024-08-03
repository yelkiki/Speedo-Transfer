package com.example.speedotansfer.service.impl;

import com.example.speedotansfer.dto.favoriteDTOs.CreateFavouriteDTO;
import com.example.speedotansfer.exception.custom.FavouriteNotFoundException;
import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Favourite;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.FavouriteRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.security.JwtUtils;
import com.example.speedotansfer.service.IFavourite;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouriteService implements IFavourite {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final FavouriteRepository favouriteRepository;

    @Override
    public Favourite addToFavourites(String token, CreateFavouriteDTO createFavouriteDTO)
            throws UserNotFoundException, InvalidJwtTokenException {

        token = token.substring(7);

        long id = jwtUtils.getIdFromJwtToken(token);

        User user = userRepository.findUserByInternalId(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        User favUser = userRepository.getUserFromAccountNumber(createFavouriteDTO.getAccountNumber())
                .orElseThrow(() -> new UserNotFoundException("User With This Account Number does not exist "));

        Favourite favourite = Favourite
                .builder()
                .user(user)
                .favouriteUser(favUser)
                .addedAt(LocalDateTime.now())
                .build();
        try {
            return favouriteRepository.save(favourite);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Favourite User already exists");
        }
    }

    @Override
    public List<Favourite> getAllFavourites(String token)
            throws UserNotFoundException, InvalidJwtTokenException {
        token = token.substring(7);
        long id = jwtUtils.getIdFromJwtToken(token);
        User user = userRepository.findUserByInternalId(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return favouriteRepository.getAllByUser(user);
    }

    public List<Favourite> getAllFavourites(String token, int page, int size)
            throws UserNotFoundException, InvalidJwtTokenException {
        token = token.substring(7);
        long id = jwtUtils.getIdFromJwtToken(token);
        User user = userRepository.findUserByInternalId(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return favouriteRepository.getAllByUser(user, PageRequest.of(page, size, Sort.by("addedAt").descending()));
    }

    @Override
    public void removeFromFavourites(String token, Long favouriteId)
            throws UserNotFoundException, AuthenticationException, InvalidJwtTokenException {
        // Make sure the token user is same as the user who owns the relationship of favourite
        token = token.substring(7);
        long id = jwtUtils.getIdFromJwtToken(token);
        User user = userRepository.findUserByInternalId(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        Favourite favourite = favouriteRepository.findById(favouriteId)
                .orElseThrow(() -> new FavouriteNotFoundException("Favourite does not exist"));
        if (favourite.getUser().getExternalId().equals(user.getExternalId())) {
            favouriteRepository.delete(favourite);
        } else {
            throw new AuthenticationException("You are not authorized to delete this favourite");
        }
    }


}
