package com.m.elshenawy.musicapp.interactor;

import com.m.elshenawy.musicapp.data.api.client.SpotifyService;
import com.m.elshenawy.musicapp.data.model.Artist;

import java.util.List;

import io.reactivex.Observable;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class ArtistsInteractor {
    private final SpotifyService spotifyService;

    public ArtistsInteractor(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    public Observable<List<Artist>> searchArtists(String query) {
        return spotifyService.search(query);
    }
}
