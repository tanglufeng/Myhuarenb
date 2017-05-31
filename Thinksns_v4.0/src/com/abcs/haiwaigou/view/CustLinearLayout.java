package com.abcs.haiwaigou.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.huaqiaobang.util.LogUtil;
import com.astuetz.PagerSlidingTabStrip;


public class CustLinearLayout extends LinearLayout {
	boolean allowDragTop = true; //如果是true，则允许拖动至底部的下一页
	float downY = 0;
	private int downX;
	boolean needConsumeTouch = true; // 是否需要承包touch事件，needConsumeTouch一旦被定性，则不会更改
	private int mTouchSlop;
	public CustLinearLayout(Context arg0) {
		this(arg0, null);
		mTouchSlop = ViewConfiguration.get(arg0).getScaledTouchSlop();
	}

	public CustLinearLayout(Context arg0, AttributeSet arg1) {
		this(arg0, arg1, 0);
		mTouchSlop = ViewConfiguration.get(arg0).getScaledTouchSlop();
	}

	public CustLinearLayout(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
		mTouchSlop = ViewConfiguration.get(arg0).getScaledTouchSlop();
	}




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
//				if(Math.max(moveY,downY)-Math.min(moveY,downY)<Math.max(moveX,downX)-Math.min(moveX, downX)){
//					return  super.onInterceptTouchEvent(e);
//				}

				if (Math.abs(moveY - downY) > mTouchSlop) {
					return    super.onInterceptTouchEvent(e);
				}
		}
		return super.onInterceptTouchEvent(e);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			downY = ev.getRawY();
			needConsumeTouch = true; //默认情况下，listView内部的滚动优先，默认情况下由该listView去消费touch事件
			allowDragTop = isAtTop();
		} else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			ViewPager viewPager= (ViewPager) getChildAt(2);
//			PagerSlidingTabStrip pagerSlidingTabStrip= (PagerSlidingTabStrip) linearLayout.getChildAt(0);
			RelativeLayout relativeLayout= (RelativeLayout) viewPager.getChildAt(0);
			ScrollView scrollView= (ScrollView) relativeLayout.getChildAt(0);

//			RelativeLayout relative_comment= (RelativeLayout) viewPager.getChildAt(1);
//			FrameLayout frameLayout= (FrameLayout) relative_comment.getChildAt(1);
//			RelativeLayout relative1= (RelativeLayout) frameLayout.getChildAt(0);
			LogUtil.e("zjz", "which_isvisible=" + GoodsDetailActivity.getIsVisible());
			if (!needConsumeTouch) {
				// 在最顶端且向上拉了，则这个touch事件交给父类去处理
				getParent().requestDisallowInterceptTouchEvent(false);
				return false;
			}

			else if (GoodsDetailActivity.getIsVisible().equals(GoodsDetailActivity.DETAIL)&&allowDragTop&&scrollView.getScrollY()==0) {
				//needConsumeTouch尚未被定性，此处给其定性
				// 允许拖动到底部的下一页，而且又向上拖动了，就将touch事件交给父view
				if (ev.getRawY() - downY > 2) {
					//flag设置，由父类去消费
					needConsumeTouch = false;
					getParent().requestDisallowInterceptTouchEvent(false);
					return false;
				}
			}
			else if ((GoodsDetailActivity.getIsVisible().equals(GoodsDetailActivity.COMMENT))
					||(GoodsDetailActivity.getIsVisible().equals(GoodsDetailActivity.ALLCOMMENT))
					||(GoodsDetailActivity.getIsVisible().equals(GoodsDetailActivity.GOODCOMMENT))
					||(GoodsDetailActivity.getIsVisible().equals(GoodsDetailActivity.BADCOMMENT))
					||(GoodsDetailActivity.getIsVisible().equals(GoodsDetailActivity.MIDDLECOMMENT))
					||(GoodsDetailActivity.getIsVisible().equals(GoodsDetailActivity.IMGCOMMENT))
					&&allowDragTop) {
				//needConsumeTouch尚未被定性，此处给其定性
				// 允许拖动到底部的下一页，而且又向上拖动了，就将touch事件交给父view
				if (ev.getRawY() - downY > 2) {
					//flag设置，由父类去消费
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

	/**
	 * 判断listView是否在顶部
	 *
	 * @return 是否在顶部
	 */
	private boolean isAtTop() {
		return getScrollY() == 0;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(event);
	}
}
