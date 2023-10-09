package de.nikogenia.mtsmp.spawn;

import de.nikogenia.mtsmp.Main;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class SpawnManager {

    private Map<Player, Integer> flying;

    public SpawnManager() {

        flying = new HashMap<>();

        run();

    }

    public void run() {

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {

                    if (!player.getGameMode().equals(GameMode.SURVIVAL)) continue;
                    player.setAllowFlight(isInFlyRadius(player));

                    if (flying.containsKey(player) & !player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isAir()) {
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        player.setGliding(false);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                flying.remove(player);
                            }
                        }.runTaskLater(Main.getInstance(), 5);
                    }

                }
            }
        }.runTaskTimer(Main.getInstance(), 3, 3);

    }

    public static boolean isInFlyRadius(Player player) {

        if (!player.getWorld().getName().equals("world")) return false;

        return player.getWorld().getSpawnLocation().distance(player.getLocation()) <= 40;

    }

    public Map<Player, Integer> getFlying() { return flying; }

    public static int getSpawnRadius() {
        return 140;
    }

    public static int getBoostCount() {
        return 3;
    }

}
