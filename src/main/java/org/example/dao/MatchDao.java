package org.example.dao;

import org.example.entity.Match;

import java.util.List;

public interface MatchDao {
    Match save (Match match);
    List <Match> getMatches (int page, int size);
    List <Match> getMatches (int page, int size, String playerName);
    long countMatches(String playerName);
}
