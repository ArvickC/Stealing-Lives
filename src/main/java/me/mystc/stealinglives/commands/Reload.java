package me.mystc.stealinglives.commands;

import me.mystc.stealinglives.Files.PlayerHealth;
import me.mystc.stealinglives.StealingLives;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Checks
        if(!sender.hasPermission("stealinglives.reload")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&cStealing Lives&7]&c No permission"));
            return false;
        }

        PlayerHealth.reload();

        StealingLives.getInstance().reloadConfig();

        for(Player p : Bukkit.getOnlinePlayers()) {
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(PlayerHealth.get().getDouble(p.getName()));
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&cStealing Lives&7] Plugin reloaded."));

        return false;
    }
}
