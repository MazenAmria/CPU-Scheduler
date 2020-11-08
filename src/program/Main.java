package program;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import schedulers.components.Process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static program.MainScreenHandeler.handelMainScreenActions;

public class Main extends Application {

    public static ArrayList<Process> processes = new ArrayList<>();

    public static Node getElementById(Pane pane, String id) {
        for (Node node : pane.getChildren()) {
            if (node.getId() != null && node.getId().compareTo(id) == 0)
                return node;
        }

        return null;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane fileLoadingScreen = FXMLLoader.load(getClass().getResource("screens/file_loading_screen.fxml"));
        Pane mainScreen = FXMLLoader.load(getClass().getResource("screens/main_screen.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(true);
        Button loadFile = (Button) getElementById(fileLoadingScreen, "load_file");
        Button browse = (Button) getElementById(fileLoadingScreen, "browse");
        Button generateFileAutomatically = (Button) getElementById(fileLoadingScreen, "generate_file_automatically");
        TextField fileName = (TextField) getElementById(fileLoadingScreen, "file_name");
        Button exit = (Button) getElementById(fileLoadingScreen, "exit");
        exit.setOnAction(actionEvent -> {
            System.exit(0);
        });

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
                    if (file[0] != null)
                        fileName.setText(file[0].getAbsolutePath());
                }
        );
        generateFileAutomatically.setOnAction(
                event -> {
                    generateFileAutomatically(fileName);
                    try {
                        primaryStage.setScene(
                                new Scene(mainScreen, 600, 400)
                        );
                        handelMainScreenActions(mainScreen);
                    } catch (Exception e) {
                        processes.clear();
                        // Stop the Operation until a file is entered
                        (new Alert(Alert.AlertType.ERROR, e.getMessage())).show();
                    }
                }
        );
        fileName.setOnAction(
                event -> {
                    try {
                        if (fileName.getText() == "") throw new Exception("Invalid File Name");
                        file[0] = new File(fileName.getText());
                        readFile(file[0]);
                        primaryStage.setScene(
                                new Scene(mainScreen, 600, 400)
                        );
                        handelMainScreenActions(mainScreen);
                    } catch (Exception e) {
                        processes.clear();
                        // Stop the Operation until a file is entered
                        (new Alert(Alert.AlertType.ERROR, e.getMessage())).show();
                    }
                }
        );
        loadFile.setOnAction(
                event -> {
                    if (file[0] == null) {
                        try {
                            if (fileName.getText() == "") throw new Exception("Invalid File Name");
                            file[0] = new File(fileName.getText());
                            readFile(file[0]);
                            primaryStage.setScene(
                                    new Scene(mainScreen, 600, 400)
                            );
                            handelMainScreenActions(mainScreen);
                        } catch (Exception e) {
                            processes.clear();
                            // Stop the Operation until a file is entered
                            (new Alert(Alert.AlertType.ERROR, e.getMessage())).show();
                        }
                    } else {
                        try {
                            readFile(file[0]);
                            primaryStage.setScene(
                                    new Scene(mainScreen, 782, 715)
                            );
                            handelMainScreenActions(mainScreen);
                        } catch (Exception e) {
                            processes.clear();
                            (new Alert(Alert.AlertType.ERROR, e.getMessage())).show();
                        }
                    }
                }
        );
        primaryStage.setScene(new Scene(fileLoadingScreen, 600, 400));
        primaryStage.widthProperty().addListener(widthListener);
        primaryStage.heightProperty().addListener(heightListener);
        primaryStage.show();
    }

    private void readFile(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] fields = line.split(",");
            Process process = new Process(Long.parseLong(fields[0]), Double.parseDouble(fields[1]),
                    Double.parseDouble(fields[2]), Long.parseLong(fields[3]), Long.parseLong(fields[4]),
                    Long.parseLong(fields[5].trim()));
            processes.add(process);
        }
    }


    public static void generateFileAutomatically(TextField fileName) {
        // More than 40 process would be exhaustive, less than 5 would be meaningless
        int numberOfLines = (int) (5 + Math.random() * (41 - 5)); // generate a number n: 5 <= n <= 40
        try {
            FileWriter fw = new FileWriter("auto_generated");
            Integer[] arr = new Integer[numberOfLines];
            for (int i = 0; i < numberOfLines; i++) arr[i] = i;
            List<Integer> temp = Arrays.asList(arr);
            Collections.shuffle(temp);
            temp.toArray(arr);
            for (int i = 0; i < numberOfLines; i++) {
                long pID = arr[i]; // unique
                // processes with burstTime larger than 40 will take more time to simulate and would occupy a wider chart
                double arrivalTime = Math.floor(Math.random() * 41); // arrivalTime <= 40.0 (floor is just to make numbers easier to read and deal with)
                double burstTime = Math.floor(1 + Math.random() * 41); // burstTime <= 40.0 (floor is just to make numbers easier to read and deal with)
                fw.write(pID + "," + arrivalTime + "," + burstTime + ",0,0,0\n");
                processes.add(new Process(pID, arrivalTime, burstTime, 0, 0, 0));
            }
            fw.close();
            fileName.setText("auto_generated");
        } catch (IOException e) {
            processes.clear();
            (new Alert(Alert.AlertType.ERROR, e.getMessage())).show();
        }
    }

    public static double findTimeQuantum(ArrayList<Process> processes) {
        double timeQuantum;
        processes = new ArrayList<>(processes);
        // Sort processes based on the task duration parameter
        Collections.sort(processes, Comparator.comparingDouble(Process::getTaskDuration));
        int numberOfProcessesToIncludeInTQ = (int) Math.ceil(0.8 * (double) processes.size());
        if (numberOfProcessesToIncludeInTQ == 0) return 0;
        timeQuantum = processes.get(numberOfProcessesToIncludeInTQ - 1).getTaskDuration();
        return timeQuantum;
    }

    public static long findAgeFactor(ArrayList<Process> processes) {
        long MaxPID = Long.MIN_VALUE, MinPID = Long.MAX_VALUE;
        for (Process process : processes) {
            MaxPID = Long.max(MaxPID, process.getProcessID());
            MinPID = Long.min(MinPID, process.getProcessID());
        }
        return (MaxPID - MinPID) / processes.size();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
