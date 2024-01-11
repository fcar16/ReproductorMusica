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

    public void play() {
        if (!playing) {
            playing = true;
            playerThread = new Thread(() -> playSong(playlist[currentSong]));
            playerThread.start();
        } else if (paused) {
            resume();
        }
    }

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

    public void pause() {
        if (playing && clip != null && clip.isRunning()) {
            currentPosition = clip.getMicrosecondPosition();
            clip.stop();
            paused = true;
            // Logs
            System.out.println("Paused at: " + currentPosition);
        }
    }

    public void resume() {
        if (playing && paused) {
            playSong(playlist[currentSong], currentPosition);
            paused = false;
            // Logs
            System.out.println("Resumed from: " + currentPosition);
        }
    }

    public void nextSong() {
        stop();
        currentSong = (currentSong + 1) % playlist.length;
        play();
    }

    private void playSong(String filePath) {
        playSong(filePath, 0);
    }

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

    public String[] getPlaylist() {
        return playlist;
    }

    public int getCurrentSong() {
        return currentSong;
    }
}
