package com.abcs.sociax.net;

import android.content.Context;

import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

public class HttpHelper {
	private static String host;
	private static String path;
	private static Context context;

	public static void initHttp(Context context) {
		HttpHelper.setContext(context);
		String[] configHttp = context.getResources().getStringArray(
				R.array.site_url);
		HttpHelper.host = TLUrl.getInstance().getHuaUrl();
//		HttpHelper.host = configHttp[0];
		HttpHelper.path = configHttp[1];
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		HttpHelper.host = host;
	}

	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		HttpHelper.path = path;
	}

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		HttpHelper.context = context;
	}

}
