package com.eric.sportradar_coding_challenge_java;


import com.eric.sportradar_coding_challenge_java.dto.request.CreateEventRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the sports events REST API.
 *
 * Uses Testcontainers to spin up a real PostgreSQL instance — the same
 * database engine used in production. Flyway migrations run automatically
 * on startup, including seed data, so all tests run against a realistic
 * data set.
 *
 * @ServiceConnection wires the container's dynamic port into Spring's
 * datasource configuration automatically — no manual URL overrides needed.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class EventIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // ── GET /api/events ───────────────────────────────────────────────────

    @Test
    void getAllEvents_shouldReturn200_withSeededData() throws Exception {
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThan(0)));
    }

    @Test
    void getAllEvents_filteredBySport_shouldReturnOnlyMatchingSport() throws Exception {
        // Sport ID 1 = Football from seed data
        mockMvc.perform(get("/api/events?sportId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].sportName").value("Football"));
    }

    @Test
    void getAllEvents_filteredByStatus_shouldReturnOnlyMatchingStatus() throws Exception {
        mockMvc.perform(get("/api/events?statusCode=scheduled"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].statusCode").value("scheduled"));
    }

    // ── GET /api/events/{id} ──────────────────────────────────────────────

    @Test
    void getEventById_withValidId_shouldReturn200() throws Exception {
        // Event ID 1 is seeded — Bournemouth vs Man Utd
        mockMvc.perform(get("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.homeTeamName").exists())
                .andExpect(jsonPath("$.awayTeamName").exists())
                .andExpect(jsonPath("$.statusCode").exists());
    }

    @Test
    void getEventById_withInvalidId_shouldReturn404WithMessage() throws Exception {
        mockMvc.perform(get("/api/events/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Event with id 999999 not found"));
    }

    // ── POST /api/events ──────────────────────────────────────────────────

    @Test
    void createEvent_withValidRequest_shouldReturn201() throws Exception {
        CreateEventRequest request = new CreateEventRequest();
        request.setSeason(2026);
        request.setStatusCode("scheduled");
        request.setEventDate(LocalDate.of(2026, 4, 1));
        request.setEventTime(LocalTime.of(15, 0));
        request.setSportId(1);
        request.setCompetitionId("premier-league");
        request.setHomeTeamId(1);  // Bournemouth
        request.setAwayTeamId(2);  // Manchester United

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.statusCode").value("scheduled"));
    }

    @Test
    void createEvent_withBlankTitle_shouldReturn400WithValidationError() throws Exception {
        // Missing required fields — season, statusCode, eventDate, sportId,
        // competitionId, awayTeamId
        CreateEventRequest request = new CreateEventRequest();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createEvent_withNullHomeTeam_shouldReturn201() throws Exception {
        // TBD home team is valid for knockout stage finals
        CreateEventRequest request = new CreateEventRequest();
        request.setSeason(2024);
        request.setStatusCode("scheduled");
        request.setEventDate(LocalDate.of(2024, 5, 1));
        request.setSportId(1);
        request.setCompetitionId("afc-champions-league");
        request.setHomeTeamId(null);  // TBD
        request.setAwayTeamId(9);     // Urawa Reds

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.homeTeamName").value("TBD"));
    }

    @Test
    void createEvent_withNoStage_shouldReturn201WithoutFKViolation() throws Exception {
        // Premier League events have no stage — empty stageId must not
        // trigger the composite FK constraint
        CreateEventRequest request = new CreateEventRequest();
        request.setSeason(2026);
        request.setStatusCode("scheduled");
        request.setEventDate(LocalDate.of(2026, 4, 5));
        request.setEventTime(LocalTime.of(15, 0));
        request.setSportId(1);
        request.setCompetitionId("premier-league");
        request.setHomeTeamId(3);  // Brighton
        request.setAwayTeamId(4);  // Liverpool
        request.setStageId("");    // No stage — league match

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    // ── GET /api/events/stages ────────────────────────────────────────────

    @Test
    void getStages_forCompetitionWithStages_shouldReturnStageList() throws Exception {
        mockMvc.perform(get("/api/events/stages?competitionId=afc-champions-league"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void getStages_forLeagueWithNoStages_shouldReturnEmptyList() throws Exception {
        // Premier League has no stages defined — should return empty array
        // not an error
        mockMvc.perform(get("/api/events/stages?competitionId=premier-league"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}