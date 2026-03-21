package com.eric.sportradar_coding_challenge_java.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Full DTO for the event detail page.
 * Includes all match information: teams, result, venue, competition,
 * and stage. Goals and cards are intentionally excluded from this
 * DTO — they would be added as nested lists if the project were extended.
 */
@Getter
@Builder
public class EventDetailDto {

    private Integer id;
    private Integer season;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private String sportName;

    // Teams
    private String homeTeamName;
    private String homeTeamOfficialName;
    private String homeTeamCountry;
    private String awayTeamName;
    private String awayTeamOfficialName;
    private String awayTeamCountry;

    // Status
    private String statusCode;
    private String statusDescription;

    // Competition and stage
    private String competitionName;
    private String stageName;

    // Venue
    private String venueName;
    private String venueCity;

    // Result — null if not yet played
    private Integer homeGoals;
    private Integer awayGoals;
    private String winnerTeamName;

    private String groupName;
    private Integer attendance;
    private String description;
}
