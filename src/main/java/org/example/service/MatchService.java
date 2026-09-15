package org.example.service;

import org.example.dao.MatchDao;
import org.example.dao.MatchDaoImpl;
import org.example.entity.Match;
import org.example.entity.Player;
import org.example.exception.MatchNotFoundException;
import org.example.model.MatchScore;

import java.util.List;
import java.util.UUID;

public class MatchService {
    private final PlayerService playerService = new PlayerService();
    private final OngoingMatchesService ongoingMatchesService =
            OngoingMatchesService.getINSTANCE();

    public UUID createMatch(String first_playerName, String second_PlayerName) {
        Player player1 = playerService.getOrCreatePlayer(first_playerName);
        Player player2 = playerService.getOrCreatePlayer(second_PlayerName);
        Match match = new Match(player1, player2);
        MatchScore matchScore = new MatchScore(match);
        UUID uuid = UUID.randomUUID();
        ongoingMatchesService.createNewMatch(uuid, matchScore);
        return uuid;
    }
    private final MatchDao matchDao = new MatchDaoImpl();

    public List<Match> getFinishedMatches(int page, int size, String playerName) {
        if (playerName != null && !playerName.isBlank()) {
            return matchDao.getMatches(page, size, playerName);
        }
        return matchDao.getMatches(page, size);
    }

    public long countFinishedMatches(String playerName) {
        return matchDao.countMatches(playerName);
    }
    public MatchScore getMatch(UUID uuid) {
        MatchScore matchScore = ongoingMatchesService.getCurrentMatch(uuid);
        if (matchScore == null) {
            throw new MatchNotFoundException(uuid);
        }
        return matchScore;
    }

    public MatchScore addPoint(UUID uuid, String scoringPlayerName) {
        MatchScore matchScore = getMatch(uuid);
        boolean firstPlayerScored = matchScore.getMatch().getPlayer1().getName().equals(scoringPlayerName);
        matchScore.addPoint(firstPlayerScored);
        if (matchScore.getWinner() != null) {
            matchDao.save(matchScore.getMatch());
            ongoingMatchesService.deleteMatch(uuid);
        }
        return matchScore;
    }
}

