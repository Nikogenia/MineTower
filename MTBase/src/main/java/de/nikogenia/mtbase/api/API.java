package de.nikogenia.mtbase.api;

import de.nikogenia.mtbase.MTBase;
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
        auth.put("key", MTBase.getSql().getGeneralEntry("api_key"));
        auth.put("id", MTBase.getConfiguration().getName());

        IO.Options options = IO.Options.builder()
                .setAuth(auth)
                .build();

        socket = IO.socket(URI.create(MTBase.getConfiguration().getApi().getUrl() + "/api"), options);

        socket.on("connect", this::connect);

        socket.on("disconnect", this::disconnect);

        socket.on("api_error", this::apiError);

        socket.on("command", this::command);

        socket.on("tab_complete", this::tabComplete);

        socket.connect();

    }

    private void connect(Object... args) {

        MTBase.getInstance().getLogger().info("API connected");

    }

    private void disconnect(Object... args) {

        MTBase.getInstance().getLogger().info("API disconnected");

    }

    private void apiError(Object... args) {

        MTBase.getInstance().getLogger().info("API error: " + args[0]);

    }

    private void command(Object... args) {

        try {

            JSONObject data = (JSONObject) args[0];

            if (!data.getString("server").equals(MTBase.getConfiguration().getName())) return;

            MTBase.getInstance().getLogger().info("(" + data.getString("user") + ") > " + data.getString("command"));

            MTBase.getInstance().runCommand(data.getString("command"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void tabComplete(Object... args) {

        try {

            JSONObject data = (JSONObject) args[0];

            if (!data.getString("server").equals(MTBase.getConfiguration().getName())) return;

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
