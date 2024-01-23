package com.example.reproductormusica;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.io.File;

public class MusicPlayerController {
    @FXML
    private Label currentSongLabel;
    @FXML
    private ProgressBar progressBar;

    private PauseTransition pauseTransition;

    private MusicPlayer musicPlayer;

    // Método llamado para establecer el MusicPlayer en el controlador
    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
        updateCurrentSongLabel();  // Actualiza la etiqueta con la canción actual
        setupProgressBar();  // Configura la barra de progreso
        this.pauseTransition = new PauseTransition(Duration.millis(1000));
    }

    // Metodos para controlar la reproducción en el controllador
    @FXML
    private void play() {
        if (musicPlayer != null) {
            musicPlayer.play();

            // Hilo separado para actualizar la barra de progreso mientras se reproduce la canción
            new Thread(() -> {
                while (musicPlayer.isPlaying()) {
                    double progress = (double) musicPlayer.getCurrentPosition() / musicPlayer.getSongDuration();
                    progressBar.setProgress(progress);

                    try {
                        Thread.sleep(500); // Actualiza cada 500 milisegundos (0.5 segundo)
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }
    }

    //Metodo para detener la reproducción desde el controllador
    @FXML
    private void stop() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            resetProgressBar();  // Resetea la barra de progreso al detener la reproducción
        }
    }

    //Metodo para pausar la reproducción desde el controllador
    @FXML
    private void pause() {
        if (musicPlayer != null) {
            musicPlayer.pause();
            updateProgressBar();  // Actualiza la barra de progreso cuando se pausa
        }
    }

    //Metodo para reanudar la reproducción desde el controllador
    @FXML
    private void resume() {
        if (musicPlayer != null) {
            musicPlayer.resume();
            updateProgressBar();  // Actualiza la barra de progreso cuando se reanuda
        }
    }

    //Metodo para pasar a la siguiente cancion desde el controllador
    @FXML
    private void nextSong() {
        if (musicPlayer != null) {
            musicPlayer.nextSong();
            Platform.runLater(this::updateCurrentSongLabel);  // Actualiza la etiqueta de la canción actual en la interfaz de usuario
            resetProgressBar();  // Resetea la barra de progreso al cambiar de canción
        }
    }

    // Método para actualizar la etiqueta con el nombre de la canción actual
    private void updateCurrentSongLabel() {
        if (musicPlayer != null) {
            int currentSongIndex = musicPlayer.getCurrentSong();
            String currentSongName = getSongName(currentSongIndex);
            Platform.runLater(() -> currentSongLabel.setText( currentSongName));
        }
    }

    // Método para obtener el nombre de la canción a partir del índice
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

    // Método para actualizar la barra de progreso
    private void updateProgressBar() {
        if (musicPlayer != null) {
            long duration = musicPlayer.getSongDuration();
            if (duration > 0) {
                double progress = (double) musicPlayer.getCurrentPosition() / duration;
                progressBar.setProgress(progress);
            }
        }
    }

    // Método para resetear la barra de progreso
    private void resetProgressBar() {
        if (progressBar != null) {
            progressBar.setProgress(0.0);
        }
    }

    // Método para configurar la barra de progreso
    private void setupProgressBar() {
        resetProgressBar();
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.prefWidthProperty().bind(currentSongLabel.widthProperty());

        // Crea un hilo separado para actualizar la barra de progreso cada segundo
        Thread progressBarThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500); // Espera 0.5 segundos antes de actualizar la barra de progreso
                    updateProgressBar();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        progressBarThread.setDaemon(true); // Hace que el hilo sea un hilo daemon, que se detendrá cuando todos los hilos no daemon finalicen
        progressBarThread.start();
    }
}
