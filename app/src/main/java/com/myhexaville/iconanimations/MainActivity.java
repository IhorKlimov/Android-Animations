package com.myhexaville.iconanimations;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.myhexaville.iconanimations.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @SuppressLint("NewApi")
    public void animate(View view) {
        Drawable d = mBinding.icon.getDrawable();
        if (Build.VERSION.SDK_INT >= 21 && d instanceof AnimatedVectorDrawable) {
            Log.d(LOG_TAG, "animate: 1");
            ((AnimatedVectorDrawable) d ).start();
        } else if (d instanceof AnimatedVectorDrawableCompat) {
            Log.d(LOG_TAG, "animate: 2");
            ((AnimatedVectorDrawableCompat) d).start();
        }
    }
}
