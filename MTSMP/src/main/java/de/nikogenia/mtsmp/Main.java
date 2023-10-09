package de.nikogenia.mtsmp;

import de.nikogenia.mtbase.MTBase;
import de.nikogenia.mtsmp.commands.ShopCommand;
import de.nikogenia.mtsmp.listeners.ConnectionListeners;
import de.nikogenia.mtsmp.listeners.EntityListeners;
import de.nikogenia.mtsmp.listeners.InteractionListeners;
import de.nikogenia.mtsmp.listeners.PlayerListeners;
import de.nikogenia.mtsmp.shop.ShopManager;
import de.nikogenia.mtsmp.spawn.SpawnManager;
import de.nikogenia.mtsmp.sql.SQLShop;
import de.nikogenia.mtsmp.status.StatusManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    private static Main instance;

    private ShopManager shopManager;

    private SpawnManager spawnManager;

    private StatusManager statusManager;

    @Override
    public void onLoad() {

        instance = this;

        MTBase.getSql().addTable(SQLShop.class);

        MTBase.getTabListManager().setCustomTabList(true);

    }

    @Override
    public void onEnable() {

        shopManager = new ShopManager();
        spawnManager = new SpawnManager();
        statusManager = new StatusManager();

        Bukkit.getPluginManager().registerEvents(new EntityListeners(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListeners(), this);
        Bukkit.getPluginManager().registerEvents(new InteractionListeners(), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListeners(), this);

        Objects.requireNonNull(getCommand("shop")).setExecutor(new ShopCommand());
        Objects.requireNonNull(getCommand("status")).setExecutor(new ShopCommand());

    }

    @Override
    public void onDisable() {



    }

    public static Main getInstance() {
        return instance;
    }

    public static ShopManager getShopManager() {
        return instance.shopManager;
    }

    public static SpawnManager getSpawnManager() {
        return instance.spawnManager;
    }

    public static StatusManager getStatusManager() {
        return instance.statusManager;
    }

}
