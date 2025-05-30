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
            label.setStyle("-fx-background-color: rgba(0, 0, 0, 0.85); -fx-font-size: 13px; -fx-text-fill: #f5c518; -fx-padding: 15px; -fx-border-radius: 10; -fx-background-radius: 6;");
            label.setPrefWidth(380); // set fixed width
            label.setWrapText(true); // enable wrap text

            StackPane pane = new StackPane(label);
            pane.setStyle("-fx-background-color: transparent;");
            popup.getContent().add(pane);

            // Position: unten rechts
            double x = ownerStage.getX() + ownerStage.getWidth() - 420;
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
