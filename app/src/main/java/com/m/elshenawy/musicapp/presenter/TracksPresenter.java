package com.m.elshenawy.musicapp.presenter;

import com.m.elshenawy.musicapp.data.model.Track;
import com.m.elshenawy.musicapp.interactor.TracksInteractor;

import java.util.List;

import io.reactivex.disposables.Disposable;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class TracksPresenter extends Presenter<TracksPresenter.View> {
    private final TracksInteractor tracksInteractor;

    public TracksPresenter(TracksInteractor tracksInteractor) {
        this.tracksInteractor = tracksInteractor;
    }

    @Override
    public void terminate() {
        super.terminate();
        setView(null);
    }

    public void onSearchTracks(String string) {
        getView().showLoading();
        Disposable disposable = tracksInteractor.loadData(string).subscribe(tracks -> {
            if (!tracks.isEmpty()) {
                getView().hideLoading();
                getView().renderTracks(tracks);
            } else {
                getView().showTracksNotFoundMessage();
            }
        }, Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void launchArtistDetail(List<Track> tracks, Track track, int position) {
        getView().launchTrackDetail(tracks, track, position);
    }

    public interface View extends Presenter.View {
        void showLoading();

        void hideLoading();

        void showTracksNotFoundMessage();

        void renderTracks(List<Track> tracks);

        void launchTrackDetail(List<Track> tracks, Track track, int position);

        void showConnectionErrorMessage();
    }
}



