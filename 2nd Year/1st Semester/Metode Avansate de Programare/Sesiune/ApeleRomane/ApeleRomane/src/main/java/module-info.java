module org.example.apeleromane {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.apeleromane to javafx.fxml;
    exports org.example.apeleromane;
}