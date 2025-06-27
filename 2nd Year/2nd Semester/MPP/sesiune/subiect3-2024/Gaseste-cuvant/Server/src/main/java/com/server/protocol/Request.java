package com.server.protocol;

import java.io.Serializable;

public class Request implements Serializable {
    private RequestType type;
    private Object data;

    public Request(RequestType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public RequestType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public enum RequestType {
        LOGIN,
        START_GAME,
        SUBMIT_WORD,
        FINISH_GAME,
        LOGOUT
    }
}