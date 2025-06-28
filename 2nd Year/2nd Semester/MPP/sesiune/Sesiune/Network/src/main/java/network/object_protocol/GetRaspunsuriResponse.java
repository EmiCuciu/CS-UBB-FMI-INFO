package network.object_protocol;

import domain.RaspunsJucator;

import java.util.List;

public class GetRaspunsuriResponse implements Response {
    private List<RaspunsJucator> raspunsuri;

    public GetRaspunsuriResponse(List<RaspunsJucator> raspunsuri) {
        this.raspunsuri = raspunsuri;
    }

    public List<RaspunsJucator> getRaspunsuri() {
        return raspunsuri;
    }
}