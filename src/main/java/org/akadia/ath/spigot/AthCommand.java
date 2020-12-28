package org.akadia.ath.spigot;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AthCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String pAth = ChatColor.translateAlternateColorCodes('&', Main.getMain().configManager.notify)
                .replaceAll("%player_count%", String.valueOf(Main.getMain().getMaxCount()));

        sender.sendMessage(pAth);

        return true;
    }
}