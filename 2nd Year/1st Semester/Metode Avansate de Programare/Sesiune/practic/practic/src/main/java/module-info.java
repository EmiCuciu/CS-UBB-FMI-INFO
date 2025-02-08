module org.example.practic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.practic to javafx.fxml;
    exports org.example.practic;
}