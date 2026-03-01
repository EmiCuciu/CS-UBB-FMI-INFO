package org.example.Model;

import java.io.Serializable;
import java.util.Objects;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private ResponseType type;
    private Serializable data;

    public Response(ResponseType type, Serializable data) {
        this.type = type;
        this.data = data;
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public Serializable getData() {
        return data;
    }

    public void setData(Serializable data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return getType() == response.getType() && Objects.equals(getData(), response.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getData());
    }

    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}
