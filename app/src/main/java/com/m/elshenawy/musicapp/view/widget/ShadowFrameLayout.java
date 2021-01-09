package com.m.elshenawy.musicapp.view.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.Property;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.m.elshenawy.musicapp.R;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public class ShadowFrameLayout extends FrameLayout {
    private final Drawable mShadowDrawable;
    private NinePatchDrawable mShadowNinePatchDrawable;
    private int mShadowTopOffset;
    private int mWidth;
    private int mHeight;
    private float mAlpha = 1f;
    private boolean mShadowVisible;
    private ObjectAnimator mAnimator;

    private static final Property<ShadowFrameLayout, Float> SHADOW_ALPHA = new Property<ShadowFrameLayout, Float>(Float.class, "shadowAlpha") {
        @Override
        public Float get(ShadowFrameLayout object) {
            return object.mAlpha;
        }

        @Override
        public void set(ShadowFrameLayout object, Float value) {
            object.mAlpha = value;
            ViewCompat.postInvalidateOnAnimation(object);
        }
    };


    public ShadowFrameLayout(@NonNull Context context) {
        this(context, null, 0);
    }

    public ShadowFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowFrameLayout, 0, 0);
        mShadowDrawable = typedArray.getDrawable(R.styleable.ShadowFrameLayout_shadowDrawable);
        if (mShadowDrawable != null) {
            mShadowDrawable.setCallback(this);
            if (mShadowDrawable instanceof NinePatchDrawable) {
                mShadowNinePatchDrawable = (NinePatchDrawable) mShadowDrawable;
            }
        }
        mShadowVisible = typedArray.getBoolean(R.styleable.ShadowFrameLayout_shadowVisible, true);
        setWillNotDraw(!mShadowVisible || mShadowDrawable == null);
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        updateShadowBounds();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mShadowDrawable != null && mShadowVisible) {
            if (mShadowNinePatchDrawable != null) {
                mShadowNinePatchDrawable.getPaint().setAlpha((int) (255 * mAlpha));
            }
            mShadowDrawable.draw(canvas);
        }
    }

    private void updateShadowBounds() {
        if (mShadowDrawable != null) {
            mShadowDrawable.setBounds(0, mShadowTopOffset, mWidth, mHeight);
        }
    }


    public void setShadowTopOffset(int shadowTopOffset) {
        this.mShadowTopOffset = shadowTopOffset;
        updateShadowBounds();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setShadowVisible(boolean showVisible, boolean animate) {
        this.mShadowVisible = showVisible;
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }

        if (animate && mShadowDrawable != null) {
            mAnimator = ObjectAnimator.ofFloat(this, SHADOW_ALPHA, showVisible ? 0f : 1f, showVisible ? 1f : 0f);
            mAnimator.setDuration(1000);
            mAnimator.start();
        }

        ViewCompat.postInvalidateOnAnimation(this);
        setWillNotDraw(!mShadowVisible || mShadowDrawable == null);
    }

}
