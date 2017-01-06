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

package com.myhexaville.iconanimations.progress_circle_layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myhexaville.iconanimations.R;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class ProgressCircleLayout extends FrameLayout {
    private ProgressCircleView mCircle;
    private LinearLayout mValuesLayout;
    private TextView mValue;
    private TextView mMetrics;

    private static final String LOG_TAG = "ProgressCircleLayout";

    public ProgressCircleLayout(Context context) {
        super(context);
    }

    public ProgressCircleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(LOG_TAG, "ProgressCircleLayout: ");
        init(context, attrs);
        mCircle.init(context, attrs);
    }

    public ProgressCircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        mCircle.init(context, attrs);
    }

    public ProgressCircleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
        mCircle.init(context, attrs);
    }

    private void init(Context c, AttributeSet attrs) {
        findViews();

        setOnCircleAnimationListener();

        setupValues(c, attrs);
    }

    private void setOnCircleAnimationListener() {
        mCircle.setOnCircleAnimationListener(new OnCircleAnimationListener() {
            @Override
            public void onCircleAnimation(String currentAnimationValue) {
                if (!mValue.getText().equals(currentAnimationValue)) {
                    mValue.setText(currentAnimationValue);
                }
            }
        });
    }

    private void findViews() {
        inflate(getContext(), R.layout.progress_circle_layout, this);
        mCircle = (ProgressCircleView) findViewById(R.id.circle);
        mValuesLayout = (LinearLayout) findViewById(R.id.values_layout);
        mValue = (TextView) findViewById(R.id.value);
        mMetrics = (TextView) findViewById(R.id.metrics);
    }

    public void setInterpolator(Interpolator i) {
        mCircle.setInterpolator(i);
    }

    public void showAnimation() {
        mCircle.showAnimation();
    }

    public void setupValues(Context c, AttributeSet attrs) {
        TypedArray a = c.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ProgressCircleLayout,
                0, 0);

        try {
            int metricsGravity = a.getInt(R.styleable.ProgressCircleLayout_metricsGravity, 0);
            String metrics = a.getString(R.styleable.ProgressCircleLayout_metrics);
            String value = a.getString(R.styleable.ProgressCircleLayout_currentValue);

            setupValues(metricsGravity, metrics, value);
        } finally {
            a.recycle();
        }
    }

    private void setupValues(int metricsGravity, String metrics, String value) {
        if (metricsGravity == 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mMetrics.getLayoutParams();
            params.setMargins(
                    (int) (getContext().getResources().getDisplayMetrics().density * 4),
                    0,
                    0,
                    0);

            mValuesLayout.setOrientation(HORIZONTAL);
        } else {
            mValuesLayout.setOrientation(VERTICAL);
        }

        mValue.setText(value);
        mMetrics.setText(metrics);
    }
}
