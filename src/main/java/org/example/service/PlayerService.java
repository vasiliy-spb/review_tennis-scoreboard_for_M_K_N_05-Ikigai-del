package org.example.service;

import org.example.dao.PlayerDao;
import org.example.dao.PlayerDaoImpl;
import org.example.entity.Player;

import java.util.Optional;

public class PlayerService {
    private final PlayerDao playerDao = new PlayerDaoImpl();
    public Player getOrCreatePlayer(String name){
        Optional<Player>  player = playerDao.findByName(name);
        if ( player.isPresent()){
            return player.get();
            Player newPlayer = new Player(name);
            playerDao.save(newPlayer);
            return newPlayer;
        }
    }
}
