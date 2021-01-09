package com.m.elshenawy.musicapp.data.api.client;

import com.m.elshenawy.musicapp.data.api.retrofit.SpotifyRetrofitClient;
import com.m.elshenawy.musicapp.data.model.Artist;
import com.m.elshenawy.musicapp.data.model.Track;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class SpotifyClient extends SpotifyRetrofitClient implements SpotifyService {
    @Override
    public Observable<List<Artist>> search(String query) {
        return getSpotifyRetrofitService().searchArtist(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Track>> getTracks(String artistId) {
        return getSpotifyRetrofitService().getTracks(artistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
