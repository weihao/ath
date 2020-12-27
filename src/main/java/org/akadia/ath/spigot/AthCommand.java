package org.akadia.ath.spigot;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AthCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(
                ChatColor.GOLD + "Server Reached ATH Player Record: "
                        + ChatColor.RED + ChatColor.BOLD + Main.getMain().getMaxCount());

        return true;
    }
}