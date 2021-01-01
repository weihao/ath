package org.akadia.ath.spigot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AthCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            String pAth = ChatColor.translateAlternateColorCodes('&', Main.getMain().configManager.notify)
                    .replaceAll("%player_count%", String.valueOf(Main.getMain().getMaxCount()))
                    .replaceAll("%date%", Main.getMain().achievedDate);

            sender.sendMessage(pAth);
        } else if (args[0].equals("reload")) {
            sender.sendMessage(Main.getMain().configManager.reloading);
            Bukkit.getPluginManager().disablePlugin(Main.getMain());
            Bukkit.getPluginManager().enablePlugin(Main.getMain());
            sender.sendMessage(Main.getMain().configManager.reloaded);
        }

        return true;
    }
}