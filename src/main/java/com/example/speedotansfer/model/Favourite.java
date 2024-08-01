package com.example.speedotansfer.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "favourites", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "favId"}))
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favourite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="userId", nullable=false)
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="favId", nullable=false)
    private User favouriteUser;

    @CreationTimestamp
    private LocalDateTime addedAt;

}

