package com.abcs.huaqiaobang.ytbt.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.bean.AddFriendRequestBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.util.GlobalConstant;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AddFriendRequestAdapter extends BaseAdapter {
	private List<AddFriendRequestBean> list;

	public void setList() {
		try {
			List<AddFriendRequestBean> a = MyApplication.dbUtils
					.findAll(Selector.from(AddFriendRequestBean.class).orderBy(
							"time", true));
			if (a!=null) {
				list.clear();
				list.addAll(a);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		notifyDataSetChanged();
	}


	private LayoutInflater inflater;
	private Context context;
	private AddFriendRequestBean bean;

	public AddFriendRequestAdapter(List<AddFriendRequestBean> list,
			Context context) {
		super();
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		bean = list.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_addfriend_request,
					null);
			holder = new ViewHolder();
			holder.avadar = (ImageView) convertView.findViewById(R.id.avatar);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.notice = (TextView) convertView.findViewById(R.id.notice);
			holder.accept = (Button) convertView.findViewById(R.id.accept);
			holder.reject = (Button) convertView.findViewById(R.id.reject);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(bean.getNickname());
		MyApplication.bitmapUtils.display(holder.avadar, bean.getAvadar());
		switch (bean.getState()) {
		case 0:
			holder.notice.setVisibility(View.GONE);
			holder.accept.setVisibility(View.VISIBLE);
			holder.reject.setVisibility(View.VISIBLE);
			holder.accept.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					choicefrienduser(bean.getId(), 1);
				}
			});
			holder.reject.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					choicefrienduser(bean.getId(), 2);
				}
			});
			break;
		case 1:
			setAccept(holder);
			break;
		case 2:
			setReject(holder);
			break;
		}
		return convertView;
	}

	private void setReject(ViewHolder holder) {
		holder.accept.setVisibility(View.GONE);
		holder.reject.setVisibility(View.GONE);
		holder.notice.setVisibility(View.VISIBLE);
		holder.notice.setText("已拒绝");
	}

	private void setAccept(ViewHolder holder) {
		holder.accept.setVisibility(View.GONE);
		holder.reject.setVisibility(View.GONE);
		holder.notice.setVisibility(View.VISIBLE);
		holder.notice.setText("已接受");
	}

	class ViewHolder {
		ImageView avadar;
		TextView name, notice;
		Button accept, reject;
	}

	protected void choicefrienduser(String msgid, final int i) {
		Tool.showProgressDialog(context, "正在提交", true);
		HttpUtils httpUtils = new HttpUtils(30000);
		httpUtils.configCurrentHttpCacheExpiry(1000);
		httpUtils.send(HttpMethod.GET, TLUrl.URL_GET_VOIP
				+ "User/choicefrienduser?id=" + msgid + "&state=" + i,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Tool.removeProgressDialog();
						addFriendsFailed();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Tool.removeProgressDialog();
						try {
							JSONObject jsonObject = new JSONObject(arg0.result);
							if (jsonObject.getInt("status") == 1) {
								bean.setState(i == 1 ? 1 : 2);
								try {
									MyApplication.dbUtils.saveOrUpdate(bean);
								} catch (DbException e) {
									e.printStackTrace();
								}
								// list.clear();
								// try {
								// list.addAll(MyApplication.dbUtils.findAll(AddFriendRequestBean.class));
								// } catch (DbException e) {
								// e.printStackTrace();
								// }
								// notifyDataSetChanged();
								if (i == 1) {
									User user = new User();
									user.setAvatar(bean.getAvadar());
									user.setVoipAccout(bean.getVoip());
									user.setNickname(bean.getNickname());
									user.setRemark("");
									user.setUid(Integer.parseInt(bean.getUid()));
									MyApplication.users.add(user);
									MyApplication.friends.put(bean.getNickname(), user);
									try {
										MyApplication.dbUtils.saveOrUpdate(user);
									} catch (DbException e) {
										e.printStackTrace();
									}
									context.sendBroadcast(new Intent(
											GlobalConstant.ACTION_UPDATE_FRIENDS));
								}
								setList();
								// try {
								// List<AddFriendRequestBean> list =
								// MyApplication.dbUtils.findAll(Selector.from(AddFriendRequestBean.class).orderBy("time",
								// true));
								// setList(list);
								// } catch (DbException e) {
								// e.printStackTrace();
								// }
							} else
								addFriendsFailed();
						} catch (JSONException e) {
							e.printStackTrace();
							addFriendsFailed();
						}
					}
				});
	}

	private void addFriendsFailed() {
		Tool.showInfo(context, "服务器异常");
	}
}
