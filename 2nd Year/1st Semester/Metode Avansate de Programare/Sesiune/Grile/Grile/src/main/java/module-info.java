module org.example.grile {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.grile to javafx.fxml;
    exports org.example.grile;
}