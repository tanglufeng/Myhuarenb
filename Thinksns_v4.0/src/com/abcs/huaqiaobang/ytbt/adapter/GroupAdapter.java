package com.abcs.huaqiaobang.ytbt.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.bean.GroupBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.chats.ChattingActivity;
import com.abcs.huaqiaobang.ytbt.util.CircleImageView;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GroupAdapter extends BaseAdapter {

    private Context context;
    private List<GroupBean> groups;
    private boolean isQZ;

    public GroupAdapter(Context context, List<GroupBean> groups, boolean isQZ) {
        this.context = context;
        this.groups = groups;
        this.isQZ = isQZ;
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GroupBean bean = groups.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.group_listview_item, null);
            holder.groupName = (TextView) convertView.findViewById(R.id.name);
            holder.groupId = (TextView) convertView.findViewById(R.id.groupid);
            holder.groupheader = (CircleImageView) convertView
                    .findViewById(R.id.gridview);
            holder.bt = (Button) convertView.findViewById(R.id.bt_numOK);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyApplication.bitmapUtils.display(holder.groupheader,
                groups.get(position).getGroupAvatar());
        // groupheader.setImageResource(R.drawable.img_qunzu);
        holder.groupName.setText(bean.getGroupName());
        holder.groupId.setText(bean.getGroupId());
        holder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinGroup(bean);
            }
        });
        if (isQZ) {
            holder.groupId.setVisibility(View.GONE);
            holder.bt.setVisibility(View.VISIBLE);
        } else {
            holder.groupId.setVisibility(View.VISIBLE);
            holder.bt.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView groupName, groupId;
        CircleImageView groupheader;
        Button bt;
    }

    private void joinGroup(final GroupBean bean) {
        Tool.showProgressDialog(context, "正在进入...", true);
		HttpUtils httpUtils = new HttpUtils(30000);
		httpUtils.configCurrentHttpCacheExpiry(1000);
		httpUtils.send(HttpRequest.HttpMethod.GET, TLUrl.URL_GET_VOIP + "group/JoinGroup"+
				"?uid=" + MyApplication.getInstance().getUid() + "&groupId="
						+ bean.getGroupId() + "&declared="+bean.getGroupDeclared(), new RequestCallBack<String>() {
							@Override
							public void onFailure(HttpException arg0,String arg1) {
								Tool.removeProgressDialog();
								Tool.showInfo(context, "网络异常");
							}
							@Override
							public void onSuccess(ResponseInfo<String> arg0) {
								try {
									JSONObject jsonObject = new JSONObject(arg0.result);
									String info = jsonObject.getString("msg");
									if (jsonObject.getInt("status") == 1||info.equals("群组成员已存在")) {
										Tool.showInfo(context, "进入成功");
										Intent intent = new Intent(context,
												ChattingActivity.class);
										User user = new User();
										user.setNickname(bean.getGroupName());
										user.setVoipAccout(bean.getGroupId());
										user.setAvatar(bean.getGroupAvatar());
										intent.putExtra("isgroup", true);
										intent.putExtra("group", bean);
										Bundle bundle = new Bundle();
										bundle.putSerializable("friend", user);
										intent.putExtras(bundle);
										((Activity)context).startActivity(intent);
									}else{
										Tool.showInfo(context, info);
									}
								} catch (JSONException e) {
									Tool.showInfo(context, "网络异常");
									e.printStackTrace();
								} finally {
									Tool.removeProgressDialog();
								}
							}
						});
	}
}
