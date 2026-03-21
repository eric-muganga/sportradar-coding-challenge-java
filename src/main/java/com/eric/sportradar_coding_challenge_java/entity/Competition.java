package com.eric.sportradar_coding_challenge_java.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Competition (league or cup) reference table.
 */
@Entity
@Table(name = "competition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Competition {

    /** URL-slug identifier, e.g. "afc-champions-league". */
    @Id
    @Column(length = 100, nullable = false)
    private String id;

    /** Competition display name, e.g. "AFC Champions League". */
    @Column(nullable = false, length = 200)
    private String name;
}