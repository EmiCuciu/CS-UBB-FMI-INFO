package com.server.protocol;

import java.io.Serializable;

public class Response implements Serializable {
    private ResponseType type;
    private Object data;
    private String message;

    public Response(ResponseType type, Object data) {
        this.type = type;
        this.data = data;
        this.message = null;
    }

    public Response(ResponseType type, String message) {
        this.type = type;
        this.data = null;
        this.message = message;
    }

    public ResponseType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public enum ResponseType {
        SUCCESS,
        ERROR,
        GAME_STARTED,
        WORD_RESULT,
        GAME_OVER,
        RANKING_UPDATE
    }
}