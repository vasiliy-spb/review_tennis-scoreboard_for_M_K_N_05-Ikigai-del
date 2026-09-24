package org.example.service;

import org.example.model.MatchScore;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {

    // TODO: Сервис не является Spring-бином. Это лишает проект преимуществ Spring
        // (см. файл "Common.md" в директории code-review).

    // Для класса хранилища завершённых матчей лучше создавать интерфейс и реализации, а не его делать синглтоном.
        // Это позволит подменять реализацию для тестирования или при изменении способа хранения объектов.

    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();
    private final Map<UUID, MatchScore> ongoingMatches;

    private OngoingMatchesService() {
        ongoingMatches = new ConcurrentHashMap<>();
    }

    public static OngoingMatchesService getINSTANCE() {
        return INSTANCE;
    }

    // Хранилище может само создавать ID для матча (по аналогии с БД) и возвращать его из этого метода.
    // Стоит удалять комментарии (вроде того, что указан в следующей строке) из кода перед тем, как выполнять коммит
    public void createNewMatch(UUID uuid, MatchScore matchScore) {   // ← тип поменяли на MatchScore
        ongoingMatches.put(uuid, matchScore);
    }

    // По аналогии с репозиториями можно в этом методе возвращать Optional, чтобы из него никогда не возвращался null.
    // Стоит удалять комментарии (вроде того, что указан в следующей строке) из кода перед тем, как выполнять коммит
    public MatchScore getCurrentMatch(UUID uuid) {   // ← и здесь тоже
        return ongoingMatches.get(uuid);
    }

    public void deleteMatch(UUID uuid) {
        ongoingMatches.remove(uuid);
    }
}