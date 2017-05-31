package com.abcs.haiwaigou.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zjz on 2016/3/14.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {




        // Add top margin only for the first item to avoid double space between items
        if ((parent.getChildLayoutPosition(view)%2) == 0) {
            outRect.top = 0;
            outRect.left = 0;
            outRect.right = space/2;
            outRect.bottom = space;
        } else {
            outRect.top = 0;
            outRect.left = space/2;
            outRect.right = 0;
            outRect.bottom = space;
        }
    }
}