package com.m.elshenawy.musicapp.presenter;

import android.content.ServiceConnection;

import com.m.elshenawy.musicapp.interactor.AudioFinishedListener;
import com.m.elshenawy.musicapp.interactor.PlayerInteractor;
import com.m.elshenawy.musicapp.view.service.AudioPlayerService;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class AudioPlayerPresenter extends Presenter<AudioPlayerPresenter.View> implements AudioFinishedListener {
    private final PlayerInteractor playerInteractor;
    private ServiceConnection serviceConnection;

    public AudioPlayerPresenter(PlayerInteractor playerInteractor) {
        this.playerInteractor = playerInteractor;
        this.playerInteractor.setAudioFinishedListener(this);
    }

    @Override
    public void onPlay() {
        getView().isPlay();
    }

    @Override
    public void onPause() {
        getView().isPause();
    }

    @Override
    public void onSetTimeStart(int trackCurrentPosition) {
        getView().setTimeStart(trackCurrentPosition);
    }

    @Override
    public void onSetTimeFinished(AudioPlayerService audioPlayerService) {
        getView().setTimeFinished(audioPlayerService);
    }

    @Override
    public void onResetTrackDuration() {
        getView().onResetTrackDuration();
    }

    @Override
    public void onSetInfoTrackPlayer(int trackPosition) {
        getView().setInfoTrackPlayer(trackPosition);
    }

    public void onStartAudioService(String trackUrl) {
        getView().onStartAudioService(trackUrl, serviceConnection);
    }

    @Override
    public void onServiceConnection(ServiceConnection serviceConnection) {
        this.serviceConnection = serviceConnection;

    }

    @Override
    public void terminate() {
        super.terminate();
        playerInteractor.destroyAudioService();
        setView(null);
    }

    public void onPreviewTrack() {
        playerInteractor.onPreview();
    }

    public void onNextTrack() {
        playerInteractor.onNext();
    }

    public void onPlayPauseTrack() {
        playerInteractor.onPlayStop();
    }


    public interface View extends Presenter.View {
        void onStartAudioService(String trackUrl, ServiceConnection serviceConnection);

        void setInfoTrackPlayer(int trackPosition);

        void isPlay();

        void isPause();

        void setTimeStart(int trackCurrentPosition);

        void setTimeFinished(AudioPlayerService audioPlayerService);

        void onResetTrackDuration();
    }
}

