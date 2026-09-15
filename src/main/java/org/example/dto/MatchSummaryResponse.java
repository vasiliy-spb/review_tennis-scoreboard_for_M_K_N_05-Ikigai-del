package org.example.dto;

public record MatchSummaryResponse(
        String firstPlayerName,
        String secondPlayerName,
        String winnerName
) {
}