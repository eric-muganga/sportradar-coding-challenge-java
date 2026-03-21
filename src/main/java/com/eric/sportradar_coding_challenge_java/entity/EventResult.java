package com.eric.sportradar_coding_challenge_java.entity;

import lombok.*;
import jakarta.persistence.*;

/**
 * Represents the final result of a played event.
 * Uses a shared primary key with Event (@MapsId) — one result
 * per event, identified by the same ID.
 */
@Entity
@Table(name = "event_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResult {

    @Id
    @Column(name = "_event")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "_event")
    private Event event;

    @Column(name = "home_goals", nullable = false)
    private Integer homeGoals = 0;

    @Column(name = "away_goals", nullable = false)
    private Integer awayGoals = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_winner_team")
    private Team winnerTeam;

    @Column(length = 255)
    private String message;

    @Column(columnDefinition = "TEXT")
    private String notes;
}