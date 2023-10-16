package de.nikogenia.mtsmp.commands;

import de.nikogenia.mtbase.utils.CommandUtils;
import de.nikogenia.mtsmp.Main;
import de.nikogenia.mtsmp.home.HomeManager;
import de.nikogenia.mtsmp.spawn.SpawnManager;
import de.nikogenia.mtsmp.sql.SQLHome;
import de.nikogenia.mtsmp.sql.SQLShop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!CommandUtils.checkPlayer(sender, SpawnManager.getPrefix())) return true;

        if (!CommandUtils.checkArgsCount(sender, args, 0, SpawnManager.getPrefix())) return true;

        Player player = (Player) sender;

        SQLHome home = Main.getHomeManager().getHome(player);

        if (home == null) {
            sender.sendMessage(HomeManager.getPrefix().append(Component.text("You've not set your home yet! Use /sethome!")
                    .color(NamedTextColor.RED)));
            return true;
        }

        Location pos = new Location(Objects.requireNonNull(Bukkit.getWorld(home.getWorld())),
                home.getX(), home.getY(), home.getZ(), player.getYaw(), player.getPitch());
        player.teleport(pos);

        sender.sendMessage(SpawnManager.getPrefix().append(Component.text("You were teleported to your home!")
                .color(NamedTextColor.GREEN)));

        return true;

    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        return new ArrayList<>();

    }

}
