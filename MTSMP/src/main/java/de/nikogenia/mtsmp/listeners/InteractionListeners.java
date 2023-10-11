package de.nikogenia.mtsmp.listeners;

import de.nikogenia.mtbase.permission.Perm;
import de.nikogenia.mtsmp.Main;
import de.nikogenia.mtsmp.shop.ShopManager;
import de.nikogenia.mtsmp.spawn.SpawnManager;
import de.nikogenia.mtsmp.sql.SQLShop;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class InteractionListeners implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        if (event.getBlock().getWorld().getName().equals("world_nether")) {
            if (event.getPlayer().hasPermission(Perm.SPAWN_BYPASS.getValue())) return;
            Location netherSpawn = new Location(event.getBlock().getWorld(), -13, 77, 0);
            if (event.getBlock().getLocation().distance(netherSpawn) < SpawnManager.getNetherSpawnRadius()) {
                event.setCancelled(true);
            }
            return;
        }

        if (!event.getBlock().getWorld().getName().equals("world")) return;

        Location spawn = event.getBlock().getWorld().getSpawnLocation();
        spawn.setY(event.getBlock().getY());
        if (event.getBlock().getLocation().distance(spawn) < SpawnManager.getSpawnRadius()) {
            if (event.getPlayer().hasPermission(Perm.SPAWN_BYPASS.getValue())) return;
            SQLShop shop = Main.getShopManager().getShop(event.getBlock().getLocation());
            if (shop == null) {
                event.setCancelled(true);
                return;
            }
            if (shop.getOwner() == null) {
                event.setCancelled(true);
                return;
            }
            if (shop.getOwner().getUuid().equals(event.getPlayer().getUniqueId().toString()) &
                    event.getBlock().getY() <= ShopManager.getMaxShopHeight()) return;
            event.setCancelled(true);
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        if (event.getBlock().getWorld().getName().equals("world_nether")) {
            if (event.getPlayer().hasPermission(Perm.SPAWN_BYPASS.getValue())) return;
            Location netherSpawn = new Location(event.getBlock().getWorld(), -13, 77, 0);
            if (event.getBlock().getLocation().distance(netherSpawn) < SpawnManager.getNetherSpawnRadius()) {
                event.setCancelled(true);
            }
            return;
        }

        if (!event.getBlock().getWorld().getName().equals("world")) return;

        Location spawn = event.getBlock().getWorld().getSpawnLocation();
        spawn.setY(event.getBlock().getY());
        if (event.getBlock().getLocation().distance(spawn) < SpawnManager.getSpawnRadius()) {
            if (event.getPlayer().hasPermission(Perm.SPAWN_BYPASS.getValue())) return;
            SQLShop shop = Main.getShopManager().getShop(event.getBlock().getLocation());
            if (shop == null) {
                event.setCancelled(true);
                return;
            }
            if (shop.getOwner() == null) {
                event.setCancelled(true);
                return;
            }
            if (shop.getOwner().getUuid().equals(event.getPlayer().getUniqueId().toString()) &
                    event.getBlock().getY() <= ShopManager.getMaxShopHeight()) return;
            event.setCancelled(true);
        }

    }

}
