import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {
    private static ArrayList<Process> processes;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Thread to read the file...

        //Thread to schedule...
        Scheduler scheduler = new RoundRobinScheduler(processes, 2);
        Thread thread = new Thread(scheduler);
        thread.start();
        // Building The GUI
        VBox root = new VBox();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        // Wait unitl the Scheduling in done...
        while(thread.isAlive());
        for (Quantum quantum : scheduler.getCpuLog()) {
            root.getChildren().add(new Text(quantum.toString()));
        }
    }


    public static void main(String[] args) {
        processes = new ArrayList<>();
        processes.add(
                new Process(2, 1, 6, 0, 0, 0)
        );
        processes.add(
                new Process(1, 2, 10, 0, 0, 0)
        );
        processes.add(
                new Process(4, 7, 8, 0, 0, 0)
        );
        processes.add(
                new Process(103, 4, 8, 0, 0, 0)
        );
        processes.add(
                new Process(104, 3, 3, 0, 0, 0)
        );
        processes.add(
                new Process(3, 5, 2, 0, 0, 0)
        );
        launch(args);
    }
}