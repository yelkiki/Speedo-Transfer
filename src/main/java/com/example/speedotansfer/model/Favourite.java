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
public class Favourite implements Serializable {

//    @EmbeddedId
//    private FavouriteId id;
//
//    @Column(name="test")
//    private String test;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="userID", nullable=false)
    private User user;


    @ManyToOne
    @JoinColumn(name="favID", nullable=false)
    private User favouriteUser;

}

