package de.nikogenia.mtproxy.utils;

import net.md_5.bungee.api.ChatColor;

public class MathUtils {

    public static String formatTime(int time, boolean chat) {

        String formattedTime = "";

        int years = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        int remainingTime = time;

        if (remainingTime / 60 / 60 / 24 / 365 / 50 >= 1) {
            remainingTime -= remainingTime / 60 / 60 / 24 / 365 / 68 * 60 * 60 * 24 * 365 * 50;
        }
        if (remainingTime / 60 / 60 / 24 / 365 >= 1) {
            years = remainingTime / 60 / 60 / 24 / 365;
            remainingTime -= remainingTime / 60 / 60 / 24 / 365 * 60 * 60 * 24 * 365;
        }
        if (remainingTime / 60 / 60 / 24 >= 1) {
            days = remainingTime / 60 / 60 / 24;
            remainingTime -= remainingTime / 60 / 60 / 24 * 60 * 60 * 24;
        }
        if (remainingTime / 60 / 60 >= 1) {
            hours = remainingTime / 60 / 60;
            remainingTime -= remainingTime / 60 / 60 * 60 * 60;
        }
        if (remainingTime / 60 >= 1) {
            minutes = remainingTime / 60;
            remainingTime -= remainingTime / 60 * 60;
        }
        if (remainingTime >= 1) {
            seconds = remainingTime;
        }

        if (time >= 31557600) {
            if (years <= 9) {
                formattedTime = formattedTime + ChatColor.YELLOW + "0" + years + ChatColor.GOLD + "y ";
            } else {
                formattedTime = formattedTime + ChatColor.YELLOW + years + ChatColor.GOLD + "y ";
            }
        }
        if (time >= 86400) {
            if (days <= 9) {
                formattedTime = formattedTime + ChatColor.YELLOW + "0" + days + ChatColor.GOLD + "d ";
            } else {
                formattedTime = formattedTime + ChatColor.YELLOW + days + ChatColor.GOLD + "d ";
            }
        }
        if (time >= 3600) {
            if (hours <= 9) {
                formattedTime = formattedTime + ChatColor.YELLOW + "0" + hours + ChatColor.GOLD + "h ";
            } else {
                formattedTime = formattedTime + ChatColor.YELLOW + hours + ChatColor.GOLD + "h ";
            }
        }
        if (time >= 60) {
            if (minutes <= 9) {
                formattedTime = formattedTime + ChatColor.YELLOW + "0" + minutes + ChatColor.GOLD + "m ";
            } else {
                formattedTime = formattedTime + ChatColor.YELLOW + minutes + ChatColor.GOLD + "m ";
            }
        }
        if (seconds <= 9) {
            formattedTime = formattedTime + ChatColor.YELLOW + "0" + seconds + ChatColor.GOLD + "s";
        } else {
            formattedTime = formattedTime + ChatColor.YELLOW + seconds + ChatColor.GOLD + "s";
        }

        if (chat) return formattedTime;
        else return ChatColor.stripColor(formattedTime);

    }

}
