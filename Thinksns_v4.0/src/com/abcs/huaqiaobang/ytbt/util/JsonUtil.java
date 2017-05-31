package com.abcs.huaqiaobang.ytbt.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.abcs.huaqiaobang.ytbt.bean.User;

public class JsonUtil {
	public static String toString(ArrayList<User> list){
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\"msg\":[");
		for (User user : list) {
			buffer.append(user.toString()+",");
		}
		buffer.deleteCharAt(buffer.length()-1);
		buffer.append("]}");
		return buffer.toString();
	}
	public static ArrayList<User> parseString(String s) throws JSONException{
		ArrayList<User> list = new ArrayList<User>();
		JSONObject object = new JSONObject(s);
		JSONArray array = object.getJSONArray("msg");
		for (int i = 0; i < array.length(); i++) {
			JSONObject o = array.getJSONObject(i);
			User u = new User();
			u.setVoipAccout(o.getString("voipAccout"));
			u.setNickname(o.getString("nickname"));
			u.setAvatar(o.getString("avatar"));
			u.setUid(o.getInt("uid"));
			list.add(u);
		}
		return list;
	}
}
