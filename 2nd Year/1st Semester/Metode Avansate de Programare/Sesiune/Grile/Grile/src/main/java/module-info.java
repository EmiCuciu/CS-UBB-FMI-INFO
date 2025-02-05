module org.example.grile {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.grile to javafx.fxml;
    exports org.example.grile;
}