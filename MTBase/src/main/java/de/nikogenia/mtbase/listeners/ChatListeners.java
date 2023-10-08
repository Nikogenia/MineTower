package de.nikogenia.mtbase.listeners;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatDecorateEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatListeners implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        event.renderer((Player source, Component displayName, Component message, Audience viewer) ->
                Component.text("[").color(NamedTextColor.GRAY)
                        .append(Component.text(formatter.format(LocalDateTime.now())).color(NamedTextColor.GRAY))
                        .append(Component.text("] <"))
                        .append(displayName)
                        .append(Component.text("> "))
                        .append(message.color(NamedTextColor.WHITE)));

    }

}
