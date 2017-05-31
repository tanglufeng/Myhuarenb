package com.abcs.haiwaigou.view.TabLayout;

import com.abcs.haiwaigou.model.SDMangSong;

import java.util.List;

/**
 * Created by HanHailong on 15/10/20.
 */
public interface OnTagSelectListenerBean {
    void onItemSelect(FlowTagLayout parent, List<SDMangSong> selectedList);
}
