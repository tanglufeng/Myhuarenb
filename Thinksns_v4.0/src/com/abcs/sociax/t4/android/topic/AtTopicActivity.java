package com.abcs.sociax.t4.android.topic;

import android.content.Intent;
import android.os.Bundle;
import com.abcs.sociax.android.R;
import com.abcs.sociax.component.CustomTitle;
import com.abcs.sociax.component.LeftAndRightTitle;
import com.abcs.sociax.t4.android.ThinksnsAbscractActivity;

/**
 * 类说明：推荐话题
 *
 * @version 1.0
 * @date Jan 18, 2013
 */
public class AtTopicActivity extends ThinksnsAbscractActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentRecommendTopicList fragment = FragmentRecommendTopicList.newInstance();
        fragmentTransaction.replace(R.id.ll_content, fragment)
                .commit();

    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.topic_title);
    }

    @Override
    protected CustomTitle setCustomTitle() {
        return new LeftAndRightTitle(R.drawable.img_back, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_common;
    }

    /**
     * 选择话题回传给上一个页面
     * @param topic_name 话题名称
     */
    public void setResult(String topic_name) {
        Intent intent = new Intent();
        intent.putExtra("recent_topic", topic_name);
        setResult(RESULT_OK, intent);
        finish();
    }
}
