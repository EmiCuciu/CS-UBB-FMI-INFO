module com.example.laboratoriss {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.laboratoriss to javafx.fxml;
    exports com.example.laboratoriss;
}
