package org.example.exception;

import java.util.UUID;

public class MatchNotFoundException extends RuntimeException {
    public MatchNotFoundException(UUID uuid) {
        super("Матч не найден: " + uuid);
    }
}