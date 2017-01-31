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

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.myhexaville.iconanimations.R;

import static android.graphics.Paint.Style.FILL;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_HORIZONTAL;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

/**
 * Created with love by ihor on 2017-01-27.
 */
final class GooeyFabCompatImpl extends FrameLayout {
    private static final String LOG_TAG = "GooeyFabCompatImpl";
    static final Interpolator ANIM_INTERPOLATOR = new FastOutLinearInInterpolator();
    static final int[] FOCUSED_ENABLED_STATE_SET = {android.R.attr.state_focused,
            android.R.attr.state_enabled};
    static final long PRESSED_ANIM_DURATION = 100;

    private ImageView mIcon;
    static final int POSITION_LARGE_FAB = -1;
    private final int mStartTanslationY;
    private final int mAnimationTanslationY;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private boolean mIsLargeFab;

    private int mPositionFromBottom;
    private boolean mIsExpanded;
    private float mCenterY;
    private float mGooeyPart;
    private View mRipple;

    public GooeyFabCompatImpl(Context context, boolean isLarge, int positionFromBottom) {
        super(context);
        mIsLargeFab = isLarge;
        mPositionFromBottom = positionFromBottom;
        int position = mPositionFromBottom + 1;
        mStartTanslationY = getFabHeight() * position + (getBottomMargin() * position) + fourDp();
        mAnimationTanslationY = getFabWidth() * position + (getBottomMargin() * position);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        mCenterY = getCenterY();

        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            setOutlineProvider(new CustomOutline());
        }
    }

    private float getCenterY() {
        return mIsLargeFab
                ? getGooeyPartHeight() + (getFabWidth() / 2)
                : mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF gooeyPart = new RectF(0,
                mCenterY - getArcAbsolute(),
                mWidth,
                mCenterY + getArcAbsolute());

        RectF backgroundCircle = new RectF(0,
                mCenterY - getFabWidth() / 2,
                mWidth,
                mCenterY + getFabWidth() / 2);

        canvas.drawArc(
                gooeyPart,
                0,
                mIsLargeFab ? -180 : 180,
                false,
                mPaint
        );

        canvas.drawArc(
                backgroundCircle,
                0,
                360,
                false,
                mPaint
        );
    }

    public void showAnimation() {
        if (!mIsLargeFab) {
            if (!mIsExpanded) {
                mIcon.setAlpha(0f);
            }

            animate().translationYBy(mIsExpanded
                    ? mAnimationTanslationY
                    : -mAnimationTanslationY)
                    .setDuration(250)
                    .start();

            mIcon.animate().alpha(!mIsExpanded ? 1f : 0f).setDuration(500).start();
            mGooeyPart = getGooeyPartHeight();
            invalidate();
        } else {
            mIcon.animate().rotationBy(mIsExpanded ? -45 : 45).setDuration(187).start();
            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(187);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float v = (float) animation.getAnimatedValue();
                    mGooeyPart = getGooeyPartHeight() * v;
                    invalidate();
                }
            });
            animator.start();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f).setDuration(187);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float v = (float) animation.getAnimatedValue();
                        mGooeyPart = getGooeyPartHeight() * v;
                        invalidate();
                    }
                });
                animator.start();
            }
        }, 187);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mIsExpanded = !mIsExpanded;
            }
        }, 250);

    }

    private void init() {
        inflate();
        addRipple();
        setWillNotDraw(false);
        setSize();
        setPaint();
        moveToStartingPosition();
        setParams();
        addIcon();
    }

    private void inflate() {
        inflate(getContext(), R.layout.fab_layout_compat, this);
        mRipple = findViewById(R.id.ripple);
    }

    private void addRipple() {
        LayoutParams params = new LayoutParams(getFabWidth(), getFabWidth());
        if (mIsLargeFab) {
            params.gravity = BOTTOM;
        } else {
            params.gravity = CENTER;
        }
        mRipple.setLayoutParams(params);
        if (isLollipopOrAbove()) {
            mRipple.setClipToOutline(true);
        }
    }

    private void addIcon() {
        mIcon = new ImageView(getContext());
        LayoutParams params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        if (mIsLargeFab) {
            params.gravity = BOTTOM | CENTER_HORIZONTAL;
            params.bottomMargin = dpToPixels(16);
        } else {
            params.gravity = CENTER;

        }
        mIcon.setLayoutParams(params);
        addView(mIcon);
    }

    private void setPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#008080"));
        mPaint.setStyle(FILL);
    }

    private void setSize() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(getFabWidth(), getFabHeight());
        layoutParams.bottomMargin = getBottomMargin();
        setLayoutParams(layoutParams);
    }

    private void setParams() {
        if (isLollipopOrAbove()) {
            onElevationsChanged(getFabElevation(true), dpToPixels(3));
        }
    }

    private void moveToStartingPosition() {
        if (!mIsLargeFab) {
            setTranslationY(mStartTanslationY);
        }
    }

    private int getFabWidth() {
        return getContext().getResources().getDimensionPixelSize(
                mIsLargeFab
                        ? R.dimen.fab_size
                        : R.dimen.fab_size_small);
    }

    private int getFabHeight() {
        if (mIsLargeFab) {
            return getFabWidth() + getGooeyPartHeight();
        } else {
            return getFabWidth() + getGooeyPartHeight() * 2;
        }
    }

    private int getBottomMargin() {
        if (mIsLargeFab) {
            return 0;
        } else {
            return getContext().getResources().getDimensionPixelSize(R.dimen.small_fab_margin_bottom);
        }
    }

    private int getFabElevation(boolean isRest) {
        return getContext().getResources().getDimensionPixelSize(
                isRest
                        ? R.dimen.fab_elevation_rest
                        : R.dimen.fab_elevation_pressed);
    }

    private int getGooeyPartHeight() {
        return getContext().getResources()
                .getDimensionPixelSize(
                        mIsLargeFab
                                ? R.dimen.fab_gooey_part
                                : R.dimen.fab_gooey_part_small);
    }

    @TargetApi(LOLLIPOP)
    private class CustomOutline extends ViewOutlineProvider {

        CustomOutline() {
        }

        @Override
        public void getOutline(View view, Outline outline) {
            int top = mIsLargeFab ? getGooeyPartHeight() : (int) (mCenterY - getFabWidth() / 2);
            int bottom = mIsLargeFab ? mHeight : (int) (mCenterY + getFabWidth() / 2);

            outline.setOval(
                    0,
                    top,
                    mWidth,
                    bottom);
        }
    }

    private float getArcAbsolute() {
        return getFabWidth() / 2 + mGooeyPart;
    }

    void setIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
    }

    private int fourDp() {
        return getResources().getDimensionPixelSize(R.dimen.fab_gooey_part_small);
    }

    private int dpToPixels(int i) {
        return (int) (getResources().getDisplayMetrics().density * i);
    }

    @TargetApi(21)
    void onElevationsChanged(final float elevation, final float pressedTranslationZ) {
        final StateListAnimator stateListAnimator = new StateListAnimator();


        // Animate elevation and translationZ to our values when pressed
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(this, "elevation", elevation).setDuration(0))
                .with(ObjectAnimator.ofFloat(this, View.TRANSLATION_Z, pressedTranslationZ)
                        .setDuration(PRESSED_ANIM_DURATION));
        set.setInterpolator(ANIM_INTERPOLATOR);
        stateListAnimator.addState(PRESSED_ENABLED_STATE_SET, set);

        // Same deal for when we're focused
        set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(this, "elevation", elevation).setDuration(0))
                .with(ObjectAnimator.ofFloat(this, View.TRANSLATION_Z, pressedTranslationZ)
                        .setDuration(PRESSED_ANIM_DURATION));
        set.setInterpolator(ANIM_INTERPOLATOR);
        stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, set);

        // Animate translationZ to 0 if not pressed
        set = new AnimatorSet();
        // Use an AnimatorSet to set a start delay since there is a bug with ValueAnimator that
        // prevents it from being cancelled properly when used with a StateListAnimator.
        AnimatorSet anim = new AnimatorSet();
        anim.play(ObjectAnimator.ofFloat(this, View.TRANSLATION_Z, 0f)
                .setDuration(PRESSED_ANIM_DURATION))
                .after(PRESSED_ANIM_DURATION);
        set.play(ObjectAnimator.ofFloat(this, "elevation", elevation).setDuration(0))
                .with(anim);
        set.setInterpolator(ANIM_INTERPOLATOR);
        stateListAnimator.addState(ENABLED_STATE_SET, set);

        // Animate everything to 0 when disabled
        set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(this, "elevation", 0f).setDuration(0))
                .with(ObjectAnimator.ofFloat(this, View.TRANSLATION_Z, 0f).setDuration(0));
        set.setInterpolator(ANIM_INTERPOLATOR);
        stateListAnimator.addState(EMPTY_STATE_SET, set);

        setStateListAnimator(stateListAnimator);
    }

    private boolean isLollipopOrAbove() {
        return Build.VERSION.SDK_INT >= 21;
    }
}
