/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myhexaville.iconanimations;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class GooeyFab extends FrameLayout {
    private static final String LOG_TAG = "GooeyFab";

    private int mHeight;
    private boolean mIsAnimating;
    private boolean mIsExpanded;


    public GooeyFab(Context context) {
        super(context);
    }

    public GooeyFab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GooeyFab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GooeyFab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mHeight = h;

        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            setOutlineProvider(new CustomOutline(w, h));
        }
    }

    private void init() {
        inflate(getContext(), R.layout.fab_layout, this);
        View v = findViewById(R.id.ripple);
        v.setClipToOutline(true);
        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!mIsAnimating) {
                    mIsAnimating = true;
                    onLargeFabClicked();
                    v.animate().scaleY(1.1f)
                            .setDuration(187)
                            .start();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            v.animate().scaleY(1f)
                                    .setDuration(187)
                                    .start();
                        }
                    }, 187);
                }
            }
        });

        setBackground(
                AnimatedVectorDrawableCompat
                        .create(getContext(), R.drawable.avd_gooey));

        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (!isClickedOnLargeFab(event)) {
                        onSmallFabClicked();
                    }
                }
                return true;
            }
        });
    }

    private void onLargeFabClicked() {
        Drawable d = getBackground();
        if (d instanceof AnimatedVectorDrawableCompat) {
            Log.d(LOG_TAG, "gooey: 2");
            AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) d;
            avd.start();
        }

        setNextAvd();
    }

    private void onSmallFabClicked() {
        Log.d(LOG_TAG, "onSmallFabClicked: ");
    }

    private boolean isClickedOnLargeFab(MotionEvent e) {
        return e.getY() > getFabSize();
    }

    private void setNextAvd() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mIsExpanded = !mIsExpanded;
                mIsAnimating = false;

                if (mIsExpanded) {
                    setBackground(
                            AnimatedVectorDrawableCompat
                                    .create(getContext(), R.drawable.avd_gooey_reverse));
                } else {
                    setBackground(
                            AnimatedVectorDrawableCompat
                                    .create(getContext(), R.drawable.avd_gooey));
                }
            }
        }, 500);
    }

    public int getFabSize() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.fab_size);
    }

    @TargetApi(LOLLIPOP)
    private class CustomOutline extends ViewOutlineProvider {

        int width;

        int height;

        CustomOutline(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setOval(
                    0,
                    mHeight - getFabSize(),
                    width,
                    height);
        }
    }
}
