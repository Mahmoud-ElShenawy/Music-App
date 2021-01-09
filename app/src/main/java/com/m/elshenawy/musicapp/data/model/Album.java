package com.m.elshenawy.musicapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.NAME;
import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.IMAGES;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class Album {
    @SerializedName(NAME)
    public String albumName;
    @SerializedName(IMAGES)
    public List<ArtistImages> trackImages;

}
