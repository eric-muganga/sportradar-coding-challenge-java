package com.eric.sportradar_coding_challenge_java.repository;

import com.eric.sportradar_coding_challenge_java.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for competition data (e.g., AFC Champions League).
 */
@Repository
public interface CompetitionRepository extends JpaRepository<Competition, String> {
}