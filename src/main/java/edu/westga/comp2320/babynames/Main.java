package edu.westga.comp2320.babynames;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point for the Baby Name Statistics application.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application.
     *
     * @param stage the primary stage
     * @throws Exception if loading FXML fails
     */
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader =
                new FXMLLoader(this.getClass().getResource("/BabyNameView.fxml"));

        Scene scene = new Scene(loader.load());

        stage.setTitle("Baby Names - Cason Robins");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}