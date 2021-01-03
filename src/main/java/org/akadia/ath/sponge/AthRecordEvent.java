package org.akadia.ath.sponge;

import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;

public class AthRecordEvent extends AbstractEvent implements Cancellable {

    private final Cause cause;
    private final int maxCount;
    private final String achievedDate;
    private boolean cancelled = false;

    public AthRecordEvent(Cause cause, int maxCount, String achievedDate) {
        this.cause = cause;
        this.maxCount = maxCount;
        this.achievedDate = achievedDate;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public String getAchievedDate() {
        return achievedDate;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

}
