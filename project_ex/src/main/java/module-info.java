module org.example.project_ex {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens classes_controller to javafx.fxml;
    opens org.example.project_ex to javafx.fxml;
    exports org.example.project_ex;
    exports classes_controller;
}