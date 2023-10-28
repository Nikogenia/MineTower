package de.nikogenia.mtsmp.listeners;

import de.nikogenia.mtsmp.spawn.SpawnManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

public class BlockListeners implements Listener {

    @EventHandler
    public void onSpread(BlockSpreadEvent event) {

        Location spawn = event.getBlock().getWorld().getSpawnLocation();
        spawn.setY(event.getBlock().getY());
        if (event.getBlock().getLocation().distance(spawn) < SpawnManager.getSpawnRadius()) {
            event.setCancelled(true);
        }

    }

}
