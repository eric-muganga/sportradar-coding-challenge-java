package com.eric.sportradar_coding_challenge_java.entity;

import jakarta.persistence.*;
import lombok.*;


/**
 * Lookup table for match statuses.
 * Possible values: played, scheduled, cancelled, postponed.
 * Stored as a separate table rather than an enum to allow
 * new statuses to be added without a code change.
 */
@Entity
@Table(name = "event_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventStatus {

    @Id
    @Column(length = 20, nullable = false)
    private String code;

    @Column(nullable = false, length = 200)
    private String description;
}
