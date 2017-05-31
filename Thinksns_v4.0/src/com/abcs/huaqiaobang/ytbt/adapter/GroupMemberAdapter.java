package com.abcs.huaqiaobang.ytbt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.yuntongxun.ecsdk.im.ECGroupMember;

import java.util.List;

public class GroupMemberAdapter extends BaseAdapter {

	private Context context;
	
	private List<ECGroupMember> members;

	public GroupMemberAdapter(Context context,
			List<ECGroupMember> members) {
				this.context = context;
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
		return members.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView=LayoutInflater.from(context).inflate(R.layout.group_listview_item, null);
		TextView groupName=(TextView) convertView.findViewById(R.id.name);
		TextView groupId=(TextView) convertView.findViewById(R.id.groupid);
		groupName.setText(members.get(position).getVoipAccount());
		if(members.get(position).getRole()==1){
			groupId.setText("群主");
		}if(members.get(position).getRole()==2){
			groupId.setText("管理员");
		}else{
			groupId.setText("成员");
		}	
		return convertView;
	}

}
