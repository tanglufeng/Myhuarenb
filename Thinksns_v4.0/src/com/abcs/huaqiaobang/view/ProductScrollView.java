package com.abcs.huaqiaobang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ProductScrollView extends ScrollView {

	public ProductScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	private OnScrollToBottomListener onScrollToBottom;

	public ProductScrollView(Context context) {
		super(context);
	}

	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		if (scrollY != 0 && null != onScrollToBottom) {
			onScrollToBottom.onScrollBottomListener(clampedY,scrollY);
		}
	}

	public void setOnScrollToBottomLintener(OnScrollToBottomListener listener) {
		onScrollToBottom = listener;
	}

	public interface OnScrollToBottomListener {
		public void onScrollBottomListener(boolean isBottom, int scrollY);
	}

}
