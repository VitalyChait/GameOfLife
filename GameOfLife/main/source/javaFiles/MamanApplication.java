package javaFiles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.io.IOException;

public class MamanApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String fxmlName = "Scene1.fxml";
        String fxmlPath = "file://" + MamanApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath() + fxmlName;

        FXMLLoader loader = new FXMLLoader(new URL(fxmlPath));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setTitle("Game of Life");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}