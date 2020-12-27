package org.akadia.ath;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends Plugin implements Listener {
    private static Main main;
    private Configuration configuration;
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
        PluginManager pm = getProxy().getPluginManager();
        pm.registerListener(this, this);
        pm.registerCommand(this, new ATH());

        createFile("config.yml", true);

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        maxCount = configuration.getInt("record");
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        int onlineCount = getProxy().getOnlineCount();

        if (onlineCount <= maxCount) {
            return;
        }

        maxCount = onlineCount;
        logToFile(
                String.format("(%s) - ATH Concurrent Online Player Record: %s", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()), maxCount)
        );

        configuration.set("record", maxCount);
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (ProxiedPlayer player : getProxy().getPlayers()) {
            player.sendMessage(
                    new ComponentBuilder("Server Reached ATH Player Record: ")
                            .color(ChatColor.GOLD)
                            .bold(true)
                            .append(String.valueOf(maxCount))
                            .color(ChatColor.RED)
                            .create());
        }
    }

    public void logToFile(String message) {
        if (pw == null) {
            try {
                File saveTo = createFile("log.txt");
                FileWriter fw = new FileWriter(saveTo, true);
                pw = new PrintWriter(fw);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pw.println(message);
        pw.flush();
    }

    public File createFile(String filename) {
        return createFile(filename, false);
    }

    public File createFile(String filename, boolean copyFromResource) {
        File saveTo = null;
        try {
            File dataFolder = getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }

            saveTo = new File(dataFolder, filename);
            if (!saveTo.exists()) {
                if (copyFromResource) {
                    InputStream in = getResourceAsStream(filename);
                    Files.copy(in, saveTo.toPath());
                } else {
                    saveTo.createNewFile();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return saveTo;
    }
}