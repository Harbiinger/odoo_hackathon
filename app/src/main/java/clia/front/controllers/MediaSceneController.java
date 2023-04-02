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
    private MediaView mediaView;


    @FXML
    void initialize() {
        URL url = App.class.getResource("/misc/jellyfish jam.mp4" );
        Media media = new Media(url.toString());

        MediaPlayer mediaPlayer = new MediaPlayer(media);

        mediaView = new MediaView(mediaPlayer);

        mainPane = new StackPane();
        mainPane.getChildren().add(mediaView);

        mediaPlayer.play();

        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            System.out.println("Media ended");
            // TODO : set end scene here
        });
    }


}