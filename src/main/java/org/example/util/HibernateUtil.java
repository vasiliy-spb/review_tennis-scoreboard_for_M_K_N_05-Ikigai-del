package org.example.util;
import org.example.entity.Match;
import org.example.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private HibernateUtil() {
    }

    private static final SessionFactory SESSION_FACTORY;

    static {
        SESSION_FACTORY = new Configuration()
                .configure()
                .addAnnotatedClass(Player.class)
                .addAnnotatedClass(Match.class)
                .buildSessionFactory();
    }

    public static Session getSession() {
        return SESSION_FACTORY.openSession();
    }
}