package com.m.elshenawy.musicapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.HEIGHT;
import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.URL;
import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.WIDTH;


import com.google.gson.annotations.SerializedName;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class ArtistImages implements Parcelable {
    @SerializedName(HEIGHT)
    private int height;
    @SerializedName(URL)
    public String url;
    @SerializedName(WIDTH)
    private int width;

    protected ArtistImages(Parcel in) {
        this.height = in.readInt();
        this.url = in.readString();
        this.width = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.height);
        dest.writeString(this.url);
        dest.writeInt(this.width);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ArtistImages> CREATOR = new Creator<ArtistImages>() {
        @Override
        public ArtistImages createFromParcel(Parcel in) {
            return new ArtistImages(in);
        }

        @Override
        public ArtistImages[] newArray(int size) {
            return new ArtistImages[size];
        }
    };
}
