package org.example.lab6.domain;

import java.time.LocalDateTime;

public class Friendship extends Entity<Tuple<Long, Long>> {
    private final LocalDateTime date;
    String user1;
    String user2;

    public Friendship(LocalDateTime date, String user1, String user2) {
        this.date = date;
        this.user1 = user1;
        this.user2 = user2;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }
}