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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MyFab extends LinearLayout {
    private static final String LOG_TAG = "MyFab";

    public MyFab(Context context) {
        super(context);
    }

    public MyFab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyFab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyFab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        setOrientation(VERTICAL);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        final View fab = new View(getContext());
        fab.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                getContext().getResources().getDimensionPixelSize(R.dimen.fab_size),
                getContext().getResources().getDimensionPixelSize(R.dimen.fab_2_size)));
        fab.setBackground(AnimatedVectorDrawableCompat.create(getContext(), R.drawable.avd_gooey_2));

        addView(fab);

        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable d = fab.getBackground();
                if (d instanceof AnimatedVectorDrawableCompat) {
                    ((AnimatedVectorDrawableCompat) d).start();
                }
            }
        });
//        View fab2 = new View(getContext());
//        fab2.setLayoutParams(new LinearLayoutCompat.LayoutParams(
//                getContext().getResources().getDimensionPixelSize(R.dimen.fab_size),
//                getContext().getResources().getDimensionPixelSize(R.dimen.fab_size)));
//        fab2.setBackgroundResource(R.drawable.fab);
//
//        addView(fab2);
    }


}
