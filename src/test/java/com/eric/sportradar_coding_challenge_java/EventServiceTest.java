package com.eric.sportradar_coding_challenge_java;

import com.eric.sportradar_coding_challenge_java.dto.request.CreateEventRequest;
import com.eric.sportradar_coding_challenge_java.dto.response.EventDetailDto;
import com.eric.sportradar_coding_challenge_java.dto.response.EventSummaryDto;
import com.eric.sportradar_coding_challenge_java.entity.*;
import com.eric.sportradar_coding_challenge_java.exception.EventNotFoundException;
import com.eric.sportradar_coding_challenge_java.mapper.EventMapper;
import com.eric.sportradar_coding_challenge_java.repository.*;
import com.eric.sportradar_coding_challenge_java.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EventService.
 *
 * All dependencies are mocked — no database, no Spring context.
 * Tests focus on business logic: filtering behaviour, exception paths,
 * and correct delegation to repositories and mapper.
 *
 * Each test follows the Arrange / Act / Assert pattern.
 */
@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock private EventRepository eventRepository;
    @Mock private SportRepository sportRepository;
    @Mock private TeamRepository teamRepository;
    @Mock private CompetitionRepository competitionRepository;
    @Mock private EventStatusRepository eventStatusRepository;
    @Mock private StageRepository stageRepository;
    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    private Event sampleEvent;
    private EventSummaryDto sampleSummaryDto;
    private EventDetailDto sampleDetailDto;

    @BeforeEach
    void setUp() {
        Sport sport = Sport.builder().id(1).name("Football").build();

        EventStatus status = new EventStatus("scheduled", "Match is scheduled");

        Team homeTeam = Team.builder()
                .id(1).name("Brighton").abbreviation("BHA")
                .slug("brighton-hove-albion").build();

        Team awayTeam = Team.builder()
                .id(2).name("Liverpool").abbreviation("LIV")
                .slug("liverpool-fc").build();

        Competition competition = new Competition("premier-league", "Premier League");

        sampleEvent = Event.builder()
                .id(1)
                .season(2026)
                .status(status)
                .eventDate(LocalDate.of(2026, 3, 21))
                .eventTime(LocalTime.of(12, 30))
                .sport(sport)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .competition(competition)
                .build();

        sampleSummaryDto = EventSummaryDto.builder()
                .id(1)
                .eventDate(LocalDate.of(2026, 3, 21))
                .eventTime(LocalTime.of(12, 30))
                .sportName("Football")
                .homeTeamName("Brighton")
                .awayTeamName("Liverpool")
                .statusCode("scheduled")
                .competitionName("Premier League")
                .build();

        sampleDetailDto = EventDetailDto.builder()
                .id(1)
                .season(2026)
                .eventDate(LocalDate.of(2026, 3, 21))
                .sportName("Football")
                .homeTeamName("Brighton")
                .awayTeamName("Liverpool")
                .statusCode("scheduled")
                .competitionName("Premier League")
                .build();
    }

    // ── getAll ────────────────────────────────────────────────────────────

    @Test
    void getAll_withNoFilters_shouldCallFindAllWithDetails() {
        when(eventRepository.findAllWithDetails()).thenReturn(List.of(sampleEvent));
        when(eventMapper.toSummaryDto(sampleEvent)).thenReturn(sampleSummaryDto);

        List<EventSummaryDto> result = eventService.getAll(null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getHomeTeamName()).isEqualTo("Brighton");
        verify(eventRepository).findAllWithDetails();
        verify(eventRepository, never()).findAllBySportId(any());
        verify(eventRepository, never()).findAllByStatusCode(any());
    }

    @Test
    void getAll_withSportFilter_shouldCallFindAllBySportId() {
        when(eventRepository.findAllBySportId(1)).thenReturn(List.of(sampleEvent));
        when(eventMapper.toSummaryDto(sampleEvent)).thenReturn(sampleSummaryDto);

        List<EventSummaryDto> result = eventService.getAll(1, null);

        assertThat(result).hasSize(1);
        verify(eventRepository).findAllBySportId(1);
        verify(eventRepository, never()).findAllWithDetails();
    }

    @Test
    void getAll_withStatusFilter_shouldCallFindAllByStatusCode() {
        when(eventRepository.findAllByStatusCode("scheduled"))
                .thenReturn(List.of(sampleEvent));
        when(eventMapper.toSummaryDto(sampleEvent)).thenReturn(sampleSummaryDto);

        List<EventSummaryDto> result = eventService.getAll(null, "scheduled");

        assertThat(result).hasSize(1);
        verify(eventRepository).findAllByStatusCode("scheduled");
        verify(eventRepository, never()).findAllWithDetails();
    }

    @Test
    void getAll_withBothFilters_sportShouldTakePrecedence() {
        // When both filters are provided, sport filter takes precedence
        when(eventRepository.findAllBySportId(1)).thenReturn(List.of(sampleEvent));
        when(eventMapper.toSummaryDto(sampleEvent)).thenReturn(sampleSummaryDto);

        List<EventSummaryDto> result = eventService.getAll(1, "scheduled");

        verify(eventRepository).findAllBySportId(1);
        verify(eventRepository, never()).findAllByStatusCode(any());
        verify(eventRepository, never()).findAllWithDetails();
        assertThat(result).hasSize(1);
    }

    @Test
    void getAll_whenNoEventsFound_shouldReturnEmptyList() {
        when(eventRepository.findAllWithDetails()).thenReturn(List.of());

        List<EventSummaryDto> result = eventService.getAll(null, null);

        assertThat(result).isEmpty();
    }

    // ── getById ───────────────────────────────────────────────────────────

    @Test
    void getById_whenEventExists_shouldReturnDetailDto() {
        when(eventRepository.findByIdWithFullDetails(1))
                .thenReturn(Optional.of(sampleEvent));
        when(eventMapper.toDetailDto(sampleEvent)).thenReturn(sampleDetailDto);

        EventDetailDto result = eventService.getById(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getHomeTeamName()).isEqualTo("Brighton");
        assertThat(result.getSeason()).isEqualTo(2026);
    }

    @Test
    void getById_whenEventDoesNotExist_shouldThrowEventNotFoundException() {
        when(eventRepository.findByIdWithFullDetails(999))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.getById(999))
                .isInstanceOf(EventNotFoundException.class)
                .hasMessageContaining("999");
    }

    // ── create ────────────────────────────────────────────────────────────

    @Test
    void create_withValidRequest_shouldSaveAndReturnSummaryDto() {
        CreateEventRequest request = new CreateEventRequest();
        request.setSeason(2026);
        request.setStatusCode("scheduled");
        request.setEventDate(LocalDate.of(2026, 3, 21));
        request.setEventTime(LocalTime.of(12, 30));
        request.setSportId(1);
        request.setCompetitionId("premier-league");
        request.setHomeTeamId(1);
        request.setAwayTeamId(2);

        EventStatus status = new EventStatus("scheduled", "Match is scheduled");
        Sport sport = Sport.builder().id(1).name("Football").build();
        Competition competition = new Competition("premier-league", "Premier League");
        Team homeTeam = Team.builder().id(1).name("Brighton").slug("brighton-hove-albion").build();
        Team awayTeam = Team.builder().id(2).name("Liverpool").slug("liverpool-fc").build();

        when(eventStatusRepository.findById("scheduled")).thenReturn(Optional.of(status));
        when(sportRepository.findById(1)).thenReturn(Optional.of(sport));
        when(competitionRepository.findById("premier-league")).thenReturn(Optional.of(competition));
        when(teamRepository.findById(1)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findById(2)).thenReturn(Optional.of(awayTeam));
        when(eventRepository.save(any(Event.class))).thenReturn(sampleEvent);
        when(eventMapper.toSummaryDto(sampleEvent)).thenReturn(sampleSummaryDto);

        EventSummaryDto result = eventService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.getHomeTeamName()).isEqualTo("Brighton");
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void create_withNullHomeTeam_shouldSaveEventWithNullHomeTeam() {
        // Knockout stage events may have TBD home team
        CreateEventRequest request = new CreateEventRequest();
        request.setSeason(2024);
        request.setStatusCode("scheduled");
        request.setEventDate(LocalDate.of(2024, 1, 19));
        request.setSportId(1);
        request.setCompetitionId("afc-champions-league");
        request.setHomeTeamId(null);  // TBD
        request.setAwayTeamId(2);

        EventStatus status = new EventStatus("scheduled", "Match is scheduled");
        Sport sport = Sport.builder().id(1).name("Football").build();
        Competition competition = new Competition("afc-champions-league", "AFC Champions League");
        Team awayTeam = Team.builder().id(2).name("Urawa Reds").slug("urawa-red-diamonds").build();

        when(eventStatusRepository.findById("scheduled")).thenReturn(Optional.of(status));
        when(sportRepository.findById(1)).thenReturn(Optional.of(sport));
        when(competitionRepository.findById("afc-champions-league")).thenReturn(Optional.of(competition));
        when(teamRepository.findById(2)).thenReturn(Optional.of(awayTeam));
        when(eventRepository.save(any(Event.class))).thenReturn(sampleEvent);
        when(eventMapper.toSummaryDto(sampleEvent)).thenReturn(sampleSummaryDto);

        EventSummaryDto result = eventService.create(request);

        assertThat(result).isNotNull();
        // homeTeamId is null — teamRepository should never be called for home team
        verify(teamRepository, never()).findById(null);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void create_withUnknownStatus_shouldThrowIllegalArgumentException() {
        CreateEventRequest request = new CreateEventRequest();
        request.setStatusCode("invalid-status");
        request.setSportId(1);
        request.setCompetitionId("premier-league");
        request.setAwayTeamId(2);
        request.setSeason(2026);
        request.setEventDate(LocalDate.of(2026, 3, 21));

        when(eventStatusRepository.findById("invalid-status")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("invalid-status");
    }

    @Test
    void create_withUnknownSport_shouldThrowIllegalArgumentException() {
        CreateEventRequest request = new CreateEventRequest();
        request.setStatusCode("scheduled");
        request.setSportId(99);
        request.setCompetitionId("premier-league");
        request.setAwayTeamId(2);
        request.setSeason(2026);
        request.setEventDate(LocalDate.of(2026, 3, 21));

        when(eventStatusRepository.findById("scheduled"))
                .thenReturn(Optional.of(new EventStatus("scheduled", "Match is scheduled")));
        when(sportRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_withBlankStageId_shouldSetBothStageFieldsToNull() {
        // Empty string stage should be treated as no stage — prevents FK violation
        CreateEventRequest request = new CreateEventRequest();
        request.setSeason(2026);
        request.setStatusCode("scheduled");
        request.setEventDate(LocalDate.of(2026, 3, 21));
        request.setSportId(1);
        request.setCompetitionId("premier-league");
        request.setAwayTeamId(2);
        request.setStageId("");  // empty string from "No stage" dropdown

        EventStatus status = new EventStatus("scheduled", "Match is scheduled");
        Sport sport = Sport.builder().id(1).name("Football").build();
        Competition competition = new Competition("premier-league", "Premier League");
        Team awayTeam = Team.builder().id(2).name("Liverpool").slug("liverpool-fc").build();

        when(eventStatusRepository.findById("scheduled")).thenReturn(Optional.of(status));
        when(sportRepository.findById(1)).thenReturn(Optional.of(sport));
        when(competitionRepository.findById("premier-league")).thenReturn(Optional.of(competition));
        when(teamRepository.findById(2)).thenReturn(Optional.of(awayTeam));

        // Capture the Event that gets passed to save()
        when(eventRepository.save(argThat(event ->
                event.getStageId() == null && event.getStageCompetition() == null
        ))).thenReturn(sampleEvent);
        when(eventMapper.toSummaryDto(sampleEvent)).thenReturn(sampleSummaryDto);

        EventSummaryDto result = eventService.create(request);

        assertThat(result).isNotNull();
        // Verify save was called with null stage fields
        verify(eventRepository).save(argThat(event ->
                event.getStageId() == null && event.getStageCompetition() == null
        ));
    }
}
