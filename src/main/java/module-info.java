module org.example.zlagoda {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens Entities to javafx.base;

    opens main to javafx.fxml;
    exports main;
}