package de.nikogenia.mtsmp.commands;

import de.nikogenia.mtbase.MTBase;
import de.nikogenia.mtbase.permission.Perm;
import de.nikogenia.mtbase.sql.SQLPlayer;
import de.nikogenia.mtbase.utils.CommandUtils;
import de.nikogenia.mtsmp.Main;
import de.nikogenia.mtsmp.shop.ShopManager;
import de.nikogenia.mtsmp.sql.SQLShop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ShopCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!CommandUtils.checkArgsMin(sender, args, 1, ShopManager.getPrefix())) return true;

        switch (args[0].toLowerCase()) {
            case "help":
                if (sender.hasPermission(Perm.SHOP_ADMIN.getValue())) sender.sendMessage(CommandUtils.buildHelp(
                        command, ShopManager.getPrefix(), "help", "info", "info <shop>",
                        "owner <player>", "owner <player> <shop>", "list"));
                else sender.sendMessage(CommandUtils.buildHelp(
                        command, ShopManager.getPrefix(),"help", "info", "info <shop>", "list"));
                break;
            case "info":
                info(sender, command, label, args);
                break;
            case "owner":
                if (!CommandUtils.checkPerm(sender, Perm.SHOP_ADMIN.getValue(), ShopManager.getPrefix())) return true;
                owner(sender, command, label, args);
                break;
            case "reload":
                if (!CommandUtils.checkArgsCount(sender, args, 1, ShopManager.getPrefix())) return true;
                if (!CommandUtils.checkPerm(sender, Perm.SHOP_ADMIN.getValue(), ShopManager.getPrefix())) return true;
                Main.getShopManager().updateShops();
                sender.sendMessage(ShopManager.getPrefix().append(Component
                        .text("All shops were reloaded!").color(NamedTextColor.GREEN)));
                break;
            case "list":
                list(sender, command, label, args);
                break;
            default:
                CommandUtils.invalidArg(sender, 1, ShopManager.getPrefix());
        }

        return true;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        ArrayList<String> result = new ArrayList<>();

        switch (args.length) {
            case 1:
                result.add("help");
                result.add("info");
                result.add("list");
                if (sender.hasPermission(Perm.SHOP_ADMIN.getValue())) {
                    result.add("owner");
                    result.add("reload");
                }
                break;
            case 2:
                if (args[0].equals("info")) {
                    Main.getShopManager().getShops().forEach(shop -> result.add(shop.getName()));
                }
                if (args[0].equals("owner")) {
                    MTBase.getSql().getSession().clear();
                    MTBase.getSql().getPlayers().forEach(player -> result.add(player.getName()));
                }
                break;
            case 3:
                if (args[0].equals("owner")) {
                    Main.getShopManager().getShops().forEach(shop -> result.add(shop.getName()));
                }
                break;
        }

        return CommandUtils.formatTabComplete(result, args);

    }

    private void info(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        SQLShop shop;

        if (args.length == 1) {
            if (!CommandUtils.checkPlayer(sender, ShopManager.getPrefix())) return;
            shop = Main.getShopManager().getShop(((Player) sender).getLocation());
        }
        else {
            if (!CommandUtils.checkArgsCount(sender, args, 2, ShopManager.getPrefix())) return;
            shop = Main.getShopManager().getShop(args[1]);
        }

        if (shop == null) {
            sender.sendMessage(ShopManager.getPrefix().append(Component
                    .text("No shop was found!").color(NamedTextColor.RED)));
            return;
        }

        sender.sendMessage(ShopManager.getPrefix().append(Component
                .text("Information about shop ").color(NamedTextColor.AQUA)
                .append(Component.text(shop.getName()).color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(": ").color(NamedTextColor.GRAY))
                .appendNewline().appendNewline()
                .append(Component.text("Owner"))
                .append(Component.text(": ").color(NamedTextColor.GRAY))
                .append(Component.text((shop.getOwner() == null) ? "-" : shop.getOwner().getName()).color(NamedTextColor.LIGHT_PURPLE))
                .appendNewline()
                .append(Component.text("Created"))
                .append(Component.text(": ").color(NamedTextColor.GRAY))
                .append(Component.text(shop.getCreated().toString()).color(NamedTextColor.LIGHT_PURPLE))
                .appendNewline()));


    }

    private void owner(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        SQLShop shop;

        if (!CommandUtils.checkArgsMin(sender, args, 2, ShopManager.getPrefix())) return;

        if (args.length == 2) {
            if (!CommandUtils.checkPlayer(sender, ShopManager.getPrefix())) return;
            shop = Main.getShopManager().getShop(((Player) sender).getLocation());
        }
        else {
            if (!CommandUtils.checkArgsCount(sender, args, 3, ShopManager.getPrefix())) return;
            shop = Main.getShopManager().getShop(args[2]);
        }

        if (shop == null) {
            sender.sendMessage(ShopManager.getPrefix().append(Component
                    .text("No shop was found!").color(NamedTextColor.RED)));
            return;
        }

        SQLPlayer player = MTBase.getSql().getPlayerByName(args[1]);

        if (player == null) {
            sender.sendMessage(ShopManager.getPrefix().append(Component
                    .text("The targeted player wasn't found!").color(NamedTextColor.RED)));
            return;
        }

        shop.setOwner(player);

        MTBase.getSql().getSession().merge(shop);
        MTBase.getSql().getSession().getTransaction().commit();
        MTBase.getSql().getSession().beginTransaction();

        sender.sendMessage(ShopManager.getPrefix().append(Component
                .text("Successfully changed the owner of ").color(NamedTextColor.GREEN)
                .append(Component.text(shop.getName()).color(NamedTextColor.GOLD))
                .append(Component.text(" to "))
                .append(Component.text(player.getName()).color(NamedTextColor.GOLD))
                .append(Component.text("!"))));

    }

    private void list(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!CommandUtils.checkArgsCount(sender, args, 1, ShopManager.getPrefix())) return;

        TextComponent.Builder builder = Component.text().content("List of all shops").color(NamedTextColor.AQUA)
                .append(Component.text(": ").color(NamedTextColor.GRAY))
                .appendNewline().appendNewline();

        if (Main.getShopManager().getShops().isEmpty())
            builder.append(Component.text("No shops defined!").color(NamedTextColor.LIGHT_PURPLE)
                    .decorate(TextDecoration.ITALIC)).appendNewline();

        for (SQLShop shop : Main.getShopManager().getShops()) {
            builder.append(Component.text("- ").color(NamedTextColor.GRAY))
                    .append(Component.text(shop.getName() + " ").color(NamedTextColor.LIGHT_PURPLE))
                    .append(Component.text("(").color(NamedTextColor.GRAY))
                    .append(Component.text((shop.getOwner() == null) ? "FREE" : shop.getOwner().getName()).color(NamedTextColor.GOLD))
                    .append(Component.text(")").color(NamedTextColor.GRAY))
                    .appendNewline();
        }

        sender.sendMessage(ShopManager.getPrefix().append(builder.build()));

    }

}
