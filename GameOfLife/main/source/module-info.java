module javaFiles {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires java.desktop;

    opens javaFiles to javafx.fxml;
    exports javaFiles;
}