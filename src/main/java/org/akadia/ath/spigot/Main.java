package org.akadia.ath.spigot;

import org.bstats.bukkit.Metrics;
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
    static Main main;
    ConfigManager configManager;
    int maxCount;
    String achievedDate;
    PrintWriter pw;

    public static Main getMain() {
        return main;
    }

    public int getMaxCount() {
        return maxCount;
    }

    @Override
    public void onEnable() {
        main = this;
        new Metrics(this, 9801);

        configManager = new ConfigManager();
        maxCount = configManager.config.getInt("record.count");
        achievedDate = configManager.config.getString("record.date");

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(this, this);
        getCommand("ath").setExecutor(new AthCommand());

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new AthPlaceholder().register();
        }

    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        int onlineCount = Bukkit.getOnlinePlayers().size();

        if (onlineCount <= maxCount) {
            return;
        }

        maxCount = onlineCount;
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        achievedDate = date;

        getLogger().info(configManager.serverLogging
                .replaceAll("%player_count%", String.valueOf(maxCount)));

        logToFile(configManager.diskLogging
                .replaceAll("%player_count%", String.valueOf(maxCount))
                .replaceAll("%date%", date));

        configManager.config.set("record.count", maxCount);
        configManager.config.set("record.date", achievedDate);
        configManager.saveConfig();

        String pAth = ChatColor.translateAlternateColorCodes('&', configManager.notify)
                .replaceAll("%player_count%", String.valueOf(maxCount))
                .replaceAll("%date%", date);
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