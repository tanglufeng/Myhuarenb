package com.abcs.sociax.t4.android.setting;

import android.os.Bundle;

import com.abcs.sociax.component.CustomTitle;
import com.abcs.sociax.component.LeftAndRightTitle;
import com.abcs.sociax.t4.android.ThinksnsAbscractActivity;
import com.abcs.sociax.t4.android.fragment.FragmentSociax;
import com.abcs.sociax.t4.android.fragment.FrgamnetFeedBackType;
import com.abcs.sociax.android.R;

/** 
 * 类说明：   反馈类型
 * @author  wz    
 * @date    2015-1-26
 * @version 1.0
 */
public class ActivityFeedBackType extends ThinksnsAbscractActivity{
	FragmentSociax fg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		fg=new FrgamnetFeedBackType();
		fragmentTransaction.add(R.id.ll_content, fg);
		fragmentTransaction.commit();
	}

	@Override
	public String getTitleCenter() {
		return "反馈类型";
	}

	@Override
	protected CustomTitle setCustomTitle() {
		return new LeftAndRightTitle(R.drawable.img_back, this);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_common;
	}
}
