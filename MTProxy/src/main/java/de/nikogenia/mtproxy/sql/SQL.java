package de.nikogenia.mtproxy.sql;

import de.nikogenia.mtproxy.Main;
import de.nikogenia.mtproxy.config.SQLConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

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
        prop.setProperty("hibernate.jdbc.time_zone", "UTC");
        prop.setProperty("show_sql", String.valueOf(Main.getConfig().isDebug()));

        SessionFactory sessionFactory = new Configuration()
                .addAnnotatedClass(SQLGeneral.class)
                .addAnnotatedClass(SQLMotd.class)
                .addAnnotatedClass(SQLAgent.class)
                .addAnnotatedClass(SQLCluster.class)
                .addAnnotatedClass(SQLInstance.class)
                .addAnnotatedClass(SQLPlayer.class)
                .addProperties(prop)
                .buildSessionFactory();
        session = sessionFactory.openSession();

        session.beginTransaction();

    }

    public String getGeneralEntry(String name) {

        List<SQLGeneral> result = session.createQuery("FROM SQLGeneral WHERE name = :name", SQLGeneral.class)
                .setParameter("name", name)
                .list();

        if (result.isEmpty()) return "";

        return result.get(0).getValue();

    }

    public String getMotd() {

        List<SQLMotd> result = session.createQuery(
                "FROM SQLMotd WHERE name = (SELECT value FROM SQLGeneral WHERE name = 'motd')", SQLMotd.class)
                .list();

        if (result.isEmpty()) return "INTERNAL SERVER ERROR - Database";

        return result.get(0).getLine1() + "\n" + result.get(0).getLine2();

    }

    public List<SQLInstance> getInstances() {

        return session.createQuery("FROM SQLInstance", SQLInstance.class).list();

    }

    public SQLInstance getInstance(String name) {

        List<SQLInstance> result = session.createQuery("FROM SQLInstance WHERE name = :name", SQLInstance.class)
                .setParameter("name", name)
                .list();

        if (result.isEmpty()) return null;

        return result.get(0);

    }

    public SQLPlayer getPlayer(String uuid) {

        List<SQLPlayer> result = session.createQuery("FROM SQLPlayer WHERE uuid = :uuid", SQLPlayer.class)
                .setParameter("uuid", uuid)
                .list();

        if (result.isEmpty()) return null;

        return result.get(0);

    }

    public void exit() {

        session.close();

    }

    public Session getSession() {
        return session;
    }

}
