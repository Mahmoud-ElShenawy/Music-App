package com.m.elshenawy.musicapp.data.api.retrofit;

import com.m.elshenawy.musicapp.data.api.Constants;
import com.m.elshenawy.musicapp.data.model.Artist;
import com.m.elshenawy.musicapp.data.model.Track;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public interface SpotifyRetrofitService {
    @GET(Constants.EndPoint.ARTIST_SEARCH)
    Observable<List<Artist>> searchArtist(@Query(Constants.Params.QUERY_SEARCH) String artist);

    @GET(Constants.EndPoint.ARTIST_TRACKS)
    Observable<List<Track>> getTracks(@Query(Constants.Params.ARTIST_ID) String artistId);

}
