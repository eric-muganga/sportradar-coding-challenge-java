package com.eric.sportradar_coding_challenge_java.entity;

import lombok.*;
import jakarta.persistence.*;

/**
 * A disciplinary card issued during an event.
 *
 * The JSON provides three separate arrays for card types:
 *   result.yellowCards, result.secondYellowCards, result.directRedCards
 *
 * Rather than three separate tables, this single table uses a _card_type
 * FK to the card_type lookup table as a discriminator. This is cleaner,
 * avoids schema duplication, and satisfies 3NF — a free-text VARCHAR
 * discriminator would create a transitive dependency.
 */
@Entity
@Table(name = "card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_event", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_team", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_card_type", nullable = false)
    private CardType cardType;

    @Column(name = "player_name", length = 100)
    private String playerName;

    private Integer minute;
}