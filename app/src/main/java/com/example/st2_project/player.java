package com.example.st2_project;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class player extends AppCompatActivity {

    private TextView textSongTitle, textSongArtist, tvTime, tvDuration;
    private SeekBar seekBarTime;
    private Button btnPlay, btnNext, btnPrevious;

    private MediaPlayer mediaPlayer;
    private ArrayList<String> playlist;
    private int currentIndex = 0;

    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        // Bind views
        textSongTitle = findViewById(R.id.textSongTitle);
        textSongArtist = findViewById(R.id.textSongArtist);
        tvTime = findViewById(R.id.tvTime);
        tvDuration = findViewById(R.id.tvDuration);
        seekBarTime = findViewById(R.id.seekBarTime);
        btnPlay = findViewById(R.id.btnPlay);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);

        // Get playlist and index
        Intent intent = getIntent();
        playlist = intent.getStringArrayListExtra("playlist");
        currentIndex = intent.getIntExtra("index", 0);

        playSong(currentIndex);

        // Play / Pause button
        btnPlay.setOnClickListener(v -> {
            if (isPlaying) {
                mediaPlayer.pause();
                btnPlay.setBackgroundResource(R.drawable.ic_play);
            } else {
                mediaPlayer.start();
                btnPlay.setBackgroundResource(R.drawable.ic_pause);
            }
            isPlaying = !isPlaying;
        });

        // Next song
        btnNext.setOnClickListener(v -> {
            if (currentIndex < playlist.size() - 1) {
                currentIndex++;
                playSong(currentIndex);
            }
        });

        // Previous song
        btnPrevious.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                playSong(currentIndex);
            }
        });
    }

    private void playSong(int index) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        String path = playlist.get(index);

        try {
            if (path.startsWith("android.resource://")) {
                // Handle raw resource songs
                Uri uri = Uri.parse(path);
                mediaPlayer = MediaPlayer.create(this, uri);
            } else {
                // Handle normal file paths
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
            }

            mediaPlayer.start();
            isPlaying = true;
            btnPlay.setBackgroundResource(R.drawable.ic_pause);

            // Update song metadata
            if (!path.startsWith("android.resource://")) {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(path);

                String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

                if (title == null) title = "Unknown Title";
                if (artist == null) artist = "Unknown Artist";

                textSongTitle.setText(title);
                textSongArtist.setText(artist);

                retriever.release();
            } else {
                textSongTitle.setText("Local Song");
                textSongArtist.setText("Unknown Artist");
            }

            // Update duration
            int duration = mediaPlayer.getDuration();
            tvDuration.setText(formatTime(duration));

            // Seekbar update
            seekBarTime.setMax(duration);
            new Thread(() -> {
                while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    runOnUiThread(() -> {
                        int currentPos = mediaPlayer.getCurrentPosition();
                        seekBarTime.setProgress(currentPos);
                        tvTime.setText(formatTime(currentPos));
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatTime(int millis) {
        int minutes = (millis / 1000) / 60;
        int seconds = (millis / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}