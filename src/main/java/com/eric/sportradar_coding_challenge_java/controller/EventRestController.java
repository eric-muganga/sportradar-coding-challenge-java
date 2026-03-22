package com.eric.sportradar_coding_challenge_java.controller;

import com.eric.sportradar_coding_challenge_java.dto.request.CreateEventRequest;
import com.eric.sportradar_coding_challenge_java.dto.response.EventDetailDto;
import com.eric.sportradar_coding_challenge_java.dto.response.EventSummaryDto;
import com.eric.sportradar_coding_challenge_java.dto.response.StageDto;
import com.eric.sportradar_coding_challenge_java.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API controller for sports events.
 *
 * Provides JSON endpoints consumed by the frontend form submission
 * and available for external API consumers.
 *
 * Separated from EventViewController deliberately — the REST controller
 * handles data, the view controller handles page rendering. This keeps
 * both controllers focused and testable independently.
 */
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventRestController {

    private final EventService eventService;

    /**
     * Returns all events as JSON, with optional sport and status filters.
     *
     * Both filters are optional and mutually exclusive — if both are
     * provided, sport filter takes precedence.
     *
     * @param sportId    optional sport ID filter
     * @param statusCode optional status code filter (e.g., "played", "scheduled")
     * @return list of event summaries ordered by date ascending
     */
    @GetMapping
    public ResponseEntity<List<EventSummaryDto>> getAll(
            @RequestParam(required = false) Integer sportId,
            @RequestParam(required = false) String statusCode) {
        return ResponseEntity.ok(eventService.getAll(sportId, statusCode));
    }

    /**
     * Returns full details of a single event by ID.
     *
     * @param id event primary key
     * @return full event detail DTO
     * @throws com.eric.sportradar_coding_challenge_java.exception.EventNotFoundException
     *         if no event exists with the given ID — handled by GlobalExceptionHandler
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventDetailDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(eventService.getById(id));
    }

    /**
     * Creates a new sports event.
     *
     * Request body is validated before reaching the service layer.
     * Returns 201 Created with the saved event summary on success.
     *
     * @param request validated event creation request
     * @return created event summary with assigned ID
     */
    @PostMapping
    public ResponseEntity<EventSummaryDto> create(
            @Valid @RequestBody CreateEventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.create(request));
    }

    /**
     * Returns stages for a given competition as JSON.
     * Called from the add-event form via fetch() when the competition
     * dropdown changes — populates the stage dropdown dynamically.
     */
    @GetMapping("/stages")
    public ResponseEntity<List<StageDto>> getStagesByCompetition(
            @RequestParam String competitionId) {
        return ResponseEntity.ok(eventService.getStagesByCompetition(competitionId));
    }
}