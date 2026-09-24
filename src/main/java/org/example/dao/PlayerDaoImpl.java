package org.example.dao;

import org.example.entity.Player;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class PlayerDaoImpl implements PlayerDao {

    // Лучше вынести текст HQL запроса в `private static final` константу.
        // Именованная константа делает код более семантически понятным.

    // Текст HQL запроса удобнее читать, когда он логично разбит на строки, даже если он короткий.
        // Для визуального разделения запросов на строки лучше использовать текстовые блоки

    // Ключевые слова в тексте HQL-запроса (`from`, `where` и др.) написаны в нижнем регистре.
        // Хотя это и не влияет на работоспособность, написание ключевых слов SQL/HQL
        // в верхнем регистре (`UPPERCASE`) является общепринятым стандартом.
        // Это значительно улучшает читаемость запросов, так как визуально отделяет
        // синтаксические конструкции языка от имён сущностей и полей.

    // Название именованного параметра тоже можно вынести в константу с понятным названием.

    // TODO: Класс использует `sessionFactory.openSession()` (в HibernateUtil.getSession()) для получения сессии.
        // Это ведёт к антипаттерну "Session-per-Operation" ("сессия на операцию")
        // (см. файл "dao.md" в этом же пакете)

    // TODO: Слой DAO не должен управлять транзакциями
        // (см. файл "dao.md" в этом же пакете)

    // TODO: В методе сохранения сущностей не происходит вызова отката транзакции.
        // Это может привести к неконсистентности данных и утечке ресурсов (например, блокировкам в БД).
        // Транзакция останется открытой, и соединение может не вернуться в пул.
        // Стоит вызывать откат транзакции при любом исключении.

    // TODO: В блоках `catch (Exception e)` перехватывается и выбрасывается слишком общее исключение
        // (см. файл "dao.md" в этом же пакете)

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