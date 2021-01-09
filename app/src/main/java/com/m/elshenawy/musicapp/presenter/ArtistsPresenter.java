package com.m.elshenawy.musicapp.presenter;

import com.m.elshenawy.musicapp.data.model.Artist;
import com.m.elshenawy.musicapp.interactor.ArtistsInteractor;

import java.util.List;

import io.reactivex.disposables.Disposable;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class ArtistsPresenter extends Presenter<ArtistsPresenter.View> {
    private final ArtistsInteractor artistsInteractor;

    public ArtistsPresenter(ArtistsInteractor artistsInteractor) {
        this.artistsInteractor = artistsInteractor;
    }

    public void onSearchArtist(String name) {
        getView().showLoading();
        Disposable disposable = artistsInteractor.searchArtists(name).subscribe(artists -> {
            if (!artists.isEmpty()) {
                getView().hideLoading();
                getView().renderArtists(artists);
            } else {
                getView().showArtistNotFoundMessage();
            }
        }, Throwable::printStackTrace);

        addDisposableObserver(disposable);
    }

    public void launchArtistDetail(Artist artist) {
        getView().launchArtistDetail(artist);
    }

    @Override
    public void terminate() {
        super.terminate();
        setView(null);
    }

    public interface View extends Presenter.View {
        void showLoading();

        void hideLoading();

        void showArtistNotFoundMessage();

        void renderArtists(List<Artist> artists);

        void launchArtistDetail(Artist artist);

        void showConnectionErrorMessage();

        void showServerError();
    }
}
