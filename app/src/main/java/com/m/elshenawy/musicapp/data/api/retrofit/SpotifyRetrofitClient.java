package com.m.elshenawy.musicapp.data.api.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.m.elshenawy.musicapp.data.api.Constants;
import com.m.elshenawy.musicapp.data.api.retrofit.deserializer.ArtistsDeserializer;
import com.m.elshenawy.musicapp.data.api.retrofit.deserializer.TracksDeserializer;
import com.m.elshenawy.musicapp.data.model.Artist;
import com.m.elshenawy.musicapp.data.model.Track;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public abstract class SpotifyRetrofitClient {
    private final SpotifyRetrofitService spotifyRetrofitService;

    public SpotifyRetrofitClient() {
        Retrofit retrofit = getRetrofit();
        spotifyRetrofitService = retrofit.create(SpotifyRetrofitService.class);
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder().baseUrl(Constants.SPOTIFY_API)
                .addConverterFactory(GsonConverterFactory.create(getSpotifyDeserializer()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();
    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        ApiInterceptor apiInterceptor = new ApiInterceptor();
        client.addInterceptor(apiInterceptor);
        return client.build();
    }

    private Gson getSpotifyDeserializer() {
        return new GsonBuilder().registerTypeAdapter(new TypeToken<List<Artist>>() {
        }.getType(), new ArtistsDeserializer<Artist>())
                .registerTypeAdapter(new TypeToken<List<Track>>() {
                }.getType(), new TracksDeserializer<Track>())
                .create();

    }

    protected SpotifyRetrofitService getSpotifyRetrofitService() {
        return spotifyRetrofitService;
    }
}
