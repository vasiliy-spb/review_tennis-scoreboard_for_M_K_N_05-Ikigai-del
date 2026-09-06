package org.example.dto;

public record CreateMatchRequest(
        String firstPlayerName,
        String secondPlayerName
) {
}