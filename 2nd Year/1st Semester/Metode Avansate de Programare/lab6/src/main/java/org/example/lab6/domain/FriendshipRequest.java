package org.example.lab6.domain;

import java.time.LocalDateTime;

public class FriendshipRequest extends Entity<Tuple<Long,Long>> {
    private final LocalDateTime dateSent;
    private final String status;

    public FriendshipRequest(Long sender, Long receiver, LocalDateTime dateSent, String status) {
        this.setId(new Tuple<>(sender, receiver));
        this.dateSent = dateSent;
        this.status = status;
    }

    public Long getSender() {
        return getId().getE1();
    }

    public Long getReceiver() {
        return getId().getE2();
    }

    public LocalDateTime getDateSent() {
        return dateSent;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "from " +  getSender() +
                " to " + getReceiver() +
                " on " + dateSent +
                " status: " + status;
    }
}
