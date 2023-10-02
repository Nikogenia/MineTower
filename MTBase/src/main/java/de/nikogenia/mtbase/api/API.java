package de.nikogenia.mtbase.api;

import de.nikogenia.mtbase.Main;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.bukkit.Bukkit;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class API {

    Socket socket;

    public API() {

        Map<String, String> auth = new HashMap<>();
        auth.put("key", Main.getSql().getGeneralEntry("api_key"));
        auth.put("id", Main.getConfiguration().getName());

        IO.Options options = IO.Options.builder()
                .setAuth(auth)
                .build();

        socket = IO.socket(URI.create(Main.getConfiguration().getApi().getUrl() + "/api"), options);

        socket.on("connect", this::connect);

        socket.on("disconnect", this::disconnect);

        socket.on("api_error", this::apiError);

        socket.on("command", this::command);

        socket.on("tab_complete", this::tabComplete);

        socket.connect();

    }

    private void connect(Object... args) {

        Main.getInstance().getLogger().info("API connected");

        socket.emit("servers", new JSONObject(Collections.singletonMap("request", true)));

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

            if (!data.getString("server").equals(Main.getConfiguration().getName())) return;

            Main.getInstance().getLogger().info("(" + data.getString("user") + ") > " + data.getString("command"));

            Main.getInstance().runCommand(data.getString("command"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void tabComplete(Object... args) {

        try {

            JSONObject data = (JSONObject) args[0];

            if (!data.getString("server").equals(Main.getConfiguration().getName())) return;

            List<String> options = Bukkit.getCommandMap().tabComplete(
                    Bukkit.getConsoleSender(), data.getString("input"));

            data.put("options", options);

            socket.emit("tab_complete", data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void exit() {

        socket.close();

    }

}
