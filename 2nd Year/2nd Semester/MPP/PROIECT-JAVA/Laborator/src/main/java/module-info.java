module com.example.laborator {
    requires org.apache.logging.log4j;
    requires java.sql;
    requires javafx.fxml;
    requires javafx.controls;

    opens com.example.laborator to javafx.fxml;
    opens com.example.laborator.GUI to javafx.fxml;
    exports com.example.laborator;
    exports com.example.laborator.GUI;
}