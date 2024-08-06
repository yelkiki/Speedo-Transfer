package com.example.speedotansfer.contoller;

import com.example.speedotansfer.dto.favoriteDTOs.CreateFavouriteDTO;
import com.example.speedotansfer.exception.custom.AuthenticationErrorException;
import com.example.speedotansfer.exception.custom.FavouriteNotFoundException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.exception.response.ErrorDetails;
import com.example.speedotansfer.model.Favourite;
import com.example.speedotansfer.service.impl.FavouriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/favorites")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", allowedHeaders = "*")

public class FavoriteController {

    private final FavouriteService favouriteService;

    @Operation(summary = "Add User To Favorite")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CreateFavouriteDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = UserNotFoundException.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema(implementation = AuthenticationErrorException.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = DataIntegrityViolationException.class), mediaType = "application/json")})
    @PostMapping
    public Favourite addToFavorite
            (@RequestBody @Valid CreateFavouriteDTO createFavouriteDTO, @RequestHeader("Authorization") String token)
            throws UserNotFoundException {
        return favouriteService.addToFavourites(token, createFavouriteDTO);
    }


    @Operation(summary = "Get All Favourites")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Favourite.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", description = "User Not Found ", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping
    public List<Favourite> getFavorites
            (@RequestHeader("Authorization") String token)
            throws UserNotFoundException {
        return favouriteService.getAllFavourites(token);
    }

    @Operation(summary = "Get All Favourites based On page and size")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Favourite.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", description = "User Not Found", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping(params = {"page", "size"})
    public List<Favourite> getFavorites
            (@RequestHeader("Authorization") String token, @RequestParam("page") int page, @RequestParam("size") int size)
            throws UserNotFoundException {
        return favouriteService.getAllFavourites(token, page, size);
    }

    @Operation(summary = "Remove From Favorites")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404", description = "User Not Found ", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", description = "FavouriteNotFoundException", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{favouriteId}")
    public void removeFromFavorites(@PathVariable Long favouriteId, @RequestHeader("Authorization") String token)
            throws UserNotFoundException, FavouriteNotFoundException {
        favouriteService.removeFromFavourites(token, favouriteId);
    }
}
