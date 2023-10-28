package de.nikogenia.mtsmp.listeners;

import de.nikogenia.mtbase.permission.Perm;
import de.nikogenia.mtsmp.spawn.SpawnManager;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import java.util.Arrays;

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
    public void onExplode(BlockExplodeEvent event) {

        if (event.getBlock().getWorld().getName().equals("world_nether")) {
            Location netherSpawn = new Location(event.getBlock().getWorld(), -13, 77, 0);
            if (event.getBlock().getLocation().distance(netherSpawn) < SpawnManager.getNetherSpawnRadius() + 15) {
                event.setCancelled(true);
            }
            return;
        }

        if (!event.getBlock().getWorld().getName().equals("world")) return;

        Location spawn = event.getBlock().getWorld().getSpawnLocation();
        spawn.setY(event.getBlock().getY());
        if (event.getBlock().getLocation().distance(spawn) < SpawnManager.getSpawnRadius() + 15) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {

        if (!event.getEntity().getWorld().getName().equals("world")) return;

        if (Arrays.asList(EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME).contains(event.getEntityType())) {
            if (event.getDamager().hasPermission(Perm.SPAWN_BYPASS.getValue())) return;
            if (event.getEntity().getScoreboardTags().contains("protected")) {
                event.setCancelled(true);
            }
            return;
        }

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
    public void onChangeBlock(EntityChangeBlockEvent event) {

        if (!event.getEntity().getWorld().getName().equals("world")) return;

        Location spawn = event.getBlock().getWorld().getSpawnLocation();
        spawn.setY(event.getBlock().getY());
        if (event.getBlock().getLocation().distance(spawn) < SpawnManager.getSpawnRadius()) {
            if (event.getEntityType().equals(EntityType.ENDERMAN)) {
                event.setCancelled(true);
            }
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
