package com.example.speedotansfer.repository;

import com.example.speedotansfer.model.Favourite;
import com.example.speedotansfer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    List<Favourite> getAllByUser(User user);
}
