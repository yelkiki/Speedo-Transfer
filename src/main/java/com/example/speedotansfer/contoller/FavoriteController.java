package com.example.speedotansfer.contoller;

import com.example.speedotansfer.dto.favoriteDTOs.CreateFavouriteDTO;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Favourite;
import com.example.speedotansfer.service.impl.FavouriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

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

    @GetMapping
    public List<Favourite> getFavorites(@RequestHeader("Authorization") String token) throws UserNotFoundException {
        return favouriteService.getAllFavourites(token);
    }

    @GetMapping(params = {"page", "size"})
    public List<Favourite> getFavorites(@RequestHeader("Authorization") String token, @RequestParam("page") int page, @RequestParam("size") int size) throws UserNotFoundException {
        return favouriteService.getAllFavourites(token, page, size);
    }

    @DeleteMapping("/{favouriteId}")
    public void removeFromFavorites(@PathVariable Long favouriteId, @RequestHeader("Authorization") String token) throws UserNotFoundException, AuthenticationException {
        favouriteService.removeFromFavourites(token, favouriteId);
    }
}
