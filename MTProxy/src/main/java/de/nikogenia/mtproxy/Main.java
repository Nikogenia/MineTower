package de.nikogenia.mtproxy;

import de.nikogenia.mtproxy.api.API;
import de.nikogenia.mtproxy.config.Config;
import de.nikogenia.mtproxy.sql.SQL;
import de.nikogenia.mtproxy.utils.FileConfig;
import net.md_5.bungee.api.plugin.Plugin;

public final class Main extends Plugin {

    private static Main instance;

    private Config config;

    private SQL sql;

    private API api;

    @Override
    public void onEnable() {

        instance = this;

        getLogger().info("Load configuration");
        config = (Config) FileConfig.load("./minetower.yml", Config.class);
        config.save("./minetower.yml");

        sql = new SQL();

        api = new API();

    }

    @Override
    public void onDisable() {

        api.exit();

        sql.exit();

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

}
