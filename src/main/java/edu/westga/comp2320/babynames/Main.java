package edu.westga.comp2320.babynames;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        Scene scene = new Scene(root, 400, 300);

        stage.setTitle("Baby Names - Cason Robins");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}