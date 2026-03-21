package com.eric.sportradar_coding_challenge_java.entity;

import lombok.*;
import jakarta.persistence.*;

/**
 * Sports team reference table.
 * Enhancement columns included because they add direct value:
 * - founded_year: standard team profile data for calendar display
 * - _home_venue:  fallback when event._venue is null — all five AFC
 *                 Champions League events in the JSON have stadium: null,
 *                 so the home team's usual stadium is the best available
 *                 venue reference
 */
@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Short display name, e.g. "Al Shabab". */
    @Column(nullable = false, length = 200)
    private String name;

    /** Official registered name, e.g. "Al Shabab FC". */
    @Column(name = "official_name", length = 300)
    private String officialName;

    /** Three-letter abbreviation, e.g. "SHA". */
    @Column(length = 10)
    private String abbreviation;

    /**
     * URL-safe slug — natural upsert key matching the JSON "slug" field.
     * Must be unique across all teams.
     */
    @Column(nullable = false, unique = true, length = 200)
    private String slug;

    /** Year the club was founded, e.g. 1947. */
    @Column(name = "founded_year")
    private Integer foundedYear;

    /**
     * Registered country — FK to country.code.
     * Maps from the JSON "teamCountryCode" field (e.g. "KSA").
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_country")
    private Country country;

    /**
     * Team's usual home stadium — FK to venue.
     * Used as a fallback when event._venue is null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_home_venue")
    private Venue homeVenue;
}
