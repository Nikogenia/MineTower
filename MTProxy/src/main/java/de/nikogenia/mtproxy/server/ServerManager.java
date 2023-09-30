package de.nikogenia.mtproxy.server;

import de.nikogenia.mtproxy.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ServerManager {

    public ServerManager() {

    }

    public void updateServers(JSONObject data) {

        List<String> oldServers = new ArrayList<>(getProxy().getConfig().getServersCopy().keySet());

        try {
            JSONArray servers = data.getJSONArray("servers");
            for (int i = 0; i < servers.length(); i++) {
                JSONObject server = servers.getJSONObject(i);
                if (!server.getString("type").equals("paper")) continue;
                oldServers.remove(server.getString("name"));
                String[] address = server.getString("address").split(":");
                getProxy().getConfig().addServer(getProxy().constructServerInfo(
                        server.getString("name"),
                        new InetSocketAddress(address[0], Integer.parseInt(address[1])),
                        "", false));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (String oldServer : oldServers) {
            getProxy().getConfig().removeServerNamed(oldServer);
        }

    }

    private ProxyServer getProxy() {
        return Main.getInstance().getProxy();
    }

}
