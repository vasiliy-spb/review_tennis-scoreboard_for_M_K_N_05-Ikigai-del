package org.example.dto;

public record MatchScoreResponse(
        PlayerScoreResponse firstPlayer,
        PlayerScoreResponse secondPlayer,
        String winnerName
) {}