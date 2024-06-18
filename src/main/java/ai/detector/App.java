package ai.detector;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) throws Exception {
        System.out.println("Starting application...");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("app.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("AI Detector Mark 3.0");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
