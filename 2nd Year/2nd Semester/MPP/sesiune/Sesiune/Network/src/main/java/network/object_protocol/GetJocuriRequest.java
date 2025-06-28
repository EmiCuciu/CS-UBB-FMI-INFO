package network.object_protocol;

import domain.Jucator;

public class GetJocuriRequest implements Request {
    private Jucator jucator;

    public GetJocuriRequest(Jucator jucator) {
        this.jucator = jucator;
    }

    public Jucator getJucator() {
        return jucator;
    }
}