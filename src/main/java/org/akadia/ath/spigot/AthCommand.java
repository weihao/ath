package org.akadia.ath.spigot;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AthCommand implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!label.equalsIgnoreCase("ath")) {
            return false;
        }
        sender.sendMessage(
                ChatColor.GOLD + "Server Reached ATH Player Record: "
                        + ChatColor.RED + ChatColor.BOLD + Main.getMain().getMaxCount());

        return true;
    }
}