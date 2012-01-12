package org.summo.blog.verticality;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public abstract class VerticalFragmentsPagerAdapter extends PagerAdapter {
    private static final String TAG = "VerticalFragmentsPagerAdapter";
    private static final boolean DEBUG = false;

    private final FragmentManager mFragmentManager;
    private final int mRows;
    private FragmentTransaction mCurTransaction = null;
    private ArrayList<Fragment> mCurrentPrimaryItem = null;

    public VerticalFragmentsPagerAdapter(FragmentManager fm, int rows) {
        mFragmentManager = fm;
        mRows = rows;
        mCurrentPrimaryItem = new ArrayList<Fragment>(mRows);
    }

    /**
     * Return the Fragments associated with a specified position.
     */
    public abstract ArrayList<Fragment> getItem(int position);

    @Override
    public void startUpdate(ViewGroup container) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        Context ctx = container.getContext();

        LayoutInflater inflater =
                (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.pager, null);
        layout.setId(123 + position);

        LinearLayout ll = (LinearLayout) layout.findViewById(R.id.pager_linear_layout);
        ll.setId(1234 + position);

        ((ViewPager) container).addView(layout);

        Fragment firstFragment =
                mFragmentManager.findFragmentByTag(
                        makeFragmentName(container.getId(), position, 0));

        ArrayList<Fragment> frags = null;

        if (firstFragment != null) {

            frags = new ArrayList<Fragment>(mRows);

            for (int i = 0; i < mRows; ++i) {
                frags.add(mFragmentManager.findFragmentByTag(makeFragmentName(container.getId(), position, i)));
            }

            for (Fragment f : frags) {
                if (DEBUG) Log.v(TAG, "Attaching item #" + position + ": f=" + f);
                mCurTransaction.attach(f);
            }

        } else {

            frags = getItem(position);

            int currentRow = 0;
            for (Fragment f : frags) {
                if (DEBUG) Log.v(TAG, "instanciateItem / Adding item #" + position + ": f= " + f);
                mCurTransaction.add(ll.getId(), f,
                        makeFragmentName(container.getId(), position, currentRow));
                currentRow++;
            }

        }

        if (frags != mCurrentPrimaryItem) {
            for (Fragment f : frags) {
                f.setMenuVisibility(false);
                f.setUserVisibleHint(false);
            }
        }

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        for (int i = 0; i < mRows; ++i) {
            if (DEBUG) Log.v(TAG, "destroyItem / Detaching item #" + position + ": row=" + i);
            mCurTransaction.detach(
                mFragmentManager.findFragmentByTag(makeFragmentName(container.getId(), position, i)));
        }

        ((ViewPager) container).removeView((View)object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {

        ArrayList<Fragment> frags = new ArrayList<Fragment>(mRows);

        for (int i = 0; i < mRows; ++i) {
            frags.add(mFragmentManager.findFragmentByTag(makeFragmentName(container.getId(), position, i)));
        }

        if (!frags.equals(mCurrentPrimaryItem)) {

            if (!mCurrentPrimaryItem.isEmpty() && (mCurrentPrimaryItem.get(0) != null)) {
                for (Fragment f : mCurrentPrimaryItem) {
                    f.setMenuVisibility(false);
                    f.setUserVisibleHint(false);
                }
            }

            if (!frags.isEmpty() && (frags.get(0) != null)) {
                for (Fragment f : frags) {
                    f.setMenuVisibility(true);
                    f.setUserVisibleHint(true);
                }
            }

            mCurrentPrimaryItem = (ArrayList<Fragment>) frags.clone();
            if (DEBUG) Log.v(TAG, "setPrimaryItem #" + position + ": " + mCurrentPrimaryItem);
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    public static String makeFragmentName(int viewId, int index, int subindex) {
        return "android:switcher:" + viewId + ":" + index + ":" + subindex;
    }
}
