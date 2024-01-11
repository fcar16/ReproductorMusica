package com.example.reproductormusica;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.File;

public class MusicPlayerController {
    @FXML
    private Label currentSongLabel;

    private MusicPlayer musicPlayer;

    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
        updateCurrentSongLabel();
    }

    @FXML
    private void play() {
        if (musicPlayer != null) {
            musicPlayer.play();
            Platform.runLater(this::updateCurrentSongLabel);
        }
    }

    @FXML
    private void stop() {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }
    }

    @FXML
    private void pause() {
        if (musicPlayer != null) {
            musicPlayer.pause();
        }
    }

    @FXML
    private void resume() {
        if (musicPlayer != null) {
            musicPlayer.resume();
        }
    }

    @FXML
    private void nextSong() {
        if (musicPlayer != null) {
            musicPlayer.nextSong();
            Platform.runLater(this::updateCurrentSongLabel);
        }
    }

    private void updateCurrentSongLabel() {
        if (musicPlayer != null) {
            int currentSongIndex = musicPlayer.getCurrentSong();
            String currentSongName = getSongName(currentSongIndex);
            Platform.runLater(() -> currentSongLabel.setText("Current Song: " + currentSongName));
        }
    }

    private String getSongName(int songIndex) {
        if (musicPlayer != null && songIndex >= 0 && songIndex < musicPlayer.getPlaylist().length) {
            String filePath = musicPlayer.getPlaylist()[songIndex];
            String fileName = new File(filePath).getName();
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                return fileName.substring(0, dotIndex);
            } else {
                return fileName;
            }
        } else {
            return "Unknown Song";
        }
    }
}
