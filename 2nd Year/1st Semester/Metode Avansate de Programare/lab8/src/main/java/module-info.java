module org.example.lab {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens org.example.lab8 to javafx.fxml;
    exports org.example.lab8;
    exports org.example.lab8.controller;
    exports org.example.lab8.domain;
    exports org.example.lab8.repository;
    exports org.example.lab8.services;
    exports org.example.lab8.utils.events;
    exports org.example.lab8.utils.observer;
    opens org.example.lab8.controller to javafx.fxml;
}