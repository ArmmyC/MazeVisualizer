module cpe231 {
    requires javafx.controls;
    requires javafx.fxml;

    opens cpe231 to javafx.fxml;
    exports cpe231;
}
