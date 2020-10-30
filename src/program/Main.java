package program;

import com.sun.javafx.fxml.FXMLLoaderHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import program.screens.FileLoadingScreen;
import program.screens.MainScreen;
import schedulers.components.Process;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;

public class Main extends Application {
    private static ArrayList<Process> processes;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = FXMLLoader.load(FileLoadingScreen.class.getResource("file_loading_screen.fxml"));
        primaryStage.setTitle("CPU Scheduling Simulator");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}