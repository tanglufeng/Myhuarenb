package com.abcs.haiwaigou.view;

/**
 * 自定义ListView，可设置滚动到顶端时的操作
 * Created by zjz on 2016/1/26.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

@SuppressLint("NewApi")
public class MyGridView extends GridViewWithHeaderAndFooter {

    public interface OnGridScroll2TopListener {
        void scroll2Top();
    }

    private OnGridScroll2TopListener listener;

    public void setOnGridScroll2TopListener(OnGridScroll2TopListener listener) {
        this.listener = listener;
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        if (listener != null) {
            listener.scroll2Top();
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.e("MyListView", " t: " + t
                + " oldt: " + oldt + ": " +
                getScrollY());
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
