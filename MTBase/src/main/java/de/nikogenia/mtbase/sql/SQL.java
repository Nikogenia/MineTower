package de.nikogenia.mtbase.sql;

import de.nikogenia.mtbase.MTBase;
import de.nikogenia.mtbase.config.SQLConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Properties;

public class SQL {

    Configuration configuration;

    Session session;

    public SQL() {

        configuration = new Configuration();

    }

    public void build() {

        SQLConfig config = MTBase.getConfiguration().getSql();

        Properties prop = new Properties();
        prop.setProperty("hibernate.connection.url", "jdbc:mysql://" + config.getHost() + ":" +
                config.getPort() + "/" + config.getDatabase());
        prop.setProperty("dialect", "org.hibernate.dialect.MySQL8Dialect");
        prop.setProperty("hibernate.connection.username", config.getUser());
        prop.setProperty("hibernate.connection.password", config.getPassword());
        prop.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        prop.setProperty("hibernate.jdbc.time_zone", "UTC");
        prop.setProperty("show_sql", String.valueOf(MTBase.getConfiguration().isDebug()));

        SessionFactory sessionFactory = configuration
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

    public void addTable(Class<?> table) {

        configuration.addAnnotatedClass(table);

    }

    public String getGeneralEntry(String name) {

        List<SQLGeneral> result = query("FROM SQLGeneral WHERE name = :name", SQLGeneral.class)
                .setParameter("name", name)
                .list();

        if (result.isEmpty()) return "";

        return result.get(0).getValue();

    }

    public List<SQLPlayer> getPlayers() {

        return query("FROM SQLPlayer", SQLPlayer.class).list();

    }

    public SQLPlayer getPlayerByUUID(String uuid) {

        List<SQLPlayer> result = query("FROM SQLPlayer WHERE uuid = :uuid", SQLPlayer.class)
                .setParameter("uuid", uuid)
                .list();

        if (result.isEmpty()) return null;

        return result.get(0);

    }

    public SQLPlayer getPlayerByName(String name) {

        List<SQLPlayer> result = query("FROM SQLPlayer WHERE name = :name", SQLPlayer.class)
                .setParameter("name", name)
                .list();

        if (result.isEmpty()) return null;

        return result.get(0);

    }

    public <T> Query<T> query(String query, Class<T> target) {
        return session.createQuery(query, target);
    }

    public void exit() {

        session.close();

    }

    public Session getSession() {
        return session;
    }

}
