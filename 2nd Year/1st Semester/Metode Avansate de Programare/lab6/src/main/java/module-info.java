module org.example.lab6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.lab6 to javafx.fxml;
    exports org.example.lab6;
}