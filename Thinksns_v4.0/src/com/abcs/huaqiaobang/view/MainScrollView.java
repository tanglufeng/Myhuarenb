package com.abcs.huaqiaobang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2015/12/18.
 */
public class MainScrollView extends ScrollView{
    public MainScrollView(Context context) {
        super(context);
    }

    private OnScroll listener;
    private int y;
    private int downX;
    private int downY;
    private int mTouchSlop;

    public MainScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    //解决Android 5以上scrollview嵌套recyclerview滑动时产生的粘贴状况
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if(listener!=null){
            listener.onScrollListener(l,t,oldl,oldt);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {


        return super.onTouchEvent(ev);
    }

    public interface OnScroll{
        public void onScrollListener(int x, int y, int oldx, int oldy);
    }

    public void setOnScroll(OnScroll listener){
        this.listener=listener;
    }

}
