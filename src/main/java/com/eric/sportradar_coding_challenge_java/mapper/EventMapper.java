package com.eric.sportradar_coding_challenge_java.mapper;

import com.eric.sportradar_coding_challenge_java.dto.response.EventDetailDto;
import com.eric.sportradar_coding_challenge_java.dto.response.EventSummaryDto;
import com.eric.sportradar_coding_challenge_java.entity.Event;
import com.eric.sportradar_coding_challenge_java.entity.EventResult;
import org.springframework.stereotype.Component;

/**
 * Maps Event entities to DTOs.
 *
 * All mapping happens inside the service transaction, ensuring
 * lazy-loaded relationships are still within the persistence context
 * when accessed. This prevents LazyInitializationException that would
 * occur if mapping happened after the transaction closed.
 *
 * Null checks are required throughout because:
 * - homeTeam is nullable (TBD participants in knockout stages)
 * - venue is nullable (all 5 AFC CL events in the JSON have stadium: null)
 * - result is nullable (scheduled events have no result yet)
 * - eventTime "00:00:00" is treated as TBD and displayed as such
 */
@Component
public class EventMapper {

    /**
     * Maps an Event to a lightweight summary DTO for the list view.
     * Only maps fields needed for the calendar card — avoids loading
     * venue and stage data that isn't displayed in the list.
     */
    public EventSummaryDto toSummaryDto(Event event) {
        EventResult result = event.getResult();

        return EventSummaryDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .eventTime(event.getEventTime())
                .sportName(event.getSport() != null
                        ? event.getSport().getName() : null)
                .homeTeamName(event.getHomeTeam() != null
                        ? event.getHomeTeam().getName() : "TBD")
                .awayTeamName(event.getAwayTeam() != null
                        ? event.getAwayTeam().getName() : "TBD")
                .homeTeamAbbreviation(event.getHomeTeam() != null
                        ? event.getHomeTeam().getAbbreviation() : "TBD")
                .awayTeamAbbreviation(event.getAwayTeam() != null
                        ? event.getAwayTeam().getAbbreviation() : "TBD")
                .statusCode(event.getStatus() != null
                        ? event.getStatus().getCode() : null)
                .competitionName(event.getCompetition() != null
                        ? event.getCompetition().getName() : null)
                .homeGoals(result != null ? result.getHomeGoals() : null)
                .awayGoals(result != null ? result.getAwayGoals() : null)
                .build();
    }

    /**
     * Maps an Event to a full detail DTO for the event detail page.
     * Includes venue, stage, result, and winner information.
     */
    public EventDetailDto toDetailDto(Event event) {
        EventResult result = event.getResult();

        return EventDetailDto.builder()
                .id(event.getId())
                .season(event.getSeason())
                .eventDate(event.getEventDate())
                .eventTime(event.getEventTime())
                .sportName(event.getSport() != null
                        ? event.getSport().getName() : null)

                // Teams — null-safe, TBD for unresolved participants
                .homeTeamName(event.getHomeTeam() != null
                        ? event.getHomeTeam().getName() : "TBD")
                .homeTeamOfficialName(event.getHomeTeam() != null
                        ? event.getHomeTeam().getOfficialName() : null)
                .homeTeamCountry(event.getHomeTeam() != null
                        && event.getHomeTeam().getCountry() != null
                        ? event.getHomeTeam().getCountry().getName() : null)
                .awayTeamName(event.getAwayTeam() != null
                        ? event.getAwayTeam().getName() : "TBD")
                .awayTeamOfficialName(event.getAwayTeam() != null
                        ? event.getAwayTeam().getOfficialName() : null)
                .awayTeamCountry(event.getAwayTeam() != null
                        && event.getAwayTeam().getCountry() != null
                        ? event.getAwayTeam().getCountry().getName() : null)

                // Status
                .statusCode(event.getStatus() != null
                        ? event.getStatus().getCode() : null)
                .statusDescription(event.getStatus() != null
                        ? event.getStatus().getDescription() : null)

                // Competition and stage
                .competitionName(event.getCompetition() != null
                        ? event.getCompetition().getName() : null)
                .stageName(event.getStageId() != null
                        ? event.getStageId() : null)

                // Venue — nullable, all sample JSON events have stadium: null
                .venueName(event.getVenue() != null
                        ? event.getVenue().getName() : null)
                .venueCity(event.getVenue() != null
                        ? event.getVenue().getCity() : null)

                // Result — null for scheduled events
                .homeGoals(result != null ? result.getHomeGoals() : null)
                .awayGoals(result != null ? result.getAwayGoals() : null)
                .winnerTeamName(result != null
                        && result.getWinnerTeam() != null
                        ? result.getWinnerTeam().getName() : null)

                .groupName(event.getGroupName())
                .attendance(event.getAttendance())
                .description(event.getDescription())
                .build();
    }

}