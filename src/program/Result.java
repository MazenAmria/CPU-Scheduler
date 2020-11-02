package program;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import schedulers.Scheduler;
import schedulers.components.Quantum;
import schedulers.components.Record;
import schedulers.components.Visualisable;

import java.util.ArrayList;

public class Result {
    private Scheduler scheduler;
    private double totalTime;

    public Result(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void show() throws Exception {
        Thread thread = new Thread(scheduler);
        thread.start();
        // Building and showing the basic UI
        Stage stage = new Stage();
        AnchorPane result = new AnchorPane();
        stage.setScene(new Scene(result, 900, 400));
        stage.show();
        VBox averages = FXMLLoader.load(getClass().getResource("screens/averages.fxml"));
        AnchorPane.setRightAnchor(averages, 650.0);
        AnchorPane.setLeftAnchor(averages, 0.0);
        AnchorPane.setTopAnchor(averages, 0.0);
        AnchorPane.setBottomAnchor(averages, 0.0);
        result.getChildren().add(averages);
        Pane axis = FXMLLoader.load(getClass().getResource("screens/axis.fxml"));
        axis.setStyle("-fx-background-color: yellow;");
        AnchorPane.setRightAnchor(axis, 550.0);
        AnchorPane.setLeftAnchor(axis, 250.0);
        AnchorPane.setTopAnchor(axis, 0.0);
        AnchorPane.setBottomAnchor(axis, 0.0);
        result.getChildren().add(axis);
        Pane ganttChart = FXMLLoader.load(getClass().getResource("screens/gantt_chart.fxml"));
        ganttChart.setStyle("-fx-background-color: blue;");
        AnchorPane.setRightAnchor(ganttChart, 0.0);
        AnchorPane.setLeftAnchor(ganttChart, 350.0);
        AnchorPane.setTopAnchor(ganttChart, 0.0);
        AnchorPane.setBottomAnchor(ganttChart, 0.0);
        result.getChildren().add(ganttChart);
        stage.widthProperty().addListener((observableValue, number, t1) -> {
            AnchorPane.setRightAnchor(averages, t1.doubleValue() * 650 / 900);
            AnchorPane.setLeftAnchor(axis, t1.doubleValue() * 250 / 900);
            AnchorPane.setRightAnchor(axis, t1.doubleValue() * 350 / 900);
            AnchorPane.setLeftAnchor(ganttChart, t1.doubleValue() * 350 / 900);
        });
        double startTime = 0;
        double finishTime = 0;
        double arrivalTime = 0;
        double taskDuration = 0;
        double turnAround = 0;
        double weightedTurnAround = 0;
        double waitTime = 0;
        HBox choose = (HBox) Main.getElementById(averages, "choose");
        ToggleButton processes = (ToggleButton) Main.getElementById(choose, "processes");
        processes.setSelected(true);
        ToggleButton cpu = (ToggleButton) Main.getElementById(choose, "cpu");
        cpu.setSelected(false);
        // Waiting for the scheduler to finish scheduling
        while(thread.isAlive());
        // Continue building the UI and filling the results
        for (Record record : scheduler.getProcessesLog()) {
            startTime += record.getStartTime();
            finishTime += record.getFinishTime();
            arrivalTime += record.getArrivalTime();
            taskDuration += record.getTaskDuration();
            turnAround += record.getTurnAround();
            weightedTurnAround += record.getWeightedTurnAround();
            waitTime += record.getWaitTime();
        }
        startTime /= scheduler.numOfProcesses;
        finishTime /= scheduler.numOfProcesses;
        arrivalTime /= scheduler.numOfProcesses;
        taskDuration /= scheduler.numOfProcesses;
        turnAround /= scheduler.numOfProcesses;
        weightedTurnAround /= scheduler.numOfProcesses;
        waitTime /= scheduler.numOfProcesses;
        GridPane avgs = (GridPane) Main.getElementById(averages, "grid");
        avgs.add(new Text(String.format("%.2f", startTime)), 1, 0);
        avgs.add(new Text(String.format("%.2f", finishTime)), 1, 1);
        avgs.add(new Text(String.format("%.2f", arrivalTime)), 1, 2);
        avgs.add(new Text(String.format("%.2f", taskDuration)), 1, 3);
        avgs.add(new Text(String.format("%.2f", turnAround)), 1, 4);
        avgs.add(new Text(String.format("%.2f", weightedTurnAround)), 1, 5);
        avgs.add(new Text(String.format("%.2f", waitTime)), 1, 6);
        this.totalTime = 0;
        for (Quantum quantum : scheduler.getCpuLog()) {
            this.totalTime = Double.max(this.totalTime, quantum.getFinishTime());
        }
        final ArrayList<Visualisable>[] visualisables = new ArrayList[]{scheduler.getProcessesLogVis()};
        setAxis(axis, result.getWidth() / 9, result.getHeight());
        for (Visualisable v : visualisables[0]) {
            map(ganttChart, v, result.getWidth() * 550 / 900, result.getHeight());
        }
        processes.setOnAction(actionEvent -> {
            processes.setSelected(true);
            cpu.setSelected(false);
            visualisables[0] = scheduler.getProcessesLogVis();
            ganttChart.getChildren().clear();
            for (Visualisable v : visualisables[0]) {
                map(ganttChart, v, result.getWidth() * 550 / 900, result.getHeight());
            }
        });
        cpu.setOnAction(actionEvent -> {
            cpu.setSelected(true);
            processes.setSelected(false);
            visualisables[0] = scheduler.getCpuLogVis();
            ganttChart.getChildren().clear();
            for (Visualisable v : visualisables[0]) {
                map(ganttChart, v, result.getWidth() * 550 / 900, result.getHeight());
            }
        });
        result.widthProperty().addListener((observableValue, number, t1) -> {
            axis.getChildren().clear();
            setAxis(axis, t1.doubleValue() / 9, result.getHeight());
            ganttChart.getChildren().clear();
            for (Visualisable v : visualisables[0]) {
                map(ganttChart, v, t1.doubleValue() * 550 / 900, result.getHeight());
            }
        });
        result.heightProperty().addListener((observableValue, number, t1) -> {
            axis.getChildren().clear();
            setAxis(axis, result.getWidth() / 9, t1.doubleValue());
            ganttChart.getChildren().clear();
            for (Visualisable v : visualisables[0]) {
                map(ganttChart, v, result.getWidth() * 550 / 900, t1.doubleValue());
            }
        });
    }

    private void setAxis(Pane axis, double w, double h){
        h -= 20;
        int n = scheduler.numOfProcesses;
        for (Long i : scheduler.processesTable.keySet()){
            int y = scheduler.processesTable.get(i);
            HBox temp = new HBox();
            temp.setAlignment(Pos.CENTER);
            temp.setLayoutY(h * y / n + 10);
            temp.setMinWidth(w);
            temp.setMinHeight(h / n);
            Text text = new Text();
            text.setText(i.toString());
            text.setFont(Font.font("Eras Demi ITC", 25.0));
            text.setFill(Color.RED);
            temp.getChildren().add(text);
            axis.getChildren().add(temp);
        }
    }

    private void map(Pane ganttChart, Visualisable v, double w, double h){
        w -= 20;
        h -= 20;
        double l = v.getStartTime();
        double r = v.getFinishTime();
        int y = scheduler.processesTable.get(v.getProcessID());
        double t = this.totalTime;
        int n = scheduler.numOfProcesses;
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(h / n);
        rectangle.setWidth((r - l) * w / t);
        rectangle.setLayoutX(l * w / t + 10);
        rectangle.setLayoutY(h * y / n + 10);
        rectangle.setArcHeight(15);
        rectangle.setArcWidth(15);
        rectangle.setFill(Color.RED);
        ganttChart.getChildren().add(rectangle);
    }
}