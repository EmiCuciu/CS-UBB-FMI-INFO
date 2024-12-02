package org.example.lab8.controller;

import org.example.lab8.services.Service;

/**
 * This class is used to create a single instance of the Controller class.
 */
public class ApplicationContext {
    private static Controller controller = new Controller();

    public static Controller getController() {
        return controller;
    }

    public static Service getService() {
        return controller.getService();
    }
}
