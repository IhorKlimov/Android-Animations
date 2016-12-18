package com.myhexaville.iconanimations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.myhexaville.iconanimations.databinding.ActivitySignInBinding;

import static android.view.View.VISIBLE;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        loadImageAndSetNavBarColor();

    }

    public void load(View view) {
        animateButtonWidth();

        fadeOutTextAndShowProgressDialog();

        nextAction();
    }

    private void fadeOutTextAndShowProgressDialog() {
        mBinding.text.animate().alpha(0f)
                .setDuration(250)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        showProgressDialog();
                    }
                })
                .start();
    }

    private void animateButtonWidth() {
        ValueAnimator anim = ValueAnimator.ofInt(mBinding.button.getMeasuredWidth(), getFabWidth());

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mBinding.button.getLayoutParams();
                layoutParams.width = val;
                mBinding.button.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(250);
        anim.start();
    }

    private void showProgressDialog() {
        mBinding.progressBar
                .getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        mBinding.progressBar.setVisibility(VISIBLE);
    }

    private int getFabWidth() {
        return (int) getResources().getDimension(R.dimen.fab_size);
    }

    private void nextAction() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                revealButton();

                fadeOutProgressDialog();

                delayedStartNextActivity();
            }
        }, 2000);
    }

private void revealButton() {
    mBinding.button.setElevation(0f);

    mBinding.reveal.setVisibility(VISIBLE);

    int cx = mBinding.reveal.getWidth();
    int cy = mBinding.reveal.getHeight();


    int x = (int) (getFabWidth() / 2 + mBinding.button.getX());
    int y = (int) (getFabWidth() / 2 + mBinding.button.getY());

    float finalRadius = Math.max(cx, cy) * 1.2f;

    Animator reveal = ViewAnimationUtils
            .createCircularReveal(mBinding.reveal, x, y, getFabWidth(), finalRadius);

    reveal.setDuration(350);
    reveal.addListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            finish();
        }
    });

    reveal.start();
}

private void fadeOutProgressDialog() {
    mBinding.progressBar.animate().alpha(0f).setDuration(200).start();
}

private void delayedStartNextActivity() {
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SignInActivity.this, ScrollingActivity.class));
        }
    }, 100);
}

    private void loadImageAndSetNavBarColor() {
        Glide.with(this).load(R.drawable.mountais).into(new ImageViewTarget<GlideDrawable>(mBinding.background) {
            @Override
            protected void setResource(GlideDrawable resource) {
                Bitmap b = ((GlideBitmapDrawable) resource.getCurrent()).getBitmap();
                mBinding.background.setImageBitmap(b);

                Palette p = Palette.from(b).generate();
                int c = p.getDarkMutedColor(Color.parseColor("#000000"));

                getWindow().setNavigationBarColor(c);
            }
        });
    }

}
