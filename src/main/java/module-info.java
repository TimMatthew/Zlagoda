module org.example.zlagoda {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens main to javafx.fxml;
    exports main;
}