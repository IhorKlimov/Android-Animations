/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.myhexaville.iconanimations.gooey_fab;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.myhexaville.iconanimations.R;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_HORIZONTAL;
import static com.myhexaville.iconanimations.gooey_fab.GooeyFabCompatImpl.POSITION_LARGE_FAB;
// not pre L yet
public class GooeyFabCompat extends LinearLayout {
    private static final String LOG_TAG = "GooeyFabCompat";
    private int mSmallFabCount = 1;
    private int mWidth;
    private int mHeight;
    private boolean mIsAnimating;
    private Drawable[] mIcons;


    public GooeyFabCompat(Context context) {
        super(context);
    }

    public GooeyFabCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GooeyFabCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public GooeyFabCompat(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context c, AttributeSet attrs) {
        getAttributes(c, attrs);
        setupLayoutParams();
        addFabs(c);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private void setupLayoutParams() {
        setOrientation(VERTICAL);
        setGravity(CENTER);
    }

    private void addFabs(Context c) {
        final GooeyFabCompatImpl largeFab = new GooeyFabCompatImpl(c, true, POSITION_LARGE_FAB);

        final List<GooeyFabCompatImpl> smallFabs = new ArrayList<>();
        addView(largeFab);
        largeFab.setIcon(mIcons[0]);
        for (int i = 0; i < mSmallFabCount; i++) {
            GooeyFabCompatImpl f = new GooeyFabCompatImpl(c, false, i);
            f.setIcon(mIcons[i + 1]);
            smallFabs.add(f);
            addView(f, 0);
            f.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        largeFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsAnimating) {
                    mIsAnimating = true;
                    largeFab.showAnimation();
                    for (GooeyFabCompatImpl smallFab : smallFabs) {
                        smallFab.showAnimation();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mIsAnimating = false;
                        }
                    }, 300);
                }
            }
        });
    }

    private void getAttributes(Context c, AttributeSet attrs) {
        TypedArray a = c.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.GooeyFabCompat,
                0, 0);
        final int id = a.getResourceId(R.styleable.GooeyFabCompat_fabIcons, 0);
        TypedArray icons = getResources().obtainTypedArray(id);
        try {
            // animation currently looks good only for one mini fab. Fix is coming in the next versions
//            mSmallFabCount = a.getInt(R.styleable.GooeyFabCompat_numberOfSmallFab, 0);
            mIcons = new Drawable[mSmallFabCount + 1];
            for (int i = 0; i < mIcons.length; i++) {
                mIcons[i] = icons.getDrawable(i);
            }
        } finally {
            a.recycle();
            icons.recycle();
        }
    }
}
