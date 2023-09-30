package de.nikogenia.mtproxy.listeners;

import de.nikogenia.mtproxy.Main;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ConnectionListeners implements Listener {

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
