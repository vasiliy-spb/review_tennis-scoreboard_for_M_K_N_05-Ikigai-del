package org.example.dto;

public record PlayerScoreResponse(
        String name,
        String points,
        int games,
        int sets,
        Integer tieBreakPoints
) {}