package de.nikogenia.mtbase.tablist;

import de.nikogenia.mtbase.MTBase;
import de.nikogenia.mtbase.permission.Rank;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class TabListManager {

    public void setHeaderFooter(Player player) {

        setHeaderFooter(player, getDefaultHeader(), getDefaultFooter());

    }

    public void setHeaderFooter(Player player, Component header, Component footer) {

        player.sendPlayerListHeaderAndFooter(header, footer);

    }

    public static Component getDefaultHeader() {

        String name = MTBase.getConfiguration().getName().toUpperCase();

        return Component.text("[ ").color(NamedTextColor.GRAY)
                .append(Component.text("PixPlex.net - " + name).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD))
                .append(Component.text(" ]"))
                .appendNewline().appendNewline()
                .append(Component.text("Join the PixPlex.net Discord server!").color(NamedTextColor.GOLD))
                .appendNewline();

    }

    public static Component getDefaultFooter() {

        String admins = "Li_Lord, Tillerheimer, Obi Wlan Kenobi and Nikogenia";

        return Component.text("").color(NamedTextColor.GRAY)
                .appendNewline()
                .append(Component.text("     [ "))
                .append(Component.text("Administrated by " + admins).color(NamedTextColor.RED))
                .append(Component.text(" ]     "))
                .appendNewline()
                .append(Component.text("     [ "))
                .append(Component.text("Powered by MineTower (Waterfall/Paper)").color(NamedTextColor.BLUE))
                .append(Component.text(" ]     "))
                .appendNewline()
                .append(Component.text("     [ "))
                .append(Component.text("Hosted on Hetzner Cloud").color(NamedTextColor.DARK_RED))
                .append(Component.text(" ]     "))
                .appendNewline()
                .append(Component.text("     [ "))
                .append(Component.text("PixPlex Version 1.0").color(NamedTextColor.DARK_PURPLE))
                .append(Component.text(" ]     "));

    }

    public void setAllPlayerTeams() {

        for (Player player : Bukkit.getOnlinePlayers()) setPlayerTeams(player);

    }

    public void setPlayerTeams(Player player) {

        List<Player> assigned = new ArrayList<>();

        Scoreboard scoreboard = player.getScoreboard();

        for (Rank rank : Rank.values()) {

            Team team = scoreboard.getTeam(rank.getId() + rank.name());

            if (team == null) team = scoreboard.registerNewTeam(rank.getId() + rank.name());

            team.prefix(rank.getFullPrefix());

            team.color(rank.getColor());

            for (Player target : Bukkit.getOnlinePlayers()) {
                if (!target.hasPermission(rank.getPerm()) | assigned.contains(target)) continue;
                team.addEntry(target.getName());
                assigned.add(target);
            }

        }

    }

}
