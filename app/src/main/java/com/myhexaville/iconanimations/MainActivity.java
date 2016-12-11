package com.myhexaville.iconanimations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.RelativeLayout;

import com.myhexaville.iconanimations.databinding.ActivityMainBinding;

import static android.view.MotionEvent.ACTION_DOWN;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";
    private static final int FIRST_COLOR = Color.parseColor("#39dec5");
    private static final int SECOND_COLOR = Color.parseColor("#4648df");
    private int currentSelected = 0;


    // this ugly part comes from Custom Tab receiving only ACTION_DOWN, when to be sure
    // to animate we need ACTION_UP (tab selected)
    // that's why I start animation when page is selected and check if it was caused
    // by swiping - no reveal animation to show
    // by clicking - show reveal animation
    private int mTabClicked = -1;
    private float x;
    private float y;


    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBinding.toolbar);

        setupToolbarDimens();

//        setupSquare();

        setupPager();
    }

    private void setupPager() {
        mBinding.pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new BlankFragment();
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        mBinding.tabs.setupWithViewPager(mBinding.pager);

        setupTouchEvents();
    }

    private void setupTouchEvents() {
        mBinding.tabs.getTabAt(0).setCustomView(new CustomTab(this, 0));
        mBinding.tabs.getTabAt(0).getCustomView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == ACTION_DOWN
                        && mBinding.pager.getCurrentItem() != 0) {
                    mTabClicked = 0;
                    x = motionEvent.getRawX();
                    y = motionEvent.getRawY();
//                    ((CustomTab) view).setIsRevealing(true);
//                    reveal(motionEvent, 0);
                }
                return false;
            }
        });

        mBinding.tabs.getTabAt(1).setCustomView(new CustomTab(this, 1));
        mBinding.tabs.getTabAt(1).getCustomView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == ACTION_DOWN
                        && mBinding.pager.getCurrentItem() != 1) {
                    mTabClicked = 1;
                    x = motionEvent.getRawX();
                    y = motionEvent.getRawY();
//                    ((CustomTab) view).setIsRevealing(true);
//                    reveal(motionEvent, 1);
                }
                return false;
            }
        });

        mBinding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mTabClicked != -1) {
                    mTabClicked = -1;
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == mTabClicked) {
                    ((CustomTab) mBinding.tabs.getTabAt(position).getCustomView()).setIsRevealing(true);
                    reveal(position);
                } else if (position == 0
                        && !((CustomTab) mBinding.tabs.getTabAt(0).getCustomView()).isIsRevealing()) {
                    mBinding.background.setBackgroundColor(FIRST_COLOR);
                } else if (position == 1
                        && !((CustomTab) mBinding.tabs.getTabAt(1).getCustomView()).isIsRevealing()) {
                    mBinding.background.setBackgroundColor(SECOND_COLOR);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @SuppressLint("NewApi")
    public void animatePencil(View view) {
        Drawable d = mBinding.icon.getDrawable();
        if (Build.VERSION.SDK_INT >= 21 && d instanceof AnimatedVectorDrawable) {
            ((AnimatedVectorDrawable) d).start();
        } else if (d instanceof AnimatedVectorDrawableCompat) {
            ((AnimatedVectorDrawableCompat) d).start();
        }
    }

    public void reveal(final int tabPosition) {
        mBinding.reveal.setVisibility(View.VISIBLE);
        int cx = mBinding.reveal.getWidth();
        int cy = mBinding.reveal.getHeight();

        float finalRadius = Math.max(cx, cy) * 1.2f;
        Animator reveal = ViewAnimationUtils
                .createCircularReveal(mBinding.reveal, (int) x, (int) y, 0f, finalRadius);

        reveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                if (tabPosition == 0) {
                    mBinding.background.setBackgroundColor(FIRST_COLOR);
                    ((CustomTab) mBinding.tabs.getTabAt(0).getCustomView()).setIsRevealing(false);
                } else {
                    mBinding.background.setBackgroundColor(SECOND_COLOR);
                    ((CustomTab) mBinding.tabs.getTabAt(1).getCustomView()).setIsRevealing(false);
                }
                mBinding.reveal.setVisibility(View.INVISIBLE);
                mTabClicked = -1;
            }
        });

        if (tabPosition == 0) {
            mBinding.reveal.setBackgroundColor(FIRST_COLOR);
        } else {
            mBinding.reveal.setBackgroundColor(SECOND_COLOR);
        }
        reveal.start();
    }

    private void setupToolbarDimens() {
        int h = getStatusBarHeight() + getActionBarSIze();
        Log.d(LOG_TAG, "setupToolbarDimens: " + h);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(0, getStatusBarHeight(), 0, 0);

        mBinding.toolbar.setLayoutParams(params);


        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                getStatusBarHeight() + getActionBarSIze() * 2
        );
        mBinding.background.setLayoutParams(params2);
        mBinding.reveal.setLayoutParams(params2);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getActionBarSIze() {
        int result = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            result = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return result;
    }
}