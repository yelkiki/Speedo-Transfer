package com.example.speedotansfer.repository;

import com.example.speedotansfer.model.Favourite;
import com.example.speedotansfer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    List<Favourite> getAllByUser(User user);
    List<Favourite> getAllByUser(User user, Pageable pageable);
}
