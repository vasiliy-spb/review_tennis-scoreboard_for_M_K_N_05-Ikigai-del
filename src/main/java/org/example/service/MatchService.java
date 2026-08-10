package org.example.service;

import org.example.entity.Match;
import org.example.entity.Player;

import java.util.UUID;

public class MatchService {
    private final PlayerService playerService = new PlayerService();
    private final OngoingMatchesService ongoingMatchesService =
            OngoingMatchesService.getINSTANCE();

    public UUID CreateMatch(String first_playerName, String second_PlayerName) {
        Player player1 = playerService.getOrCreatePlayer(first_playerName);
        Player player2 = playerService.getOrCreatePlayer(second_PlayerName);
        Match match = new Match(player1,player2);
        UUID uuid = UUID.randomUUID();
        ongoingMatchesService.CreateMatch(uuid,match);
        return uuid;
    }
}
