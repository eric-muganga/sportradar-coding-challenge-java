package com.eric.sportradar_coding_challenge_java.repository;

import com.eric.sportradar_coding_challenge_java.entity.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for event status lookup data.
 * Used to populate status filter dropdowns and validate
 * status codes on event creation.
 */
@Repository
public interface EventStatusRepository extends JpaRepository<EventStatus, String> {
}