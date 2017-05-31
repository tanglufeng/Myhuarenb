package com.abcs.haiwaigou.local.model;

import com.abcs.haiwaigou.local.model.Message;
import com.abcs.huaqiaobang.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by zjz on 2016/8/29.
 */
public class MessageModel {

    public ArrayList<Message> currentList=new ArrayList<Message>();
    public MessageModel() {
    }


    public void initDatas(JSONArray jsonArray,boolean isRefresh){
        if(isRefresh){
            currentList.clear();
        }
        initJson(jsonArray);

    }

    public void initDatas(ArrayList<Message>list){
        currentList=list;
    }


    private void initJson( JSONArray array) {
        Message message = null;
        Realm myRealm;
        try {
            myRealm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException e) {
            RealmConfiguration config = new RealmConfiguration.Builder(
                    MyApplication.getInstance()).deleteRealmIfMigrationNeeded()
                    .build();
            Realm.setDefaultConfiguration(config);
            myRealm = Realm.getDefaultInstance();
        }

        for (int i=0;i<array.length();i++){
            try {
                JSONObject jsonObject=array.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setList(ArrayList<Message> list) {
        this.currentList = list;
    }
    public ArrayList<Message> getList() {
        ArrayList<Message> tempList = new ArrayList<Message>(currentList);
        sortList(tempList);
        return tempList;
    }
    public void sortList(ArrayList<Message> list) {
        Collections.sort(list, new Comparator<Message>() {
            @Override
            public int compare(Message n1, Message n2) {
                return n2.getTime().compareTo(n1.getTime());
            }
        });

    }
}
