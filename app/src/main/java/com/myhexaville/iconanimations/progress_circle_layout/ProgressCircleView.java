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
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.myhexaville.iconanimations.R;

final class ProgressCircleView extends View {
    private static final String LOG_TAG = "ProgressCircleView";
    private static final int DEFAULT_ANIMATION_TIME = 1000;
    private Interpolator mInterpolator;
    private OnCircleAnimationListener mListener;


    private int mStartValue;
    private int mCurrentValue;
    private int mEndValue;

    private float mStrokeWidth;
    private int mAnimationDuration;
    private int mBackCircleColor;
    private int mForegroundCircleColor;

    private boolean mAnimateOnDisplay;

    private float mAnimationSpeed;
    private Paint mBackCirclePaint;
    private Paint mForegroundCirclePaint;
    private float mCurrentAngle;

    private int mEndAngle;
    private long mAnimationStartTime;

    public ProgressCircleView(Context context) {
        super(context);
        mInterpolator = new AccelerateDecelerateInterpolator();
    }

    public ProgressCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ProgressCircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(Context context, AttributeSet attrs) {
        mInterpolator = new AccelerateDecelerateInterpolator();

        readAttributesAndSetupFields(context, attrs);

        setupPaint();
    }


    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (mAnimationStartTime == 0) {
            mAnimationStartTime = System.currentTimeMillis();
        }

        canvas.drawCircle(
                getWidth() / 2,
                getHeight() / 2,
                getWidth() / 2 - mStrokeWidth / 2,
                mBackCirclePaint);


        canvas.drawArc(
                0 + mStrokeWidth / 2,
                0 + mStrokeWidth / 2,
                getWidth() - mStrokeWidth / 2,
                getHeight() - mStrokeWidth / 2,
                -90,
                mAnimateOnDisplay ? getCurrentFrameAngle() : mEndAngle,
                false,
                mForegroundCirclePaint
        );

        if (mAnimateOnDisplay && mCurrentAngle < mEndAngle) {
            invalidate();
        }
    }

    public void showAnimation() {
        mAnimateOnDisplay = true;
        mCurrentAngle = 0f;
        mAnimationStartTime = 0;
        invalidate();
    }


    private void readAttributesAndSetupFields(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ProgressCircleLayout,
                0, 0);

        try {
            applyAttributes(context, a);

            setEndAngle();

            setAnimationSpeed();

            log();
        } finally {
            a.recycle();
        }
    }

    private void applyAttributes(Context context, TypedArray a) {
        mStartValue = a.getInt(R.styleable.ProgressCircleLayout_startValue, 0);
        mCurrentValue = a.getInt(R.styleable.ProgressCircleLayout_currentValue, 0);
        mEndValue = a.getInt(R.styleable.ProgressCircleLayout_endValue, 0);

        mAnimateOnDisplay = a.getBoolean(R.styleable.ProgressCircleLayout_animateOnDisplay, true);

        mAnimationDuration = a.getInt(R.styleable.ProgressCircleLayout_animationDuration, DEFAULT_ANIMATION_TIME);

        readBackCircleColorFromAttributes(a);

        readForegroundColorFromAttributes(context, a);

        mStrokeWidth = a.getDimension(R.styleable.ProgressCircleLayout_strokeWidth, getDefaultStrokeWidth(context));
    }

    private void readForegroundColorFromAttributes(Context context, TypedArray a) {
        ColorStateList fc = a.getColorStateList(R.styleable.ProgressCircleLayout_foregroundCircleColor);
        if (fc != null) {
            mForegroundCircleColor = fc.getDefaultColor();
        } else {
            mForegroundCircleColor = getAccentColor(context);
        }
    }

    private void readBackCircleColorFromAttributes(TypedArray a) {
        ColorStateList bc = a.getColorStateList(R.styleable.ProgressCircleLayout_backgroundCircleColor);
        if (bc != null) {
            mBackCircleColor = bc.getDefaultColor();
        } else {
            mBackCircleColor = Color.parseColor("16000000");
        }
    }

    private void setAnimationSpeed() {
        float seconds = (float) mAnimationDuration / 1000;
        int i = (int) (seconds * 60);
        mAnimationSpeed = (float) mEndAngle / i;
    }

    private void setEndAngle() {
        int totalLength = mEndValue - mStartValue;
        int pathGone = mCurrentValue - mStartValue;
        float v = ((float) pathGone / totalLength);
        mEndAngle = (int) (360 * v);
    }

    private void log() {
        Log.d(LOG_TAG, "readAttributesAndSetupFields: start value " + mStartValue);
        Log.d(LOG_TAG, "readAttributesAndSetupFields: current value " + mCurrentValue);
        Log.d(LOG_TAG, "readAttributesAndSetupFields: end value " + mEndValue);
        Log.d(LOG_TAG, "readAttributesAndSetupFields: end angle " + mEndAngle);
        Log.d(LOG_TAG, "readAttributesAndSetupFields: animation speed " + mAnimationSpeed);
        Log.d(LOG_TAG, "readAttributesAndSetupFields: animation time " + mAnimationDuration);
    }

    private void setupPaint() {
        setupBackCirclePaint();

        setupFrontCirclePaint();
    }

    private void setupFrontCirclePaint() {
        mForegroundCirclePaint = new Paint();

        mForegroundCirclePaint.setColor(mForegroundCircleColor);
        mForegroundCirclePaint.setStyle(Paint.Style.STROKE);
        mForegroundCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mForegroundCirclePaint.setStrokeWidth(mStrokeWidth);
    }

    private void setupBackCirclePaint() {
        mBackCirclePaint = new Paint();

        mBackCirclePaint.setColor(mBackCircleColor);
        mBackCirclePaint.setStyle(Paint.Style.STROKE);

        mBackCirclePaint.setStrokeWidth(mStrokeWidth);
    }

    private float getCurrentFrameAngle() {
//        mCurrentAngle += mAnimationSpeed;
//
//        if (mCurrentAngle > mEndAngle) {
//            return mEndAngle;
//        } else {
//            return mCurrentAngle;
//        }
//

        long now = System.currentTimeMillis();
        float pathGone = ((float) (now - mAnimationStartTime) / (mAnimationDuration));
        float interpolatedPathGone = mInterpolator.getInterpolation(pathGone);

        if (pathGone < 1.0f) {
            mCurrentAngle = mEndAngle * interpolatedPathGone;
            mListener.onCircleAnimation(getCurrentAnimationFrameValue(interpolatedPathGone));
        } else {
            mCurrentAngle = mEndAngle;
            mListener.onCircleAnimation(getCurrentAnimationFrameValue(1.0f));

        }


        return mCurrentAngle;
    }

    void setOnCircleAnimationListener(OnCircleAnimationListener l) {
        mListener = l;
    }

    private int getAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    private int getDefaultStrokeWidth(Context context) {
        return (int) (context.getResources().getDisplayMetrics().density * 10);
    }

    public void setInterpolator(Interpolator i) {
        mInterpolator = i;
    }

    public String getCurrentAnimationFrameValue(float interpolatedPathGone) {
        int value = Math.round(((mCurrentValue - mStartValue) * interpolatedPathGone)) + mStartValue;

        return String.valueOf(value);
    }
}

