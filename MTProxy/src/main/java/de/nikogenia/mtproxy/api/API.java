package de.nikogenia.mtproxy.api;

import de.nikogenia.mtproxy.Main;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.*;

public class API {

    Socket socket;

    public API() {

        Main.getSql().getSession().clear();

        Map<String, String> auth = new HashMap<>();
        auth.put("key", Main.getSql().getGeneralEntry("api_key"));
        auth.put("id", Main.getConfig().getName());

        IO.Options options = IO.Options.builder()
                .setAuth(auth)
                .build();

        socket = IO.socket(URI.create(Main.getConfig().getApi().getUrl() + "/api"), options);

        socket.on("connect", this::connect);

        socket.on("disconnect", this::disconnect);

        socket.on("api_error", this::apiError);

        socket.on("command", this::command);

        socket.on("tab_complete", this::tabComplete);

        socket.on("servers", this::servers);

        socket.on("motd_update", this::motdUpdate);

        socket.connect();

    }

    private void connect(Object... args) {

        Main.getInstance().getLogger().info("API connected");

    }

    private void disconnect(Object... args) {

        Main.getInstance().getLogger().info("API disconnected");

    }

    private void apiError(Object... args) {

        Main.getInstance().getLogger().info("API error: " + args[0]);

    }

    private void command(Object... args) {

        try {

            JSONObject data = (JSONObject) args[0];

            if (!data.getString("server").equals(Main.getConfig().getName())) return;

            System.out.println("(" + data.getString("user") + ") > " + data.getString("command"));

            Main.getInstance().getProxy().getPluginManager().dispatchCommand(
                    Main.getInstance().getProxy().getConsole(), data.getString("command"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void tabComplete(Object... args) {

        try {

            JSONObject data = (JSONObject) args[0];

            if (!data.getString("server").equals(Main.getConfig().getName())) return;

            List<String> options = Main.getInstance().getProxy().getPluginManager().tabCompleteCommand(
                    Main.getInstance().getProxy().getConsole(), data.getString("input"));

            data.put("options", options);

            socket.emit("tab_complete", data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void servers(Object... args) {

        Main.getServerManager().updateServers();

    }

    private void motdUpdate(Object... args) {

        Main.getInstance().updateMotd();

    }

    public void exit() {

        socket.close();

    }

}
