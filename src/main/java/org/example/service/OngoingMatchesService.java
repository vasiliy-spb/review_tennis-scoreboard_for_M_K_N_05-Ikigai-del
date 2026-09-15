package org.example.service;

import org.example.model.MatchScore;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {
    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();
    private final Map<UUID, MatchScore> ongoingMatches;

    private OngoingMatchesService() {
        ongoingMatches = new ConcurrentHashMap<>();
    }

    public static OngoingMatchesService getINSTANCE() {
        return INSTANCE;
    }

    public void createNewMatch(UUID uuid, MatchScore matchScore) {   // ← тип поменяли на MatchScore
        ongoingMatches.put(uuid, matchScore);
    }

    public MatchScore getCurrentMatch(UUID uuid) {   // ← и здесь тоже
        return ongoingMatches.get(uuid);
    }

    public void deleteMatch(UUID uuid) {
        ongoingMatches.remove(uuid);
    }
}