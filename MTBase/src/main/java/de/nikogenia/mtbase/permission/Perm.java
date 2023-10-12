package de.nikogenia.mtbase.permission;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public enum Perm {

    SHOP_ADMIN("minetower.shop.admin"),
    EDIT_RANK("minetower.perm.rank"),
    SPAWN_BYPASS("minetower.spawn.bypass");

    private String value;

    Perm(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Component getPrefix() {
        return Component.text("[").color(NamedTextColor.GRAY)
                .append(Component.text("Rank").color(NamedTextColor.RED).decorate(TextDecoration.BOLD))
                .append(Component.text("] "));
    }

}
