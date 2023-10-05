package de.nikogenia.mtproxy.listeners;

import de.nikogenia.mtproxy.Main;
import de.nikogenia.mtproxy.sql.SQLPlayer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Timestamp;
import java.time.Instant;

public class ConnectionListeners implements Listener {

    @EventHandler
    public void onJoin(PostLoginEvent event) {

        Main.getInstance().getLogger().info(event.getPlayer().getName() + " joined the network!");

        SQLPlayer player = Main.getSql().getPlayerByUUID(event.getPlayer().getUniqueId().toString());
        if (player == null) {
            player = new SQLPlayer();
            player.setUuid(event.getPlayer().getUniqueId().toString());
            player.setNumberJoined(0);
            player.setFirstJoined(Timestamp.from(Instant.now()));
            player.setTimePlayed(0);
        }

        player.setName(event.getPlayer().getName());
        player.setOnline(true);
        player.setNumberJoined(player.getNumberJoined() + 1);
        player.setLastJoined(Timestamp.from(Instant.now()));
        player.setServer(Main.getSql().getInstance("smp"));
        Main.getSql().getSession().persist(player);

        Main.getSql().getSession().getTransaction().commit();
        Main.getSql().getSession().beginTransaction();

    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {

        Main.getInstance().getLogger().info(event.getPlayer().getName() + " left the network!");

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

}
