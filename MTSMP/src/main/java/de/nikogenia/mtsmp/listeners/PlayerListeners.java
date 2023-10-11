package de.nikogenia.mtsmp.listeners;

import de.nikogenia.mtsmp.Main;
import de.nikogenia.mtsmp.spawn.SpawnManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class PlayerListeners implements Listener {

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {

        if (!event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) return;

        if (!SpawnManager.isInFlyRadius(event.getPlayer())) return;

        event.setCancelled(true);

        event.getPlayer().setGliding(true);

        Main.getSpawnManager().getFlying().put(event.getPlayer(), SpawnManager.getBoostCount());

        event.getPlayer().sendActionBar(Component.text("Press ").decorate(TextDecoration.ITALIC)
                        .append(Component.keybind("key.swapOffhand"))
                        .append(Component.text(" to boost yourself!")));

    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        if (!event.getEntityType().equals(EntityType.PLAYER)) return;

        if ((event.getCause().equals(EntityDamageEvent.DamageCause.FALL) | event.getCause().equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL))
            & Main.getSpawnManager().getFlying().containsKey((Player) event.getEntity())) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onGlide(EntityToggleGlideEvent event) {

        if (!event.getEntityType().equals(EntityType.PLAYER)) return;

        if (Main.getSpawnManager().getFlying().containsKey((Player) event.getEntity())) event.setCancelled(true);

    }

    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent event) {

        if (!Main.getSpawnManager().getFlying().containsKey(event.getPlayer())) return;

        int boosts = Main.getSpawnManager().getFlying().get(event.getPlayer());
        if (boosts <= 0) return;

        Main.getSpawnManager().getFlying().put(event.getPlayer(), boosts - 1);
        event.setCancelled(true);
        event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(3));

    }

}
