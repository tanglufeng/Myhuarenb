package com.abcs.haiwaigou.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

public class CustScrollView extends ScrollView {
	boolean allowDragBottom = true; //如果是true，则允许拖动至底部的下一页
	private int downX;
	float downY = 0;
	private int mTouchSlop;
	boolean needConsumeTouch = true; // 是否需要承包touch事件，needConsumeTouch一旦被定性，则不会更改��򲻻���

	public CustScrollView(Context arg0) {
		this(arg0, null);
		mTouchSlop = ViewConfiguration.get(arg0).getScaledTouchSlop();
	}

	public CustScrollView(Context arg0, AttributeSet arg1) {
		this(arg0, arg1, 0);
		mTouchSlop = ViewConfiguration.get(arg0).getScaledTouchSlop();
	}

	public CustScrollView(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
		mTouchSlop = ViewConfiguration.get(arg0).getScaledTouchSlop();
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
				int moveX = (int) e.getRawX();
				//解决与viewpager冲突
				if(Math.max(moveY,downY)-Math.min(moveY,downY)<Math.max(moveX,downX)-Math.min(moveX, downX)){
					return  super.onInterceptTouchEvent(e);
				}

				if (Math.abs(moveY - downY) > mTouchSlop) {
					return true;
				}
		}
		return super.onInterceptTouchEvent(e);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			downY = ev.getRawY();
			needConsumeTouch = true; // 默认情况下，scrollView内部的滚动优先，默认情况下由该ScrollView去消费touch事件�

			if (getScrollY() + getMeasuredHeight() >= computeVerticalScrollRange() - 2) {
				// 允许向上拖动底部的下一页
				allowDragBottom = true;
			} else {
				// 不允许向上拖动底部的下一页
				allowDragBottom = false;
			}
		} else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			if (!needConsumeTouch) {
				//  在最顶端且向上拉了，则这个touch事件交给父类去处理�
				getParent().requestDisallowInterceptTouchEvent(false);
				return false;
			} else if (allowDragBottom) {
				//  needConsumeTouch尚未被定性，此处给其定性
				// 允许拖动到底部的下一页，而且又向上拖动了，就将touch事件交给父view���view
				if (downY - ev.getRawY() > 2) {
					// flag设置，由父类去消费
					needConsumeTouch = false;
					getParent().requestDisallowInterceptTouchEvent(false);
					return false;
				}
			}
		}

		// 通知父view是否要处理touch事件
		getParent().requestDisallowInterceptTouchEvent(needConsumeTouch);
		return super.dispatchTouchEvent(ev);
	}
}
