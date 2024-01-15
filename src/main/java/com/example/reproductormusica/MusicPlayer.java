package com.example.reproductormusica;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private String[] playlist;
    private int currentSong;
    private boolean playing;
    private boolean paused;
    private Thread playerThread;
    private Clip clip;
    private long currentPosition;

    public MusicPlayer(String[] playlist) {
        this.playlist = playlist;
        this.currentSong = 0;
        this.playing = false;
        this.paused = false;
        this.playerThread = null;
        this.clip = null;
        this.currentPosition = 0;
    }

    // Método para iniciar la reproducción de la lista de reproducción
    public Thread play() {
        if (!playing) {
            playing = true;
            playerThread = new Thread(() -> playSong(playlist[currentSong]));
            playerThread.start();

            return playerThread;
        } else if (paused) {
            resume();
            return playerThread;
        }
        return null; // En caso de que ya esté reproduciendo y no esté pausado
    }

    // Método para detener la reproducción
    public void stop() {
        if (playing) {
            playing = false;
            paused = false;
            if (playerThread != null) {
                playerThread.interrupt();
            }
            if (clip != null) {
                clip.stop();
                clip.close();
                currentPosition = 0;  // Reinicia la posición al detener
            }
            // Logs
            System.out.println("Stopped at: " + currentPosition);
        }
    }

    // Método para pausar la reproducción
    public void pause() {
        if (playing && clip != null && clip.isRunning()) {
            currentPosition = clip.getMicrosecondPosition();
            clip.stop();
            paused = true;
            // Logs
            System.out.println("Paused at: " + currentPosition);
        }
    }

    // Método para reanudar la reproducción desde la posición de pausa
    public void resume() {
        if (playing && paused) {
            playSong(playlist[currentSong], currentPosition);
            paused = false;
            // Logs
            System.out.println("Resumed from: " + currentPosition);
        }
    }

    // Método para pasar a la siguiente canción
    public void nextSong() {
        stop(); // Detiene la reproducción actual

        // Espera a que el hilo actual termine antes de comenzar el siguiente
        try {
            if (playerThread != null) {
                playerThread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Inicia la reproducción de la siguiente canción
        currentSong = (currentSong + 1) % playlist.length;
        play();
    }

    // Método para reproducir una canción desde el archivo especificado
    private void playSong(String filePath) {
        playSong(filePath, 0);
    }

    // Método para reproducir una canción desde una posición específica
    private void playSong(String filePath, long startPosition) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Establece la posición actual de reproducción antes de la pausa
            clip.setMicrosecondPosition(startPosition);

            clip.start();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    if (playing && !paused) {
                        nextSong();
                    }
                }
            });

            // Bucle mientras está reproduciendo y la canción está en ejecución
            while (playing && clip.isRunning()) {
                try {
                    Thread.sleep(100);
                    currentPosition = clip.getMicrosecondPosition();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener la lista de reproducción
    public String[] getPlaylist() {
        return playlist;
    }

    // Método para obtener la posición de la canción actual en la lista de reproducción
    public int getCurrentSong() {
        return currentSong;
    }

    // Método para obtener la posición de reproducción actual
    public long getCurrentPosition() {
        return currentPosition;
    }

    // Método para verificar si la música se está reproduciendo
    public boolean isPlaying() {
        if (clip != null) {
            return clip.isRunning() && playing && !paused;
        }
        return false;
    }

    // Método para obtener la duración de la canción actual
    public long getSongDuration() {
        if (clip != null) {
            return clip.getMicrosecondLength();
        }
        return 0;
    }

    // Método principal para probar el reproductor de música
    public static void main(String[] args) {
        String[] playlist = {"src" + File.separator + "Music" + File.separator + "Violines.wav", "src" + File.separator + "Music" + File.separator + "Purple Widow.wav"};

        MusicPlayer player = new MusicPlayer(playlist);

        try {
            player.play();
            Thread.sleep(5000);
            player.pause();
            Thread.sleep(3000);
            player.resume();
            Thread.sleep(5000);
            player.nextSong();
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            player.stop();
        }
    }
}
