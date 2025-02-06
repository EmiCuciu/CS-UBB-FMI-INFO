package org.example.grile.GUI;

import org.example.grile.Service.Service;

public class ApplicationContext {
    private static final Controller controller = new Controller();

    public static Controller getController() {
        return controller;
    }

    public static Service getService() {
        return controller.getService();
    }
}
