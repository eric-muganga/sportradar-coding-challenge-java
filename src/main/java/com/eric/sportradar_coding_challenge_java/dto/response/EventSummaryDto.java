package com.eric.sportradar_coding_challenge_java.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Lightweight DTO for displaying events in the calendar list view.
 * Contains only the fields needed for the summary card — full
 * result and venue details are loaded separately on the detail page.
 *
 * Deliberately omits goals, cards, and score-by-period to keep
 * the list query fast and the response payload small.
 */
@Getter
@Builder
public class EventSummaryDto {

    private Integer id;

    /** Human-readable date for display (e.g., "Sat., 18.07.2019") */
    private LocalDate eventDate;

    private LocalTime eventTime;

    private String sportName;

    /** Display name of the home team. Null if TBD (e.g., unresolved finals bracket) */
    private String homeTeamName;

    /** Display name of the away team. Null if TBD */
    private String awayTeamName;

    private String homeTeamAbbreviation;

    private String awayTeamAbbreviation;

    /** Status code: played, scheduled, cancelled, postponed */
    private String statusCode;

    private String competitionName;

    /** Score summary — only populated when status is "played" */
    private Integer homeGoals;

    private Integer awayGoals;
}