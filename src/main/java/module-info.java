module ai.detector {
    requires javafx.controls;
    requires javafx.fxml;

    opens ai.detector to javafx.fxml;
    exports ai.detector;
}
