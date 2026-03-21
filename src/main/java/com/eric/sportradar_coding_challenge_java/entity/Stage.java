package com.eric.sportradar_coding_challenge_java.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

/**
 * Stage within a competition (e.g., Round of 16, Final).
 * Uses a composite primary key (id + competition) because
 * stage names like "FINAL" exist across multiple competitions.
 */
@Entity
@Table(name = "stage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(Stage.StageId.class)
public class Stage {

    @Id
    @Column(length = 50)
    private String id;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_competition")
    private Competition competition;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer ordering;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class StageId implements Serializable {
        private String id;
        private String competition;
    }
}
