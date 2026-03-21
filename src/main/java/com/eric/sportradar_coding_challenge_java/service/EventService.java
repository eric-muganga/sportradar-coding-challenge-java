package com.eric.sportradar_coding_challenge_java.service;

import com.eric.sportradar_coding_challenge_java.dto.request.CreateEventRequest;
import com.eric.sportradar_coding_challenge_java.dto.response.EventDetailDto;
import com.eric.sportradar_coding_challenge_java.dto.response.EventSummaryDto;
import com.eric.sportradar_coding_challenge_java.dto.response.StageDto;
import com.eric.sportradar_coding_challenge_java.entity.*;
import com.eric.sportradar_coding_challenge_java.exception.EventNotFoundException;
import com.eric.sportradar_coding_challenge_java.mapper.EventMapper;
import com.eric.sportradar_coding_challenge_java.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic layer for sports events.
 *
 * All read methods use @Transactional(readOnly = true) which:
 * 1. Disables Hibernate dirty checking — no entity scan at transaction end
 * 2. Signals the database driver to skip undo log generation
 * 3. Allows connection pool routing to read replicas if configured
 *
 * The getAll methods delegate to JOIN FETCH queries in EventRepository,
 * ensuring all related data is loaded in a single SQL statement.
 * This is the primary defence against the N+1 problem — if lazy
 * relationships were accessed after the query, Hibernate would fire
 * one additional SELECT per event per relationship.
 */
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final SportRepository sportRepository;
    private final TeamRepository teamRepository;
    private final CompetitionRepository competitionRepository;
    private final EventStatusRepository eventStatusRepository;
    private final EventMapper eventMapper;
    private final StageRepository stageRepository;

    /**
     * Returns all events as summary DTOs, optionally filtered by sport or status.
     *
     * Filtering is applied at the database level via separate repository
     * queries rather than in-memory filtering, keeping the result set
     * minimal and the query efficient.
     *
     * @param sportId    optional sport filter — null means no filter
     * @param statusCode optional status filter — null means no filter
     * @return list of summary DTOs ordered by date and time ascending
     */
    @Transactional(readOnly = true)
    public List<EventSummaryDto> getAll(Integer sportId, String statusCode) {
        List<Event> events;

        if (sportId != null) {
            events = eventRepository.findAllBySportId(sportId);
        } else if (statusCode != null && !statusCode.isBlank()) {
            events = eventRepository.findAllByStatusCode(statusCode);
        } else {
            events = eventRepository.findAllWithDetails();
        }

        // Mapping happens inside the transaction — lazy relationships
        // are still accessible within the persistence context
        return events.stream()
                .map(eventMapper::toSummaryDto)
                .toList();
    }

    /**
     * Returns a single event with full details for the detail page.
     * Uses a separate query that also fetches venue and result,
     * which are not loaded in the list query.
     *
     * @param id event primary key
     * @throws EventNotFoundException if no event exists with the given id
     */
    @Transactional(readOnly = true)
    public EventDetailDto getById(Integer id) {
        Event event = eventRepository.findByIdWithFullDetails(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        return eventMapper.toDetailDto(event);
    }

    /**
     * Creates a new sports event from the provided request.
     *
     * Each FK reference (sport, teams, competition, status) is resolved
     * from the database before building the entity, ensuring referential
     * integrity is enforced at the application layer before the INSERT.
     *
     * @param request validated request body
     * @return summary DTO of the created event
     * @throws IllegalArgumentException if any referenced entity does not exist
     */
    @Transactional
    public EventSummaryDto create(CreateEventRequest request) {

        // Resolve all FK references — fail fast if any don't exist
        EventStatus status = eventStatusRepository.findById(request.getStatusCode())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown status: " + request.getStatusCode()));

        Sport sport = sportRepository.findById(request.getSportId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown sport id: " + request.getSportId()));

        Competition competition = competitionRepository.findById(request.getCompetitionId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown competition id: " + request.getCompetitionId()));

        // Home team is nullable — TBD in knockout brackets
        Team homeTeam = null;
        if (request.getHomeTeamId() != null) {
            homeTeam = teamRepository.findById(request.getHomeTeamId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Unknown home team id: " + request.getHomeTeamId()));
        }

        Team awayTeam = teamRepository.findById(request.getAwayTeamId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown away team id: " + request.getAwayTeamId()));

        Event event = Event.builder()
                .season(request.getSeason())
                .status(status)
                .eventDate(request.getEventDate())
                .eventTime(request.getEventTime())
                .sport(sport)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .competition(competition)
                .stageId(request.getStageId())
                .stageId(isBlank(request.getStageId()) ? null : request.getStageId())
                .stageCompetition(isBlank(request.getStageId()) ? null : request.getCompetitionId())
                .groupName(request.getGroupName())
                .description(request.getDescription())
                .build();

        Event saved = eventRepository.save(event);
        return eventMapper.toSummaryDto(saved);
    }

    /**
     * Returns all sports for populating filter dropdowns on the frontend.
     */
    @Transactional(readOnly = true)
    public List<Sport> getAllSports() {
        return sportRepository.findAll();
    }

    /**
     * Returns all event statuses for populating filter and form dropdowns.
     */
    @Transactional(readOnly = true)
    public List<EventStatus> getAllStatuses() {
        return eventStatusRepository.findAll();
    }

    /**
     * Returns all competitions for populating the add-event form dropdown.
     */
    @Transactional(readOnly = true)
    public List<Competition> getAllCompetitions() {
        return competitionRepository.findAll();
    }

    /**
     * Returns all teams for populating the add-event form dropdowns.
     */
    @Transactional(readOnly = true)
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    /**
     * Returns all stages for a given competition, ordered by stage progression.
     * Used to populate the stage dropdown when a competition is selected.
     */
    @Transactional(readOnly = true)
    public List<StageDto> getStagesByCompetition(String competitionId) {
        return stageRepository.findByCompetitionId(competitionId)
                .stream()
                .map(stage -> StageDto.builder()
                        .id(stage.getId())
                        .name(stage.getName())
                        .ordering(stage.getOrdering())
                        .build())
                .toList();
    }

    /**
     * Returns all stages across all competitions.
     * Used to populate the stage dropdown on initial form load
     * before a competition is selected.
     */
    @Transactional(readOnly = true)
    public List<StageDto> getAllStages() {
        return stageRepository.findAll()
                .stream()
                .map(stage -> StageDto.builder()
                        .id(stage.getId())
                        .name(stage.getName())
                        .ordering(stage.getOrdering())
                        .build())
                .toList();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}