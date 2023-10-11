package de.nikogenia.mtbase.permission;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public enum Rank {

    ADMIN(10, "group.admin", "ADMIN", NamedTextColor.RED),
    MOD(30, "group.mod", "MOD", NamedTextColor.AQUA),
    MEMBER(90, "group.default", "", NamedTextColor.WHITE);

    private final int id;

    private final String perm;

    private final String prefix;

    private final NamedTextColor color;

    Rank(int id, String perm, String prefix, NamedTextColor color) {
        this.id = id;
        this.perm = perm;
        this.prefix = prefix;
        this.color = color;
    }

    public static Rank fromID(int id) {

        for (Rank rank : values()) {
            if (rank.getId() == id) return rank;
        }

        return MEMBER;

    }

    public static Rank fromName(String name) {

        for (Rank rank : values()) {
            if (rank.name().equalsIgnoreCase(name)) return rank;
        }

        return MEMBER;

    }

    public static Rank fromPlayer(Player player) {

        for (Rank rank : values()) {
            if (player.hasPermission(rank.getPerm())) return rank;
        }

        return MEMBER;

    }

    public int getId() {
        return id;
    }

    public String getPerm() {
        return perm;
    }

    public String getPrefix() {
        return prefix;
    }

    public Component getFullPrefix() {
        if (prefix.isEmpty()) return Component.text("");
        return Component.text("[")
                .append(Component.text(prefix).color(color))
                .append(Component.text("] "));
    }

    public NamedTextColor getColor() {
        return color;
    }

}
