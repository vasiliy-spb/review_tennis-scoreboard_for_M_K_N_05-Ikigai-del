package org.example.dto;

import org.example.entity.Match;
import org.example.model.MatchScore;
import org.example.model.PlayerScore;

import java.text.BreakIterator;

public class MatchScoreMapper {

    // Маппер лучше вынести в отдельный пакет — он не относится к DTO.

    // TODO: После 30 в теннисе идёт счёт 40, а не 45.
    private static final String[] POINT_LABELS = {"0", "15", "30", "45"};

    public static MatchScoreResponse toResponse(MatchScore matchScore) {
        PlayerScoreResponse first = toPlayerResponse(
                matchScore.getMatch().getPlayer1().getName(),
                matchScore.getFirstPlayerScore(),
                matchScore.getSecondPlayerScore(),
                matchScore.isTieBreake());

        PlayerScoreResponse second = toPlayerResponse(
                matchScore.getMatch().getPlayer2().getName(),
                matchScore.getSecondPlayerScore(),
                matchScore.getFirstPlayerScore(),
                matchScore.isTieBreake());
        return new MatchScoreResponse(first, second, matchScore.getWinner());
    }

    // Опечатка: tieBreake —> tieBreak
    private static PlayerScoreResponse toPlayerResponse(String name, PlayerScore self, PlayerScore opponent, boolean tieBreake) {
        String pointsLabel = tieBreake ? null : pointsLabel(self.getPoints(), opponent.getPoints());
        return new PlayerScoreResponse(name, pointsLabel, self.getGames(), self.getSets(), self.getTieBreakPoints());

    }
    private static String pointsLabel(int myPoints, int opPoints ){
        int max = Math.max(myPoints,opPoints);
        if(max <= 3 ){

            // TODO: Полагаться на то, что в аргументы придёт нужный индекс — это хрупкий подход.
                // Лучше использовать в модели константы перечисления и здесь реализовать их прямое (не через индекс) преобразование.
            return  POINT_LABELS[myPoints];
        }

        // TODO: Маппер не должен сам определять, у какого игрока преимущество.
            // Он должен получать готовый счёт и только решать, каким образом его преобразовать для отображжения во View.
        int diff = myPoints - opPoints;
        return diff > 0 ? "AD": "40";
    }
    public static MatchSummaryResponse toSummary(Match match) {
        return new MatchSummaryResponse(
                match.getPlayer1().getName(),
                match.getPlayer2().getName(),
                match.getWinner() != null ? match.getWinner().getName() : null
        );
    }
}
