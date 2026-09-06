package org.example.service;

import org.example.entity.Match;
import org.example.entity.Player;
import org.example.exception.MatchNotFoundException;
import org.example.model.MatchScore;

import java.util.UUID;

public class MatchService {
    private final PlayerService playerService = new PlayerService();
    private final OngoingMatchesService ongoingMatchesService =
            OngoingMatchesService.getINSTANCE();

    public UUID createMatch(String first_playerName, String second_PlayerName) {
        Player player1 = playerService.getOrCreatePlayer(first_playerName);
        Player player2 = playerService.getOrCreatePlayer(second_PlayerName);
        Match match = new Match(player1, player2);
        UUID uuid = UUID.randomUUID();
        ongoingMatchesService.createNewMatch(uuid, match);
        return uuid;
    }
    public MatchScore getMatch (UUID uuid ){
        MatchScore matchScore = getMatch(uuid);
        if(matchScore = null){
            throw  new MatchNotFoundException (uuid);
        }
        return matchScore
    }
}
