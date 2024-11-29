package org.example.lab8.controller;

public class ApplicationContext {
    private static Controller controller = new Controller();

    public static Controller getController() {
        return controller;
    }
}
