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

    // TODO: Сервис не является Spring-бином. Это лишает проект преимуществ Spring
        // (см. файл "Common.md" в директории code-review).

    // TODO: Зависимость `MatchDaoImpl` создаётся напрямую в месте объявления.
        // Это нарушает Принцип инверсии зависимостей (DIP).
        // Класс не должен сам решать, какую конкретно реализацию использовать.
        // Он должен получать её извне через конструктор.

    // TODO: Класс способствует смешению слоёв — передаёт JPA Entity (List<Match> из метода getFinishedMatches) и
        // модель (MatchScore из методов addPoint и getMatch) в контроллер.
        // (см. файл "separation-of-concerns-principle.md" в этом же пакете).

    private final PlayerService playerService = new PlayerService();
    private final OngoingMatchesService ongoingMatchesService =
            OngoingMatchesService.getINSTANCE();

    // TODO: Этот метод должен выполняться в единой транзакции.
    // Все названия (полей, переменных, аргументов методов и тд) в Java пишутся в стиле camelCase.
    public UUID createMatch(String first_playerName, String second_PlayerName) {
        Player player1 = playerService.getOrCreatePlayer(first_playerName);
        Player player2 = playerService.getOrCreatePlayer(second_PlayerName);

        // TODO: Объект JPA Entity матча должен создаваться только перед сохранением результатов в БД.
            // Доменная модель матча должна быть независима от него.
        Match match = new Match(player1, player2);
        MatchScore matchScore = new MatchScore(match);
        UUID uuid = UUID.randomUUID();
        ongoingMatchesService.createNewMatch(uuid, matchScore);
        return uuid;
    }

    // Поля в классе объявляются выше всех методов.
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

    // TODO: Race condition при обработке выигранного очка.
        // Например, если пользователь очень быстро нажмёт кнопку выигрыша очка, браузер отправит два POST-запроса почти одновременно.
        // Эти два запроса будут обработаны в двух разных потоках и оба потока будут работать с одним и тем же общим объектом `MatchScore`,
        // что может привести к начислению лишнего очка.
        // Чтобы это исправить, нужно гарантировать, что только один поток может изменять состояние конкретного матча в один момент времени.
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

