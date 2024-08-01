package com.example.speedotansfer.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "favourites")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favourite {

    @EmbeddedId
    private FavouriteId id;

    @ManyToOne
    @JoinColumn(name="userId", nullable=false, insertable = false, updatable = false)
    private User user;


    @ManyToOne
    @JoinColumn(name="favId", nullable=false, insertable = false, updatable = false)
    private User favouriteUser;

}

