package com.eric.sportradar_coding_challenge_java.exception;

/**
 * Thrown when an event is requested by ID but does not exist in the database.
 * Extends RuntimeException so Spring's @Transactional rolls back automatically
 * and no checked exception handling is required at call sites.
 */
public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(Integer id) {
        super("Event with id " + id + " not found");
    }
}