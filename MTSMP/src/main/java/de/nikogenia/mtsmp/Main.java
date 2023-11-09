package de.nikogenia.mtsmp;

import de.nikogenia.mtbase.MTBase;
import de.nikogenia.mtsmp.commands.*;
import de.nikogenia.mtsmp.home.HomeManager;
import de.nikogenia.mtsmp.listeners.*;
import de.nikogenia.mtsmp.shop.ShopManager;
import de.nikogenia.mtsmp.spawn.SpawnManager;
import de.nikogenia.mtsmp.sql.SQLHome;
import de.nikogenia.mtsmp.sql.SQLShop;
import de.nikogenia.mtsmp.status.StatusManager;
import de.nikogenia.mtsmp.tablist.CustomTabListManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    private static Main instance;

    private ShopManager shopManager;

    private SpawnManager spawnManager;

    private StatusManager statusManager;

    private HomeManager homeManager;

    private boolean endDimension;

    @Override
    public void onLoad() {

        instance = this;

        MTBase.getSql().addTable(SQLShop.class);
        MTBase.getSql().addTable(SQLHome.class);

    }

    @Override
    public void onEnable() {

        shopManager = new ShopManager();
        spawnManager = new SpawnManager();
        statusManager = new StatusManager();
        homeManager = new HomeManager();
        MTBase.setTabListManager(new CustomTabListManager());

        Bukkit.getPluginManager().registerEvents(new EntityListeners(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListeners(), this);
        Bukkit.getPluginManager().registerEvents(new InteractionListeners(), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListeners(), this);
        Bukkit.getPluginManager().registerEvents(new BlockListeners(), this);

        Objects.requireNonNull(getCommand("shop")).setExecutor(new ShopCommand());
        Objects.requireNonNull(getCommand("status")).setExecutor(new StatusCommand());
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new SpawnCommand());
        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand());
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new SetHomeCommand());
        Objects.requireNonNull(getCommand("end_dimension")).setExecutor(new EndDimensionCommand());

        loadEndDimension();

    }

    public void loadEndDimension() {
        MTBase.getSql().getSession().getTransaction().rollback();
        MTBase.getSql().getSession().beginTransaction();
        endDimension = MTBase.getSql().getGeneralEntry("end_dimension").equals("open");
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

    public static HomeManager getHomeManager() {
        return instance.homeManager;
    }

    public static boolean isEndDimension() {
        return instance.endDimension;
    }

}
