package cpe231;

import java.io.IOException;

import cpe231.ui.MainUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        testAlgorithm.runWarmUp();

        MainUI main = new MainUI();
        scene = new Scene(main.getRootPane(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("/cpe231/styles/styles.css").toExternalForm());
        stage.setTitle("Pathfinding Algorithm Visualizer");

        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}