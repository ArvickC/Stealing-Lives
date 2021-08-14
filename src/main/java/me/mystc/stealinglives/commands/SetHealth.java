package me.mystc.stealinglives.commands;

import me.mystc.stealinglives.Files.PlayerHealth;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHealth implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Checks
        if(!sender.hasPermission("stealinglives.sethealth")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&cStealing Lives&7]&c No permission."));
            return false;
        }
        if(args.length == 0 || args == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&cStealing Lives&7]&c Incorrect Usage:&7 /sethp [player] [amount]"));
            return false;
        }

        PlayerHealth.get().set(args[0], Integer.valueOf(args[1]));
        PlayerHealth.save();
        PlayerHealth.reload();
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(PlayerHealth.get().getDouble(p.getName()));
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&cStealing Lives&7] Set user&a " + args[0] + "&7 to&a " + args[1] + "&7 health."));

        return false;
    }
}
