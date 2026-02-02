package org.example.Model;

import java.util.Objects;

public class Response {
    private ResponseType type;
    private Object data;

    public Response(ResponseType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
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
