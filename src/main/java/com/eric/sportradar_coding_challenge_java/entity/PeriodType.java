package com.eric.sportradar_coding_challenge_java.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Lookup table for match period types (HT, FT, ET, PSO).
 * The ordering column provides a stable sort key so periods always display
 * in chronological order (HT=1, FT=2, ET=3, PSO=4) without relying on
 * string comparison.
 * Seeded values: HT, FT, ET, PSO.
 */
@Entity
@Table(name = "period_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeriodType {

    @Id
    @Column(length = 20, nullable = false)
    private String code;

    /** Human-readable period name, e.g. "Half time". */
    @Column(nullable = false, length = 200)
    private String description;

    /** Chronological ordering within a match (HT=1, FT=2, ET=3, PSO=4). */
    @Column(nullable = false)
    private Integer ordering;
}