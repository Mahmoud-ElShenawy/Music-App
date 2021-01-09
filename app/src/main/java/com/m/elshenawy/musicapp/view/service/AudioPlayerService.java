package com.m.elshenawy.musicapp.view.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class AudioPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    public static final String EXTRA_IS_PLAYER = "is_player";
    public static final String EXTRA_CURRENT_TRACK_POSITION = "current_track_position";
    public static final String EXTRA_TRACK_PREVIEW_URL = "track_preview_url";

    private PlayerBinder mediaPlayerBinder = null;
    private Handler mediaPlayerHandler;
    private Timer timer;
    private int currentTrackPosition;
    private boolean isPlayerPaused;
    private MediaPlayer mediaPlayer = null;
    private String trackPreviewUrl;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayerBinder = new PlayerBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null
                && intent.hasExtra(EXTRA_TRACK_PREVIEW_URL)
                && intent.getStringExtra(EXTRA_TRACK_PREVIEW_URL) != null) {
            setTrackPreviewUrl(intent.getStringExtra(EXTRA_TRACK_PREVIEW_URL));
            onPlayAudio(0);
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mediaPlayerBinder;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        if (currentTrackPosition != 0) {
            mp.seekTo(currentTrackPosition * 1000);
        }
        updateUI();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Message completionMessage = new Message();
        Bundle completionBundle = new Bundle();
        completionBundle.putBoolean(EXTRA_IS_PLAYER, false);
        completionMessage.setData(completionBundle);
        if (mediaPlayerHandler != null) {
            mediaPlayerHandler.sendMessage(completionMessage);
        }
        noUpdateUI();
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public void setTrackPreviewUrl(String trackPreviewUrl) {
        this.trackPreviewUrl = trackPreviewUrl;
    }

    public void onPlayAudio(int trackPosition) {
        currentTrackPosition = trackPosition;
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
        }
        setUpAudioPlayer();
        isPlayerPaused = false;
    }

    public int onPauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlayerPaused = true;
            noUpdateUI();
            return mediaPlayer.getDuration() / 1000;
        } else {
            return 0;
        }
    }

    public void setUpAudioPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(trackPreviewUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnCompletionListener(AudioPlayerService.this);
            mediaPlayer.setOnPreparedListener(AudioPlayerService.this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnErrorListener(AudioPlayerService.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            noUpdateUI();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mediaPlayerHandler != null) {
            mediaPlayerHandler = null;
        }
    }

    public void updateUI() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendCurrentTrackPosition();
            }
        }, 0, 1000);
    }


    public void sendCurrentTrackPosition() {
        Message message = new Message();
        message.setData(getCurrentTrackPosition());
        if (mediaPlayerHandler != null) {
            mediaPlayerHandler.sendMessage(message);
        }
    }


    public Bundle getCurrentTrackPosition() {
        Bundle uiBundle = new Bundle();
        if (mediaPlayerHandler != null
                && (isPlayerPaused || mediaPlayer.isPlaying())) {
            uiBundle.putBoolean(EXTRA_IS_PLAYER, true);
            int trackPosition = (int) Math.ceil((double) mediaPlayer.getCurrentPosition() / 1000);
            uiBundle.putInt(EXTRA_CURRENT_TRACK_POSITION, trackPosition);
        }
        return uiBundle;
    }


    public void toSeekTrack(int trackProgress, boolean isTrackPaused) {
        if ((mediaPlayer != null && isTrackPaused && !mediaPlayer.isPlaying()) || (mediaPlayer != null
                && mediaPlayer.isPlaying())) {
            mediaPlayer.seekTo(trackProgress * 1000);
            if (mediaPlayer.isPlaying()) {
                updateUI();
            }
        }
    }

    @SuppressLint("DefaultLocale")
    public String getTrackDurationString() {
        return "00:" + String.format("%02d", getTrackDuration());
    }


    public void noUpdateUI() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    public int getTrackDuration() {
        if (mediaPlayer != null && (isPlayerPaused || mediaPlayer.isPlaying())) {
            return mediaPlayer.getDuration() / 1000;
        } else {
            return 0;
        }
    }

    public void setAudioPlayerHandler(Handler spotifyPlayerHandler) {
        this.mediaPlayerHandler = spotifyPlayerHandler;
        Message playerMessage = new Message();
        Bundle playerBundle;
        if (this.mediaPlayerHandler != null && (isPlayerPaused || mediaPlayer.isPlaying())) {
            playerBundle = getCurrentTrackPosition();

            if (!isPlayerPaused) {
                updateUI();
            } else {
                playerBundle.putBoolean(EXTRA_IS_PLAYER, false);
            }
            playerMessage.setData(playerBundle);
            if (this.mediaPlayerHandler != null) {
                this.mediaPlayerHandler.sendMessage(playerMessage);
            }
        }
    }

    public class PlayerBinder extends Binder {
        public AudioPlayerService getService() {
            return AudioPlayerService.this;
        }
    }
}
