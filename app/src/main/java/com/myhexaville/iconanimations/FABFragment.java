package com.myhexaville.iconanimations;


import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.myhexaville.iconanimations.databinding.FragmentFabBinding;


public class FABFragment extends Fragment {
    private static final String LOG_TAG = "FABFragment";
    private FragmentFabBinding mBinding;

    public FABFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_fab, container, false);
        mBinding.fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable d = mBinding.fab2.getDrawable();
                if (d instanceof AnimatedVectorDrawable) {
                    ((AnimatedVectorDrawable)d ).start();
                } else if (d instanceof AnimatedVectorDrawableCompat) {
                    ((AnimatedVectorDrawableCompat) d).start();
                }
            }
        });

        mBinding.heartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView v = (ImageView) view;
                Drawable d = v.getDrawable();
                if (d instanceof AnimatedVectorDrawable) {
                    AnimatedVectorDrawable avd = (AnimatedVectorDrawable) d;
                    avd.start();
                } else if (d instanceof AnimatedVectorDrawableCompat) {
                    AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) d;
                    avd.start();
                }
            }
        });
        return mBinding.getRoot();
    }


}
