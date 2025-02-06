module org.example.ati {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.ati to javafx.fxml;
    exports org.example.ati;
}