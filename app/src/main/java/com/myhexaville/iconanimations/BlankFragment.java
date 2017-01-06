package com.myhexaville.iconanimations;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.myhexaville.iconanimations.databinding.FragmentBlankBinding;


public class BlankFragment extends Fragment {
    private FragmentBlankBinding mBinding;

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_blank, container, false);

        mBinding.circleLinear.setInterpolator(new LinearInterpolator());
        mBinding.circleAccelerate.setInterpolator(new AccelerateInterpolator());
        mBinding.circleDecelerate.setInterpolator(new DecelerateInterpolator());
        mBinding.circleAccelerateDecelerate.setInterpolator(new AccelerateDecelerateInterpolator());

        mBinding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.circleLinear.showAnimation();
                mBinding.circleAccelerate.showAnimation();
                mBinding.circleDecelerate.showAnimation();
                mBinding.circleAccelerateDecelerate.showAnimation();
            }
        });

        return mBinding.getRoot();
    }
}
