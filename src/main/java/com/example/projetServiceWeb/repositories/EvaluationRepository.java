package com.example.projetServiceWeb.repositories;

import com.example.projetServiceWeb.entities.EvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository  extends JpaRepository<EvaluationEntity, Long> {
    List<EvaluationEntity> findByEvaluateur(String evaluateur);

    Optional<EvaluationEntity> findByIdAndRestaurantId(Long id, Long restaurantId);

}

