package com.eric.sportradar_coding_challenge_java.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * A single goal scored during an event.
 *
 * Maps to the "result.goals" array in the JSON.
 *
 * Own goal logic: _team always records the player's own team (the team
 * whose player struck the ball). When ownGoal is true, the goal counts
 * for the opposing team — the application layer applies this logic.
 * The database stores raw facts only.
 *
 * Boolean (wrapper type) is used instead of the primitive boolean to
 * avoid potential issues with Hibernate proxy initialisation on lazily
 * loaded entities where the primitive would default to false rather than
 * remaining unset.
 */
@Entity
@Table(name = "goal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_event", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_team", nullable = false)
    private Team team;

    @Column(name = "player_name", length = 100)
    private String playerName;

    private Integer minute;

    @Column(name = "is_own_goal", nullable = false)
    private Boolean isOwnGoal = false;
}
