package com.example.speedotansfer.service.impl;

import com.example.speedotansfer.dto.favoriteDTOs.CreateFavouriteDTO;
import com.example.speedotansfer.exception.custom.AuthenticationErrorException;
import com.example.speedotansfer.exception.custom.FavouriteNotFoundException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Favourite;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.FavouriteRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.service.IFavourite;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouriteService implements IFavourite {

    private final UserRepository userRepository;
    private final FavouriteRepository favouriteRepository;
    private final RedisService redisService;

    @Override
    public Favourite addToFavourites(String token, CreateFavouriteDTO createFavouriteDTO)
            throws UserNotFoundException {

        token = token.substring(7);

        if (!redisService.exists(token))
            throw new AuthenticationErrorException("Unauthorized");

        long id = redisService.getUserIdByToken(token);

        User user = userRepository.findUserByInternalId(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

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
            // Change Massage to me more readable
            throw new DataIntegrityViolationException("Favourite User already exists");
        }
    }

    @Override
    public List<Favourite> getAllFavourites(String token)
            throws UserNotFoundException {
        token = token.substring(7);

        if (!redisService.exists(token))
            throw new AuthenticationErrorException("Unauthorized");


        long id = redisService.getUserIdByToken(token);

        User user = userRepository.findUserByInternalId(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return favouriteRepository.getAllByUser(user);
    }

    public List<Favourite> getAllFavourites(String token, int page, int size)
            throws UserNotFoundException {
        token = token.substring(7);

        if (!redisService.exists(token))
            throw new AuthenticationErrorException("Unauthorized");

        long id = redisService.getUserIdByToken(token);

        User user = userRepository.findUserByInternalId(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return favouriteRepository.
                getAllByUser(user, PageRequest.of(page, size, Sort.by("addedAt").descending()));
    }

    @Override
    public void removeFromFavourites(String token, Long favouriteId)
            throws UserNotFoundException, AuthenticationException {
        token = token.substring(7);

        if (!redisService.exists(token))
            throw new AuthenticationErrorException("Unauthorized");

        long id = redisService.getUserIdByToken(token);

        User user = userRepository.findUserByInternalId(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Favourite favourite = favouriteRepository.findById(favouriteId)
                .orElseThrow(() -> new FavouriteNotFoundException("Favourite does not exist"));

        if (favourite.getUser().getExternalId().equals(user.getExternalId())) {
            favouriteRepository.delete(favourite);
        } else {
            throw new AuthenticationErrorException("You are not authorized to delete this favourite");
        }
    }


}
