package com.m.elshenawy.musicapp.interactor;

import android.content.ServiceConnection;

import com.m.elshenawy.musicapp.view.service.AudioPlayerService;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public interface AudioFinishedListener {
    void onPlay();

    void onPause();

    void onSetTimeStart(int trackCurrentPosition);

    void onSetTimeFinished(AudioPlayerService audioPlayerService);

    void onResetTrackDuration();

    void onSetInfoTrackPlayer(int trackPosition);

    void onServiceConnection(ServiceConnection serviceConnection);
}
