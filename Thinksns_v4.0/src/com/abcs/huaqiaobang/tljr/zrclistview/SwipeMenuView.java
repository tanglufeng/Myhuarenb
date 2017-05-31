package com.abcs.huaqiaobang.tljr.zrclistview;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

import java.util.List;

/**
 * 
 * @author baoyz
 * @date 2014-8-23
 * 
 */
public class SwipeMenuView extends LinearLayout implements OnClickListener {

	private ZrcListView mListView;
	private SwipeMenuLayout mLayout;
	private SwipeMenu mMenu;
	private OnSwipeItemClickListener onItemClickListener;
	private int position;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public SwipeMenuView(SwipeMenu menu, ZrcListView listView) {
		super(menu.getContext());
		mListView = listView;
		mMenu = menu;
		List<SwipeMenuItem> items = menu.getMenuItems();
		int id = 0;
		for (SwipeMenuItem item : items) {
			addItem(item, id++);
		}
	}

	private void addItem(SwipeMenuItem item, int id) {
		LayoutParams params = new LayoutParams(item.getWidth(),
				LayoutParams.MATCH_PARENT);
		LinearLayout parent = new LinearLayout(getContext());
		
	 
		 
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view_text=inflater.inflate(R.layout.tljr_item_news_menuitem, null);
		view_text.setOnClickListener(this);
		view_text.setLayoutParams(params);
		((LinearLayout) view_text).setGravity(Gravity.CENTER);
		view_text.setId(id);
		
		
		parent.setId(id);
		parent.setGravity(Gravity.CENTER);
		parent.setOrientation(LinearLayout.VERTICAL);
		parent.setLayoutParams(params);
		parent.setBackgroundDrawable(item.getBackground());
		parent.setOnClickListener(this);
	 //	addView(view_text);
		addView(parent);
	
		
		
		/*
		 * 添加自己新的layout进去容易控制，不要用这个
		 */
		
		
 		if (item.getIcon() != null) {
 		parent.addView(createIcon(item));
 		 
 	}

		
	 
			parent.addView(createTitle(item));
		 
		 
		
		

		  

	}

	private ImageView createIcon(SwipeMenuItem item) {
//		ImageView iv = new ImageView(getContext());
//		item.set
//		iv.setScaleType(ScaleType.FIT_XY);
//		iv.setLayoutParams(new android.view.ViewGroup.LayoutParams(80, 80));
//	
//		if(item.getType()==1){
//			if(item.isCollect()){
//				iv.setImageDrawable(getResources().getDrawable(R.drawable.img_shoucang1));
//			}else{
//				iv.setImageDrawable(item.getIcon());
//			}
//		}else{
//			iv.setImageDrawable(item.getIcon());
//		}
		
		LayoutInflater inflater = LayoutInflater.from(getContext());
		LinearLayout view_img=(LinearLayout) inflater.inflate(R.layout.tljr_item_news_menuitem, null);
		
		if(item.getType()==0){
			ImageView   	 tv = (ImageView)view_img.findViewById(R.id.iv_news_zan);
		 
				view_img.removeView(tv);
				  
				tv.setScaleType(ScaleType.FIT_XY);
 
				return tv;
		}else{
			ImageView     tv2 = (ImageView)view_img.findViewById(R.id.iv_news_collect);
		 
			view_img.removeView(tv2); 
			tv2.setScaleType(ScaleType.FIT_XY);
				return tv2;
		}
		
		 
	}

	private TextView createTitle(SwipeMenuItem item) {
		
		LayoutInflater inflater = LayoutInflater.from(getContext());
		LinearLayout view_text=(LinearLayout) inflater.inflate(R.layout.tljr_item_news_menuitem, null);
		
		if(item.getType()==0){
			TextView   	 tv = (TextView)view_text.findViewById(R.id.tljr_tv_zan);
		 
				view_text.removeView(tv);
				 

				//tv.setText(item.getTitle());
				tv.setGravity(Gravity.CENTER);
				tv.setTextSize(item.getTitleSize());
				tv.setTextColor(item.getTitleColor());
				return tv;
		}else{
			TextView     tv2 = (TextView)view_text.findViewById(R.id.tljr_tv_collect);
		 
				view_text.removeView(tv2);
				 

				//tv.setText(item.getTitle());
				tv2.setGravity(Gravity.CENTER);
				tv2.setTextSize(item.getTitleSize());
				tv2.setTextColor(item.getTitleColor());
				return tv2;
		}
		

		
		//tv.setLayoutParams(new android.view.ViewGroup.LayoutParams(30, 30));
		
	}

	@Override
	public void onClick(View v) {
		if (onItemClickListener != null && mLayout.isOpen()) {
			onItemClickListener.onItemClick(this, mMenu, v.getId());
		}
	}

	public OnSwipeItemClickListener getOnSwipeItemClickListener() {
		return onItemClickListener;
	}

	public void setOnSwipeItemClickListener(
			OnSwipeItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void setLayout(SwipeMenuLayout mLayout) {
		this.mLayout = mLayout;
	}

	public static interface OnSwipeItemClickListener {
		void onItemClick(SwipeMenuView view, SwipeMenu menu, int index);
	}
}
