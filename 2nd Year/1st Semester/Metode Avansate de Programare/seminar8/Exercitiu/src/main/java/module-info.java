module org.example.exercitiu {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.exercitiu to javafx.fxml;
    exports org.example.exercitiu;
}