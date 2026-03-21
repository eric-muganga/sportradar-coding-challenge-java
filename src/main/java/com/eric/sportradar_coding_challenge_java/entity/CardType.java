package com.eric.sportradar_coding_challenge_java.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Lookup table for disciplinary card types.
 * Seeded values: yellow, second_yellow, red.
 */
@Entity
@Table(name = "card_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardType {

    @Id
    @Column(length = 20, nullable = false)
    private String code;

    @Column(nullable = false, length = 200)
    private String description;
}