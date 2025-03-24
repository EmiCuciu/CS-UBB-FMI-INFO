module com.example.laboratoriss {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires java.sql;

    opens com.example.laboratoriss.GUI to javafx.fxml, javafx.base;  // Add javafx.base here
    opens com.example.laboratoriss.Domain to javafx.base;
    exports com.example.laboratoriss;
}