package org.example.dao;

import org.example.entity.Match;

import java.util.List;

public interface MatchDao {

    // Методы репозитория (DAO) должны принимать смещение (offset), а не вычислять его самостоятельно
        // (см. файл "separation-of-concerns-principle.md" в этом же пакете).

    Match save (Match match);

    // Можно назвать findAll
    List <Match> getMatches (int page, int size);

    // Можно назвать findAllByPlayerName
    List <Match> getMatches (int page, int size, String playerName);

    // Лучше иметь разные методы для подсчёта количества с фильтром по имени и без него,
        // чем собирать эту логику в одном методе. Если правила фильтрации поменяются,
        // то нужно будет изменить/дописать только некоторые методы, оставив логику выборки без фильтра без изменений.
    // Можно назвать просто count и countByPlayerName
    // Текущая сигнатура countMatches(String playerName) подразумевает, что "посчитать всё" можно только передав null.
        // Это является скрытым контрактом — тем, что не очевидно и до чего разработчику приходится догадываться.
    long countMatches(String playerName);
}
