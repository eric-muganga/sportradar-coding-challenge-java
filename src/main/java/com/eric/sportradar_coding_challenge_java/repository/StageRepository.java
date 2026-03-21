package com.eric.sportradar_coding_challenge_java.repository;

import com.eric.sportradar_coding_challenge_java.entity.Stage;
import com.eric.sportradar_coding_challenge_java.entity.Stage.StageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for stage lookup data.
 * Used to populate the stage dropdown on the add-event form,
 * filtered by competition so only valid stages are shown.
 */
@Repository
public interface StageRepository extends JpaRepository<Stage, StageId> {

    /**
     * Returns all stages for a given competition.
     * Called when the user selects a competition on the add-event form
     * to populate the stage dropdown with only valid options.
     */
    @Query("SELECT s FROM Stage s WHERE s.competition.id = :competitionId ORDER BY s.ordering ASC")
    List<Stage> findByCompetitionId(@Param("competitionId") String competitionId);
}
