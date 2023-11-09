package de.nikogenia.mtsmp.commands;

import de.nikogenia.mtbase.utils.CommandUtils;
import de.nikogenia.mtsmp.Main;
import de.nikogenia.mtsmp.home.HomeManager;
import de.nikogenia.mtsmp.spawn.SpawnManager;
import de.nikogenia.mtsmp.sql.SQLHome;
import de.nikogenia.mtsmp.status.Status;
import de.nikogenia.mtsmp.status.StatusManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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

public class EndDimensionCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!CommandUtils.checkArgsCount(sender, args, 1, getPrefix())) return true;

        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(CommandUtils.buildHelp(command, getPrefix(),"help", "reload", "info"));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            Main.getInstance().loadEndDimension();
            sender.sendMessage(getPrefix().append(Component.text("Reloaded end dimension!")
                    .color(NamedTextColor.GREEN)));
        }

        sender.sendMessage(getPrefix().append(Component.text("End dimension: ").color(NamedTextColor.GREEN)
                .append(Component.text(Main.isEndDimension()).color(NamedTextColor.YELLOW))));

        return true;

    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        ArrayList<String> result = new ArrayList<>();

        result.add("reload");
        result.add("info");
        result.add("help");

        return CommandUtils.formatTabComplete(result, args);

    }

    public static Component getPrefix() {
        return Component.text("[").color(NamedTextColor.GRAY)
                .append(Component.text("End").color(NamedTextColor.BLACK).decorate(TextDecoration.BOLD))
                .append(Component.text("] "));
    }

}
