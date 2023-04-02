package clia;

import clia.front.scenes.Scenes;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.net.URL;

public class test extends Application {

    @Override
    public void start(Stage stage) {
        URL url = App.class.getResource("/video/jellyfish jam.mp4" );
        Media media = new Media(url.toString());

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        System.out.println("Media loaded");
        MediaView mediaView = new MediaView(mediaPlayer);
        System.out.println("MediaView created");

        StackPane mainPane = new StackPane();
        mainPane.getChildren().add(mediaView);
        System.out.println("StackPane created");

        mediaPlayer.play();
        System.out.println("Media playing");

        Scene scene = new Scene(mainPane);

        stage.setTitle("CLIA");
        stage.setResizable(false);
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
