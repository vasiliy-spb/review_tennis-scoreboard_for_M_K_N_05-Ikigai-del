package org.example.util;
import org.example.entity.Match;
import org.example.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    // Этот класс не предназначен для наследования и может быть final.

    // TODO: В текущей реализации отсутствует метод для закрытия `SessionFactory` при остановке приложения.
        // `SessionFactory` владеет важными ресурсами, включая пул соединений с базой данных.
        // Если эти ресурсы не освободить корректно, это может привести к утечкам памяти и проблемам с подключением к БД,
        // особенно в окружениях, которые не управляют жизненным циклом приложения автоматически.
        //
        // Стоит добавить статический метод, который будет закрывать `SessionFactory`
        // и вызывать его при завершении работы приложения.
        // Это гарантирует, что все ресурсы, удерживаемые Hibernate, будут освобождены при остановке приложения
        // и предотвращает потенциальные утечки и ошибки, связанные с "висячими" соединениями.
        //
        // Ещё более правильным выбором для приложения со Spring будет создать LocalSessionFactoryBean
        // и доверить управление его жизненным циклом фреймворку.

    private HibernateUtil() {
    }

    // Константы нужно объявлять первыми (самыми верхними) в классе
    private static final SessionFactory SESSION_FACTORY;

    // Статический блок в Java пишется до конструктора — в самом верху класса, сразу после объявления статических переменных.
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