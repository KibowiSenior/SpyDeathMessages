package com.spy.spydeathmessages.commands;

import com.spy.spydeathmessages.SpyDeathMessages;
import com.spy.spydeathmessages.util.ColorUtil;
import com.spy.spydeathmessages.util.MessageSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeathMessagesCommand implements CommandExecutor, TabCompleter {

    private final SpyDeathMessages plugin;

    public DeathMessagesCommand(SpyDeathMessages plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "reload":
                if (!sender.hasPermission("spydm.reload") && !sender.hasPermission("spydm.admin")) {
                    sendSystem(sender, "system_messages.no_permission.module");
                    return true;
                }
                plugin.reloadConfig();
                sendSystem(sender, "system_messages.reload_success.module");
                return true;

            case "toggle":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ColorUtil.colorize(
                            plugin.getConfigPrefix() + "#ff1313Only players can use this command."));
                    return true;
                }
                Player player = (Player) sender;
                boolean nowOff = plugin.togglePlayer(player.getUniqueId());
                if (nowOff) {
                    sendSystem(sender, "system_messages.toggle_disabled.module");
                } else {
                    sendSystem(sender, "system_messages.toggle_enabled.module");
                }
                return true;

            case "help":
            default:
                showHelp(sender);
                return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            if (sender.hasPermission("spydm.reload") || sender.hasPermission("spydm.admin")) {
                return Arrays.asList("toggle", "reload", "help");
            }
            return Arrays.asList("toggle", "help");
        }
        return Collections.emptyList();
    }

    // ── Helpers ─────────────────────────────────────────────────────

    private void showHelp(CommandSender sender) {
        sendSystem(sender, "system_messages.help.module");
    }

    private void sendSystem(CommandSender sender, String path) {
        ConfigurationSection module = plugin.getConfig().getConfigurationSection(path);

        if (sender instanceof Player) {
            MessageSender.sendSystem(plugin, module, (Player) sender);
        } else {
            if (module == null) return;
            ConfigurationSection chat = module.getConfigurationSection("chat");
            if (chat == null || !chat.getBoolean("enabled", false)) return;
            List<String> lines = chat.getStringList("message");
            for (String line : lines) {
                sender.sendMessage(net.md_5.bungee.api.ChatColor.stripColor(
                        ColorUtil.colorize(line.replace("%prefix%", plugin.getConfigPrefix()))));
            }
        }
    }
}
