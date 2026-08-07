package org.example.dao;

import org.example.entity.Player;

import java.util.Optional;

public interface  PlayerDao {
    Player save (Player player);
    Optional<Player> findByName(String name );
}
