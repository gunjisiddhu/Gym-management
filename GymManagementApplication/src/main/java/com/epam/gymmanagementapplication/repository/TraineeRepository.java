package com.epam.gymmanagementapplication.repository;

import com.epam.gymmanagementapplication.model.Trainee;
import com.epam.gymmanagementapplication.model.Trainer;
import com.epam.gymmanagementapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Integer> {
    Optional<Trainee> findByUser(User user);

    @Query("SELECT t FROM Trainer t WHERE t NOT IN :trainerList")
    List<Trainer> findTrainersNotInList(@Param("trainerList") List<Trainer> trainerList);
}
