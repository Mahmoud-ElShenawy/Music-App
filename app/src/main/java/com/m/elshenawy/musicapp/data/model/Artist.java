package com.m.elshenawy.musicapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.FOLLOWERS;
import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.HREF;
import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.ID;
import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.IMAGES;
import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.NAME;
import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.POPULARITY;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class Artist implements Parcelable {
    @SerializedName(FOLLOWERS)
    public Followers followers;
    @SerializedName(HREF)
    private String href;
    @SerializedName(IMAGES)
    public List<ArtistImages> artistImages;
    @SerializedName(NAME)
    public String name;
    @SerializedName(ID)
    public String id;
    @SerializedName(POPULARITY)
    private int popularity;

    protected Artist(Parcel in) {
        this.href = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.followers = in.readParcelable(Followers.class.getClassLoader());
        this.popularity = in.readInt();

        if (this.artistImages == null) {
            this.artistImages = new ArrayList();
        }

        in.readTypedList(this.artistImages, ArtistImages.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.href);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.followers, 0);
        dest.writeInt(this.popularity);
        dest.writeTypedList(this.artistImages);

    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}
