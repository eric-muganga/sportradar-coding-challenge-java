package com.eric.sportradar_coding_challenge_java.repository;

import com.eric.sportradar_coding_challenge_java.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for sports events.
 *
 * All queries use JOIN FETCH to load related entities in a single
 * SQL query, preventing the N+1 problem that would occur if lazy
 * relationships were accessed after the transaction closes.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    /**
     * Retrieves all events with their core relationships eagerly loaded.
     * A single JOIN query fetches event + sport + status + home team +
     * away team + competition, avoiding N+1 queries on the events list page.
     *
     * Result is stored as a Set internally by JPQL to deduplicate rows
     * from the JOIN, then sorted by date ascending.
     */
    @Query("""
        SELECT DISTINCT e FROM Event e
        LEFT JOIN FETCH e.status
        LEFT JOIN FETCH e.sport
        LEFT JOIN FETCH e.homeTeam ht
        LEFT JOIN FETCH ht.country
        LEFT JOIN FETCH e.awayTeam at
        LEFT JOIN FETCH at.country
        LEFT JOIN FETCH e.competition
        LEFT JOIN FETCH e.result
        ORDER BY e.eventDate ASC, e.eventTime ASC
        """)
    List<Event> findAllWithDetails();

    /**
     * Retrieves all events filtered by sport ID.
     * Uses the same JOIN FETCH strategy as findAllWithDetails()
     * to maintain single-query efficiency.
     */
    @Query("""
            SELECT DISTINCT e FROM Event e
            LEFT JOIN FETCH e.status
            LEFT JOIN FETCH e.sport s
            LEFT JOIN FETCH e.homeTeam ht
            LEFT JOIN FETCH ht.country
            LEFT JOIN FETCH e.awayTeam at
            LEFT JOIN FETCH at.country
            LEFT JOIN FETCH e.competition
            LEFT JOIN FETCH e.result
            WHERE s.id = :sportId
            ORDER BY e.eventDate ASC, e.eventTime ASC
            """)
    List<Event> findAllBySportId(@Param("sportId") Integer sportId);

    /**
     * Retrieves all events filtered by status code (e.g., "played", "scheduled").
     */
    @Query("""
            SELECT DISTINCT e FROM Event e
            LEFT JOIN FETCH e.status st
            LEFT JOIN FETCH e.sport
            LEFT JOIN FETCH e.homeTeam ht
            LEFT JOIN FETCH ht.country
            LEFT JOIN FETCH e.awayTeam at
            LEFT JOIN FETCH at.country
            LEFT JOIN FETCH e.competition
            LEFT JOIN FETCH e.result
            WHERE st.code = :statusCode
            ORDER BY e.eventDate ASC, e.eventTime ASC
            """)
    List<Event> findAllByStatusCode(@Param("statusCode") String statusCode);

    /**
     * Retrieves a single event with ALL relationships loaded for the detail page.
     * Includes result, venue, and stage in addition to the core relationships.
     */
    @Query("""
            SELECT e FROM Event e
            LEFT JOIN FETCH e.status
            LEFT JOIN FETCH e.sport
            LEFT JOIN FETCH e.homeTeam ht
            LEFT JOIN FETCH ht.country
            LEFT JOIN FETCH e.awayTeam at
            LEFT JOIN FETCH at.country
            LEFT JOIN FETCH e.competition
            LEFT JOIN FETCH e.venue v
            LEFT JOIN FETCH v.country
            LEFT JOIN FETCH e.result r
            LEFT JOIN FETCH r.winnerTeam
            WHERE e.id = :id
            """)
    Optional<Event> findByIdWithFullDetails(@Param("id") Integer id);
}