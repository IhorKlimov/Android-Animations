package com.myhexaville.iconanimations;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.databinding.DataBindingUtil;

import com.myhexaville.iconanimations.databinding.ActivityMain2Binding;


public class Main2Activity extends AppCompatActivity {

    private ActivityMain2Binding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main2);
        setSupportActionBar(mBinding.toolbar);


    }
    public void gooey(View view) {
        Drawable d = mBinding.gooey.getBackground();
        if (d instanceof AnimatedVectorDrawable) {
            ((AnimatedVectorDrawable) d).start();
        }
    }

    public void dot(View view) {

    }
}