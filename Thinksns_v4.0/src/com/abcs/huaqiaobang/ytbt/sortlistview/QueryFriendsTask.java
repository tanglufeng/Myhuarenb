package com.abcs.huaqiaobang.ytbt.sortlistview;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.util.GlobalConstant;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QueryFriendsTask extends
		AsyncTask<Void, Void, HashMap<String, User>> {
	private Context context;

	public QueryFriendsTask(Context context) {
		super();
		this.context = context;
	}

	private HashMap<String, User> list = new HashMap<>();
	@Override
	protected HashMap<String, User> doInBackground(Void... params) {
		HttpRequest.sendGet(TLUrl.URL_GET_VOIP + "User/findfriendUser", "uid="
				+ MyApplication.getInstance().getUid() + "&page=1"
				+ "&size=100", new HttpRevMsg() {
			private ArrayList<User> users = new ArrayList<User>();

			@Override
			public void revMsg(String msg) {
				Log.i("xbb好友", "好友：" + msg);
				if (msg.length() <= 0) {

				}
				try {
					JSONObject jsonObject = new JSONObject(msg);
					if (jsonObject.getInt("status") == 1) {

						JSONArray jsonArray = jsonObject.getJSONArray("msg");
						if (jsonArray.length() == 0) {
							return;
						}
						MyApplication.friends.clear();
						list.clear();
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject object = jsonArray.getJSONObject(i);
							User user = new User();
							user.setVoipAccout(object.getString("voipAccount"));
							user.setNickname(object.getString("nickname"));
							user.setUid(object.getInt("frienduid"));
							user.setAvatar(object.getString("avatar"));
							user.setRemark(object.getString("remarks"));
							users.add(user);
							if (user.getRemark().trim().equals("")) {
								list.put(user.getNickname(), user);
							} else {
								list.put(user.getRemark(), user);
							}
						}
						try {
							MyApplication.dbUtils.saveOrUpdateAll(users);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		return list;
	}

	protected void onPostExecute(Map<? extends String, ? extends User> result) {
		super.onPostExecute((HashMap<String, User>) result);
		// ((FriendsListActivity)b).setAllContacts(result);
		MyApplication.friends.clear();
		MyApplication.friends.putAll(result);
		context.sendBroadcast(new Intent(GlobalConstant.ACTION_READ_FRIEND));
	}
}
