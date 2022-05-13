module ggapf {
    requires javafx.controls;
    requires javafx.fxml;

    opens ggapf to javafx.fxml;
    exports ggapf;
}
