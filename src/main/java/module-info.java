module ai.detector {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.opencsv;

    opens ai.detector to javafx.fxml;
    exports ai.detector;
}
