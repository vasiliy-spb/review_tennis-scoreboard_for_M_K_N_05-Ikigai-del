package org.example.service;

import org.example.dao.PlayerDao;
import org.example.dao.PlayerDaoImpl;
import org.example.entity.Player;

import java.util.Optional;

public class PlayerService {

    // TODO: Зависимость `PlayerDaoImpl` создаётся напрямую в месте объявления.
        // Это нарушает Принцип инверсии зависимостей (DIP).
        // Класс не должен сам решать, какую конкретно реализацию использовать.
        // Он должен получать её извне через конструктор.

    // TODO: Сервис не является Spring-бином. Это лишает проект преимуществ Spring
        // (см. файл "Common.md" в директории code-review).

    // Race condition при создании игрока.
        // Если два запроса одновременно попытаются создать игрока с одинаковым именем, то:
        // - оба вызовут findByName и получат Optional.empty()
        // - оба создадут new Player(name)
        // - первый save успешно вставит запись
        // - второй save упадёт с нарушением уникального индекса
        // В итоге один из пользователей получит ошибку вместо существующего игрока.

    private final PlayerDao playerDao = new PlayerDaoImpl();
    public Player getOrCreatePlayer(String name){
        Optional<Player>  player = playerDao.findByName(name);
        if ( player.isPresent()) {
            return player.get();
        }
            Player newPlayer = new Player(name);
            playerDao.save(newPlayer);
            return newPlayer;

    }
}
