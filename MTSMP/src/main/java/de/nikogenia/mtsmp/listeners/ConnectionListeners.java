package de.nikogenia.mtsmp.listeners;

import de.nikogenia.mtbase.MTBase;
import de.nikogenia.mtbase.tablist.TabListManager;
import de.nikogenia.mtsmp.tablist.CustomTabList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        event.getPlayer().sendMessage(Component.text("")
                .append(Component.text("Welcome on PixPlex SMP!").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD))
                .appendNewline().appendNewline()
                .append(Component.text("You can glide from the spawn island with double space!").color(NamedTextColor.YELLOW))
                .appendNewline().appendNewline()
                .append(Component.text("Join the PixPlex.net Discord server! Have fun!").color(NamedTextColor.GREEN))
                .appendNewline());

        MTBase.getTabListManager().setHeaderFooter(event.getPlayer(), TabListManager.getDefaultHeader(), TabListManager.getDefaultFooter());
        CustomTabList.setAllPlayerTeams();

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

    }

}
