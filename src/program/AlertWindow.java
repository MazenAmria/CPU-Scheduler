package program;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertWindow {

    public void displayAlertWindow(String title, String message) {
        Stage alertWindow = new Stage();
        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle(title);
        alertWindow.setMinWidth(250);
        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Close the window");
        closeButton.setOnAction(ee -> alertWindow.close());
        VBox vbox = new VBox(10);
        vbox.setPrefHeight(150);
        vbox.setPrefWidth(300);
        vbox.getChildren().addAll(label, closeButton);
        vbox.setAlignment(Pos.CENTER);
        Scene alertScene = new Scene(vbox);
        alertWindow.setScene(alertScene);
        alertWindow.showAndWait();
    }

}
