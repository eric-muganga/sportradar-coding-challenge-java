package com.eric.sportradar_coding_challenge_java.repository;

import com.eric.sportradar_coding_challenge_java.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for team data.
 * Used when creating new events to look up participating teams.
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
}
