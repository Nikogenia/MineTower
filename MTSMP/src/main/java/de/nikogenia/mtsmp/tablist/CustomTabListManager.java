package de.nikogenia.mtsmp.tablist;

import de.nikogenia.mtbase.permission.Rank;
import de.nikogenia.mtbase.tablist.TabListManager;
import de.nikogenia.mtsmp.Main;
import de.nikogenia.mtsmp.status.Status;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class CustomTabListManager extends TabListManager {

    public void setAllPlayerTeams() {

        for (Player player : Bukkit.getOnlinePlayers()) setPlayerTeams(player);

    }

    public void setPlayerTeams(Player player) {

        List<Player> assigned = new ArrayList<>();

        Scoreboard scoreboard = player.getScoreboard();

        for (Status status : Status.values()) {

            for (Rank rank : Rank.values()) {

                Team team = scoreboard.getTeam(rank.getId() + rank.name() + status.name());

                if (team == null) team = scoreboard.registerNewTeam(rank.getId() + rank.name() + status.name());

                team.prefix(Component.text("[").color(NamedTextColor.WHITE)
                        .append(status.getText())
                        .append(Component.text("] "))
                        .append(rank.getFullPrefix().colorIfAbsent(NamedTextColor.GRAY)));

                team.color(rank.getColor());

                for (Player target : Bukkit.getOnlinePlayers()) {
                    if (!target.hasPermission(rank.getPerm()) | !Main.getStatusManager().getStatus(target).equals(status) |
                            assigned.contains(target)) continue;
                    team.addEntry(target.getName());
                    assigned.add(target);
                }

            }

        }

    }

}
