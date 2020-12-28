package org.akadia.ath.spigot;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AthPlaceholder extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "ath";
    }

    @Override
    public @NotNull String getAuthor() {
        return "akadia";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equalsIgnoreCase("count")) {
            return String.valueOf(Main.getMain().maxCount);
        }

        if (identifier.equalsIgnoreCase("date")) {
            return Main.getMain().achievedDate;
        }

        return null;
    }
}
