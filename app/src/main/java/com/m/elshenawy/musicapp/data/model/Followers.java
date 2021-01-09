package com.m.elshenawy.musicapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.HREF;
import static com.m.elshenawy.musicapp.data.api.Constants.Serialized.TOTAL;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class Followers implements Parcelable {
    @SerializedName(HREF)
    private final String href;
    @SerializedName(TOTAL)
    public int totalFollowers;

    protected Followers(Parcel in) {
        this.href = in.readString();
        this.totalFollowers = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.href);
        dest.writeInt(this.totalFollowers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Followers> CREATOR = new Creator<Followers>() {
        @Override
        public Followers createFromParcel(Parcel in) {
            return new Followers(in);
        }

        @Override
        public Followers[] newArray(int size) {
            return new Followers[size];
        }
    };
}
