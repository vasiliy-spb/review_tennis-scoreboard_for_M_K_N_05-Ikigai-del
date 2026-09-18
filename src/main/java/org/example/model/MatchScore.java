package org.example.model;

import org.example.entity.Match;
import org.example.entity.Player;

public class MatchScore {

    private final Match match;
    private final PlayerScore firstPlayerScore = new PlayerScore();
    private final PlayerScore secondPlayerScore = new PlayerScore();
    private boolean tieBreake = false;
    private String winnerName = null;

    public MatchScore(Match match) {
        this.match = match;
    }

    public Match getMatch() {
        return match;
    }

    public PlayerScore getFirstPlayerScore() {
        return firstPlayerScore;
    }

    public PlayerScore getSecondPlayerScore() {
        return secondPlayerScore;
    }

    public boolean isTieBreake() {
        return tieBreake;
    }

    public void setTieBreake(boolean tieBreake) {
        this.tieBreake = tieBreake;
    }

    public String getWinner() {
        return winnerName;
    }

    public void setWinner(String winner) {
        this.winnerName = winner;
    }

    public void addPoint(boolean firstPlayerScored) {
        if (winnerName != null) {
            throw new IllegalStateException("Матч уже завершен");
        }
        if (tieBreake) {
            addTieBreakPoint(firstPlayerScored);
        } else {
            addPointgame(firstPlayerScored);
        }
    }

    public void addPointgame(boolean firstPlayerScored) {
        PlayerScore scorer = firstPlayerScored ? firstPlayerScore : secondPlayerScore;
        PlayerScore aponent = firstPlayerScored ? secondPlayerScore : firstPlayerScore;

        scorer.setPoints(scorer.getPoints() + 1);

        if (scorer.getPoints() >= 4 && scorer.getPoints() - aponent.getPoints() >= 2) {
            winGame(scorer, aponent);
        }
    }
    private void winGame(PlayerScore winner, PlayerScore loser) {
        winner.setPoints(0);
        loser.setPoints(0);
        winner.setGames(winner.getGames() + 1);

        if (winner.getGames() >= 6 && winner.getGames() - loser.getGames() >= 2) {
            winSet(winner, loser);
        } else if (winner.getGames() == 6 && loser.getGames() == 6) {
            tieBreake = true;
            winner.setTieBreakPoints(0);
            loser.setTieBreakPoints(0);
        }
    }

    private void addTieBreakPoint(boolean firstPlayerScored) {
        PlayerScore scorer = firstPlayerScored ? firstPlayerScore : secondPlayerScore;
        PlayerScore opponent = firstPlayerScored ? secondPlayerScore : firstPlayerScore;
        scorer.setTieBreakPoints(scorer.getTieBreakPoints() + 1);
        if (scorer.getTieBreakPoints() >= 7 && scorer.getTieBreakPoints() - opponent.getTieBreakPoints() >= 2) {
            scorer.setGames(scorer.getGames() + 1);
            tieBreake = false;
            scorer.setTieBreakPoints(null);
            opponent.setTieBreakPoints(null);
            winSet(scorer, opponent);

        }
    }

    private void winSet(PlayerScore winner, PlayerScore loser) {
        winner.setGames(0);
        loser.setGames(0);
        winner.setSets(winner.getSets() + 1);

        if (winner.getSets() >= 2) {
            Player winnerPlayer = (winner == firstPlayerScore) ? match.getPlayer1() : match.getPlayer2();
            winnerName = winnerPlayer.getName();
            match.setWinner(winnerPlayer);
        }
    }

}

