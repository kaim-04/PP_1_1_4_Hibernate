package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS test" +
                    " (id mediumint not null auto_increment, name VARCHAR(15), " +
                    "lastname VARCHAR(15), " +
                    "age tinyint, " +
                    "PRIMARY KEY (id))").executeUpdate();
            transaction.commit();
            session.close();
            System.out.println("Создание таблицы прошло успешно");
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE if exists test").executeUpdate();
            transaction.commit();
            System.out.println("Таблица была удалена из БД");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        Session session = sessionFactory.getCurrentSession();
        User user = new User(name, lastName, age);
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        System.out.println("пользователь с именем " + name + " сохранен в БД");

    }

    @Override
    public void removeUserById(long id) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        User user = session.load(User.class, id);
        session.delete(user);
        transaction.commit();
        session.close();
        System.out.println("Пользователь под id №" + id + " был удален");

    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("From " + User.class.getName()).list();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery("truncate table test").executeUpdate();
            transaction.commit();
            System.out.println("Удаление данных из таблицы прошло успешно");
        }
    }
}
