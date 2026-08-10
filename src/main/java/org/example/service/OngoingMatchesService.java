package org.example.service;

import org.example.entity.Match;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {
    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();
    private final Map <UUID, Match> ongoingMatches;
    private OngoingMatchesService(){
        ongoingMatches = new ConcurrentHashMap<>();
    }
    public OngoingMatchesService ongoingMatchesService

    public static OngoingMatchesService getINSTANCE() {
        return INSTANCE;
    }
    public void CreateMatch(UUID uuid, Match match){
        ongoingMatches.put(uuid, match);
    }
    public Match getCurrentMatch(UUID uuid){
        ongoingMatches.get(uuid);
    }
    public void DeleteMatch(UUID uuid){
        ongoingMatches.remove(uuid);
    }
}
