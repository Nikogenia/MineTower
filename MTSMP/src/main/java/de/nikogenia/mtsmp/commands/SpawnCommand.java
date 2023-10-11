package de.nikogenia.mtsmp.commands;

import de.nikogenia.mtbase.utils.CommandUtils;
import de.nikogenia.mtsmp.spawn.SpawnManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpawnCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!CommandUtils.checkPlayer(sender, SpawnManager.getPrefix())) return true;

        if (!CommandUtils.checkArgsCount(sender, args, 0, SpawnManager.getPrefix())) return true;

        Player player = (Player) sender;

        player.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());

        sender.sendMessage(SpawnManager.getPrefix().append(Component.text("You was teleported to spawn!")
                .color(NamedTextColor.GREEN)));

        return true;

    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        return new ArrayList<>();

    }

}
