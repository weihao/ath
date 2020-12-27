package org.akadia.ath.spigot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends JavaPlugin implements Listener {
    private static Main main;
    private ConfigManager configManager;
    private int maxCount;
    private PrintWriter pw;


    public static Main getMain() {
        return main;
    }

    public int getMaxCount() {
        return maxCount;
    }

    @Override
    public void onEnable() {
        main = this;
        configManager = new ConfigManager();
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(this, this);
        getCommand("ath").setExecutor(new AthCommand());

    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        int onlineCount = Bukkit.getOnlinePlayers().size();

        if (onlineCount <= maxCount) {
            return;
        }

        maxCount = onlineCount;
        String ath = String.format("(%s) - ATH Concurrent Online Player Record: %s", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()), maxCount);
        getLogger().info(ath);
        logToFile(ath);

        configManager.config.set("record", maxCount);
        configManager.saveConfig();

        String pAth = ChatColor.GOLD + "Server Reached ATH Player Record: "
                + ChatColor.RED + ChatColor.BOLD + Main.getMain().getMaxCount();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(pAth);
        }
    }

    public void logToFile(String message) {
        if (pw == null) {
            try {
                FileWriter fw = new FileWriter(configManager.logFile, true);
                pw = new PrintWriter(fw);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pw.println(message);
        pw.flush();
    }
}