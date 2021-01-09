package com.m.elshenawy.musicapp.interactor;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import com.m.elshenawy.musicapp.data.model.Track;
import com.m.elshenawy.musicapp.view.service.AudioPlayerService;

import java.util.List;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class PlayerInteractor {
    private AudioFinishedListener audioFinishedListener;
    private AudioPlayerService audioPlayerService;
    private int trackDuration = 0;
    private int trackCurrentPosition;
    private int trackPosition;
    private boolean isPlayerPlaying = false;
    private boolean isPlayerPaused = false;
    private boolean isServiceBounded;
    private final Context context;
    private final List<Track> trackList;

    private final Handler playerHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (trackDuration == 0) {
                setTrackDuration();
            }
            trackCurrentPosition = msg.getData().getInt(AudioPlayerService.EXTRA_CURRENT_TRACK_POSITION);
            audioFinishedListener.onSetTimeStart(trackCurrentPosition);

            if (trackCurrentPosition == trackDuration && trackCurrentPosition != 0) {
                isPlayerPlaying = false;
                isPlayerPaused = false;
                trackCurrentPosition = 0;
            }
            if (isPlayerPlaying) {
                audioFinishedListener.onPause();
            } else {
                audioFinishedListener.onPlay();
            }
        }
    };

    private void setTrackDuration() {
        if (audioPlayerService != null) {
            trackDuration = audioPlayerService.getTrackDuration();
            audioFinishedListener.onSetTimeFinished(audioPlayerService);
        }
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioPlayerService.PlayerBinder playerBinder = (AudioPlayerService.PlayerBinder) service;
            audioPlayerService = playerBinder.getService();
            isServiceBounded = true;
            if (!isPlayerPlaying) {
                isPlayerPlaying = true;
            }
            setTrackDuration();
            audioPlayerService.setAudioPlayerHandler(playerHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBounded = false;
        }
    };

    public PlayerInteractor(List<Track> trackList, Context context) {
        this.context = context;
        this.trackList = trackList;
    }

    public void setAudioFinishedListener(AudioFinishedListener audioFinishedListener) {
        this.audioFinishedListener = audioFinishedListener;
        audioFinishedListener.onServiceConnection(serviceConnection);
    }

    private void changeTrack(int trackPosition) {
        isPlayerPaused = false;
        isPlayerPlaying = true;
        audioFinishedListener.onSetInfoTrackPlayer(trackPosition);
        audioPlayerService.setTrackPreviewUrl(trackList.get(trackPosition).previewUrl);
        audioPlayerService.noUpdateUI();
        audioPlayerService.onPlayAudio(0);
        audioFinishedListener.onResetTrackDuration();
        trackDuration = 0;
    }

    public void destroyAudioService() {
        if (audioPlayerService != null) {
            audioPlayerService.noUpdateUI();
            if (isServiceBounded) {
                context.getApplicationContext().unbindService(serviceConnection);
                isServiceBounded = false;
            }
        }
        if (!isPlayerPaused && !isPlayerPlaying) {
            context.getApplicationContext().stopService(new Intent(context, AudioPlayerService.class));
        }
    }

    public void onPreview() {
        trackPosition = trackPosition - 1;
        if (trackPosition < 0) {
            trackPosition = trackList.size() - 1;
        }
        changeTrack(trackPosition);
    }

    public void onNext() {
        trackPosition = (trackPosition + 1) % trackList.size();
        changeTrack(trackPosition);
    }

    public void onPlayStop() {
        if (isPlayerPlaying) {
            audioFinishedListener.onPlay();
            audioPlayerService.onPauseAudio();
            isPlayerPaused = true;
            isPlayerPlaying = false;
        } else {
            audioFinishedListener.onPause();
            audioPlayerService.onPlayAudio(trackCurrentPosition);
            isPlayerPaused = true;
            isPlayerPlaying = true;
        }
    }
}
