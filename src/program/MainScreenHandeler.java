package program;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import schedulers.Scheduler;
import schedulers.algorithms.*;

import static program.Main.*;

public class MainScreenHandeler {

    private static boolean isPreemptive;

    public static void handelMainScreenActions(Pane mainScreen) {

        Button firstComeFirstServed = (Button) getElementById(mainScreen, "first_come_first_served");
        Button shortestJobFirst = (Button) getElementById(mainScreen, "shortest_job_first");
        Button shortestRemainingTimeFirst = (Button) getElementById(mainScreen, "shortest_remaining_time_first");
        Button explicitPriorityWithPreemption = (Button) getElementById(mainScreen, "explicit_priority_with_preemption");
        Button explicitPriorityWithoutPreemption = (Button) getElementById(mainScreen, "explicit_priority_without_preemption");
        Button roundRobin = (Button) getElementById(mainScreen, "round_robin");
        Button multiprogrammingWithUniformIoPercentage = (Button) getElementById(mainScreen, "multiprogramming_with_uniform_io_percentage");
        TextField timeQuantum = (TextField) getElementById(mainScreen, "time_quantum");
        TextField ageFactor = (TextField) getElementById(mainScreen, "age_factor");
        CheckBox autoTimeQuantum = (CheckBox) getElementById(mainScreen, "auto_time_quantum");
        CheckBox autoAgeFactor = (CheckBox) getElementById(mainScreen, "auto_age_factor");
        Spinner IO = (Spinner) getElementById(mainScreen, "io_percentage");
        IO.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, 50.0, 0.1));
        Button exit = (Button) getElementById(mainScreen, "exit");
        exit.setOnAction(actionEvent -> {
            System.exit(0);
        });

        timeQuantum.setDisable(true);
        autoTimeQuantum.setDisable(true);
        ageFactor.setDisable(true);
        autoAgeFactor.setDisable(true);
        IO.setDisable(true);

        firstComeFirstServed.setOnAction(
                event -> {
                    Scheduler firstComeFirstServedScheduler = new FirstComeFirstServedScheduler(processes);
                    Result result = new Result(firstComeFirstServedScheduler);
                    try {
                        result.show();
                    } catch (Exception e) {
                        (new Alert(Alert.AlertType.ERROR, e.getMessage())).show();
                    }
                }
        );
        shortestJobFirst.setOnAction(
                event -> {
                    Scheduler shortestJobFirstScheduler = new ShortestJobFirstScheduler(processes);
                    Result result = new Result(shortestJobFirstScheduler);
                    try {
                        result.show();
                    } catch (Exception e) {
                        (new Alert(Alert.AlertType.ERROR, e.getMessage())).show();
                    }
                }
        );
        shortestRemainingTimeFirst.setOnAction(
                event -> {
                    Scheduler shortestRemainingTimeFirstScheduler = new ShortestRemainingTimeFirstScheduler(processes);
                    Result result = new Result(shortestRemainingTimeFirstScheduler);
                    try {
                        result.show();
                    } catch (Exception e) {
                        (new Alert(Alert.AlertType.ERROR, e.getMessage())).show();
                    }
                }
        );
        explicitPriorityWithPreemption.setOnAction(
                event -> {
                    MainScreenHandeler.isPreemptive = true;
                    setAutoOrUserEntryForPriorityScheduler(autoAgeFactor, ageFactor);
                }
        );
        explicitPriorityWithoutPreemption.setOnAction(
                event -> {
                    MainScreenHandeler.isPreemptive = false;
                    setAutoOrUserEntryForPriorityScheduler(autoAgeFactor, ageFactor);
                }
        );
        roundRobin.setOnAction(
                event -> {
                    if (autoTimeQuantum != null) {
                        setAutoOrUserEntryForRoundRobinScheduler(autoTimeQuantum, timeQuantum);
                    }
                }
        );
        multiprogrammingWithUniformIoPercentage.setOnAction(
                event -> {
                    IO.setDisable(false);
                }
        );
        IO.getEditor().textProperty().addListener((observableValue, s, t1) -> {
            try {
                Double.parseDouble(t1);
            } catch (Exception e) {
                if (t1.compareTo("") == 0)
                    IO.getEditor().setText("0");
                else
                    IO.getEditor().setText(s);
            }
        });
        IO.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().compareTo(KeyCode.ENTER) == 0) {
                try {
                    double IOPercent = Double.parseDouble(IO.getEditor().getText());
                    Scheduler multiprogrammedWithUniformIOPercentageScheduler = new MultiprogrammedWithUniformIOPercentage(processes, IOPercent);
                    Result result = new Result(multiprogrammedWithUniformIOPercentageScheduler);
                    result.show();
                    IO.setDisable(true);
                } catch (Exception e) {
                    (new Alert(Alert.AlertType.ERROR, e.getMessage())).show();
                }
            }
        });


    }

    public static void setAutoOrUserEntryForRoundRobinScheduler(CheckBox auto, TextField userEnrty) {
        auto.setDisable(false);
        userEnrty.setDisable(false);
        auto.setOnAction(
                event1 -> {
                    userEnrty.setDisable(true);
                    Scheduler roundRobinScheduler = new RoundRobinScheduler(processes, findTimeQuantum(processes));
                    Result result = new Result(roundRobinScheduler);
                    try {
                        result.show();
                    } catch (Exception e) {
                        (new Alert(Alert.AlertType.ERROR, e.getMessage())).show();
                    } finally {
                        auto.setSelected(false);
                        auto.setDisable(true);
                    }
                }
        );

        userEnrty.setOnAction( // enter
                event2 -> {
                    auto.setDisable(true);
                    Scheduler roundRobinScheduler = new RoundRobinScheduler(processes, Double.parseDouble(userEnrty.getText()));
                    Result result = new Result(roundRobinScheduler);
                    try {
                        result.show();
                    } catch (Exception e) {
                        (new Alert(Alert.AlertType.ERROR, e.getMessage())).show();
                    } finally {
                        userEnrty.clear();
                        userEnrty.setDisable(true);
                    }
                }
        );
    }

    public static void setAutoOrUserEntryForPriorityScheduler(CheckBox auto, TextField userEnrty) {
        auto.setDisable(false);
        userEnrty.setDisable(false);
        auto.setOnAction(
                event1 -> {
                    userEnrty.setDisable(true);
                    Scheduler scheduler;
                    if (MainScreenHandeler.isPreemptive)
                        scheduler = new PreemptiveExplicitPriorityScheduler(processes, findAgeFactor(processes));
                    else
                        scheduler = new NonPreemptiveExplicitPriorityScheduler(processes, findAgeFactor(processes));
                    Result result = new Result(scheduler);
                    try {
                        result.show();
                    } catch (Exception e) {
                        (new Alert(Alert.AlertType.ERROR, e.getMessage())).show();
                    } finally {
                        auto.setSelected(false);
                        auto.setDisable(true);
                    }
                }
        );

        userEnrty.setOnAction( // enter
                event2 -> {
                    auto.setDisable(true);
                    Scheduler scheduler;
                    if (MainScreenHandeler.isPreemptive)
                        scheduler = new PreemptiveExplicitPriorityScheduler(processes, Long.parseLong(userEnrty.getText()));
                    else
                        scheduler = new NonPreemptiveExplicitPriorityScheduler(processes, Long.parseLong(userEnrty.getText()));
                    Result result = new Result(scheduler);
                    try {
                        result.show();
                    } catch (Exception e) {
                        (new Alert(Alert.AlertType.ERROR, e.getMessage())).show();
                    } finally {
                        userEnrty.clear();
                        userEnrty.setDisable(true);
                    }
                }
        );
    }

}
