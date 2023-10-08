package de.nikogenia.mtbase.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        event.joinMessage(Component.text(">> ").color(NamedTextColor.DARK_GRAY)
                .append(Component.text(event.getPlayer().getName()).color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD))
                .append(Component.text(" joined").color(NamedTextColor.GRAY)));

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        event.quitMessage(Component.text("<< ").color(NamedTextColor.DARK_GRAY)
                .append(Component.text(event.getPlayer().getName()).color(NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD))
                .append(Component.text(" left").color(NamedTextColor.GRAY)));

    }

}
