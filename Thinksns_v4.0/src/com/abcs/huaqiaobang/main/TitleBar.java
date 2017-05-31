package com.abcs.huaqiaobang.main;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.MainFragmentLayout;

import java.util.ArrayList;

public class TitleBar {
    MainActivity activity;
    TextView[] views;
    ImageView[] img;
    ViewPager vpContent;
    View titlebar;
    public int[] shows = {View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE};
    int barCount;
    private LinearLayout grps;

    public TitleBar(ViewPager vp, View bar, MainActivity activity) {
        vpContent = vp;
        titlebar = bar;
        this.activity = activity;
        initData(bar);

    }

    private void initData(View v) {
        grps = (LinearLayout) v.findViewById(R.id.tljr_grp_bottom);
        Log.i("tga", "=====" + grps.getChildCount());
        img = new ImageView[grps.getChildCount()];
        views = new TextView[grps.getChildCount()];
    }

    public void initTextView() {

        for (int i = 0; i < shows.length; i++) {
            barCount += (shows[i] == View.VISIBLE ? 1 : 0);
        }

        for (int i = 0; i < grps.getChildCount(); i++) {


            LinearLayout layout = (LinearLayout) grps.getChildAt(i);
            layout.setVisibility(shows[i]);
            final int m = i;
            layout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean f = (m == views.length - 1);
                    if (activity.maintype_layout_list.size() != 0) {
                        MainFragmentLayout mf = activity.maintype_layout_list.get(m);
                        f = activity.type_str[3].equals(mf.getKey());
                    }
                    if (f) {
                        if (MyApplication.getInstance().self == null) {
                            activity.login();
                            return;
                        } else {
//                            activity.my.show();
//                            return;
                        }
                    }
                    vpContent.setCurrentItem(m);
                }
            });
            img[i] = (ImageView) layout.getChildAt(0);
            views[i] = (TextView) layout.getChildAt(1);
        }
//        img[0].setImageResource(R.drawable.img_shouye1);
//        views[0].setTextColor(Color.parseColor("#555555"));
        if (activity.maintype_layout_list.size() != 0) {
            for (MainFragmentLayout mf : activity.maintype_layout_list) {
                activity.type_str[0].equals(mf.getKey());
                onPageSelected(Integer.valueOf(mf.getPosition()));
            }
        } else {
            onPageSelected(0);
        }
    }

    public void onPageSelected(int selectInd) {
        for (int i = 0; i < views.length; i++) {
            views[i].setTextColor(Color.parseColor("#555555"));
        }
        views[selectInd].setTextColor(Color.parseColor("#ed3535"));
        if (activity.maintype_layout_list.size() != 0) {
            sortLayout(activity.maintype_layout_list, selectInd);

        } else {
            defalutLayout(selectInd);
        }
    }

    private void sortLayout(ArrayList<MainFragmentLayout> maintype_layout_list, int selectInd) {


        for (int i = 0; i < maintype_layout_list.size(); i++) {
            MainFragmentLayout mf = maintype_layout_list.get(i);
            views[Integer.valueOf(mf.getPosition())].setText(mf.getName());
            if (activity.type_str[0].equals(mf.getKey()) && selectInd != Integer.valueOf(mf.getPosition())) {
                img[Integer.valueOf(mf.getPosition())].setImageResource(R.drawable.img_shouye2);
                continue;
            } else if (activity.type_str[0].equals(mf.getKey()) && selectInd == Integer.valueOf(mf.getPosition())) {
                img[Integer.valueOf(mf.getPosition())].setImageResource(R.drawable.img_shouye1);
                continue;
            }


            if (activity.type_str[1].equals(mf.getKey()) && selectInd != Integer.valueOf(mf.getPosition())) {

                img[Integer.valueOf(mf.getPosition())].setImageResource(R.drawable.img_dingqi2);
                continue;
            } else if (activity.type_str[1].equals(mf.getKey()) && selectInd == Integer.valueOf(mf.getPosition())) {
                img[Integer.valueOf(mf.getPosition())].setImageResource(R.drawable.img_dingqi1);
                continue;
            }
            if (activity.type_str[2].equals(mf.getKey()) && selectInd != Integer.valueOf(mf.getPosition())) {
                img[Integer.valueOf(mf.getPosition())].setImageResource(R.drawable.img_xinwen2);
                continue;
            } else if (activity.type_str[2].equals(mf.getKey()) && selectInd == Integer.valueOf(mf.getPosition())) {
                img[Integer.valueOf(mf.getPosition())].setImageResource(R.drawable.img_xinwen1);
                continue;
            }
            if (activity.type_str[3].equals(mf.getKey()) && selectInd != Integer.valueOf(mf.getPosition())) {
                img[Integer.valueOf(mf.getPosition())].setImageResource(R.drawable.img_geren2);
                continue;
            } else if (activity.type_str[3].equals(mf.getKey()) && selectInd == Integer.valueOf(mf.getPosition())) {
                img[Integer.valueOf(mf.getPosition())].setImageResource(R.drawable.img_geren1);
                continue;
            }

        }

    }

    private void defalutLayout(int selectInd) {
        img[0].setImageResource(R.drawable.img_shouye2);
        img[2].setImageResource(R.drawable.img_xinwen2);
        img[3].setImageResource(R.drawable.img_dingqi2);
        img[1].setImageResource(R.drawable.img_haiwaigou2);
        img[4].setImageResource(R.drawable.img_geren2);
        switch (selectInd) {
            case 0:
                img[0].setImageResource(R.drawable.img_shouye1);
                break;
            case 3:
                img[3].setImageResource(R.drawable.img_dingqi1);
                break;
            case 2:

                img[2].setImageResource(R.drawable.img_xinwen1);
                break;
            case 1:
                img[1].setImageResource(R.drawable.img_haiwaigou1);
                break;
            case 4:
                img[4].setImageResource(R.drawable.img_geren1);
                break;

            default:
                break;
        }
    }
}
