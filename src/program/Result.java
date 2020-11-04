package program;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import schedulers.Scheduler;
import schedulers.components.Quantum;
import schedulers.components.Record;
import schedulers.components.Visualisable;

import java.io.FileInputStream;
import java.util.ArrayList;

public class Result {
    private final Scheduler scheduler;
    private double finalTime;
    private double beginTime;
    private final double vScale = 1.0;
    private double hScale = 1.0;
    private double screenHeight = 400.0;
    private double screenWidth = 900.0;

    public Result(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void show() throws Exception {
        Thread thread = new Thread(scheduler);
        thread.start();
        // Building and showing the basic UI
        Stage stage = new Stage();
        try {
            stage.getIcons().add(new Image("file:icon.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] schedulerName = this.scheduler.getClass().getName().split("\\.");
        stage.setTitle(schedulerName[schedulerName.length - 1]);
        AnchorPane result = new AnchorPane();
        stage.setScene(new Scene(result, 900, 400));
        stage.show();
        // Averages Panel...
        VBox container = FXMLLoader.load(getClass().getResource("screens/averages.fxml"));
        AnchorPane.setRightAnchor(container, 650.0);
        AnchorPane.setLeftAnchor(container, 0.0);
        AnchorPane.setTopAnchor(container, 0.0);
        AnchorPane.setBottomAnchor(container, 0.0);
        result.getChildren().add(container);
        VBox averages = (VBox) Main.getElementById(container, "averages");
        // Gantt Chart Panel...
        Pane ganttChart = FXMLLoader.load(getClass().getResource("screens/gantt_chart.fxml"));
        ganttChart.setStyle("-fx-background-color: #FFFFFF;");
        ScrollPane scrollPane = new ScrollPane(ganttChart);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 250.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        result.getChildren().add(scrollPane);
        result.widthProperty().addListener((observableValue, number, t1) -> {
            AnchorPane.setRightAnchor(averages, t1.doubleValue() * 650 / 900);
            AnchorPane.setLeftAnchor(scrollPane, t1.doubleValue() * 250 / 900);
        });
        // Defining the averages...
        double startTime = 0;
        double finishTime = 0;
        double arrivalTime = 0;
        double taskDuration = 0;
        double turnAround = 0;
        double weightedTurnAround = 0;
        double waitTime = 0;
        // Toggle Buttons...
        VBox lower = (VBox) Main.getElementById(container, "lower");
        HBox choose = (HBox) Main.getElementById(lower, "choose");
        String selectedStyle = "-fx-border-color: #F93F40; -fx-border-width: 2px; -fx-border-radius: 3px; -fx-background-color: #7b6d8d;";
        String notSelectedStyle = "-fx-border-width: 0px; -fx-background-color: #7b6d8d;";
        ToggleButton processes = (ToggleButton) Main.getElementById(choose, "processes");
        processes.setSelected(false);
        processes.setStyle(notSelectedStyle);
        ToggleButton cpu = (ToggleButton) Main.getElementById(choose, "cpu");
        cpu.setSelected(true);
        cpu.setStyle(selectedStyle);
        HBox scale = (HBox) Main.getElementById(lower, "scale");
        Slider slider = (Slider) Main.getElementById(scale, "slider");
        slider.setMax(4.00);
        slider.setMin(0.05);
        slider.setValue(1.00);
        TextField value = (TextField) Main.getElementById(scale, "value");
        value.setText("1.00");
        // Waiting for the scheduler to finish scheduling
        while (thread.isAlive()) ;
        // Continue building the UI and filling the results
        // Calculating the averages...
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
        // Setting the averages...
        GridPane avgs = (GridPane) Main.getElementById(averages, "grid");
        Text tx1 = new Text(String.format("%.2f", startTime));
        tx1.setFill(Color.WHITE);
        avgs.add(tx1, 1, 0);
        Text tx2 = new Text(String.format("%.2f", finishTime));
        tx2.setFill(Color.WHITE);
        avgs.add(tx2, 1, 1);
        Text tx3 = new Text(String.format("%.2f", arrivalTime));
        tx3.setFill(Color.WHITE);
        avgs.add(tx3, 1, 2);
        Text tx4 = new Text(String.format("%.2f", taskDuration));
        tx4.setFill(Color.WHITE);
        avgs.add(tx4, 1, 3);
        Text tx5 = new Text(String.format("%.2f", turnAround));
        tx5.setFill(Color.WHITE);
        avgs.add(tx5, 1, 4);
        Text tx6 = new Text(String.format("%.2f", weightedTurnAround));
        tx6.setFill(Color.WHITE);
        avgs.add(tx6, 1, 5);
        Text tx7 = new Text(String.format("%.2f", waitTime));
        tx7.setFill(Color.WHITE);
        avgs.add(tx7, 1, 6);
        // Calculate time interval...
        this.finalTime = 0;
        this.beginTime = Double.MAX_VALUE;
        for (Quantum quantum : scheduler.getCpuLog()) {
            this.finalTime = Double.max(this.finalTime, quantum.getFinishTime());
            this.beginTime = Double.min(this.beginTime, quantum.getStartTime());
        }
        // Define the intervals to be visulaized...
        final ArrayList<Visualisable>[] visualisables = new ArrayList[]{scheduler.getCpuLogVis()};
        // Setting the Gantt Chart to fit the intervals...
        ganttChart.setPrefHeight(Double.max(this.screenHeight - 10, (this.vScale * 30 + 5) * scheduler.numOfProcesses + 5));
        ganttChart.setPrefWidth(Double.max(this.screenWidth * 650 / 900 - 10, this.finalTime * this.hScale * 20 + 10));
        // Visualize...
        setGrid(ganttChart);
        for (Visualisable v : visualisables[0]) {
            map(ganttChart, v);
        }
        // Changing the Scale...
        slider.setOnMouseDragged(mouseEvent -> {
            this.hScale = slider.getValue();
            value.setText(String.format("%.2f", slider.getValue()));
            // Clear...
            ganttChart.getChildren().clear();
            // Setting the Gantt Chart to fit the intervals...
            ganttChart.setPrefHeight(Double.max(this.screenHeight - 10, (this.vScale * 30 + 5) * scheduler.numOfProcesses + 5));
            ganttChart.setPrefWidth(Double.max(this.screenWidth * 650 / 900 - 10, this.finalTime * this.hScale * 20 + 10));
            // Visualize...
            setGrid(ganttChart);
            for (Visualisable v : visualisables[0]) {
                map(ganttChart, v);
            }
        });
        value.setOnAction(actionEvent -> {
            this.hScale = Double.parseDouble(value.getText());
            slider.setValue(this.hScale);
            // Clear...
            ganttChart.getChildren().clear();
            // Setting the Gantt Chart to fit the intervals...
            ganttChart.setPrefHeight(Double.max(this.screenHeight - 10, (this.vScale * 30 + 5) * scheduler.numOfProcesses + 5));
            ganttChart.setPrefWidth(Double.max(this.screenWidth * 650 / 900 - 10, this.finalTime * this.hScale * 20 + 10));
            // Visualize...
            setGrid(ganttChart);
            for (Visualisable v : visualisables[0]) {
                map(ganttChart, v);
            }
        });
        // Setting the Logic of the Toggle Buttons...
        processes.setOnAction(actionEvent -> {
            processes.setSelected(true);
            processes.setStyle(selectedStyle);
            cpu.setSelected(false);
            cpu.setStyle(notSelectedStyle);
            visualisables[0] = scheduler.getProcessesLogVis();
            ganttChart.setPrefHeight(Double.max(this.screenHeight - 10, (this.vScale * 30 + 5) * scheduler.numOfProcesses + 5));
            ganttChart.setPrefWidth(Double.max(this.screenWidth * 650 / 900 - 10, this.finalTime * this.hScale * 20 + 10));
            ganttChart.getChildren().clear();
            setGrid(ganttChart);
            for (Visualisable v : visualisables[0]) {
                map(ganttChart, v);
            }
        });
        cpu.setOnAction(actionEvent -> {
            cpu.setSelected(true);
            cpu.setStyle(selectedStyle);
            processes.setSelected(false);
            processes.setStyle(notSelectedStyle);
            visualisables[0] = scheduler.getCpuLogVis();
            ganttChart.setPrefHeight(Double.max(this.screenHeight - 10, (this.vScale * 30 + 5) * scheduler.numOfProcesses + 5));
            ganttChart.setPrefWidth(Double.max(this.screenWidth * 650 / 900 - 10, this.finalTime * this.hScale * 20 + 10));
            ganttChart.getChildren().clear();
            setGrid(ganttChart);
            for (Visualisable v : visualisables[0]) {
                map(ganttChart, v);
            }
        });
        // Defining a Dynamic Layout...
        result.widthProperty().addListener((observableValue, number, t1) -> {
            this.screenWidth = t1.doubleValue();
            ganttChart.setPrefHeight(Double.max(this.screenHeight - 10, (this.vScale * 30 + 5) * scheduler.numOfProcesses + 5));
            ganttChart.setPrefWidth(Double.max(this.screenWidth * 650 / 900 - 10, this.finalTime * this.hScale * 20 + 10));
            ganttChart.getChildren().clear();
            setGrid(ganttChart);
            for (Visualisable v : visualisables[0]) {
                map(ganttChart, v);
            }
        });
        result.heightProperty().addListener((observableValue, number, t1) -> {
            this.screenHeight = t1.doubleValue();
            ganttChart.setPrefHeight(Double.max(this.screenHeight - 10, (this.vScale * 30 + 5) * scheduler.numOfProcesses + 5));
            ganttChart.setPrefWidth(Double.max(this.screenWidth * 650 / 900 - 10, this.finalTime * this.hScale * 20 + 10));
            ganttChart.getChildren().clear();
            setGrid(ganttChart);
            for (Visualisable v : visualisables[0]) {
                map(ganttChart, v);
            }
        });
    }

    private void setGrid(Pane ganttChart) {
        double maxHeight = 0;
        double l = Math.ceil(this.beginTime);
        double r = Double.max(Math.floor(this.finalTime), Math.floor((this.screenWidth * 650 / 900 - 10) / (20 * this.hScale)));
        while (Double.compare(l, r) <= 0) {
            Line line = new Line(l * 20 * this.hScale + 5, 0, l * 20 * this.hScale + 5, this.screenHeight + 20);
            line.setStrokeWidth(0.25);
            ganttChart.getChildren().add(line);
            l++;
        }
        int y = 0;
        while (Double.compare((this.vScale * 30 + 5) * y + 2.5, this.screenHeight - 10) <= 0) {
            Line line = new Line(0, (this.vScale * 30 + 5) * y + 2.5, r * 20 * this.hScale + 20, (this.vScale * 30 + 5) * y + 2.5);
            line.setStrokeWidth(0.1);
            ganttChart.getChildren().add(line);
            y++;
        }
    }

    private void map(Pane ganttChart, Visualisable v) {
        // Define the Colors...
        String[] colors = {"#FFAEBC", "#A0E7E5", "#B4F8C8", "#FBE7C6", "#EFF1DB", "#FFD4DB", "#BBE7FE", "#D3B5E5"};
        // Define the interval boundaries...
        double l = v.getStartTime();
        double r = v.getFinishTime();
        // The Y coordinates of the process...
        int y = scheduler.getIndexOfProcess(v.getProcessID());
        // Drawing the rectangle which will represent the interval...
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(this.vScale * 30);
        rectangle.setWidth((r - l) * this.hScale * 20);
        rectangle.setLayoutX(l * this.hScale * 20 + 5);
        rectangle.setLayoutY((this.vScale * 30 + 5) * y + 5);
        rectangle.setArcHeight(15);
        rectangle.setArcWidth(15);
        rectangle.setFill(Color.web(colors[(int) ((v.getProcessID() + colors.length) % colors.length)]));
        // Adding some dropshadow...
        DropShadow dropShadow = new DropShadow();
        dropShadow.setHeight(1.5);
        dropShadow.setWidth(1.5);
        dropShadow.setRadius(5);
        rectangle.setEffect(dropShadow);
        // Adding a tooltip contains the information of the interval...
        Tooltip tooltip = new Tooltip("PID: " + v.getProcessID() + "\nStarting Time: " + v.getStartTime() + "\nFinish Time: " + v.getFinishTime());
        tooltip.setShowDelay(Duration.millis(30.0));
        rectangle.setOnMouseMoved(mouseEvent -> {
            tooltip.setX(mouseEvent.getScreenX());
            tooltip.setY(mouseEvent.getScreenY());
        });
        Tooltip.install(rectangle, tooltip);
        // Adding all to the Gantt Chart...
        ganttChart.getChildren().add(rectangle);
    }
}
