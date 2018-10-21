package task2;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

public class LogWindow implements Runnable {

    private double paneWidth = 500;
    private double paneHeight = 500;
    private String name = "Traffic Light Log Window";
    private String text = "";
    private TextArea textArea = new TextArea("");


    /**
     * called by TrafficLights using Platform.runLater() to update textArea
     */
    @Override
    public void run() {
        textArea.setText(text);
    }

    /**
     * appends the text to be updated when the thread is run
     * @param text
     */
    public void setText(String text) {
        this.text = text + this.text;
    }

    /**
     * constructs and displays the LogWindow GUI
     */
    public void showLogWindow(){
        Stage primaryStage = new Stage();

        //setup text area
        textArea.setPrefColumnCount(39);
        textArea.setPrefRowCount(29);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-text-fill: black");
        textArea.setFont(Font.font("Times", 12));
        textArea.setPadding(new Insets(5, 5, 5, 5));
        textArea.setEditable(false);

        //layout
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(textArea);

        //create scene and show
        Scene scene = new Scene(borderPane, paneWidth, paneHeight);
        primaryStage.setTitle(name);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
