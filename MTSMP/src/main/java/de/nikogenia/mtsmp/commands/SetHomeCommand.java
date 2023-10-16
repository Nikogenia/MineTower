package de.nikogenia.mtsmp.commands;

import de.nikogenia.mtbase.MTBase;
import de.nikogenia.mtbase.sql.SQLPlayer;
import de.nikogenia.mtbase.utils.CommandUtils;
import de.nikogenia.mtsmp.Main;
import de.nikogenia.mtsmp.home.HomeManager;
import de.nikogenia.mtsmp.spawn.SpawnManager;
import de.nikogenia.mtsmp.sql.SQLHome;
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

public class SetHomeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!CommandUtils.checkPlayer(sender, SpawnManager.getPrefix())) return true;

        if (!CommandUtils.checkArgsCount(sender, args, 0, SpawnManager.getPrefix())) return true;

        Player player = (Player) sender;

        SQLHome home = Main.getHomeManager().getHome(player);

        if (home == null) {
            SQLPlayer sqlPlayer = MTBase.getSql().getPlayerByUUID(player.getUniqueId().toString());
            if (sqlPlayer == null) {
                sender.sendMessage(HomeManager.getPrefix().append(Component.text("Failed to fetch you from database! Please report this!")
                        .color(NamedTextColor.RED)));
                return true;
            }
            home = new SQLHome();
            home.setPlayer(sqlPlayer);
            Main.getHomeManager().getHomes().add(home);
        }

        home.setWorld(player.getWorld().getName());
        home.setX(player.getX());
        home.setY(player.getY());
        home.setZ(player.getZ());

        MTBase.getSql().getSession().merge(home);
        MTBase.getSql().getSession().getTransaction().commit();
        MTBase.getSql().getSession().beginTransaction();

        sender.sendMessage(SpawnManager.getPrefix().append(Component.text("Successfully set your home to your current position!")
                .color(NamedTextColor.GREEN)));

        return true;

    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        return new ArrayList<>();

    }

}
