package at.ac.fhcampuswien.fhmdb.ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Toast {
    public static void makeText(Stage ownerStage, String message, int seconds) {
        Platform.runLater(() -> {
            Popup popup = new Popup();
            Label label = new Label(message);
            label.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-padding: 15px; -fx-border-radius: 10; -fx-background-radius: 6;");
            StackPane pane = new StackPane(label);
            pane.setStyle("-fx-background-color: transparent;");
            popup.getContent().add(pane);

            // Position: unten rechts
            double x = ownerStage.getX() + ownerStage.getWidth() - 300;
            double y = ownerStage.getY() + ownerStage.getHeight() - 100;
            popup.show(ownerStage, x, y);

            // Automatisch ausblenden nach X Sekunden
            PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
            delay.setOnFinished(e -> popup.hide());
            delay.play();

            // Per Klick schließen
            pane.setOnMouseClicked(e -> popup.hide());
        });
    }
}
