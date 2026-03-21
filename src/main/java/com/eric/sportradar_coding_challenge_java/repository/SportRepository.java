package com.eric.sportradar_coding_challenge_java.repository;

import com.eric.sportradar_coding_challenge_java.entity.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for sports lookup data.
 * Used to populate sport filter dropdowns on the frontend.
 */
@Repository
public interface SportRepository extends JpaRepository<Sport, Integer> {
}
