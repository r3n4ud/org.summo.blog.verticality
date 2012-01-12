package org.summo.blog.verticality;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class Verticality extends FragmentActivity
{
    private int mPosition;
    private int mCount;
    private VerticalityAdapter mAdapter;
    private ViewPager mPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(Constants.LOGTAG, "Verticality.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verticality);

        // Gets the data required by the activity and/or the underlying fragments
        mPosition = 2;
        mCount = 10;

        // Initializes the pager adapter and associates it with the ViewPager
        mAdapter = new VerticalityAdapter(getSupportFragmentManager(), 2);
        mPager = (ViewPager) findViewById(R.id.verticality_pager);
        mPager.setAdapter(mAdapter);

        // Sets the current item displayed by the ViewPager
        mPager.setCurrentItem(mPosition);

        // Implements onPageChange to update the header progress bar
        mPager.setOnPageChangeListener(
            new ViewPager.SimpleOnPageChangeListener() {
                public void onPageSelected(int position) {

                    mPosition = position;

                }
            });

    }

    private class VerticalityAdapter extends VerticalFragmentsPagerAdapter {

        // A public constuctor is required
        public VerticalityAdapter(FragmentManager fm, int rows) {
            super(fm, rows);
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public ArrayList<Fragment> getItem(int position) {

            // Instanciates and returns the fragments to be displayed by the ViewPager
            ArrayList<Fragment> frags = new ArrayList<Fragment>(2);
            frags.add(FooFragment.newInstance(position, mCount));
            frags.add(BarFragment.newInstance(position, mCount));

            return frags;
        }

    }

}
