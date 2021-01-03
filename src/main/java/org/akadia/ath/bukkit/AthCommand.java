package org.akadia.ath.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AthCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission("ath.*") || sender.hasPermission("ath.check")) {
                String pAth = Main.getMain().configManager.notify
                        .replaceAll("%player_count%", String.valueOf(Main.getMain().getMaxCount()))
                        .replaceAll("%date%", Main.getMain().achievedDate);
                Main.getMain().sendMsg(sender, pAth);
            } else {
                Main.getMain().sendMsg(sender, Main.getMain().configManager.noPerm);
            }
        } else if (args[0].equals("reload")) {
            if (sender.hasPermission("ath.*") || sender.hasPermission("ath.reload")) {
                Main.getMain().sendMsg(sender, Main.getMain().configManager.reloading);
                Bukkit.getPluginManager().disablePlugin(Main.getMain());
                Bukkit.getPluginManager().enablePlugin(Main.getMain());
                Main.getMain().sendMsg(sender, Main.getMain().configManager.reloaded);
            } else {
                Main.getMain().sendMsg(sender, Main.getMain().configManager.noPerm);
            }
        } else {
            Main.getMain().sendMsg(sender, Main.getMain().configManager.unknownCommand);
        }
        return true;
    }
}