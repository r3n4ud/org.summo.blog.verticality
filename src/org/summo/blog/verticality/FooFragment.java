package org.summo.blog.verticality;

import android.util.Log;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FooFragment extends Fragment {

    private int mPosition;
    private int mCount;

    public static FooFragment newInstance(int position, int count) {
        Log.d(Constants.LOGTAG, "FooFragment.newInstance");
        FooFragment f = new FooFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putInt("count", count);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(Constants.LOGTAG, "FooFragment.onCreate");
        super.onCreate(savedInstanceState);

        mPosition = getArguments().getInt("position");
        mCount = getArguments().getInt("count");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(Constants.LOGTAG, "FooFragment.onCreateView");
        View v = inflater.inflate(R.layout.foo, container, false);

        TextView tvFoo = (TextView) v.findViewById(R.id.tvFoo);
        tvFoo.setText(Constants.LOREM_IPSUM[mPosition]);

        return v;
    }

}
