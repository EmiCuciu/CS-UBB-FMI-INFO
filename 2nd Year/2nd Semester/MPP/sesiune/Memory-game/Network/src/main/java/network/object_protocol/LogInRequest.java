package network.object_protocol;

import domain.Jucator;

public class LogInRequest implements Request {
    private Jucator jucator;

    public LogInRequest(Jucator jucator) {
        this.jucator = jucator;
    }
    public Jucator getJucator() {
        return jucator;
    }
    public void setJucator(Jucator jucator) {
        this.jucator = jucator;
    }
}
