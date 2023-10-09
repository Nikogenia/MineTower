package de.nikogenia.mtbase.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Arrays;
import java.util.Random;

public class StringUtils {

    public static String ordinalName(int i) {

        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };

        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];
        }

    }

    public static Component colorize(String text) {

        TextComponent.Builder builder = Component.text();

        Random random = new Random();

        for (int i = 0; i < text.length(); i++) {
            builder.append(Component.text(String.valueOf(text.charAt(i))).color(Arrays.asList(
                    NamedTextColor.AQUA, NamedTextColor.BLUE, NamedTextColor.RED, NamedTextColor.GREEN,
                    NamedTextColor.LIGHT_PURPLE, NamedTextColor.DARK_PURPLE, NamedTextColor.YELLOW, NamedTextColor.GOLD)
                    .get(random.nextInt(8))));
        }

        return builder.build();

    }

}
