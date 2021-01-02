package org.akadia.ath.spigot;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AthRecordEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final int maxCount;
    private final String achievedDate;
    private boolean isCancelled;

    public AthRecordEvent(int maxCount, String achievedDate) {
        this.maxCount = maxCount;
        this.achievedDate = achievedDate;
        this.isCancelled = false;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public String getAchievedDate() {
        return achievedDate;
    }


    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}