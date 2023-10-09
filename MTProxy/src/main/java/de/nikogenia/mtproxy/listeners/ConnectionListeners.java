package de.nikogenia.mtproxy.listeners;

import de.nikogenia.mtproxy.Main;
import de.nikogenia.mtproxy.sql.SQLPlayer;
import de.nikogenia.mtproxy.utils.MathUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Timestamp;
import java.time.Instant;

public class ConnectionListeners implements Listener {

    @EventHandler
    public void onJoin(PostLoginEvent event) {

        Main.getInstance().getLogger().info(event.getPlayer().getName() + " joined the network!");

        Main.getSql().getSession().clear();

        SQLPlayer player = Main.getSql().getPlayerByUUID(event.getPlayer().getUniqueId().toString());
        if (player == null) {
            player = new SQLPlayer();
            player.setUuid(event.getPlayer().getUniqueId().toString());
            player.setServer(Main.getSql().getInstance("smp"));
            player.setFirstJoined(Timestamp.from(Instant.now()));
            player.setNumberJoined(0);
            player.setTimePlayed(0);
            player.setBanReason("");
            player.setStatus(0);
        }
        player.setName(event.getPlayer().getName());

        if (player.getBanUntil() != null) {

            if (Instant.now().isBefore(player.getBanUntil().toInstant())) {
                event.getPlayer().disconnect(new TextComponent(
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                         \n" +
                                ChatColor.GREEN + ChatColor.BOLD + "PixPlex.net\n" +
                                ChatColor.GRAY + ChatColor.STRIKETHROUGH + "                         \n\n" +
                                ChatColor.RED + ChatColor.BOLD + "YOU ARE BANNED!\n\n" +
                                ChatColor.GRAY + ChatColor.UNDERLINE + "Reason\n\n" +
                                ChatColor.AQUA + player.getBanReason() + "\n\n" +
                                ChatColor.GRAY + "The ban will be removed in\n" +
                                MathUtils.formatTime((int) (player.getBanUntil().toInstant().getEpochSecond() -
                                        Instant.now().getEpochSecond()), true)));
            } else {
                player.setBanUntil(null);
                player.setBanReason("");
            }
        }

        player.setOnline(true);
        player.setNumberJoined(player.getNumberJoined() + 1);
        player.setLastJoined(Timestamp.from(Instant.now()));
        Main.getSql().getSession().persist(player);

        Main.getSql().getSession().getTransaction().commit();
        Main.getSql().getSession().beginTransaction();

    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {

        Main.getInstance().getLogger().info(event.getPlayer().getName() + " left the network!");

        Main.getSql().getSession().clear();

        SQLPlayer player = Main.getSql().getPlayerByUUID(event.getPlayer().getUniqueId().toString());
        if (player != null) {
            player.setOnline(false);
            player.setLastDisconnect(Timestamp.from(Instant.now()));
            Main.getSql().getSession().getTransaction().commit();
            Main.getSql().getSession().beginTransaction();
        }

    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {

        ServerPing ping = event.getResponse();

        ping.setDescriptionComponent(new TextComponent(Main.getMotd()));

        ServerPing.Players players = ping.getPlayers();
        players.setMax(1000);
        players.setSample(new ServerPing.PlayerInfo[]{});
        ping.setPlayers(players);

        event.setResponse(ping);

    }

    @EventHandler
    public void onConnect(ServerConnectEvent event) {

        if (event.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) {

            Main.getSql().getSession().clear();

            SQLPlayer player = Main.getSql().getPlayerByUUID(event.getPlayer().getUniqueId().toString());
            if (player == null) return;
            ServerInfo smp = Main.getInstance().getProxy().getServerInfo(player.getServer().getName());
            if (smp == null) return;

            event.setTarget(smp);

        }

    }

}
