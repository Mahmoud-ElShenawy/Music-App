package com.m.elshenawy.musicapp.data.api.retrofit.deserializer;

import com.google.gson.JsonDeserializer;

import java.util.List;

public interface ListDeserializer<T> extends JsonDeserializer<List<T>> {
}
