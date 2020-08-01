package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public Stage stage;
    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("randomStudent.fxml"));
        stage.setTitle("Student Selector");
        stage.setScene(new Scene(root, 630, 390));
        stage.setResizable(false);
        stage.getIcons().add(new Image("sample/icon.png"));
        stage.show();
        AnimationTimer quit = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(Controller.quit)
                    stage.close();
            }
        };
        quit.start();
    }
    public static void main(String[] args) {
        launch(args);
    }
}