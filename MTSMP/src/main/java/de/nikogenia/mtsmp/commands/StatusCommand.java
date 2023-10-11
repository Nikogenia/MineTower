package de.nikogenia.mtsmp.commands;

import de.nikogenia.mtbase.MTBase;
import de.nikogenia.mtbase.utils.CommandUtils;
import de.nikogenia.mtsmp.Main;
import de.nikogenia.mtsmp.status.Status;
import de.nikogenia.mtsmp.status.StatusManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StatusCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!CommandUtils.checkPlayer(sender, StatusManager.getPrefix())) return true;

        if (!CommandUtils.checkArgsCount(sender, args, 1, StatusManager.getPrefix())) return true;

        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(CommandUtils.buildHelp(command, StatusManager.getPrefix(),"help", "<status>"));
            return true;
        }

        Player player = (Player) sender;

        Main.getStatusManager().setStatus(player, Status.fromName(args[0]));

        MTBase.getTabListManager().setAllPlayerTeams();

        sender.sendMessage(StatusManager.getPrefix().append(Component.text("Your status was set to ").color(NamedTextColor.GREEN))
                .append(Status.fromName(args[0]).getText())
                .append(Component.text("!")));

        return true;

    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        ArrayList<String> result = new ArrayList<>();

        for (Status status : Status.values()) {
            result.add(status.name().toLowerCase());
        }

        return CommandUtils.formatTabComplete(result, args);

    }

}
