package com.eric.sportradar_coding_challenge_java.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Sports venue (stadium) reference table.
 *
 * Venue data is independent of any specific event, satisfying 2NF by design.
 *
 * Enhancement columns included because they add direct value to the calendar:
 * - address: allows fans to navigate to the stadium
 * - surface:  relevant match context ("grass", "artificial", "indoor")
 */
@Entity
@Table(name = "venue")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Stadium display name, e.g. "Red Bull Arena". */
    @Column(nullable = false, length = 200)
    private String name;

    /** Host city, e.g. "Salzburg". */
    @Column(length = 100)
    private String city;

    /** Street address for navigation, e.g. "Am Bulgariplatz 7, 5020 Salzburg". */
    @Column(length = 300)
    private String address;

    /** Playing surface — "grass", "artificial", or "indoor". */
    @Column(length = 50)
    private String surface;

    /** Maximum stadium capacity. */
    @Column
    private Integer capacity;

    /** Host country — FK to country.code. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_country")
    private Country country;
}