package com.eric.sportradar_coding_challenge_java.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Sport reference table (Football, Ice Hockey, Basketball, etc.).
 *
 * Extracted so the sport name is not repeated on every event row —
 * a repeating non-key value that would violate first normal form.
 */
@Entity
@Table(name = "sport")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Sport display name, e.g. "Football". Must be unique. */
    @Column(nullable = false, unique = true, length = 100)
    private String name;
}