package org.akadia.ath.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class AthCommand extends Command {

    public AthCommand() {
        super("ath");
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if (strings.length == 0) {
            sender.sendMessage(
                    new TextComponent(
                            ChatColor.translateAlternateColorCodes('&',
                                    Main.getMain().notify)
                                    .replaceAll("%player_count%", String.valueOf(Main.getMain().maxCount))
                                    .replaceAll("%date%", Main.getMain().achievedDate))
            );
        } else if (strings[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(new ComponentBuilder((Main.getMain().reloading)).create());
            Main.getMain().initializeConfig();
            sender.sendMessage(new ComponentBuilder((Main.getMain().reloaded)).create());
        }

    }
}