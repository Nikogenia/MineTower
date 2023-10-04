package de.nikogenia.mtproxy.server;

import de.nikogenia.mtproxy.Main;
import de.nikogenia.mtproxy.sql.SQLInstance;
import net.md_5.bungee.api.ProxyServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerManager {

    public ServerManager() {

    }

    public void updateServers() {

        List<String> oldServers = new ArrayList<>(getProxy().getConfig().getServersCopy().keySet());

        for (SQLInstance server : Main.getSql().getInstances()) {
            if (!server.getType().equals("paper")) continue;
            oldServers.remove(server.getName());
            String[] address = server.getAddress().split(":");
            getProxy().getConfig().addServer(getProxy().constructServerInfo(
                    server.getName(),
                    new InetSocketAddress(address[0], Integer.parseInt(address[1])),
                    "", false));
        }

        for (String oldServer : oldServers) {
            getProxy().getConfig().removeServerNamed(oldServer);
        }

    }

    private ProxyServer getProxy() {
        return Main.getInstance().getProxy();
    }

}
