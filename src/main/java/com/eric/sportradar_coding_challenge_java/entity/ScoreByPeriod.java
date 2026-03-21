package com.eric.sportradar_coding_challenge_java.entity;

import lombok.*;
import jakarta.persistence.*;

/**
 * Score at the end of a specific match period.
 *
 * Maps to the "result.scoreByPeriods" array in the JSON.
 *
 * One row per period per event is enforced by the UNIQUE (_event, _period)
 * constraint, mirrored here in @Table so Hibernate validates it on startup.
 *
 * The _period FK to period_type:
 * - Ensures only valid period codes are stored (HT, FT, ET, PSO)
 * - Provides a stable ordering column for chronological display
 * - Fixes the transitive dependency a free-text VARCHAR would create
 */
@Entity
@Table(
        name = "score_by_period",
        uniqueConstraints = @UniqueConstraint(
                name        = "uq_score_by_period_event_period",
                columnNames = {"_event", "_period"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreByPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_event", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_period", nullable = false)
    private PeriodType period;

    @Column(name = "home_goals", nullable = false)
    private Integer homeGoals = 0;

    @Column(name = "away_goals", nullable = false)
    private Integer awayGoals = 0;
}