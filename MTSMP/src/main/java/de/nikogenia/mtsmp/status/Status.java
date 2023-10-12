package de.nikogenia.mtsmp.status;

import de.nikogenia.mtbase.utils.StringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum Status {

    ONLINE(0, "ONLINE", NamedTextColor.GREEN),
    CHILL(1, "CHILL", NamedTextColor.DARK_PURPLE),
    GRIND(2, "GRIND", NamedTextColor.GRAY),
    BUILD(3, "BUILD", NamedTextColor.YELLOW),
    REDSTONE(4, "REDSTONE", NamedTextColor.RED),
    AFK(5, "AFK", NamedTextColor.BLUE),
    TROLL(6, "TROLL", null),
    REC(7, "REC", NamedTextColor.DARK_RED),
    LIVE(8, "LIVE", NamedTextColor.DARK_BLUE),
    EXPLORE(9, "EXPLORE", NamedTextColor.BLACK),
    TOUCH_GRASS(10, "TOUCH GRASS", NamedTextColor.AQUA),
    HELL_HUNTER(11, "HELL HUNTER", NamedTextColor.RED),
    SAD(12, "SAD", NamedTextColor.DARK_GRAY),
    STONKS(13, "STONKS", NamedTextColor.GOLD),
    NO_LIFE(14, "NO LIFE", NamedTextColor.LIGHT_PURPLE),
    FARM(15, "FARM", NamedTextColor.GOLD);

    private final int id;

    private final String text;

    private final NamedTextColor color;

    Status(int id, String text, NamedTextColor color) {
        this.id = id;
        this.text = text;
        this.color = color;
    }

    public static Status fromID(int id) {

        for (Status status : values()) {
            if (status.getId() == id) return status;
        }

        return ONLINE;

    }

    public static Status fromName(String name) {

        for (Status status : values()) {
            if (status.name().equalsIgnoreCase(name)) return status;
        }

        return ONLINE;

    }

    public int getId() {
        return id;
    }

    public Component getText() {
        if (color == null) return StringUtils.colorize(text);
        return Component.text(text).color(color);
    }

    public NamedTextColor getColor() {
        return color;
    }

}
