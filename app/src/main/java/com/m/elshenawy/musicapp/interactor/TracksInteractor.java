package com.m.elshenawy.musicapp.interactor;

import com.m.elshenawy.musicapp.data.api.client.SpotifyService;
import com.m.elshenawy.musicapp.data.model.Track;

import java.util.List;

import io.reactivex.Observable;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class TracksInteractor {
    private final SpotifyService spotifyService;

    public TracksInteractor(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    public Observable<List<Track>> loadData(String artistId) {
        return spotifyService.getTracks(artistId);
    }


}
