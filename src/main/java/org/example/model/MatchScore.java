package org.example.model;

import org.example.entity.Match;
import org.example.entity.Player;

public class MatchScore {

    // TODO: Класс отвечает за обработку очков на всех этапах игрового процесса в матче —
        // это слишком большая ответственность для одного класса и нарушает SRP (Single Responsibility Principle).
        // Лучшим решением в этом направлении было бы, чтобы за логику на каждом этапе (матч-сет-гейм) отвечал отдельный класс.
        // Такой подход больше соответствовал бы ООП-стилю и принципу единственной ответственности для каждого класса.
        // А также это значительно упростит код и улучшит его читаемость, тестирование и поддержку.

    // TODO: Класс позволяет любому внешнему коду в произвольный момент изменять своё состояние (например через метод setTieBreake).
        // Это является признаком анемичной модели.
        // (см. файл "reach-anemic-model.md" в этом же пакете)
        // Класс должен самостоятельно и полностью управлять своим состоянием,
        // предоставляя наружу только необходимые методы, для запуска этих изменений.

    // TODO: Класс хранит ссылку на JPA-сущность (`Match`). Использование объектов JPA Entity в доменной логике
        // создаёт прямую зависимость доменного слоя от слоя персистентности (долговременного хранения данных)
        // и смешивает слои приложения, что нарушает чистоту архитектуры.
        // Это может привести к проблемам с ленивой загрузкой (`LazyInitializationException`)
        // или к неожиданным изменениям в базе данных, если состояние `Match` будет изменено в ходе бизнес-логики.
        // Доменные модели должны оперировать другими доменными моделями, а не сущностями, привязанными к базе данных.

    // Класс хранит в поле `winnerName` данные, которые являются производными от основного состояния (счёта).
        // Это нарушает Принцип Единого источника истины. Источником истины является счёт, поле `winnerName` — это лишь следствие.
        // Хранение производных данных создаёт риск рассинхронизации: можно изменить счёт,
        // но забыть обновить поле, и объект окажется в неконсистентном состоянии.
        // Лучше удалить поле `winnerName` и заменить его методом, который вычисляет результат на лету из текущего счёта.
        // (см. файл "ssot-principle.md" в этом же пакете)

    // Все магические числа лучше вынести в `private static final` константы с понятными именами.
        // Именованная константа делает код более семантически понятным.

    // Составные условия из `if` требуют усилий для понимания. Сложные логические выражения ухудшают читаемость кода
        // и увеличивают вероятность ошибки при их написании или изменении. Лучше выносить такие условия
        // в отдельный `private`-метод с понятным названием, которое описывает бизнес-правило.

    private final Match match;
    private final PlayerScore firstPlayerScore = new PlayerScore();
    private final PlayerScore secondPlayerScore = new PlayerScore();
    private boolean tieBreake = false; // Опечатка: tieBreake —> tieBreak
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

    // Опечатка: isTieBreake —> isTieBreak
    public boolean isTieBreake() {
        return tieBreake;
    }

    // Опечатка: setTieBreake —> setTieBreak
    public void setTieBreake(boolean tieBreake) {
        this.tieBreake = tieBreake;
    }

    // Из метода получения победителя можно возвращать Optional, чтобы он никогда не возвращал null/
    public String getWinner() {
        return winnerName;
    }

    // TODO: У поля победителя не должно быть сеттера —
        // это позволяет внешнему коду установить победителя в произвольный момент и в обход логики игры.
    public void setWinner(String winner) {
        this.winnerName = winner;
    }

    // Вместо флага boolean firstPlayerScored лучше принимать имя игрока, его доменную модель
        // или enum представляющий сторону в матче.
        // Так класс будет сам решать, какой именно игрок/сторона является у него первым или вторым
        // и логика обработки очка будет находиться в одном, подходящем для неё месте.
        // Это усилит инкапсуляцию доменной модели.
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

    // Лучше addGamePoint или addPointTo(player)
    public void addPointgame(boolean firstPlayerScored) {
        PlayerScore scorer = firstPlayerScored ? firstPlayerScore : secondPlayerScore;
        PlayerScore aponent = firstPlayerScored ? secondPlayerScore : firstPlayerScore; // opponent

        scorer.setPoints(scorer.getPoints() + 1);

        // Составные условия из if лучше выносить во вспомогательный метод с понятным названием
        if (scorer.getPoints() >= 4 && scorer.getPoints() - aponent.getPoints() >= 2) {
            winGame(scorer, aponent);
        }
    }
    private void winGame(PlayerScore winner, PlayerScore loser) {
        winner.setPoints(0);
        loser.setPoints(0);
        winner.setGames(winner.getGames() + 1);

        // Составные условия из if лучше выносить во вспомогательный метод с понятным названием
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

        // Составные условия из if лучше выносить во вспомогательный метод с понятным названием
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

