package com.m.elshenawy.musicapp.view.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;


import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class BlurEffectUtils extends BitmapTransformation {
    private static final int UP_LIMIT = 25;
    private static final int LOW_LIMIT = 1;
    protected final Context context;
    private final int blurRadius;
    private RenderScript rs;


    public BlurEffectUtils(Context context, int blurRadius) {
        this.context = context;

        if (blurRadius < LOW_LIMIT) {
            this.blurRadius = LOW_LIMIT;
        } else if (blurRadius > UP_LIMIT) {
            this.blurRadius = UP_LIMIT;
        } else {
            this.blurRadius = blurRadius;
        }
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap blurredBitmap = toTransform.copy(Bitmap.Config.ARGB_8888, true);
        rs = RenderScript.create(context);
        Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SCRIPT);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setInput(input);
        script.setRadius(20);
        script.forEach(output);
        output.copyTo(blurredBitmap);

        return blurredBitmap;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update("blur transformation".getBytes());
    }
}
