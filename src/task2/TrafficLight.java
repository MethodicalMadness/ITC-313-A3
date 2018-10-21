package task2;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class TrafficLight implements Runnable{

    private double paneWidth = 500;
    private double paneHeight = 500;
    private int initialSecs = 3;
    private int redSec = 3;
    private int yellowSec = 3;
    private int greenSec = 3;
    private int duration = 3;
    public enum Status {RED, YELLOW, GREEN, STOPPED, STARTING}
    private Status status = Status.STOPPED;
    private Circle circle;
    private Circle circle1;
    private Circle circle2;
    private String name;
    private LogWindow logWindow;

    /**
     * Constructor.
     * @param name
     * @param logWindow
     */
    public TrafficLight(String name, LogWindow logWindow) {
        this.name = name;
        this.logWindow = logWindow;
    }

    /**
     * is called by the TrafficController threads using Platform.runLater().
     * calls colour change switching methods and sets duration based on prev status.
     */
    @Override
    public void run() {
        if (status == Status.STOPPED) {
            duration = 1;
        } else if (status == Status.STARTING){
            switchStarting();
            duration = redSec;
            switchRed();
        }else if(status == Status.RED){
            duration = greenSec;
            switchGreen();
        }else if(status == Status.GREEN){
            duration = yellowSec;
            switchYellow();
        }else if(status == Status.YELLOW){
            duration = redSec;
            switchRed();
        }
    }

    /**
     * getter for duration in seconds represented as int
     * @return int duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * checks if string is numeric
     * @param str
     */
    public static Boolean isNumeric(String str){
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    /**
     * creates and shows GUI for traffic lights
     */
    public void showTrafficLight(){

        Stage primaryStage = new Stage();

        //setup our background
        StackPane stackPane = new StackPane();
        Rectangle rectangle = new Rectangle(125, 250, 125, 300);
        stackPane.getChildren().add(rectangle);
        rectangle.setFill(Color.BLACK);
        rectangle.setStroke(Color.BLACK);

        //grid for the lights
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(5, 5, 5, 5));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        stackPane.getChildren().add(gridPane);

        //lights
        circle = new Circle(paneWidth / 2, 60, 40);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.GREY);
        gridPane.add(circle, 1, 1);

        circle1 = new Circle(paneWidth / 2, 60, 40);
        circle1.setStroke(Color.BLACK);
        circle1.setFill(Color.GREY);
        gridPane.add(circle1, 1, 2);

        circle2 = new Circle(paneWidth / 2, 60, 40);
        circle2.setStroke(Color.BLACK);
        circle2.setFill(Color.GREY);
        gridPane.add(circle2, 1, 3);

        //buttons
        Button btStart = new Button("Start");
        Button btStop = new Button("Stop");

        //text field and labels
        TextField txRed = new TextField();
        txRed.setText(String.valueOf(initialSecs));
        TextField txYellow = new TextField();
        txYellow.setText(String.valueOf(initialSecs));
        TextField txGreen = new TextField();
        txGreen.setText(String.valueOf(initialSecs));
        Label lbRed = new Label("red Duration: ");
        Label lbYellow = new Label("yellow Duration: ");
        Label lbGreen = new Label("green Duration: ");

        //grid for text, labels, and buttons
        GridPane gridpane2 = new GridPane();
        gridpane2.setPadding(new Insets(5, 5, 5, 5));
        gridpane2.setHgap(5);
        gridpane2.setVgap(5);
        gridpane2.add(lbRed, 1, 1);
        gridpane2.add(lbYellow, 1, 2);
        gridpane2.add(lbGreen, 1, 3);
        gridpane2.add(txRed, 2, 1);
        gridpane2.add(txYellow, 2, 2);
        gridpane2.add(txGreen, 2, 3);
        gridpane2.add(new Label("sec"), 3, 1);
        gridpane2.add(new Label("sec"), 3, 2);
        gridpane2.add(new Label("sec"), 3, 3);
        gridpane2.add(btStart, 4, 1);
        gridpane2.add(btStop, 5, 1);

        //layout
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(stackPane);
        borderPane.setBottom(gridpane2);

        //create scene and show
        Scene scene = new Scene(borderPane, paneWidth, paneHeight);
        primaryStage.setTitle(name);
        primaryStage.setScene(scene);
        primaryStage.show();

        //button actions
        btStart.setOnAction(e -> {
            if (isNumeric(txRed.getText())) {
                redSec = Integer.valueOf(txRed.getText());
            } else {
                txRed.setText(String.valueOf(redSec));
            }
            if (isNumeric(txYellow.getText())) {
                yellowSec = Integer.valueOf(txYellow.getText());
            } else {
                txYellow.setText(String.valueOf(yellowSec));
            }
            if (isNumeric(txGreen.getText())) {
                greenSec = Integer.valueOf(txGreen.getText());
            } else {
                txGreen.setText(String.valueOf(greenSec));
            }
            status = Status.STARTING;
        });

        btStop.setOnAction(e -> {
            switchIdle();
        });
    }

    /**
     * changes circles colours so that traffic light displays RED
     */
    private void switchRed(){
        circle.setFill(Color.RED);
        circle1.setFill(Color.GREY);
        circle2.setFill(Color.GREY);
        status = Status.RED;
        updateLog();
    }

    /**
     * changes circles colours so that traffic light displays YELLOW
     */
    private void switchYellow(){
        circle.setFill(Color.GREY);
        circle1.setFill(Color.YELLOW);
        circle2.setFill(Color.GREY);
        status = Status.YELLOW;
        updateLog();
    }

    /**
     * changes circles colours so that traffic light displays GREEN
     */
    private void switchGreen(){
        circle.setFill(Color.GREY);
        circle1.setFill(Color.GREY);
        circle2.setFill(Color.GREEN);
        status = Status.GREEN;
        updateLog();
    }

    /**
     * changes circles colours so that traffic light displays GREY
     */
    private void switchIdle(){
        circle.setFill(Color.GREY);
        circle1.setFill(Color.GREY);
        circle2.setFill(Color.GREY);
        status = Status.STOPPED;
        updateLog();
    }

    /**
     * Starting status ensures durations are properly set in advance
     */
    private void  switchStarting(){
        status = Status.STARTING;
        updateLog();

    }

    /**
     * updates the log window, is called whenever a status change occurs
     */
    private void updateLog(){
        String newline = System.lineSeparator();
        if (status == Status.STOPPED) {
            logWindow.setText(name + " has " + status + newline);
            Platform.runLater(logWindow);
        } else if (status == Status.STARTING) {
            logWindow.setText(name + " is " + status + newline);
            Platform.runLater(logWindow);
        } else {
            logWindow.setText(name + " is " + status + " for " + duration + " seconds" + newline);
            Platform.runLater(logWindow);
        }
    }
}
