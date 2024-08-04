package com.example.speedotansfer.service;

import com.example.speedotansfer.dto.favoriteDTOs.CreateFavouriteDTO;
import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Favourite;

import javax.naming.AuthenticationException;
import java.util.List;

public interface IFavourite {
    Favourite addToFavourites(String token, CreateFavouriteDTO createFavouriteDTO) throws UserNotFoundException, InvalidJwtTokenException, AuthenticationException;
    List<Favourite> getAllFavourites(String token) throws UserNotFoundException, InvalidJwtTokenException, AuthenticationException;
    // Should we create an id for each favourite item and remove by this id ?
    void removeFromFavourites(String token, Long favouriteId) throws UserNotFoundException, AuthenticationException, InvalidJwtTokenException;
}
