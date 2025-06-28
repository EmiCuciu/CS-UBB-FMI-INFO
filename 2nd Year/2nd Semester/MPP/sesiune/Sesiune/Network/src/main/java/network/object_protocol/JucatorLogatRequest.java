package network.object_protocol;

public class JucatorLogatRequest implements Request {
    private String jucatorAlias;

    public JucatorLogatRequest(String jucatorAlias) {
        this.jucatorAlias = jucatorAlias;
    }

    public String getJucatorAlias() {
        return jucatorAlias;
    }

    public void setJucatorAlias(String jucatorAlias) {
        this.jucatorAlias = jucatorAlias;
    }
}
