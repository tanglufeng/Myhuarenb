package com.abcs.huaqiaobang.ytbt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.bean.User;
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

import java.util.ArrayList;
import java.util.List;

public class SearchFriendsAdapter extends BaseAdapter {

    private Context context;
    private List<User> userlist;

    public SearchFriendsAdapter(Context context, ArrayList<User> userlist) {
        this.context = context;
        this.userlist = userlist;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return userlist.size();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User bean = userlist.get(position);
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
        holder.bt.setText("先加为好友");
        MyApplication.bitmapUtils.display(holder.groupheader,
                userlist.get(position).getAvatar());
        // groupheader.setImageResource(R.drawable.img_qunzu);
        holder.groupName.setText(bean.getNickname());
        holder.groupId.setText(bean.getUid() + "");
        holder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriends(bean);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView groupName, groupId;
        CircleImageView groupheader;
        Button bt;
    }

    protected void addFriends(User bean) {
        Tool.showProgressDialog(context, "正在发送请求", true);
        HttpUtils httpUtils = new HttpUtils(30000);
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, TLUrl.URL_GET_VOIP + "User/addMongoDBfrienduser" +
                "?uid=" + MyApplication.getInstance().getUid() + "&frienduid="
                + bean.getUid(), new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0,
                                  String arg1) {
                Tool.removeProgressDialog();
                Tool.showInfo(context, "好友请求发送失败!");
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                try {
                    JSONObject jsonObject = new JSONObject(arg0.result);
                    if (jsonObject.getInt("status") == 1) {
                        Tool.showInfo(context, "好友请求发送成功!");
                    } else {
                        Tool.showInfo(context, jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Tool.showInfo(context, "好友请求发送失败!");
                } finally {
                    Tool.removeProgressDialog();
                }
            }

        });
    }
}
