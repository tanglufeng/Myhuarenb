package com.abcs.huaqiaobang.presenter;

import java.util.List;

/**
 * Created by zhou on 2016/4/19.
 */
public interface LoadDataInterFace<T> {

    public void loadSuccess(List<T> mData);
    public void loadFailed(String error);
}
