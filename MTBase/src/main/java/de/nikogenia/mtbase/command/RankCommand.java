package de.nikogenia.mtbase.command;

import de.nikogenia.mtbase.MTBase;
import de.nikogenia.mtbase.permission.Perm;
import de.nikogenia.mtbase.utils.CommandUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RankCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!CommandUtils.checkArgsMin(sender, args, 1, Perm.getPrefix())) return true;

        switch (args[0].toLowerCase()) {
            case "help":
                sender.sendMessage(CommandUtils.buildHelp(
                        command, Perm.getPrefix(), "help", "reload"));
                break;
            case "reload":
                if (!CommandUtils.checkArgsCount(sender, args, 1, Perm.getPrefix())) return true;
                if (!CommandUtils.checkPerm(sender, Perm.EDIT_RANK.getValue(), Perm.getPrefix())) return true;
                MTBase.getTabListManager().setAllPlayerTeams();
                sender.sendMessage(Perm.getPrefix().append(Component
                        .text("All ranks were reloaded!").color(NamedTextColor.GREEN)));
                break;
            default:
                CommandUtils.invalidArg(sender, 1, Perm.getPrefix());
        }

        return true;

    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        ArrayList<String> result = new ArrayList<>();

        result.add("help");
        result.add("reload");

        return CommandUtils.formatTabComplete(result, args);

    }

}
