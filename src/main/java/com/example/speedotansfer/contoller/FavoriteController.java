package com.example.speedotansfer.contoller;

import com.example.speedotansfer.dto.favoriteDTOs.CreateFavouriteDTO;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Favourite;
import com.example.speedotansfer.service.FavouriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/favorites")
@RequiredArgsConstructor
@Validated
public class FavoriteController {

    private final FavouriteService favouriteService;

    @PostMapping
    public Favourite addToFavorite(@RequestBody @Valid CreateFavouriteDTO createFavouriteDTO, @RequestHeader("Authorization") String token) throws UserNotFoundException {
        return favouriteService.addToFavourites(token, createFavouriteDTO);
    }
}
