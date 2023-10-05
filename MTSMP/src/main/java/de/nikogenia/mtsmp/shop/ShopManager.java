package de.nikogenia.mtsmp.shop;

import de.nikogenia.mtbase.MTBase;
import de.nikogenia.mtsmp.sql.SQLShop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;

import java.util.List;

public class ShopManager {

    private List<SQLShop> shops;

    public ShopManager() {

        updateShops();

    }

    public void updateShops() {

        shops = MTBase.getSql().query("FROM SQLShop", SQLShop.class).list();

    }

    public boolean isPosInShop(Location pos, SQLShop shop) {

        String[] points = shop.getArea().split(";");
        if (points.length < 4 | points.length % 4 != 0) return false;

        for (int i = 0; i < points.length; i += 4) {
            int minX = Math.min(Integer.parseInt(points[i]), Integer.parseInt(points[i + 2]));
            int minZ = Math.min(Integer.parseInt(points[i + 1]), Integer.parseInt(points[i + 3]));
            int maxX = Math.max(Integer.parseInt(points[i]), Integer.parseInt(points[i + 2]));
            int maxZ = Math.max(Integer.parseInt(points[i + 1]), Integer.parseInt(points[i + 3]));
            if (minX <= pos.getX() && pos.getX() <= maxX &&
                minZ <= pos.getZ() && pos.getZ() <= maxZ) return true;
        }

        return false;

    }

    public SQLShop getShop(String name) {
        for (SQLShop shop : shops) {
            if (shop.getName().equals(name)) return shop;
        }
        return null;
    }

    public SQLShop getShop(Location pos) {
        for (SQLShop shop : shops) {
            if (isPosInShop(pos, shop)) return shop;
        }
        return null;
    }

    public List<SQLShop> getShops() {
        return shops;
    }

    public static Component getPrefix() {
        return Component.text("[").color(NamedTextColor.GRAY)
                .append(Component.text("Shop").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD))
                .append(Component.text("] "));
    }

}
