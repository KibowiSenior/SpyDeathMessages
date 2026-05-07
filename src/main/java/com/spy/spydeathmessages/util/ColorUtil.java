package com.spy.spydeathmessages.util;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]{6})");

    private ColorUtil() {}

    /**
     * Translates hex colour codes (#rrggbb) and legacy & colour codes into
     * Minecraft colour strings.
     */
    public static String colorize(String message) {
        if (message == null) return "";

        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(0);
            matcher.appendReplacement(buffer, ChatColor.of(hex).toString());
        }
        matcher.appendTail(buffer);

        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }

    /**
     * Strip all colour codes from a string.
     */
    public static String strip(String message) {
        return ChatColor.stripColor(colorize(message));
    }
}
