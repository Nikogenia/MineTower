package de.nikogenia.mtsmp.status;

import de.nikogenia.mtbase.MTBase;
import de.nikogenia.mtbase.sql.SQLPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

public class StatusManager {

    public StatusManager() {

    }

    public Status getStatus(Player player) {

        MTBase.getSql().getSession().clear();

        SQLPlayer sqlPlayer = MTBase.getSql().getPlayerByUUID(player.getUniqueId().toString());
        if (sqlPlayer == null) return Status.ONLINE;

        return Status.fromID(sqlPlayer.getStatus());

    }

    public void setStatus(Player player, Status status) {

        MTBase.getSql().getSession().clear();

        SQLPlayer sqlPlayer = MTBase.getSql().getPlayerByUUID(player.getUniqueId().toString());
        if (sqlPlayer == null) return;

        sqlPlayer.setStatus(status.getId());
        MTBase.getSql().getSession().persist(sqlPlayer);
        MTBase.getSql().getSession().getTransaction().commit();
        MTBase.getSql().getSession().beginTransaction();

    }

    public static Component getPrefix() {
        return Component.text("[").color(NamedTextColor.GRAY)
                .append(Component.text("Status").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD))
                .append(Component.text("] "));
    }

}
