package gui;

import services.IServices;

public class MainController {
    private IServices server;

    public void setServer(IServices server) {
        this.server = server;
    }
}
