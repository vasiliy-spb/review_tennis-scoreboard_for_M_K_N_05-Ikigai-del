package model;

import org.checkerframework.dataflow.qual.TerminatesExecution;
import org.example.entity.Match;
import org.example.entity.Player;
import org.example.model.MatchScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
   void  addPoint_increseByPoint{
        matchScore.addPoint(true);

        assertEquals
    }

}
