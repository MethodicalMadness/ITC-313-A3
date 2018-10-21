package task2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    static private LogWindow logWindow = new LogWindow();
    static private TrafficLight trafficLight1 = new TrafficLight("traffic light 1", logWindow);
    static private TrafficLight trafficLight2 = new TrafficLight("traffic light 2", logWindow);
    static private TrafficLight trafficLight3 = new TrafficLight("traffic light 3", logWindow);

    /**
     * calls all the GUIs and starts the threads
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        //show the GUIs
        trafficLight1.showTrafficLight();
        trafficLight2.showTrafficLight();
        trafficLight3.showTrafficLight();
        logWindow.showLogWindow();

        //create the threads that manage the traffic lights
        trafficController(trafficLight1);
        trafficController(trafficLight2);
        trafficController(trafficLight3);
    }

    /**
     * creates a thread to manage a single TrafficLight
     * @param trafficLight
     */
    private void trafficController(TrafficLight trafficLight){
        new Thread(() -> {
            try{
                while(true){
                    Platform.runLater(trafficLight);
                    Thread.sleep(trafficLight.getDuration() * 1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * provided for IDEs that require it.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
