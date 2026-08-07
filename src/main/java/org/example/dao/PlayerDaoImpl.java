package org.example.dao;

import org.example.entity.Player;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class PlayerDaoImpl implements PlayerDao {
    @Override
    public Player save(Player player) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(player);
            transaction.commit();
            return player;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Player> findByName(String name) {
        String query = "from Player where name = :name";
        try (Session session = HibernateUtil.getSession()){
            return session.createQuery (query, Player.class)
                    .setParameter("name", name)
                    .uniqueResultOptional();

        }catch (Exception e ){
            throw new RuntimeException(e);
        }
    }
}