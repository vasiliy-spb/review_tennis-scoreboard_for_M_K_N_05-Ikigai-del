package org.example.dao;

import org.example.entity.Match;
import org.example.exception.DataBaseOperationException;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MatchDaoImpl implements MatchDao {

    // Лучше выносить тексты HQL запросов в `private static final` константы и давать им понятные имена.

    // Ключевые слова в тексте HQL-запросов (`from`, `where` и др.) написаны в нижнем регистре.
        // Хотя это и не влияет на работоспособность, написание ключевых слов SQL/HQL
        // в верхнем регистре (`UPPERCASE`) является общепринятым стандартом.
        // Это значительно улучшает читаемость запросов, так как визуально отделяет
        // синтаксические конструкции языка от имён сущностей и полей.

    // Обязательное наличие победителя должны гарантировать ограничения в БД.
        // После их добавления, из запроса это условие стоит убрать.

    // TODO: Класс использует `sessionFactory.openSession()` (в HibernateUtil.getSession()) для получения сессии.
        // Это ведёт к антипаттерну "Session-per-Operation" ("сессия на операцию")
        // (см. файл "dao.md" в этом же пакете)

    // TODO: Слой DAO не должен управлять транзакциями
        // (см. файл "dao.md" в этом же пакете)

    // TODO: В методе сохранения сущностей не происходит вызова отката транзакции.
        // Это может привести к неконсистентности данных и утечке ресурсов (например, блокировкам в БД).
        // Транзакция останется открытой, и соединение может не вернуться в пул.
        // Стоит вызывать откат транзакции при любом исключении.

    // TODO: В блоках `catch (Exception e)` перехватывается слишком общее исключение
        // (см. файл "dao.md" в этом же пакете)
    
    // TODO: Проблема N+1 в запросах выборки матчей.
        // Методы выборки списка матчей выполняют HQL-запросы вида `"FROM Match m ..."`.
        // Сущность `Match` имеет связи `@ManyToOne` с `Player`, поэтому при выполнении такого запроса
        // Hibernate сначала получит список матчей (1 запрос), а затем он будет выполнять по 2 дополнительных `SELECT` запроса
        // для каждого матча, чтобы получить связанных с ним игроков. Если на странице 10 матчей,
        // это приведёт к 21 запросу (если все игроки будут разные) вместо одного.

    @Override
    public Match save(Match match) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(match);
            transaction.commit();
            return match;

        } catch (Exception e) {
            throw new DataBaseOperationException(
                    "Failed to save match", e);
        }
    }


    @Override
    public List<Match> getMatches(int page, int size) {
        int offset = (page - 1) * size;
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                            "from Match  where winner is not null order by id desc", Match.class)
                    .setFirstResult(offset)
                    .setMaxResults(size)
                    .getResultList();
        } catch (Exception e) {
            throw new DataBaseOperationException("Failed to get matches", e);
        }
    }

    @Override
    public List<Match> getMatches(int page, int size, String playerName) {
        int offset = (page - 1) * size;
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                            "from Match " +
                                    "where winner is not null " +
                                    "and (player1.name = :name or player2.name = :name) " +
                                    "order by id desc",
                            Match.class)
                    .setParameter("name", playerName)
                    .setFirstResult(offset)
                    .setMaxResults(size)
                    .getResultList();

        } catch (Exception e) {
            throw new DataBaseOperationException("Failed to get matches by player name", e);
        }
    }

    @Override
    public long countMatches(String playerName) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "Select count(m) from Match m where m.winner is not null";
            if (playerName != null && !playerName.isBlank()) {
                hql += " and (m.player1.name = :name or m.player2.name = :name)";
            }
            var query = session.createQuery(hql, Long.class);
            if (playerName != null && !playerName.isBlank()) {
                query.setParameter("name", playerName);
            }
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DataBaseOperationException("Failed to count match", e);
        }
    }
}
