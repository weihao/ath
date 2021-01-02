package org.akadia.ath.bungee;

import net.md_5.bungee.api.plugin.Event;

public class AthRecordEvent extends Event {
    public int getMaxCount() {
        return maxCount;
    }

    public String getAchievedDate() {
        return achievedDate;
    }

    private final int maxCount;
    private final String achievedDate;

    public AthRecordEvent(int maxCount, String achievedDate) {
        this.maxCount = maxCount;
        this.achievedDate = achievedDate;
    }

}
