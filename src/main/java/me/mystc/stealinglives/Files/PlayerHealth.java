package me.mystc.stealinglives.Files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerHealth {
    // Vars
    private static File file;
    private static FileConfiguration customFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("StealingLives").getDataFolder(), "playerHealth.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch(IOException e) {
                // yikes that isn't good
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return customFile;
    }

    public static void save() {
        try {
            customFile.save(file);
        }catch (IOException e) {
            System.out.println(ChatColor.translateAlternateColorCodes('&', "&7[&cStealing Lives&7]&c Couldn't save file"));
        }
    }

    public static void reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
    }

}
