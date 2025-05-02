module com.example.laboratoriss {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.apache.logging.log4j;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.naming;

    opens com.example.laboratoriss to javafx.fxml;
    opens com.example.laboratoriss.GUI to javafx.fxml;
    opens com.example.laboratoriss.Domain to org.hibernate.orm.core, javafx.base;

    exports com.example.laboratoriss;
    exports com.example.laboratoriss.GUI;
    exports com.example.laboratoriss.Domain;
}