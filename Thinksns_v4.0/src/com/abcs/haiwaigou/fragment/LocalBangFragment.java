package com.abcs.haiwaigou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/3/8.
 */
public class LocalBangFragment extends Fragment {
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.hwg_local_bang, null);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.reset(this);
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
