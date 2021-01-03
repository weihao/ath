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
import org.akadia.ath.util.Util;
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
    static Main main;
    final String CONFIG_FILENAME = "config.yml";
    final String TAG = ChatColor.translateAlternateColorCodes('&', "&f[&6Ath&f]");
    Configuration config;
    int maxCount;
    String achievedDate;

    PrintWriter pw;

    String diskLogging;
    String consoleLogging;
    String notify;
    String reloading;
    String reloaded;
    String unknownCommand;
    String outdated;
    String upToDate;

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

        initializeConfig();

        getProxy().getScheduler().runAsync(this, () -> {
            if (Util.isUpdateToDate()) {
                getLogger().info(upToDate);
            } else {
                getLogger().info(outdated);
            }
        });
    }

    public void initializeConfig() {
        createFile(CONFIG_FILENAME, true);

        config = load(CONFIG_FILENAME);

        maxCount = config.getInt("record.count");
        achievedDate = getMsg("record.date");

        consoleLogging = getMsg("logs.console");
        diskLogging = getMsg("logs.disk");
        notify = getMsg("msg.notify");
        reloading = getMsg("msg.reloading");
        reloaded = getMsg("msg.reloaded");
        unknownCommand = getMsg("msg.unknownCommand");
        outdated = getMsg("logs.outdated");
        upToDate = getMsg("logs.upToDate");
    }

    public String getMsg(String context) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(context));
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        int onlineCount = getProxy().getOnlineCount();

        if (onlineCount <= maxCount) {
            return;
        }

        maxCount = onlineCount;
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        achievedDate = date;
        logToDisk(diskLogging
                .replaceAll("%player_count%", String.valueOf(maxCount))
                .replaceAll("%date%", date));

        getLogger().info(consoleLogging
                .replaceAll("%player_count%", String.valueOf(maxCount)));


        config.set("record.count", maxCount);
        config.set("record.date", achievedDate);
        save(config, CONFIG_FILENAME);

        TextComponent pAth = new TextComponent(Main.getMain().TAG + " " + notify
                .replaceAll("%player_count%", String.valueOf(maxCount))
                .replaceAll("%date%", date));
        for (ProxiedPlayer player : getProxy().getPlayers()) {
            player.sendMessage(pAth);
        }

        getProxy().getPluginManager().callEvent(new AthRecordEvent(maxCount, achievedDate));
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