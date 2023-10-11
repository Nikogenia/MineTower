package de.nikogenia.mtsmp.listeners;

import de.nikogenia.mtbase.permission.Perm;
import de.nikogenia.mtsmp.spawn.SpawnManager;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

public class EntityListeners implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {

        if (event.getLocation().getWorld().getName().equals("world_nether")) {
            Location netherSpawn = new Location(event.getLocation().getWorld(), -13, 77, 0);
            if (event.getLocation().distance(netherSpawn) < SpawnManager.getNetherSpawnRadius() + 15) {
                event.setCancelled(true);
            }
            return;
        }

        if (!event.getLocation().getWorld().getName().equals("world")) return;

        Location spawn = event.getLocation().getWorld().getSpawnLocation();
        spawn.setY(event.getLocation().getY());
        if (event.getLocation().distance(spawn) < SpawnManager.getSpawnRadius() + 15) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {

        if (!event.getEntity().getWorld().getName().equals("world")) return;

        if (!event.getEntityType().equals(EntityType.PLAYER)) return;

        Location spawn = event.getEntity().getWorld().getSpawnLocation();
        spawn.setY(event.getEntity().getY());
        if (event.getEntity().getLocation().distance(spawn) < SpawnManager.getSpawnRadius()) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {

        if (!event.getEntity().getWorld().getName().equals("world")) return;

        if (!event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) return;

        Location spawn = event.getEntity().getWorld().getSpawnLocation();
        spawn.setY(event.getEntity().getY());
        if (event.getEntity().getLocation().distance(spawn) < SpawnManager.getSpawnRadius()) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent event) {

        if (event.getEntity().getScoreboardTags().contains("protected")) {
            if (event.getRemover().hasPermission(Perm.SPAWN_BYPASS.getValue())) return;
            event.setCancelled(true);
        }

    }

}
