package org.akadia.ath.velocity;

public class AthRecordEvent {
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