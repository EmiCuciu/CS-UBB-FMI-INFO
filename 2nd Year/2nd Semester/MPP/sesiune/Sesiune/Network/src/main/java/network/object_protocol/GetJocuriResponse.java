package network.object_protocol;

import domain.Joc;

import java.util.List;

public class GetJocuriResponse implements Response {
    private List<Joc> jocuri;

    public GetJocuriResponse(List<Joc> jocuri) {
        this.jocuri = jocuri;
    }

    public List<Joc> getJocuri() {
        return jocuri;
    }
}