package de.nikogenia.mtsmp.spawn;

import de.nikogenia.mtsmp.Main;
import jakarta.persistence.criteria.CriteriaBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

                    if (player.getWorld().getName().equals("world")) {
                        Location spawn = player.getWorld().getSpawnLocation();
                        spawn.setY(player.getY());
                        if (player.getLocation().distance(spawn) < getSpawnRadius()) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,
                                    20, 255, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                                    20, 255, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,
                                    20, 255, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                                    20, 1, false, false));
                        }
                    }

                    if (isInFlyRadius(player) & !flying.containsKey(player)) {
                        player.sendActionBar(Component.text("Press double space to glide from the island!").decorate(TextDecoration.ITALIC));
                    }

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
                        }.runTaskLater(Main.getInstance(), 1);
                    }

                }
            }
        }.runTaskTimer(Main.getInstance(), 3, 3);

    }

    public static boolean isInFlyRadius(Player player) {

        if (!player.getWorld().getName().equals("world")) return false;

        return player.getWorld().getSpawnLocation().distance(player.getLocation()) <= 100;

    }

    public Map<Player, Integer> getFlying() { return flying; }

    public static int getSpawnRadius() {
        return 140;
    }

    public static int getNetherSpawnRadius() {
        return 20;
    }

    public static int getBoostCount() {
        return 3;
    }

    public static Component getPrefix() {
        return Component.text("[").color(NamedTextColor.GRAY)
                .append(Component.text("Spawn").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD))
                .append(Component.text("] "));
    }

}
