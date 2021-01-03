package org.akadia.ath.sponge;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Plugin(id = "ath", name = "Ath", version = "1.0")
public class Main {
    public final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";
    public final char COLOR_CHAR = '\u00A7';
    int maxCount;
    String achievedDate;
    String diskLogging;
    String consoleLogging;
    String notify;
    String reloading;
    String reloaded;
    String unknownCommand;
    String outdated;
    String upToDate;
    CommentedConfigurationNode config;
    @Inject
    @DefaultConfig(sharedRoot = true)
    ConfigurationLoader<CommentedConfigurationNode> configurationLoader;
    private Main instance;
    @Inject
    private Logger logger;
    @Inject
    @DefaultConfig(sharedRoot = true)
    private File configFile;

//    @Inject
//    public Main(Metrics2.Factory metricsFactory) {
//        metricsFactory.make(9801);
//    }

    @Listener
    public void onInitialization(GamePreInitializationEvent event) {
        instance = this;

        ConfigManager configManager = ConfigManager.getInstance();
        configManager.setup(this, configFile, this.configurationLoader);
        config = ConfigManager.getInstance().getConfig();

        maxCount = config.getNode("record", "count").getInt();
        achievedDate = getMsg("record.date");

        consoleLogging = getMsg("logs.console");
        diskLogging = getMsg("logs.disk");
        notify = getMsg("msg.notify");
        reloading = getMsg("msg.reloading");
        reloaded = getMsg("msg.reloaded");
        unknownCommand = getMsg("msg.unknownCommand");
        outdated = getMsg("logs.outdated");
        upToDate = getMsg("logs.upToDate");


        CommandSpec checkCommand = CommandSpec.builder()
                .description(Text.of("Check ATH record"))
                .permission("ath.check")
                .executor((CommandSource src, CommandContext args) -> {
                    String pAth = notify
                            .replaceAll("%player_count%", String.valueOf(maxCount))
                            .replaceAll("%date%", achievedDate);
                    src.sendMessage(Text.of(pAth));
                    return CommandResult.success();
                })
                .build();

        Sponge.getCommandManager().register(this, checkCommand, "ath");
    }

    public String getMsg(String context) {
        String str = ConfigManager.getInstance().getConfig().getNode(context.split("\\.")).getString();
        if (str == null) {
            return "";
        }
        return translateAlternateColorCodes('&', str);
    }

    public String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == altColorChar && ALL_CODES.indexOf(b[i + 1]) > -1) {
                b[i] = COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }

    @Listener(order = Order.FIRST)
    public void onPlayerJoin(ClientConnectionEvent.Join evt) {
        int onlineCount = Sponge.getServer().getOnlinePlayers().size();
        if (onlineCount <= maxCount) {
            return;
        }

        maxCount = onlineCount;
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        achievedDate = date;

        logger.info(consoleLogging
                .replaceAll("%player_count%", String.valueOf(maxCount)));
        config.getNode("record", "count").setValue(maxCount);
        config.getNode("record", "date").setValue(achievedDate);
        ConfigManager.getInstance().saveConfig();

        String pAth = notify
                .replaceAll("%player_count%", String.valueOf(maxCount))
                .replaceAll("%date%", date);
        for (Player onlinePlayer : Sponge.getServer().getOnlinePlayers()) {
            onlinePlayer.sendMessage(Text.of(pAth));
        }
        EventContext eventContext = EventContext.builder().add(EventContextKeys.PLUGIN, getContainer()).build();

        AthRecordEvent event = new AthRecordEvent(Cause.of(eventContext, this), maxCount, achievedDate);
        Sponge.getEventManager().post(event);
    }

    public PluginContainer getContainer() {
        return Sponge.getPluginManager().fromInstance(instance).get();
    }

}