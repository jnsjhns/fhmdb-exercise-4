package at.ac.fhcampuswien.fhmdb.ui;

import javafx.stage.Stage;

public class StageHelper {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
