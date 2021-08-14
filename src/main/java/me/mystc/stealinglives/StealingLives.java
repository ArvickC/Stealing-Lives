package me.mystc.stealinglives;

import me.mystc.stealinglives.Files.PlayerHealth;
import me.mystc.stealinglives.commands.Reload;
import me.mystc.stealinglives.commands.SetHealth;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class StealingLives extends JavaPlugin implements Listener {
    // Var
    Reload reload = new Reload();
    SetHealth sethealth = new SetHealth();

    static StealingLives instance;

    @Override
    public void onEnable() {
        // When Plugin Starts
        instance = this;

        PlayerHealth.setup();
        PlayerHealth.get().options().copyDefaults(true);
        PlayerHealth.save();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        getCommand("vreload").setExecutor(reload);
        getCommand("sethealth").setExecutor(sethealth);
        Bukkit.getPluginManager().registerEvents(this, this);
        System.out.println(ChatColor.translateAlternateColorCodes('&', "&7[&cStealing Lives&7] Plugin&a Activated&7."));
    }

    @Override
    public void onDisable() {
        // When Plugin Stops
        System.out.println(ChatColor.translateAlternateColorCodes('&', "&7[&cStealing Lives&7] Plugin&c Activated&7."));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if(p.getKiller() == null) return;
        Player killer = p.getKiller();

        PlayerHealth.get().set(p.getName(), PlayerHealth.get().getDouble(p.getName())-2);
        PlayerHealth.save();

        // Ban
        if(PlayerHealth.get().getDouble(p.getName()) <= getConfig().getDouble("lowest-heart-value") && getConfig().getString("if-no-hearts").equalsIgnoreCase("ban")) {
            Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), ChatColor.translateAlternateColorCodes('&', getConfig().getString("ban-message")), null, ChatColor.translateAlternateColorCodes('&', "&7[&cStealing Lives&7]"));
            p.kickPlayer(ChatColor.translateAlternateColorCodes('&', getConfig().getString("kick-message")));
            PlayerHealth.get().set(p.getName(), 0.0);

            if(getConfig().getBoolean("broadcast-deahts")) {
                PlayerHealth.get().set(killer.getName(), PlayerHealth.get().getDouble(killer.getName())+2);
                PlayerHealth.save();
                killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(PlayerHealth.get().getDouble(killer.getName()));

                e.setDeathMessage(null);

                String message = getConfig().getString("broadcast-message");
                message = message.replaceAll("<player>", p.getName());
                message = message.replaceAll("<killer>", killer.getName());
                message = message.replaceAll("<health>", killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).toString());
                for(Player pl : Bukkit.getOnlinePlayers()) {
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                return;
            }
        // Keep
        } else if(PlayerHealth.get().getDouble(p.getName()) <= getConfig().getDouble("lowest-heart-value") && getConfig().getString("if-no-hearts").equalsIgnoreCase("keep")) {
            PlayerHealth.get().set(p.getName(), getConfig().getDouble("lowest-heart-value"));
            return;
        // Kick
        } else if(PlayerHealth.get().getDouble(p.getName()) <= getConfig().getDouble("lowest-heart-value") && getConfig().getString("if-no-hearts").equalsIgnoreCase("kick")) {
            p.kickPlayer(ChatColor.translateAlternateColorCodes('&', getConfig().getString("kick-message")));
            PlayerHealth.get().set(p.getName(), getConfig().getDouble("lowest-heart-value"));

            if(getConfig().getBoolean("broadcast-deahts")) {
                PlayerHealth.get().set(killer.getName(), PlayerHealth.get().getDouble(killer.getName())+2);
                PlayerHealth.save();
                killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(PlayerHealth.get().getDouble(killer.getName()));

                e.setDeathMessage(null);

                String message = getConfig().getString("broadcast-message");
                message = message.replaceAll("<player>", p.getName());
                message = message.replaceAll("<killer>", killer.getName());
                message = message.replaceAll("<health>", killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).toString());
                for(Player pl : Bukkit.getOnlinePlayers()) {
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                return;
            }
        }

        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(PlayerHealth.get().getDouble(p.getName()));

        //System.out.println(ChatColor.translateAlternateColorCodes('&', "&aSET PLAYER HEALTH"));

        PlayerHealth.get().set(killer.getName(), PlayerHealth.get().getDouble(killer.getName())+2);
        PlayerHealth.save();
        killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(PlayerHealth.get().getDouble(killer.getName()));

        //System.out.println(ChatColor.translateAlternateColorCodes('&', "&aSET KILLER HEALTH"));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PlayerHealth.get().set(p.getName(), p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        double health = PlayerHealth.get().getDouble(p.getName());
        PlayerHealth.save();
        p.setHealth(health);
    }

    public static StealingLives getInstance() {
        return instance;
    }
}
