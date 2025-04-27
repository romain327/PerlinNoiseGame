package com.exemple.game;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.nio.file.Paths;

public class BackgroundMusic {

    private final MediaPlayer mediaPlayer;

    public BackgroundMusic(String filePath) {
        Media media = new Media(Paths.get(filePath).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
    }

    public void play() {
        mediaPlayer.play();
    }

    public void stop() {
        mediaPlayer.stop();
    }
}

