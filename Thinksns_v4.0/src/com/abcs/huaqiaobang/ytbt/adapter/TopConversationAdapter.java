package com.abcs.huaqiaobang.ytbt.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.bean.GroupBean;
import com.abcs.huaqiaobang.ytbt.bean.MsgBean;
import com.abcs.huaqiaobang.ytbt.bean.TopConversationBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.bean.UserBean;
import com.abcs.huaqiaobang.ytbt.util.CircleImageView;
import com.abcs.huaqiaobang.ytbt.util.MsgTimeUtil;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/26.
 */
public class TopConversationAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<UserBean> userBeans;
	private ArrayList<User> userlist;
	private ArrayList<MsgBean> list = new ArrayList<>();
	private List<TopConversationBean> topconversationBeans;
	User user;
	GroupBean group;

	public void setList(List<TopConversationBean> topconversationBeans) {
//		list.clear();
//		for (int i = 0; i < msgBeans.size(); i++) {
//			if (msgBeans.get(i).getFlag() == 0) {
//				list.add(msgBeans.get(i));
//				MsgBean bean = msgBeans.get(i);
//				bean.setFlag(1);
//				msgBeans.set(i, bean);
//			}
//			;
//		}
		//MyApplication.getInstance().setMsgBeans(msgBeans);
		this.topconversationBeans=topconversationBeans;
		notifyDataSetChanged();
	}

	public ArrayList<MsgBean> getList() {
		return list;
	}

	public TopConversationAdapter(Context context,
			List<TopConversationBean> topconversationBeans) {
		this.context = context;
		this.topconversationBeans = topconversationBeans;
	}

	@Override
	public int getCount() {
		return topconversationBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return topconversationBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TopConversationBean c = topconversationBeans.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.conversation_listitem, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.msg = (TextView) convertView.findViewById(R.id.msg);
			holder.hint = (TextView) convertView.findViewById(R.id.hint);
			holder.msglasttime = (TextView) convertView
					.findViewById(R.id.lastmsgtime);
			holder.avatar = (CircleImageView) convertView
					.findViewById(R.id.avatar);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		holder.msg.setText(c.getMsg());
		holder.msglasttime.setText(MsgTimeUtil.getShowconversationTime(c
				.getMsglasttime()));
		if(c.getUnread()!=0){
			holder.hint.setText(c.getUnread()+"");
			holder.hint.setVisibility(View.VISIBLE);
		}else holder.hint.setVisibility(View.GONE);
		if (c.isIsgroup() == true) {
			try {
				group = MyApplication.dbUtils.findFirst(Selector.from(GroupBean.class).where("groupId", "=", c.getMsgto()));
				JSONObject object = new JSONObject(c.getMsgfrom()==null?"":c.getMsgfrom());
				MyApplication.bitmapUtils.display(holder.avatar,group == null ? object.optString("avatar") : group.getGroupAvatar());
				holder.name.setText(group == null ? object.optString("nickname","群组") : group.getGroupName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;

		} else {
			try {
				user = MyApplication.dbUtils.findFirst(Selector.from(User.class).where("voipAccout", "=", c.getMsgto()));
				if (user != null) {
					MyApplication.bitmapUtils.display(holder.avatar,user.getAvatar());
					holder.name.setText(user.getRemark().trim().equals("") ? user.getNickname() : user.getRemark());
				} else {
						try {
							JSONObject object = new JSONObject(c.getMsgfrom());
							holder.name.setText(object.optString("nickname"));
							if (object.optString("avatar", "").equals("")) {
								holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.img_qunzu));
							} else
								MyApplication.bitmapUtils.display(holder.avatar,object.optString("avatar"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
				}
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView msg;
		TextView msglasttime,hint;
		CircleImageView avatar;
	}


}
