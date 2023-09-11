package de.nikogenia.mtproxy.api;

import de.nikogenia.mtproxy.Main;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class API {

    Socket socket;

    public API() {

        Map<String, String> auth = new HashMap<>();
        auth.put("key", Main.getSql().getGeneralEntry("api_key"));
        auth.put("id", Main.getConfig().getName());

        IO.Options options = IO.Options.builder()
                .setAuth(auth)
                .build();

        socket = IO.socket(URI.create(Main.getConfig().getApi().getUrl() + "/api"), options);

        socket.on("connect", args -> connect());

        socket.on("disconnect", args -> Main.getInstance().getLogger().info("API disconnected"));

        socket.on("api_error", args -> Main.getInstance().getLogger().info("API error: " + args[1]));

        socket.connect();

    }

    private void connect() {

        Main.getInstance().getLogger().info("API connected");

    }

    public void exit() {

        socket.close();

    }

}
