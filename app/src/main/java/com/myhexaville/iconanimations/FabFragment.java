package com.myhexaville.iconanimations;


import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FabFragment extends Fragment {


    public FabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_fab, container, false);

        inflate.findViewById(R.id.gooey)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Drawable d = v.getBackground();
                        if (d instanceof AnimatedVectorDrawable) {
                            ((AnimatedVectorDrawable) d).start();
                        }
                    }
                });

        return inflate;
    }
}
