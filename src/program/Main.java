package program;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import schedulers.Scheduler;
import schedulers.algorithms.*;
import schedulers.components.Process;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import static program.MainScreenHandeler.handelMainScreenActions;

public class Main extends Application {

    public static ArrayList<Process> processes = new ArrayList<>();

    public static Node getElementById(Pane pane, String id) {
        for (Node node : pane.getChildren()) {
            if (node.getId() != null && node.getId().compareTo(id) == 0)
                return node;
        }

        return null ;
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
                            if (fileName.getText() == "") throw new Exception("InValid File Name");
                        } catch (Exception e) {
                            // Stop the Operation until a file is entered
                            AlertWindow alert = new AlertWindow();
                            alert.displayAlertWindow("File Not Found Error" , "You have to include a file to continue");
                        }
                    }
                    else{
                        try {
                            readFile(file[0]);
                            primaryStage.setScene(
                                    new Scene(mainScreen, 782, 715)
                            );

                            handelMainScreenActions(mainScreen);
                        } catch (FileNotFoundException e){
                            AlertWindow alert = new AlertWindow();
                            alert.displayAlertWindow("File Not Found Error" , "You have to include a file to continue");
                        } catch (Exception e) {
                            System.out.print(e.getMessage());
                        }
                    }
                }
        );
        primaryStage.setScene(new Scene(fileLoadingScreen,600 ,400));
      //  primaryStage.widthProperty().addListener(widthListener);
      //  primaryStage.heightProperty().addListener(heightListener);
        primaryStage.show();
    }

    private void readFile(File file) throws FileNotFoundException {
        System.out.println(file.getAbsolutePath());
        Scanner sc = new Scanner(file);
        while(sc.hasNext()) {
            String line = sc.nextLine();
            String[] fields = line.split(",");
            Process process = new Process(Long.parseLong(fields[0]), Double.parseDouble(fields[1]),
                    Double.parseDouble(fields[2]), Long.parseLong(fields[3]), Long.parseLong(fields[4]),
                    Long.parseLong(fields[5].trim()));
            processes.add(process);
        }
    }

    public static double findTimeQuantum(ArrayList<Process> processes) {
        double timeQuantum = 0;
        processes = new ArrayList<>(processes);
        // Sort processes based on the task duration parameter
        Collections.sort(processes, Comparator.comparingDouble(Process::getTaskDuration));
        int numberOfProcessesToIncludeInTQ = (int) Math.ceil(0.8 * (double) processes.size());
        if (numberOfProcessesToIncludeInTQ == 0) return 0;
        timeQuantum = processes.get(numberOfProcessesToIncludeInTQ - 1).getTaskDuration();
        return timeQuantum;
    }

    public static long findAgeFactor(ArrayList<Process> processes) {
        long MaxPID = Long.MAX_VALUE , MinPID = 0 ;
        for(int i=0;i<processes.size();i++){
            MaxPID = Math.max(MaxPID , processes.get(i).getProcessID()) ;
            MinPID = Math.min(MinPID , processes.get(i).getProcessID()) ;
        }
        return (MaxPID-MinPID)/processes.size() ;
    }



    public static void main(String[] args) {
        launch(args);
    }
}
