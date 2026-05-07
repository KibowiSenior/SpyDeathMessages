package com.spy.spydeathmessages;

import com.spy.spydeathmessages.commands.DeathMessagesCommand;
import com.spy.spydeathmessages.listeners.DeathListener;
import com.spy.spydeathmessages.util.ColorUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SpyDeathMessages extends JavaPlugin {

    private final Set<UUID> optedOut = new HashSet<>();
    private File optedOutFile;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        optedOutFile = new File(getDataFolder(), "opted_out.txt");
        loadOptedOut();

        getServer().getPluginManager().registerEvents(new DeathListener(this), this);

        DeathMessagesCommand cmdHandler = new DeathMessagesCommand(this);
        getCommand("deathmessages").setExecutor(cmdHandler);
        getCommand("deathmessages").setTabCompleter(cmdHandler);

        getLogger().info("SpyDeathMessages v" + getDescription().getVersion() + " enabled.");
    }

    @Override
    public void onDisable() {
        saveOptedOut();
        getLogger().info("SpyDeathMessages disabled.");
    }

    // ── Per-player opt-out ────────────────────────────────────────────

    /**
     * @return true  → player is now opted OUT (will not see messages)
     *         false → player is now opted IN  (will see messages)
     */
    public boolean togglePlayer(UUID uuid) {
        boolean nowOff;
        if (optedOut.contains(uuid)) {
            optedOut.remove(uuid);
            nowOff = false;
        } else {
            optedOut.add(uuid);
            nowOff = true;
        }
        saveOptedOut();
        return nowOff;
    }

    public boolean hasOptedOut(UUID uuid) {
        return optedOut.contains(uuid);
    }

    // ── Persistence ───────────────────────────────────────────────────

    private void loadOptedOut() {
        optedOut.clear();
        if (!optedOutFile.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(optedOutFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    try {
                        optedOut.add(UUID.fromString(line));
                    } catch (IllegalArgumentException ignored) {}
                }
            }
        } catch (IOException e) {
            getLogger().warning("Could not load opted_out.txt: " + e.getMessage());
        }
    }

    private void saveOptedOut() {
        getDataFolder().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(optedOutFile))) {
            for (UUID uuid : optedOut) {
                writer.write(uuid.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            getLogger().warning("Could not save opted_out.txt: " + e.getMessage());
        }
    }

    // ── Accessors ─────────────────────────────────────────────────────

    public String getConfigPrefix() {
        String raw = getConfig().getString("settings.prefix", "&8[&bDeathMsg&8] ");
        return ColorUtil.colorize(raw);
    }
}
