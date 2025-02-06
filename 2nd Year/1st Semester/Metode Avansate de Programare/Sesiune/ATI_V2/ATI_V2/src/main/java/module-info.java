module org.example.ati_v2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.ati_v2 to javafx.fxml;
    exports org.example.ati_v2;
}