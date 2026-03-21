package com.eric.sportradar_coding_challenge_java.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * Lightweight DTO for populating stage dropdowns on the add-event form.
 * Carries only the stage ID and display name — the competition ID is
 * already known from the form context.
 */
@Getter
@Builder
public class StageDto {
    private String id;
    private String name;
    private Integer ordering;
}