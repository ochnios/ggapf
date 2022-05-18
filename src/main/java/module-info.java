module ggapf {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens ggapf to javafx.fxml;
    exports ggapf;
}
