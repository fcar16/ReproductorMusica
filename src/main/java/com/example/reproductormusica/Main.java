package com.example.reproductormusica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("music_player.fxml"));
        Parent root = loader.load();

        MusicPlayerController controller = loader.getController();
        MusicPlayer musicPlayer = new MusicPlayer(new String[]{"src" + File.separator + "Music" + File.separator + "Violines.wav","src" + File.separator + "Music" + File.separator + "Thalassophobia.wav", "src" + File.separator + "Music" + File.separator + "Purple Widow.wav"});
        controller.setMusicPlayer(musicPlayer);

        primaryStage.setTitle("Music Player");
        primaryStage.setScene(new Scene(root, 300, 200));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
