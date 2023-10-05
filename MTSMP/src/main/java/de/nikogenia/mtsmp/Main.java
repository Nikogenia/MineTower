package de.nikogenia.mtsmp;

import de.nikogenia.mtbase.MTBase;
import de.nikogenia.mtsmp.commands.ShopCommand;
import de.nikogenia.mtsmp.shop.ShopManager;
import de.nikogenia.mtsmp.sql.SQLShop;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    private static Main instance;

    private ShopManager shopManager;

    @Override
    public void onLoad() {

        instance = this;

        MTBase.getSql().addTable(SQLShop.class);

    }

    @Override
    public void onEnable() {

        getLogger().info("Enable server as " + MTBase.getConfiguration().getName());

        shopManager = new ShopManager();

        Objects.requireNonNull(getCommand("shop")).setExecutor(new ShopCommand());

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

}
