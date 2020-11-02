package program;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import schedulers.components.Process;

import java.io.File;
import java.util.ArrayList;

public class Main extends Application {
    public static ArrayList<Process> processes;

    public Node getElementById(Pane pane, String id) {
        for (Node node : pane.getChildren()) {
            if (node.getId().compareTo(id) == 0)
                return node;
        }
        return null;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane fileLoadingScreen = FXMLLoader.load(getClass().getResource("screens/file_loading_screen.fxml"));
        Pane mainScreen = FXMLLoader.load(getClass().getResource("screens/main_screen.fxml"));
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        //primaryStage.setResizable(true);
        Button loadFile = (Button) getElementById(fileLoadingScreen, "load_file");
        Button browse = (Button) getElementById(fileLoadingScreen, "browse");
        TextField fileName = (TextField) getElementById(fileLoadingScreen, "file_name");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./"));
        File[] file = new File[1];
        double[] pos = new double[4];
        EventHandler<MouseEvent> capturePos = event -> {
            pos[0] = event.getScreenX();
            pos[1] = event.getScreenY();
            pos[2] = primaryStage.getX();
            pos[3] = primaryStage.getY();
        };
        EventHandler<MouseEvent> changePos = event -> {
            primaryStage.setX(pos[2] + event.getScreenX() - pos[0]);
            primaryStage.setY(pos[3] + event.getScreenY() - pos[1]);
        };
        fileLoadingScreen.setOnMousePressed(capturePos);
        fileLoadingScreen.setOnMouseDragged(changePos);
        mainScreen.setOnMousePressed(capturePos);
        mainScreen.setOnMouseDragged(changePos);
        ChangeListener<Number> widthListener = (observable, oldValue, newValue) -> primaryStage.setWidth(600);
        ChangeListener<Number> heightListener = (observable, oldValue, newValue) -> primaryStage.setHeight(400);
        browse.setOnAction(
                event -> {
                    file[0] = fileChooser.showOpenDialog(primaryStage);
                    fileName.setText(file[0].getAbsolutePath());
                }
        );
        loadFile.setOnAction(
                event -> {
                    if (file[0] == null) {
                        try {
                            file[0] = new File(fileName.getText());
                        } catch (Exception e) {
                            // Stop the Operation until a file is entered
                        }
                    }
                    readFile(file[0]);
                    primaryStage.widthProperty().removeListener(widthListener);
                    primaryStage.heightProperty().removeListener(heightListener);
                    primaryStage.setScene(
                        new Scene(mainScreen, 600, 400)
                    );
                }
        );
        primaryStage.setScene(new Scene(fileLoadingScreen, 600, 400));
        primaryStage.widthProperty().addListener(widthListener);
        primaryStage.heightProperty().addListener(heightListener);
        primaryStage.show();
    }

    private void readFile(File file) {
        System.out.println(file.getAbsolutePath());
    }

    public static void main(String[] args) {
        launch(args);
    }
}