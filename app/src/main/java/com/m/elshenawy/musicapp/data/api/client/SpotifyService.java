package com.m.elshenawy.musicapp.data.api.client;

import com.m.elshenawy.musicapp.data.model.Artist;
import com.m.elshenawy.musicapp.data.model.Track;

import java.util.List;

import io.reactivex.Observable;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public interface SpotifyService {
    Observable<List<Artist>> search(String query);

    Observable<List<Track>> getTracks(String artistId);
}
