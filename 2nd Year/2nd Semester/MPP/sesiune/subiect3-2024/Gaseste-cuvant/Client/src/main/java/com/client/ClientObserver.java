package com.client;

import com.server.protocol.Response;

public interface ClientObserver {
    void responseReceived(Response response);
}