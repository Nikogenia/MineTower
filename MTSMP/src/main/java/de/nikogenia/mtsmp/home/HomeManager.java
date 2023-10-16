package de.nikogenia.mtsmp.home;

import de.nikogenia.mtbase.MTBase;
import de.nikogenia.mtsmp.sql.SQLHome;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomeManager {

    private List<SQLHome> homes;

    public HomeManager() {

        homes = new ArrayList<>();

        updateHomes();

    }

    public void updateHomes() {

        MTBase.getSql().getSession().getTransaction().rollback();
        MTBase.getSql().getSession().beginTransaction();

        homes = MTBase.getSql().query("FROM SQLHome", SQLHome.class).list();

    }

    public SQLHome getHome(Player player) {
        for (SQLHome home : homes) {
            if (home.getPlayer().getUuid().equals(player.getUniqueId().toString())) return home;
        }
        return null;
    }

    public List<SQLHome> getHomes() {
        return homes;
    }

    public static Component getPrefix() {
        return Component.text("[").color(NamedTextColor.GRAY)
                .append(Component.text("Home").color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.BOLD))
                .append(Component.text("] "));
    }

}
