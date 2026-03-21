package com.eric.sportradar_coding_challenge_java.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Request body for creating a new sports event.
 *
 * Teams are referenced by ID to allow the frontend to present
 * a dropdown of existing teams rather than free-text input,
 * reducing the risk of duplicate or inconsistent team names.
 *
 * homeTeamId is nullable to support events where one participant
 * is not yet determined (e.g., a final before semi-finals are played).
 */
@Getter
@Setter
public class CreateEventRequest {

    @NotNull(message = "Season is required")
    private Integer season;

    @NotBlank(message = "Status is required")
    private String statusCode;

    @NotNull(message = "Event date is required")
    private LocalDate eventDate;

    private LocalTime eventTime;

    @NotNull(message = "Sport is required")
    private Integer sportId;

    /** Nullable — home team may be TBD in knockout stages */
    private Integer homeTeamId;

    @NotNull(message = "Away team is required")
    private Integer awayTeamId;

    private Integer venueId;

    @NotBlank(message = "Competition is required")
    private String competitionId;

    private String stageId;

    private String groupName;

    private String description;
}