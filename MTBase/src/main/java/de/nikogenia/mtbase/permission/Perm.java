package de.nikogenia.mtbase.permission;

public enum Perm {

    SHOP_ADMIN("minetower.shop.admin"),
    SPAWN_BYPASS("minetower.spawn.bypass");

    private String value;

    Perm(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
