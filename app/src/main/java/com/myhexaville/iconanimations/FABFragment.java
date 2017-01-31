package com.myhexaville.iconanimations;


import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return mBinding.getRoot();
    }


}
