package com.abcs.sociax.t4.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class AdapterViewPagerMessage extends FragmentStatePagerAdapter {
	private List<Fragment> list;

	public AdapterViewPagerMessage(FragmentManager fragmentManager) {
		super(fragmentManager);
	}
	
	public void bindData(List<Fragment> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int position) {
		return list.get(position);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}
}
