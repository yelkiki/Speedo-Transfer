package com.example.speedotansfer.repository;

import com.example.speedotansfer.model.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
}
