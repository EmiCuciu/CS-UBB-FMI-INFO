package org.example.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Request trimis de client către server
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private RequestType type;
    private int showId;
    private List<Integer> seats;

    public Request(RequestType type, int showId, List<Integer> seats) {
        this.type = type;
        this.showId = showId;
        this.seats = seats;
    }

    public RequestType getType() {
        return type;
    }

    public int getShowId() {
        return showId;
    }

    public List<Integer> getSeats() {
        return seats;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", showId=" + showId +
                ", seats=" + seats +
                '}';
    }
}
