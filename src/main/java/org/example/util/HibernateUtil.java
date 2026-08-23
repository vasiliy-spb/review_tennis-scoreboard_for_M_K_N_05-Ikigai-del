package org.example.util;

import org.example.entity.Match;
import org.example.entity.Player;
import org.hibernate.SessionFactory;

import javax.security.auth.login.Configuration;

public class HibernateUtil {
    private static HibernateUtil() {
    }

    private static final SessionFactory SESSION_FACTORY;

    static {
        SESSION_FACTORY = new Configuration()
                .configure()
                .addAnnotatedClass(Player.class)
                .addAnnotatedClass(Match.class)
                .buildSessionFactory();

    }
    public static final SessionFactory getSession{
        return SESSION_FACTORY.openSession();
    }
}

