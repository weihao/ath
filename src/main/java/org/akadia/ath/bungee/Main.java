package org.akadia.ath.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import org.bstats.bungeecord.Metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends Plugin implements Listener {
    final String CONFIG_FILENAME = "config.yml";

    static Main main;
    Configuration config;
    int maxCount;
    PrintWriter pw;

    String diskLogging;
    String serverLogging;
    String notify;

    public static Main getMain() {
        return main;
    }

    @Override
    public void onEnable() {
        main = this;
        new Metrics(this, 9801);

        PluginManager pm = getProxy().getPluginManager();
        pm.registerListener(this, this);
        pm.registerCommand(this, new AthCommand());

        createFile(CONFIG_FILENAME, true);

        config = load(CONFIG_FILENAME);

        maxCount = config.getInt("record");
        serverLogging = config.getString("msg.serverLogging");
        diskLogging = config.getString("msg.diskLogging");
        notify = config.getString("msg.notify");
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        int onlineCount = getProxy().getOnlineCount();

        if (onlineCount <= maxCount) {
            return;
        }

        maxCount = onlineCount;


        logToDisk(diskLogging
                .replaceAll("%player_count%", String.valueOf(maxCount))
                .replaceAll("%date%", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())));

        getLogger().info(serverLogging
                .replaceAll("%player_count%", String.valueOf(maxCount)));

        config.set("record", maxCount);
        save(config, CONFIG_FILENAME);

        TextComponent pAth = new TextComponent(ChatColor.translateAlternateColorCodes('&', notify.replaceAll("%player_count%", String.valueOf(maxCount))));
        for (ProxiedPlayer player : getProxy().getPlayers()) {
            player.sendMessage(pAth);
        }
    }

    public void logToDisk(String message) {
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

    public boolean save(Configuration configuration, String filename) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(getDataFolder(), filename));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Configuration load(String filename) {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}