package de.nikogenia.mtbase.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandUtils {

    public static ArrayList<String> formatTabComplete(ArrayList<String> list, String[] args) {

        if (args.length == 0) return null;

        ArrayList<String> result = new ArrayList<>();

        String currentArg = args[args.length - 1].toLowerCase();

        for (String c : list) {

            String complete = c.toLowerCase();

            if (complete.startsWith(currentArg)) result.add(c);

        }

        return result;

    }

    public static boolean checkArgsMin(CommandSender sender, String[] args, int min, Component prefix) {

        if (args.length < min) {
            sender.sendMessage(prefix.append(Component
                    .text("Missing arguments! Expecting at least " + min + " argument(s)!")
                    .color(NamedTextColor.RED)));
            return false;
        }

        return true;

    }

    public static void invalidArg(CommandSender sender, int pos, Component prefix) {

        sender.sendMessage(prefix.append(Component
                .text("Invalid " + getOrdinalName(pos) + " argument!")
                .color(NamedTextColor.RED)));

    }

    public static boolean checkPerm(CommandSender sender, String perm, Component prefix) {

        if (sender.hasPermission(perm)) return true;

        sender.sendMessage(prefix.append(Component
                .text("Missing permission! You need ")
                .color(NamedTextColor.RED)
                .append(Component.text(perm).decorate(TextDecoration.ITALIC))
                .append(Component.text(" to execute this command!"))));
        return false;

    }

    public static boolean checkPlayer(CommandSender sender, Component prefix) {

        if (sender instanceof Player) return true;

        sender.sendMessage(prefix.append(Component
                .text("This command can only be executed as a player!")
                .color(NamedTextColor.RED)));
        return false;

    }

    public static boolean checkArgsCount(CommandSender sender, String[] args, int count, Component prefix) {

        if (args.length != count) {
            sender.sendMessage(prefix.append(Component
                    .text("Invalid argument count! Expecting " + count + " argument(s)!")
                    .color(NamedTextColor.RED)));
            return false;
        }

        return true;

    }

    public static Component buildHelp(Command command, Component prefix, String... usages) {

        TextComponent.Builder builder = Component.text().content("Help for command ").color(NamedTextColor.YELLOW)
                .append(Component.text("/" + command.getName()).color(NamedTextColor.GOLD))
                .append(Component.text(":").color(NamedTextColor.GRAY))
                .appendNewline().appendNewline()
                .append(Component.text("Aliases"))
                .append(Component.text(": ").color(NamedTextColor.GRAY));

        List<Component> aliases = new ArrayList<>();
        command.getAliases().forEach(alias -> aliases.add(Component.text("/" + alias).color(NamedTextColor.GOLD)));
        builder.append(Component.join(JoinConfiguration.separator(
                Component.text(", ").color(NamedTextColor.GRAY)), aliases));

        if (command.getAliases().isEmpty()) builder.append(Component.text("-").color(NamedTextColor.GOLD));

        builder.appendNewline().appendNewline()
                .append(Component.text("Usages"))
                .append(Component.text(":").color(NamedTextColor.GRAY))
                .appendNewline();

        for (String usage : usages) {
            builder.append(Component.text("- ").color(NamedTextColor.GRAY))
                    .append(Component.text("/" + command.getName() + " " + usage).color(NamedTextColor.GOLD))
                    .appendNewline();
        }

        return prefix.append(builder.build());

    }

    public static String getOrdinalName(int i) {

        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };

        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];
        }

    }

}
