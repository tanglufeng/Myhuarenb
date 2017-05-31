package com.abcs.huaqiaobang.ytbt.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.util.CircleImageView;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

	private Context context;

	private ArrayList<User> members;

	public GridViewAdapter(Context context, ArrayList<User> members) {
		this.context = context;
		// User user=new User();
		// user.setAvatar("");
		// user.setNickname("");
		// members.add(user);
		this.members = members;
		// TODO Auto-generated constructor stub

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return members.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		User user = members.get(position);
		convertView = LayoutInflater.from(context).inflate(
				R.layout.member_gridview_item, null);
		CircleImageView memberavatar = (CircleImageView) convertView
				.findViewById(R.id.member_avatar);
		TextView membername = (TextView) convertView
				.findViewById(R.id.nickname);
		if (position == members.size()) {
		} else {
			MyApplication.bitmapUtils.display(memberavatar,user.getAvatar());
			String name = "";
			if(!(user.getRemark()== null)){
				name = TextUtils.isEmpty(user.getRemark().trim())?user.getNickname():user.getRemark();
			}else name = user.getNickname();
			membername.setText(name);
		}
		return convertView;
	}
}
