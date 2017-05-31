package com.abcs.haiwaigou.local.interfacer;

/**
 * Created by zjz on 2016/9/28.
 */

public interface LoadStateInterface {
    public void LoadSuccess(String sucMsg);
    public void LoadFailed(String faiMsg);
    public void LoadEmpty(String empMsg);
    public void isLoadMore(boolean isMore);
}
