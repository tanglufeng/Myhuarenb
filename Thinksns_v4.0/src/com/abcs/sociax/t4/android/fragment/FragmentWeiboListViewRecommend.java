package com.abcs.sociax.t4.android.fragment;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;

import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.adapter.AdapterWeiboAll;
import com.abcs.sociax.t4.android.presenter.WeiboRecommendListPresenter;
import com.abcs.sociax.t4.android.weibo.ActivityFindCicle;
import com.abcs.sociax.t4.model.ModelComment;
import com.abcs.sociax.t4.model.ModelWeibo;
import com.abcs.sociax.t4.unit.UnitSociax;
import com.thinksns.sociax.thinksnsbase.base.ListBaseAdapter;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hedong on 16/2/20.
 * 推荐微博类
 */
public class FragmentWeiboListViewRecommend extends FragmentWeiboListViewNew {

    private ActivityFindCicle cicleWeibo;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof ActivityFindCicle) {
            cicleWeibo = (ActivityFindCicle)activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    protected IntentFilter getIntentFilter() {
        return null;
    }

    @Override
    protected String getCacheKey() {
        return "recommend_weibo";
    }

    @Override
    protected void initPresenter() {
        mPresenter = new WeiboRecommendListPresenter(getActivity(), this, this);
        mPresenter.setCacheKey(getCacheKey());
    }

    @Override
    protected void initListViewAttrs() {
        super.initListViewAttrs();
        mListView.setDividerHeight(UnitSociax.dip2px(getActivity(), 0.5f));
        mListView.setSelector(R.drawable.list_selector);
    }

    @Override
    protected boolean requestDataIfViewCreated() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onTabClickListener() {
        if(mAdapter.getData().size() == 0)
            mPresenter.loadInitData(true);
    }

    @Override
    protected ListBaseAdapter<ModelWeibo> getListAdapter() {
        return new AdapterWeiboAll(getActivity(), this, mListView);
    }

    @Override
    public void onCommentWeibo(ModelWeibo weibo, ModelComment comment) {
        super.onCommentWeibo(weibo, comment);
        if(cicleWeibo != null) {
            cicleWeibo.toggleCreateBtn(false);
        }
    }

    @Override
    protected void resetComentUI() {
        super.resetComentUI();
        if(cicleWeibo != null) {
            cicleWeibo.toggleCreateBtn(true);
        }
    }
}
