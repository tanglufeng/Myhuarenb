package com.abcs.haiwaigou.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


//解决
//java.lang.IllegalArgumentException: pointerIndex out of range pointerIndex=-1 pointerCount=1
//在做多点触控放大缩小，操作自己所绘制的图形时发生这个异常，如果是操作图片的放大缩小多点触控不会出现这个错误
// 这个bug是Android系统原因 ,捕获IllegalArgumentException异常即可，因此需要重写ViewPager
//PhotoView会和ViewPager冲突，解决方案为
public class HackyViewPager extends ViewPager {

    private boolean isLocked;

    public HackyViewPager(Context context) {
        super(context);
        isLocked = false;
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        isLocked = false;
    }


    //在onInterceptTouchEvent事件里面捕获
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isLocked) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    //锁定onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !isLocked && super.onTouchEvent(event);
    }

    public void toggleLock() {
        isLocked = !isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isLocked() {
        return isLocked;
    }
}
