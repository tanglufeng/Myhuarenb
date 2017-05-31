package com.abcs.huaqiaobang.ytbt.sortlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.util.CircleImageView;

import java.util.HashMap;
import java.util.List;

public class FriendsSortAdapter extends BaseAdapter implements SectionIndexer {
	private List<User> list = null;
	private Context mContext;
	private boolean hasCheckBox = false;
	// 用来控制CheckBox的选中状况
	private static HashMap<Integer, Boolean> isSelected;

	public FriendsSortAdapter(Context mContext, List<User> list) {
		this.mContext = mContext;
		this.list = list;
		isSelected = new HashMap<Integer, Boolean>();
		// 初始化数据
		initDate();
	}
	public void setHasCheckBox(boolean hasCheckBox) {
		this.hasCheckBox = hasCheckBox;
	}
	/**
	 * ListView仯,ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<User> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		FriendsSortAdapter.isSelected = isSelected;
	}

	// 初始化isSelected的数据
	private void initDate() {
		for (int i = 0; i < list.size(); i++) {
			getIsSelected().put(i, false);
		}
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final User user = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item1, null);
			viewHolder.avatar=(CircleImageView) view.findViewById(R.id.avatar);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.name);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.tvNum = (TextView) view.findViewById(R.id.groupid);
			viewHolder.cb = (CheckBox) view.findViewById(R.id.item_cb);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		// positionChar ascii
		int section = getSectionForPosition(position);

		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(user.getSortLetters());
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		MyApplication.bitmapUtils.display(viewHolder.avatar, list.get(position).getAvatar());
		viewHolder.tvTitle.setText(this.list.get(position).getNickname());
		if(this.list.get(position).getRemark().trim().equals("")){
			viewHolder.tvTitle.setText(this.list.get(position).getNickname());
		}else{
			viewHolder.tvTitle.setText(this.list.get(position).getRemark());
		}
		viewHolder.tvNum.setText(this.list.get(position).getUid()+"");
		if(!hasCheckBox){
			viewHolder.cb.setVisibility(View.GONE);
		}else{
			viewHolder.cb.setVisibility(View.VISIBLE);
			viewHolder.cb.setChecked(getIsSelected().get(position));  
		}
		return view;

	}

	public final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		TextView tvNum;
		CircleImageView avatar;
		public CheckBox cb;
	}

	/**
	 * ListViewλChar ascii
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * Char asciiγλ
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * #档
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// ж
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}