package de.nikogenia.mtsmp.listeners;

import de.nikogenia.mtsmp.Main;
import de.nikogenia.mtsmp.spawn.SpawnManager;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityListeners implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {

        if (!event.getLocation().getWorld().getName().equals("world")) return;

        Location spawn = event.getLocation().getWorld().getSpawnLocation();
        spawn.setY(event.getLocation().getY());
        if (event.getLocation().distance(spawn) < SpawnManager.getSpawnRadius()) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {

        if (!event.getEntity().getWorld().getName().equals("world")) return;

        Location spawn = event.getEntity().getWorld().getSpawnLocation();
        spawn.setY(event.getEntity().getY());
        if (event.getEntity().getLocation().distance(spawn) < SpawnManager.getSpawnRadius()) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {

        if (!event.getEntity().getWorld().getName().equals("world")) return;

        Location spawn = event.getEntity().getWorld().getSpawnLocation();
        spawn.setY(event.getEntity().getY());
        if (event.getEntity().getLocation().distance(spawn) < SpawnManager.getSpawnRadius()) {
            event.setCancelled(true);
        }

    }

}
