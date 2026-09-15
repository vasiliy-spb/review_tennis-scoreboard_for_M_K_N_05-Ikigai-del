package org.example.dao;

import org.example.entity.Match;
import org.example.exception.DataBaseOperationException;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MatchDaoImpl implements MatchDao {
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
