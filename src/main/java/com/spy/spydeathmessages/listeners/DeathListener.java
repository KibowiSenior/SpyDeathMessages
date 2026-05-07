package com.spy.spydeathmessages.listeners;

import com.spy.spydeathmessages.SpyDeathMessages;
import com.spy.spydeathmessages.util.MessageSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class DeathListener implements Listener {

    private final SpyDeathMessages plugin;

    public DeathListener(SpyDeathMessages plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();

        if (plugin.getConfig().getBoolean("settings.cancel_vanilla_message", true)) {
            event.setDeathMessage(null);
        }

        String attackerName = "";
        String weaponName   = "";
        String configKey    = "died";

        EntityDamageEvent lastDamage = victim.getLastDamageCause();

        if (lastDamage instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent edbe = (EntityDamageByEntityEvent) lastDamage;
            Entity damager = edbe.getDamager();

            if (damager instanceof Player) {
                Player killer = (Player) damager;
                attackerName = killer.getName();
                weaponName   = getItemName(killer.getInventory().getItemInMainHand());
                configKey    = "killed";

            } else if (damager instanceof Projectile) {
                ProjectileSource shooter = ((Projectile) damager).getShooter();
                if (shooter instanceof Player) {
                    Player playerShooter = (Player) shooter;
                    attackerName = playerShooter.getName();
                    weaponName   = getItemName(playerShooter.getInventory().getItemInMainHand());
                    configKey    = "killed";
                }
            }
        }

        ConfigurationSection module = plugin.getConfig()
                .getConfigurationSection("messages." + configKey + ".module");

        MessageSender.send(plugin, module, victim, attackerName, weaponName);
    }

    private String getItemName(ItemStack item) {
        if (item == null) return "their fists";
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        }
        String mat = item.getType().name().toLowerCase().replace('_', ' ');
        return Character.toUpperCase(mat.charAt(0)) + mat.substring(1);
    }
}
