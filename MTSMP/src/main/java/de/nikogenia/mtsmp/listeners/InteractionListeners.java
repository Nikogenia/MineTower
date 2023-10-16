package de.nikogenia.mtsmp.listeners;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import de.nikogenia.mtbase.permission.Perm;
import de.nikogenia.mtsmp.Main;
import de.nikogenia.mtsmp.shop.ShopManager;
import de.nikogenia.mtsmp.spawn.SpawnManager;
import de.nikogenia.mtsmp.sql.SQLShop;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

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

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {

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

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {

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

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {

        if (Arrays.asList(EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME).contains(event.getRightClicked().getType())) {
            if (event.getPlayer().hasPermission(Perm.SPAWN_BYPASS.getValue())) return;
            if (event.getRightClicked().getScoreboardTags().contains("protected")) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (event.getClickedBlock() == null) return;

        if (event.getClickedBlock().getType().equals(Material.CHISELED_BOOKSHELF)) {
            if (event.getPlayer().hasPermission(Perm.SPAWN_BYPASS.getValue())) return;
            if (event.getClickedBlock().getY() > 140) {
                event.setCancelled(true);
            }
        }

        if (Arrays.asList(Material.ARMOR_STAND, Material.ITEM_FRAME, Material.GLOW_ITEM_FRAME)
                .contains(event.getMaterial())) {

            if (event.getClickedBlock().getWorld().getName().equals("world_nether")) {
                if (event.getPlayer().hasPermission(Perm.SPAWN_BYPASS.getValue())) return;
                Location netherSpawn = new Location(event.getClickedBlock().getWorld(), -13, 77, 0);
                if (event.getClickedBlock().getLocation().distance(netherSpawn) < SpawnManager.getNetherSpawnRadius()) {
                    event.setCancelled(true);
                }
                return;
            }

            if (!event.getClickedBlock().getWorld().getName().equals("world")) return;

            Location spawn = event.getClickedBlock().getWorld().getSpawnLocation();
            spawn.setY(event.getClickedBlock().getY());
            if (event.getClickedBlock().getLocation().distance(spawn) < SpawnManager.getSpawnRadius()) {
                if (event.getPlayer().hasPermission(Perm.SPAWN_BYPASS.getValue())) return;
                SQLShop shop = Main.getShopManager().getShop(event.getClickedBlock().getLocation());
                if (shop == null) {
                    event.setCancelled(true);
                    return;
                }
                if (shop.getOwner() == null) {
                    event.setCancelled(true);
                    return;
                }
                if (shop.getOwner().getUuid().equals(event.getPlayer().getUniqueId().toString()) &
                        event.getClickedBlock().getY() <= ShopManager.getMaxShopHeight()) return;
                event.setCancelled(true);
            }

        }

        if (Arrays.asList(Material.ACACIA_TRAPDOOR, Material.BAMBOO_TRAPDOOR, Material.BIRCH_TRAPDOOR,
                Material.IRON_TRAPDOOR, Material.CHERRY_TRAPDOOR, Material.CRIMSON_TRAPDOOR,
                Material.DARK_OAK_TRAPDOOR, Material.OAK_TRAPDOOR, Material.JUNGLE_TRAPDOOR,
                Material.MANGROVE_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.WARPED_TRAPDOOR)
                .contains(event.getClickedBlock().getType())) {

            if (event.getClickedBlock().getWorld().getName().equals("world_nether")) {
                if (event.getPlayer().hasPermission(Perm.SPAWN_BYPASS.getValue())) return;
                Location netherSpawn = new Location(event.getClickedBlock().getWorld(), -13, 77, 0);
                if (event.getClickedBlock().getLocation().distance(netherSpawn) < SpawnManager.getNetherSpawnRadius()) {
                    event.setCancelled(true);
                }
                return;
            }

            if (!event.getClickedBlock().getWorld().getName().equals("world")) return;

            Location spawn = event.getClickedBlock().getWorld().getSpawnLocation();
            spawn.setY(event.getClickedBlock().getY());
            if (event.getClickedBlock().getLocation().distance(spawn) < SpawnManager.getSpawnRadius()) {
                if (event.getPlayer().hasPermission(Perm.SPAWN_BYPASS.getValue())) return;
                SQLShop shop = Main.getShopManager().getShop(event.getClickedBlock().getLocation());
                if (shop == null) {
                    event.setCancelled(true);
                    return;
                }
                if (shop.getOwner() == null) {
                    event.setCancelled(true);
                    return;
                }
                if (shop.getOwner().getUuid().equals(event.getPlayer().getUniqueId().toString()) &
                        event.getClickedBlock().getY() <= ShopManager.getMaxShopHeight()) return;
                event.setCancelled(true);
            }

        }

    }

    @EventHandler
    public void onBoost(PlayerElytraBoostEvent event) {

        if (Main.getSpawnManager().getFlying().containsKey(event.getPlayer())) {
            if (event.getPlayer().getInventory().getChestplate() == null) {
                event.setCancelled(true);
                return;
            }
            if (!event.getPlayer().getInventory().getChestplate().getType().equals(Material.ELYTRA)) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onChangeSign(SignChangeEvent event) {

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
