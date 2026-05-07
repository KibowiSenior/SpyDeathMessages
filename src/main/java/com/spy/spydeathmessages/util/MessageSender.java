package com.spy.spydeathmessages.util;

import com.spy.spydeathmessages.SpyDeathMessages;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class MessageSender {

    private static final Random RANDOM = new Random();

    private MessageSender() {}

    /**
     * Broadcast a death message to all eligible players.
     *
     * @param module  the config section at messages.<key>.module
     */
    public static void send(SpyDeathMessages plugin,
                            ConfigurationSection module,
                            Player victim,
                            String attacker,
                            String weapon) {
        if (module == null) return;

        String world = victim.getWorld().getName();
        String vName = victim.getName();
        String prefix = plugin.getConfigPrefix();

        List<Player> targets = getTargets(plugin, victim);

        // ── Chat ────────────────────────────────────────────────────
        ConfigurationSection chat = module.getConfigurationSection("chat");
        if (chat != null && chat.getBoolean("enabled", false)) {
            String raw = pickRandom(chat.getStringList("message"));
            if (raw != null && !raw.isEmpty()) {
                String msg = ColorUtil.colorize(
                        applyPlaceholders(raw, vName, attacker, weapon, world, prefix));
                for (Player p : targets) p.sendMessage(msg);
            }
        }

        // ── ActionBar ───────────────────────────────────────────────
        ConfigurationSection actionbar = module.getConfigurationSection("actionbar");
        if (actionbar != null && actionbar.getBoolean("enabled", false)) {
            String raw = pickRandom(actionbar.getStringList("message"));
            if (raw != null && !raw.isEmpty()) {
                TextComponent component = new TextComponent(ColorUtil.colorize(
                        applyPlaceholders(raw, vName, attacker, weapon, world, prefix)));
                for (Player p : targets) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
                }
            }
        }

        // ── Title & Subtitle ────────────────────────────────────────
        ConfigurationSection titleSec    = module.getConfigurationSection("title");
        ConfigurationSection subtitleSec = module.getConfigurationSection("subtitle");

        boolean titleEnabled    = titleSec    != null && titleSec.getBoolean("enabled", false);
        boolean subtitleEnabled = subtitleSec != null && subtitleSec.getBoolean("enabled", false);

        if (titleEnabled || subtitleEnabled) {
            String titleText = "";
            String subText   = "";

            if (titleEnabled) {
                String raw = pickRandom(titleSec.getStringList("message"));
                if (raw != null && !raw.isEmpty()) {
                    titleText = ColorUtil.colorize(
                            applyPlaceholders(raw, vName, attacker, weapon, world, prefix));
                }
            }
            if (subtitleEnabled) {
                String raw = pickRandom(subtitleSec.getStringList("message"));
                if (raw != null && !raw.isEmpty()) {
                    subText = ColorUtil.colorize(
                            applyPlaceholders(raw, vName, attacker, weapon, world, prefix));
                }
            }

            int fadeIn  = plugin.getConfig().getInt("settings.title_fade_in", 10);
            int stay    = plugin.getConfig().getInt("settings.title_stay", 60);
            int fadeOut = plugin.getConfig().getInt("settings.title_fade_out", 10);

            for (Player p : targets) p.sendTitle(titleText, subText, fadeIn, stay, fadeOut);
        }
    }

    /**
     * Send a system message directly to one player (command feedback).
     * No opt-out check — it is a response to their own action.
     *
     * @param module  the config section at system_messages.<key>.module
     */
    public static void sendSystem(SpyDeathMessages plugin,
                                  ConfigurationSection module,
                                  Player target) {
        if (module == null || target == null) return;

        String prefix = plugin.getConfigPrefix();

        ConfigurationSection chat = module.getConfigurationSection("chat");
        if (chat != null && chat.getBoolean("enabled", false)) {
            String raw = pickRandom(chat.getStringList("message"));
            if (raw != null && !raw.isEmpty()) {
                target.sendMessage(ColorUtil.colorize(raw.replace("%prefix%", prefix)));
            }
        }
    }

    // ── Internal helpers ────────────────────────────────────────────

    private static List<Player> getTargets(SpyDeathMessages plugin, Player victim) {
        String mode   = plugin.getConfig().getString("settings.broadcast_mode", "global");
        int    radius = plugin.getConfig().getInt("settings.broadcast_radius", 100);

        List<Player> targets = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if ("world".equalsIgnoreCase(mode) && !p.getWorld().equals(victim.getWorld())) continue;
            if ("radius".equalsIgnoreCase(mode)
                    && p.getWorld().equals(victim.getWorld())
                    && p.getLocation().distance(victim.getLocation()) > radius) continue;
            if (plugin.hasOptedOut(p.getUniqueId())) continue;
            targets.add(p);
        }
        return targets;
    }

    private static String applyPlaceholders(String raw, String victim, String attacker,
                                            String weapon, String world, String prefix) {
        return raw
                .replace("%victim%",   victim)
                .replace("%attacker%", attacker)
                .replace("%weapon%",   weapon)
                .replace("%world%",    world)
                .replace("%prefix%",   prefix);
    }

    private static String pickRandom(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        if (list.size() == 1) return list.get(0);
        return list.get(RANDOM.nextInt(list.size()));
    }
}
