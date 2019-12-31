package E1;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.awt.*;

public class E3 extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
            Parent root= FXMLLoader.load(getClass().getClassLoader().getResource("e13.fxml"));
            primaryStage.setTitle("Liste");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
