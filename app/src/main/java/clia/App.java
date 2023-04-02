package clia;
import clia.front.navigation.Flow;
import clia.front.scenes.SceneLoader;
import clia.front.scenes.Scenes;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage_) {
        stage = stage_;

        Scenes.MainScene = SceneLoader.load("MainScene");

        Flow.add(Scenes.MainScene);

        stage.setTitle("CLIA");
        // stage.setResizable(false);
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.setScene(Scenes.MainScene);
        stage.setFullScreen(false);
        stage.show();
    }

    public static void setScene(Scene scene) {
        stage.setScene(scene);
    }

}
