package com.abcs.haiwaigou.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment {

	/** Fragment当前状态是否可见 */
	protected boolean isVisible;
	
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		
		if(getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}
	
	
	/**
	 * 可见
	 */
	protected void onVisible() {
		lazyLoad();		
	}
	
	
	/**
	 * 不可见
	 */
	protected void onInvisible() {
		
		
	}
	
	
	/** 
	 * 延迟加载
	 * 子类必须重写此方法
	 */
	protected abstract void lazyLoad();

	public Toast toast;

	public void showToast(Context context,String msg) {

		if (toast != null) {
			toast.setText(msg);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();
		} else {
			toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}