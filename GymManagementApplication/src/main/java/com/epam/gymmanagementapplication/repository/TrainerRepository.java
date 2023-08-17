package com.epam.gymmanagementapplication.repository;

import com.epam.gymmanagementapplication.model.Trainer;
import com.epam.gymmanagementapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
    Optional<Trainer> findByUser(User user);
}
