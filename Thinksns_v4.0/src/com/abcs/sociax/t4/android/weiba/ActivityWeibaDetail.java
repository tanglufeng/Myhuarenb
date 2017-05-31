package com.abcs.sociax.t4.android.weiba;

import android.os.Bundle;
import com.abcs.sociax.android.R;
import com.abcs.sociax.component.CustomTitle;
import com.abcs.sociax.component.LeftAndRightTitle;
import com.abcs.sociax.t4.android.ThinksnsAbscractActivity;
import com.abcs.sociax.t4.android.fragment.FragmentWeibaDetail;
import com.abcs.sociax.t4.model.ModelWeiba;

/** 
 * 类说明：微吧详情
 *
 */
public class ActivityWeibaDetail extends ThinksnsAbscractActivity {
	private String title = "微吧详情";
	private ModelWeiba weiba;

	private FragmentWeibaDetail fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initIntentData();
		super.onCreate(savedInstanceState);
		initFragment();
	}

	private void initIntentData() {
		weiba = (ModelWeiba)getIntent().getSerializableExtra("weiba");
	}
	/**
	 * 根据传入的title获取对应的fragment
	 */
	private void initFragment() {
		fragment = FragmentWeibaDetail.newInstance(weiba);
		fragmentTransaction.add(R.id.ll_content, fragment);
		fragmentTransaction.commit();
	}

	@Override
	public String getTitleCenter() {
		if(weiba != null)
			return weiba.getWeiba_name();
		return title;
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
	public void refreshHeader() {
		fragment.doRefreshHeader();
	}

	@Override
	public void refreshFooter() {
		fragment.doRefreshFooter();
	}
}
