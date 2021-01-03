package org.akadia.ath.bungeecord;

import net.md_5.bungee.api.plugin.Event;

public class AthRecordEvent extends Event {
    private final int maxCount;
    private final String achievedDate;

    public AthRecordEvent(int maxCount, String achievedDate) {
        this.maxCount = maxCount;
        this.achievedDate = achievedDate;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public String getAchievedDate() {
        return achievedDate;
    }

}
