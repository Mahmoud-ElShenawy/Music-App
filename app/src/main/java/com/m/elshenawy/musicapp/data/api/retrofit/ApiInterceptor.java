package com.m.elshenawy.musicapp.data.api.retrofit;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.m.elshenawy.musicapp.data.api.Constants.ACCESS_TOKEN;
import static com.m.elshenawy.musicapp.data.api.Constants.API_KEY;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class ApiInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request request = originalRequest.newBuilder().header(API_KEY, ACCESS_TOKEN).method(originalRequest.method(), originalRequest.body()).build();
        return chain.proceed(request);
    }
}
