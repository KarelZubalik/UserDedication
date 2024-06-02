package org.userDedication;

import java.time.LocalDateTime;

public class UserObject {
    String user;
    String testName;
    LocalDateTime time;
    boolean universalPick;

    public UserObject(String user, String testName, LocalDateTime time,boolean universalPick) {
        this.user = user;
        this.testName = testName;
        this.time = time;
        this.universalPick=universalPick;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s [%s]", testName, user, time);
    }
}
