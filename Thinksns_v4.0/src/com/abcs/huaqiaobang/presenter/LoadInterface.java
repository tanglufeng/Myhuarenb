package com.abcs.huaqiaobang.presenter;

import java.util.List;

/**
 * Created by zhou on 2016/4/15.
 */
public interface LoadInterface<T> {
    public void loadData(List<T> mData);
    public void loadNewsData(List<T> mData);
}
