package org.akadia.ath;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class ATH extends Command {
    public ATH() {
        super("ath");
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        sender.sendMessage(
                new ComponentBuilder("Server reached ATH player record: ")
                        .color(ChatColor.GOLD)
                        .bold(true)
                        .append(String.valueOf(Main.getMain().getMaxCount()))
                        .color(ChatColor.RED)
                        .create()
        );
    }
}