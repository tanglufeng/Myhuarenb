package com.abcs.huaqiaobang.wxapi.loginmodule.util;

import com.abcs.huaqiaobang.wxapi.loginmodule.imp.HttpCallbackListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) new URL(address).openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer response = new StringBuffer();
                    String line;

                    while ((line = bufferedReader.readLine()) != null)
                        response.append(line);
                    if (listener != null)// 回调 onFinish() 方法
                        listener.onFinish(response.toString());
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调 onError 方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendHttpRequestDoPost(String url, RequestParams params, final HttpCallbackListener callBack) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] headers, String response) {
                callBack.onFinish(response);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                callBack.onError(arg3.getMessage());
            }
        });
    }
}
