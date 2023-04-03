package clia.front.controllers;

import clia.App;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URL;

public class MediaSceneController extends Controller {
    @FXML
    private StackPane mainPane;

    @FXML
    void initialize() {
        URL url = App.class.getResource("/video/jellyfish jam.mp4" );
        Media media = new Media(url.toString());

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        System.out.println("Media loaded");
        MediaView mediaView = new MediaView(mediaPlayer);
        System.out.println("MediaView created");

        mainPane = new StackPane();
        mainPane.getChildren().add(mediaView);
        System.out.println("StackPane created");

        mediaPlayer.play();
        System.out.println("Media playing");

        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            System.out.println("Media ended");
            // TODO : set end scene here
        });
    }
}