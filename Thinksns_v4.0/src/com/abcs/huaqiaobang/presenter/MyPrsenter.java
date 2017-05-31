package com.abcs.huaqiaobang.presenter;

/**
 * Created by zhou on 2016/4/26.
 */
public class MyPrsenter {

    UserDataInterface listener;

    public MyPrsenter(UserDataInterface listener) {
        this.listener = listener;
    }

    public void loginSuccess(){
        listener.loginSuccess();
    }
}
