package com.abcs.sociax.t4.android.weibo;

import android.os.Bundle;

import com.abcs.sociax.component.CustomTitle;
import com.abcs.sociax.component.LeftAndRightTitle;
import com.abcs.sociax.listener.OnTouchListListener;
import com.abcs.sociax.t4.android.ThinksnsAbscractActivity;
import com.abcs.sociax.t4.android.fragment.FragmentCommentMeWeibo;
import com.abcs.sociax.android.R;

/**
 * 类说明： 评论我的
 * type : 1--微博评论我的 2--微吧评论我的
 */
public class ActivityCommentMeWeibo extends ThinksnsAbscractActivity {
	FragmentCommentMeWeibo fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragment = FragmentCommentMeWeibo.newInstance(getIntent().getIntExtra("type", 1));
		fragmentTransaction.add(R.id.ll_content, fragment);
		fragmentTransaction.commit();
	}
	
	@Override
	public String getTitleCenter() {
		return "评论我的";
	}

	@Override
	protected CustomTitle setCustomTitle() {
		return new LeftAndRightTitle(R.drawable.img_back, this);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_common;
	}

	@Override
	public OnTouchListListener getListView() {
		return null;//fragment.getListView();
	}

	@Override
	public void refreshHeader() {
//		fragment.doRefreshHeader();
	}

	@Override
	public void refreshFooter() {
//		fragment.doRefreshFooter();
	}

}
