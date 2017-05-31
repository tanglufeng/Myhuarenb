package com.abcs.haiwaigou.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;

/**
 * Created by zjz on 2016/6/23 0023.
 */
public class CommentRecyclerView extends RecyclerView {
    boolean allowDragTop = true; //如果是true，则允许拖动至底部的下一页
    float downY = 0;
    private int downX;
    boolean needConsumeTouch = true; // 是否需要承包touch事件，needConsumeTouch一旦被定性，则不会更改
    private int mTouchSlop;
    public CommentRecyclerView(Context context) {
        super(context);
    }

    public CommentRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downY = ev.getRawY();
            needConsumeTouch = true; //默认情况下，listView内部的滚动优先，默认情况下由该listView去消费touch事件
            allowDragTop = isAtTop();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (!needConsumeTouch) {
                // 在最顶端且向上拉了，则这个touch事件交给父类去处理
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }

        }

        // 通知父view是否要处理touch事件
        getParent().requestDisallowInterceptTouchEvent(needConsumeTouch);
        return super.dispatchTouchEvent(ev);
    }
    private boolean isAtTop() {
        return getScrollY() == 0;
    }
}
