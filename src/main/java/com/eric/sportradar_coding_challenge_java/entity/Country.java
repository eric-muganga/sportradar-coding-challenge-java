package com.eric.sportradar_coding_challenge_java.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a country used to associate teams and venues with their origin.
 * Uses ISO 3-letter country codes as the primary key (e.g., "KSA", "JPN").
 */
@Entity
@Table(name = "country")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country {

    /** ISO-3166-1 alpha-3 code, e.g. "KSA", "AUT". */
    @Id
    @Column(length = 3, nullable = false)
    private String code;

    /** Full country name, e.g. "Saudi Arabia". */
    @Column(nullable = false, unique = true, length = 100)
    private String name;
}