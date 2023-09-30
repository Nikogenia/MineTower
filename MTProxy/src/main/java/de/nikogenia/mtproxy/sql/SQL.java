package de.nikogenia.mtproxy.sql;

import de.nikogenia.mtproxy.Main;
import de.nikogenia.mtproxy.config.SQLConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Properties;

public class SQL {

    Session session;

    public SQL() {

        SQLConfig config = Main.getConfig().getSql();

        Properties prop = new Properties();
        prop.setProperty("hibernate.connection.url", "jdbc:mysql://" + config.getHost() + ":" +
                config.getPort() + "/" + config.getDatabase());
        prop.setProperty("dialect", "org.hibernate.dialect.MySQL8Dialect");
        prop.setProperty("hibernate.connection.username", config.getUser());
        prop.setProperty("hibernate.connection.password", config.getPassword());
        prop.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        prop.setProperty("show_sql", String.valueOf(Main.getConfig().isDebug()));

        SessionFactory sessionFactory = new Configuration()
                .addAnnotatedClass(General.class)
                .addAnnotatedClass(Motd.class)
                .addProperties(prop)
                .buildSessionFactory();
        session = sessionFactory.openSession();

        session.beginTransaction();

    }

    public String getGeneralEntry(String name) {

        List<General> result = session.createQuery("FROM General WHERE name = :name", General.class)
                .setParameter("name", name)
                .list();

        if (result.isEmpty()) return "";

        return result.get(0).getValue();

    }

    public String getMotd() {

        List<Motd> result = session.createQuery(
                "FROM Motd Where name = (SELECT value FROM General WHERE name = 'motd')", Motd.class)
                .list();

        if (result.isEmpty()) return "INTERNAL SERVER ERROR - Database";

        return result.get(0).getLine1() + "\n" + result.get(0).getLine2();

    }

    public void exit() {

        session.close();

    }

    public Session getSession() {
        return session;
    }

}
