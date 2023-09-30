package de.nikogenia.mtproxy;

import de.nikogenia.mtproxy.api.API;
import de.nikogenia.mtproxy.config.Config;
import de.nikogenia.mtproxy.listeners.ConnectionListeners;
import de.nikogenia.mtproxy.server.ServerManager;
import de.nikogenia.mtproxy.sql.SQL;
import de.nikogenia.mtproxy.utils.FileConfig;
import net.md_5.bungee.api.plugin.Plugin;

public final class Main extends Plugin {

    private static Main instance;

    private Config config;

    private SQL sql;

    private API api;

    private ServerManager serverManager;

    private String motd;

    @Override
    public void onEnable() {

        instance = this;

        getLogger().info("Load configuration");
        config = (Config) FileConfig.load("./minetower.yml", Config.class);
        config.save("./minetower.yml");

        getLogger().info("Setup SQL");
        sql = new SQL();

        serverManager = new ServerManager();

        getLogger().info("Setup API");
        api = new API();

        getProxy().getPluginManager().registerListener(this, new ConnectionListeners());

        updateMotd();

    }

    @Override
    public void onDisable() {

        getLogger().info("Quit API");
        api.exit();

        getLogger().info("Quit SQL");
        sql.exit();

        getLogger().info("Exit");

    }

    public void updateMotd() {

        motd = sql.getMotd();

    }

    public static Main getInstance() {
        return instance;
    }

    public static Config getConfig() {
        return instance.config;
    }

    public static SQL getSql() {
        return instance.sql;
    }

    public static API getApi() {
        return instance.api;
    }

    public static ServerManager getServerManager() {
        return instance.serverManager;
    }

    public static String getMotd() {
        return instance.motd;
    }

}
