package network.object_protocol;

import domain.Joc;

public class GetRaspunsuriRequest implements Request {
    private Joc joc;

    public GetRaspunsuriRequest(Joc joc) {
        this.joc = joc;
    }

    public Joc getJoc() {
        return joc;
    }
}