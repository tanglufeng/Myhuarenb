package com.abcs.haiwaigou.local.model;

import android.os.Handler;

import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;

/**
 * Created by zjz on 2016/8/29.
 */
public class CommunityModel {
    private Handler handler=new Handler();
    public void initCommunityDatas(){
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_city, "", new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }

    public void initCommunityList(JSONArray array){

    }
}
