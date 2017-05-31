package com.abcs.sociax.t4.android.fragment;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;

import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.adapter.AdapterWeiboAll;
import com.abcs.sociax.t4.android.presenter.WeiboFriendsListPresenter;
import com.abcs.sociax.t4.android.weibo.ActivityFindCicle;
import com.abcs.sociax.t4.model.ModelComment;
import com.abcs.sociax.t4.model.ModelWeibo;
import com.abcs.sociax.t4.unit.UnitSociax;
import com.thinksns.sociax.thinksnsbase.base.ListBaseAdapter;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hedong on 16/2/20.
 * 好友微博
 */
public class FragmentWeiboListViewFriends extends FragmentWeiboListViewNew {
    boolean autoRefresh = false;

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
        if(getArguments() != null) {
            autoRefresh = getArguments().getBoolean("refresh", false);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    protected void initListViewAttrs() {
        super.initListViewAttrs();
        mListView.setDividerHeight(UnitSociax.dip2px(getActivity(), 0.5f));
        mListView.setSelector(R.drawable.list_selector);
    }

    @Override
    protected void initPresenter() {
        //创建prenster实例
        mPresenter = new WeiboFriendsListPresenter(getActivity(), this, this);
        mPresenter.setCacheKey(getCacheKey());
    }

    @Override
    protected IntentFilter getIntentFilter() {
        return null;
    }

    //不在Fragment被创建时请求网络数据
    @Override
    protected boolean requestDataIfViewCreated() {
        return autoRefresh;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected String getCacheKey() {
        return "friends_weibo";
    }

    @Override
    public void onTabClickListener() {
        if(mAdapter != null) {
            if (mAdapter.getData().size() == 0) {
                mPresenter.loadInitData(true);
            }
        }
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
