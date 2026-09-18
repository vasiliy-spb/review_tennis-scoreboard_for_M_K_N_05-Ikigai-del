package org.example.model;

import org.checkerframework.checker.units.qual.A;
import org.example.entity.Match;
import org.example.entity.Player;
import org.example.model.MatchScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatchScoreTest {

    private MatchScore matchScore;

    @BeforeEach
    void setUp() {
        Player player1 = new Player("Novak");
        Player player2 = new Player("Kodak");
        Match match = new Match(player1, player2);
        matchScore = new MatchScore(match);

    }

    @Test
    void addPoint_increaseByPointOne() {
        matchScore.addPoint(true);
        assertEquals(1, matchScore.getFirstPlayerScore().getPoints());
        assertEquals(0, matchScore.getSecondPlayerScore().getPoints());

    }

    @Test
    void fourPointsInARow_winsGame() {
        matchScore.addPoint(true);
        matchScore.addPoint(true);
        matchScore.addPoint(true);
        matchScore.addPoint(true);
        assertEquals(0, matchScore.getFirstPlayerScore().getPoints());
        assertEquals(1, matchScore.getFirstPlayerScore().getGames());

    }

    @Test
    void deuceThenAdvantage_ThenWinGame() {
        for (int i = 0; i < 3; i++) {
            matchScore.addPoint(true);
            matchScore.addPoint(false);
        }
        assertEquals(3, matchScore.getFirstPlayerScore().getPoints());
        assertEquals(3, matchScore.getSecondPlayerScore().getPoints());
        matchScore.addPoint(true);
        assertEquals(4, matchScore.getFirstPlayerScore().getPoints());
        assertEquals(3, matchScore.getSecondPlayerScore().getPoints());
        assertEquals(0, matchScore.getFirstPlayerScore().getGames());
        matchScore.addPoint(true);
        assertEquals(1, matchScore.getFirstPlayerScore().getGames());
        assertEquals(0, matchScore.getSecondPlayerScore().getPoints());
    }

    @Test
    void deuceThenOpponentEquals_BackToDeuce() {
        for (int i = 0; i < 3; i++) {
            matchScore.addPoint(true);
            matchScore.addPoint(false);
        }
        matchScore.addPoint(true);

        matchScore.addPoint(false);
        assertEquals(4, matchScore.getFirstPlayerScore().getPoints());
        assertEquals(4, matchScore.getSecondPlayerScore().getPoints());
        assertEquals(0, matchScore.getFirstPlayerScore().getGames());

    }
    @Test
    void sixGameToFour_WinSets(){
        winGames(true,4  );
        winGames(false,4);
        winGames(true, 2);

        assertEquals(1, matchScore.getFirstPlayerScore().getSets());
        assertEquals(0,matchScore.getFirstPlayerScore().getGames());
        assertEquals(0,matchScore.getSecondPlayerScore().getGames());
    }
    @Test
    void sixGamesEach_startsTieBreak(){
        for(int i=0; i<6;i++) {
            winGames(true, 1);
            winGames(false, 1);
        }
        assertTrue(matchScore.isTieBreake());
        assertEquals(0,matchScore.getFirstPlayerScore().getTieBreakPoints());
        assertEquals(0,matchScore.getSecondPlayerScore().getTieBreakPoints());
        assertEquals(6,matchScore.getFirstPlayerScore().getGames());
        assertEquals(6,matchScore.getSecondPlayerScore().getGames());

    }
    @Test
    void winningTieBreak_winsSetSevenSix(){
        for(int i=0; i<6;i++) {
            winGames(true, 1);
            winGames(false, 1);
        }
        for(int i=0; i<7;i++){
            matchScore.addPoint(true);
        }
        assertFalse(matchScore.isTieBreake());
        assertEquals(0,matchScore.getFirstPlayerScore().getGames());
        assertEquals(1,matchScore.getFirstPlayerScore().getSets());
        assertNull(matchScore.getFirstPlayerScore().getTieBreakPoints());

    }
    @Test
    void winningTwoSets_winsMatch(){
        winGames(true, 6);
        assertEquals(1, matchScore.getFirstPlayerScore().getSets());
        assertNull(matchScore.getWinner());
        winGames(true, 6);
        assertEquals(2,matchScore.getFirstPlayerScore().getSets());
        assertEquals("Novak", matchScore.getWinner());
    }
    private void winGames(boolean firstPlayer, int gamesCount) {
        for (int i = 0; i < gamesCount; i++) {
            for (int j = 0; j < 4; j++) {
                matchScore.addPoint(firstPlayer);
            }
        }
    }}
