package com.myhexaville.iconanimations;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        return mBinding.getRoot();
    }



}
