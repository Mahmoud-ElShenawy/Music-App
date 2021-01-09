package com.m.elshenawy.musicapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.NAME;
import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.ALBUM;
import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.PREVIEW_URL;
import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.TRACK_NUMBER;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class Track implements Parcelable {
    @SerializedName(NAME)
    public String name;
    @SerializedName(PREVIEW_URL)
    public String previewUrl;
    @SerializedName(ALBUM)
    public Album album;
    @SerializedName(TRACK_NUMBER)
    private final int trackNumber;

    protected Track(Parcel in) {
        this.name = in.readString();
        this.previewUrl = in.readString();
        this.trackNumber = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.previewUrl);
        dest.writeInt(this.trackNumber);
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


}
