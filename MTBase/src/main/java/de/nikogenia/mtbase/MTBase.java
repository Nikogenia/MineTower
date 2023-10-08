package de.nikogenia.mtbase;

import de.nikogenia.mtbase.api.API;
import de.nikogenia.mtbase.config.Config;
import de.nikogenia.mtbase.listeners.ChatListeners;
import de.nikogenia.mtbase.listeners.ConnectionListeners;
import de.nikogenia.mtbase.sql.SQL;
import de.nikogenia.mtbase.utils.FileConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public final class MTBase extends JavaPlugin {

    private static MTBase instance;

    private Config config;

    private SQL sql;

    private API api;

    private List<String> commands;

    private boolean customTabList;

    @Override
    public void onLoad() {

        instance = this;

        getLogger().info("Load configuration");
        config = (Config) FileConfig.load("./minetower.yml", Config.class);
        config.save("./minetower.yml");

        sql = new SQL();

        customTabList = false;

    }

    @Override
    public void onEnable() {

        getLogger().info("Enable server as " + getConfiguration().getName());

        getLogger().info("Setup SQL");
        sql.build();

        getLogger().info("Setup API");
        api = new API();

        Bukkit.getPluginManager().registerEvents(new ConnectionListeners(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListeners(), this);

        commands = new ArrayList<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                ListIterator<String> iter = commands.listIterator();
                while (iter.hasNext()) {
                    Bukkit.getCommandMap().dispatch(Bukkit.getConsoleSender(), iter.next());
                    iter.remove();
                }

            }
        }.runTaskTimer(this, 1, 1);

    }

    @Override
    public void onDisable() {

        getLogger().info("Quit API");
        api.exit();

        getLogger().info("Quit SQL");
        sql.exit();

        getLogger().info("Exit");

    }

    public void runCommand(String command) {
        commands.add(command);
    }

    public static MTBase getInstance() {
        return instance;
    }

    public static Config getConfiguration() {
        return instance.config;
    }

    public static SQL getSql() {
        return instance.sql;
    }

    public static API getApi() {
        return instance.api;
    }

    public static void setCustomTabList(boolean customTabList) {
        instance.customTabList = customTabList;
    }

}
