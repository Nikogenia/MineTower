package de.nikogenia.mtsmp;

import de.nikogenia.mtbase.MTBase;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        getLogger().info(MTBase.getConfiguration().getName());


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
