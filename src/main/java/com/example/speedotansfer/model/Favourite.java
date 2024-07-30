package com.example.speedotansfer.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "favourite")
@Builder
@NoArgsConstructor
@AllArgsConstructor
// Later
public class Favourite {

    // try to make user1,user2 both id
    @Id
    private Long id;

    // userID
    @ManyToOne
    @JoinColumn(name="userID", nullable=false)
    private User user;


    @ManyToOne
    @JoinColumn(name="favID", nullable=false)
    private User favouriteUser;

}
