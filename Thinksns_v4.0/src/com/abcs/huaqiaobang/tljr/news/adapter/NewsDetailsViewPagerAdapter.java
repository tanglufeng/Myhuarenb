package com.abcs.huaqiaobang.tljr.news.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class NewsDetailsViewPagerAdapter extends FragmentPagerAdapter{
	
	private List<Fragment> fragments; 
	
	public NewsDetailsViewPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.fragments = fragments;
		
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub 
		return fragments.get(arg0);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}
	
	 
	 

}
