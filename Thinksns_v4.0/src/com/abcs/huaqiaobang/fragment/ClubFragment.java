package com.abcs.huaqiaobang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.abcs.sociax.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhou on 2016/4/21.
 */
public class ClubFragment extends Fragment {


    @InjectView(R.id.layout)
    LinearLayout layout;
    @InjectView(R.id.club_layout)
    LinearLayout clubLayout;
    @InjectView(R.id.like_layout)
    LinearLayout likeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_club, null);

        ButterKnife.inject(this, view);


        initScrollView();
        initClub();
        initLike();


        return view;
    }

    private void initLike() {

        for (int i = 0; i < 3; i++) {
            likeLayout.addView(getLike());
        }


    }

    private void initClub() {
        for (int i = 0; i < 3; i++) {
            clubLayout.addView(getLike());
        }

    }

    private void initScrollView() {

        for (int i = 0; i < 5; i++) {

            layout.addView(getQunzi());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public View getLike() {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.club_item, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);

        return view;
    }

    public View getQunzi() {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.club_item2, null);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        view.setLayoutParams(layoutParams);

        return view;
    }
}
