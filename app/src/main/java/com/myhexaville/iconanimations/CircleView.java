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
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

public class CircleView extends View {
    private static final String LOG_TAG = "CircleView";
    public static final int DEFAULT_ANIMATION_TIME = 1000;

    private int mStartValue;
    private int mCurrentValue;
    private int mEndValue;

    private float mStrokeWidth;
    private int mAnimationDuration;
    private int mBackCircleColor;
    private int mForegroundCircleColor;
    private float mAnimationSpeed;

    private Paint mBackCirclePaint;
    private Paint mForegroundCirclePaint;
    private float mCurrentAngle;
    private int mEndAngle;

    private long mLastFrame;
    private long mAnimationStartTime;

    public CircleView(Context context) {
        super(context);
        setupPaint();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttributesAndSetupFields(context, attrs);
        setupPaint();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttributesAndSetupFields(context, attrs);
        setupPaint();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
                getNextFrameAngle(),
                false,
                mForegroundCirclePaint
        );

        if (mCurrentAngle < mEndAngle) {
            invalidate();
        }
    }


    private void readAttributesAndSetupFields(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleView,
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
        mStartValue = a.getInt(R.styleable.CircleView_startValue, 0);
        mCurrentValue = a.getInt(R.styleable.CircleView_currentValue, 0);
        mEndValue = a.getInt(R.styleable.CircleView_endValue, 0);

        mAnimationDuration = a.getInt(R.styleable.CircleView_animationDuration, DEFAULT_ANIMATION_TIME);

        readBackCircleColorFromAttributes(a);

        readForegroundColorFromAttributes(context, a);

        mStrokeWidth = a.getDimension(R.styleable.CircleView_strokeWidth, getDefaultStrokeWidth(context));
    }

    private void readForegroundColorFromAttributes(Context context, TypedArray a) {
        ColorStateList fc = a.getColorStateList(R.styleable.CircleView_foregroundCircleColor);
        if (fc != null) {
            mForegroundCircleColor = fc.getDefaultColor();
        } else {
            mForegroundCircleColor = getAccentColor(context);
        }
    }

    private void readBackCircleColorFromAttributes(TypedArray a) {
        ColorStateList bc = a.getColorStateList(R.styleable.CircleView_backgroundCircleColor);
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

    public float getNextFrameAngle() {
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
        if (pathGone < 1.0f) {
            mCurrentAngle = mEndAngle * pathGone;
        } else {
            mCurrentAngle = mEndAngle;
        }
        mLastFrame = now;
        return mCurrentAngle;
    }


    public int getAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    public int getDefaultStrokeWidth(Context context) {
        return (int) (context.getResources().getDisplayMetrics().density * 10);
    }
}

