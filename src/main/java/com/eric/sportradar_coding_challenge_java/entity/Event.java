package com.eric.sportradar_coding_challenge_java.entity;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a sports event (match) in the calendar.
 * Central entity of the domain — links sport, teams, venue,
 * competition, stage, and all match detail tables.
 *
 * homeTeam and awayTeam are nullable to support cases where
 * one or both participants are not yet determined (e.g., finals
 * where qualifiers haven't been decided).
 */
@Entity
@Table(name = "event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_status", nullable = false)
    private EventStatus status;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "event_time")
    private LocalTime eventTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_sport")
    private Sport sport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_home_team")
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_away_team")
    private Team awayTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_venue")
    private Venue venue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_competition")
    private Competition competition;

    @Column(name = "_stage_id", length = 50)
    private String stageId;

    @Column(name = "_stage_competition", length = 100)
    private String stageCompetition;

    @Column(name = "group_name", length = 50)
    private String groupName;

    @Column(name = "home_stage_position")
    private Integer homeStagePosition;

    @Column(name = "away_stage_position")
    private Integer awayStagePosition;

    private Integer attendance;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private EventResult result;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private java.util.List<Goal> goals = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private java.util.List<Card> cards = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private java.util.List<ScoreByPeriod> scoreByPeriods = new java.util.ArrayList<>();

}
