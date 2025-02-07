module org.example.faptebune {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.faptebune to javafx.fxml;
    exports org.example.faptebune;
}