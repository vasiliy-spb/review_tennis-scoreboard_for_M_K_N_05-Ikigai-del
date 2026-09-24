package org.example.dto;

public record MatchSummaryResponse(

        // Возможно лучше назвать FinishedMatchResponse

        String firstPlayerName,
        String secondPlayerName,
        String winnerName
) {
}