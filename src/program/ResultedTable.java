package program;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import schedulers.components.Process;
import schedulers.Scheduler ;
import schedulers.components.Record;

import java.beans.PropertyEditor;
import java.io.IOException;
import java.util.ArrayList;

import static program.Main.getElementById;

public class ResultedTable {

    public void showTable(Scheduler scheduler) throws IOException {
        Pane anchorPane = FXMLLoader.load(getClass().getResource("screens/process_table_screen.fxml"));

        TableView<Record> table = (TableView) getElementById(anchorPane,"table") ;

        TableColumn<Record, Long> pidCol = new TableColumn<Record, Long>("PID");
        pidCol.setMinWidth(50); pidCol.setPrefWidth(75);
       // pidCol.setCellValueFactory(new PropertyValueFactory<Record, Long>("PID"));
        pidCol.setCellValueFactory(new PropertyValueFactory<Record, Long>("processID"));

        TableColumn<Record, Double> arrivalTimeCol = new TableColumn<Record, Double>("ArrivalTime");
        arrivalTimeCol.setMinWidth(75); arrivalTimeCol.setPrefWidth(90);
        arrivalTimeCol.setCellValueFactory(new PropertyValueFactory<Record, Double>("ArrivalTime"));
        //arrivalTimeCol.setCellValueFactory(new PropertyValueFactory<Record, Double>("ArrivalTime"));

        TableColumn<Record, Double> startTimeCol = new TableColumn<Record, Double>("StartTime");
        startTimeCol.setMinWidth(75); startTimeCol.setPrefWidth(90);
        startTimeCol.setCellValueFactory(new PropertyValueFactory<Record, Double>("StartTime"));

        TableColumn<Record, Double> finishTimeCol = new TableColumn<Record, Double>("FinishTime");
        finishTimeCol.setMinWidth(75); finishTimeCol.setPrefWidth(90);
        finishTimeCol.setCellValueFactory(new PropertyValueFactory<Record, Double>("FinishTime"));

        TableColumn<Record, Double> TATimeCol = new TableColumn<Record, Double>("TATime");
        TATimeCol.setMinWidth(75); TATimeCol.setPrefWidth(90);
        //TATimeCol.setCellValueFactory(new PropertyValueFactory<Record, Double>("TATime"));
        TATimeCol.setCellValueFactory(new PropertyValueFactory<Record, Double>("turnAround"));

        TableColumn<Record, Double> WTATimeCol = new TableColumn<Record, Double>("WTATime");
        WTATimeCol.setMinWidth(75); WTATimeCol.setPrefWidth(90);
        //WTATimeCol.setCellValueFactory(new PropertyValueFactory<Record, Double>("WTATime"));
        WTATimeCol.setCellValueFactory(new PropertyValueFactory<Record, Double>("weightedTurnAround"));


        TableColumn<Record, Double> waitTimeCol = new TableColumn<Record, Double>("WaitTime");
        waitTimeCol.setMinWidth(75); waitTimeCol.setPrefWidth(90);
        waitTimeCol.setCellValueFactory(new PropertyValueFactory<Record, Double>("WaitTime"));

       /* TableColumn<Record, Double> burstTimeCol = new TableColumn<Record, Double>("BurstTime");
        burstTimeCol.setMinWidth(75); burstTimeCol.setPrefWidth(90);
        burstTimeCol.setCellValueFactory(new PropertyValueFactory<Record, Double>("BurstTime")); */
        
        Collections.sort(scheduler.getProcessesLog(), Comparator.comparingDouble(Record::getProcessID));
        ObservableList<Record> data = FXCollections.observableArrayList(scheduler.getProcessesLog());

        table.setItems(data);
        table.getColumns().addAll(pidCol, arrivalTimeCol, startTimeCol, finishTimeCol,TATimeCol,WTATimeCol,waitTimeCol);


        Stage stage = new Stage();
        try {
            stage.getIcons().add(new Image("file:icon.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        stage.setTitle("The Resulted Table");
        stage.setScene(new Scene(anchorPane, 650, 400));
        stage.show();

    }

}
