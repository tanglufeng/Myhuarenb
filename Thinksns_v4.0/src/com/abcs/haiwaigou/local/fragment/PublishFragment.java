package com.abcs.haiwaigou.local.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.local.activity.HireAndFindDetailActivity;
import com.abcs.haiwaigou.local.activity.PublishMessageActivity;
import com.abcs.haiwaigou.local.beans.HireFind;
import com.abcs.haiwaigou.local.fragment.adapter.PublishAdapter;
import com.abcs.haiwaigou.local.fragment.viewholder.PublishViewHolder;
import com.abcs.haiwaigou.local.model.HireAndFindModel;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.haiwaigou.view.recyclerview.EndlessRecyclerOnScrollListener;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.view.recyclerview.LoadingFooter;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.recyclerview.RecyclerViewStateUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjz on 2016/9/5.
 */
public class PublishFragment extends BaseFragment implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh, PublishViewHolder.RootOnClick, HireAndFindModel.LoadStateInterFace {

    Activity activity;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.t_refresh)
    TextView tRefresh;
    @InjectView(R.id.layout_null)
    RelativeLayout layoutNull;
    @InjectView(R.id.btn_delete)
    RelativeLayout btn_delete;

    @InjectView(R.id.btn_check_all)
    CheckBox btn_check_all;
    private View view;

    private static final String PUBLISH="1";
    private static final String SAVE="0";


    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    boolean isLoadMore = false;
    boolean isMore = true;
    private PublishAdapter publishAdapter;
    int currentPage = 1;
    HireAndFindModel hireAndFindModel;
    //    String countryId;
    String menuId;
    String cityId;
    String type;

    private MyBroadCastReceiver myBroadCastReceiver;

//    public static CheckBox btn_check_all;
//    //用来存放所有选中的发布的id
//    public static ArrayList<String> publishId = new ArrayList<String>();

    public static PublishFragment newInstance(String type,String menuId) {
        Bundle bundle = new Bundle();
//        bundle.putSerializable("countryId", countryId);
        bundle.putSerializable("type", type);
        bundle.putSerializable("menuId", menuId);
        PublishFragment hireJobFragment = new PublishFragment();
        hireJobFragment.setArguments(bundle);
        return hireJobFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.hwg_yyg_fragment_public, null);
        }
        if (hireAndFindModel == null) {
            hireAndFindModel = new HireAndFindModel(activity, this);
        }
        myBroadCastReceiver=new MyBroadCastReceiver(activity,updateUI);
        myBroadCastReceiver.register();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);
        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
            menuId = bundle.getString("menuId");
        }
        isPrepared = true;
        lazyLoad();
        tRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRecycler();
            }
        });

        btn_check_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (publishAdapter.getList().size()!=0) {   //判断列表中是否有数据
                    if (isChecked) {
                        for (int i = 0; i < publishAdapter.getList().size(); i++) {
                            publishAdapter.getList().get(i).setCheeck(true);
                        }
                        //通知适配器更新UI
                        publishAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < publishAdapter.getList().size(); i++) {
                            publishAdapter.getList().get(i).setCheeck(false);
                        }
                        //通知适配器更新UI
                        publishAdapter.notifyDataSetChanged();
                    }
                }else{//若列表中没有数据则隐藏全选复选框
//                    che_all.setVisibility(View.GONE);
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
        
        return view;
    }

    StringBuffer stringBuffer=null;

    private void delete() {

        //创建一个要删除内容的集合，不能直接在数据源data集合中直接进行操作，否则会报异常
       final List<HireFind> deleSelect = new ArrayList<HireFind>();

        //把选中的条目要删除的条目放在deleSelect这个集合中
        for (int i = 0; i < publishAdapter.getList().size(); i++) {
            if (publishAdapter.getList().get(i).isCheeck()) {
                deleSelect.add(publishAdapter.getList().get(i));
            }
        }

        //判断用户是否选中要删除的数据及是否有数据
        if (deleSelect.size() != 0 && publishAdapter.getList().size() != 0) {
            stringBuffer = new StringBuffer();
            //从数据源data中删除数据
            for(int i = 0; i < deleSelect.size(); i++){
                stringBuffer.append(deleSelect.get(i).getId());
                if (i != deleSelect.size() - 1) {
                    stringBuffer.append(",");
                }
//                publishAdapter.getList().remove(deleSelect.get(i));
            }

            Log.i("zjz", "delete=" + stringBuffer.toString());

            new PromptDialog(activity, "确认删除这些项？", new Complete() {
                @Override
                public void complete() {
                    ProgressDlgUtil.showProgressDlg("Loading...", activity);
                    HttpRequest.sendPost(TLUrl.getInstance().URL_local_delete_mypublish, "id=" + stringBuffer.toString(), new HttpRevMsg() {
                        @Override
                        public void revMsg(final String msg) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
//                                        {"status":"1","msg":"操作成功"}
                                        JSONObject object = new JSONObject(msg);
                                        if (object.getInt("status") == 1) {
                                            Log.i("zjz", "msg=" + msg);
//                                            //从数据源data中删除数据
                                            for(int i = 0; i < deleSelect.size(); i++){
                                                publishAdapter.getList().remove(deleSelect.get(i));
                                            }

                                            //把deleSelect集合中的数据清空
                                            deleSelect.clear();
                                            //把全选复选框设置为false
                                            btn_check_all.setChecked(false);
                                            //通知适配器更新UI
                                            publishAdapter.notifyDataSetChanged();
                                            ProgressDlgUtil.stopProgressDlg();
                                            showToast(activity,object.optString("msg")+"");
                                        }  else {
                                            ProgressDlgUtil.stopProgressDlg();
                                            Log.i("zjz", "goodsDetail:解析失败");
                                        }
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        Log.i("zjz", e.toString());
                                        Log.i("zjz", msg);
                                        e.printStackTrace();
                                        ProgressDlgUtil.stopProgressDlg();
                                    }
                                }
                            });
                        }
                    });
                }
            }).show();

        } else if (publishAdapter.getList().size() == 0) {
            showToast(activity,"没有要删除的数据");
        } else if (deleSelect.size() == 0) {
            showToast(activity,"您还没有选中任何一项");
            return;
        }
    }


    MyBroadCastReceiver.UpdateUI updateUI=new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {

        }

        @Override
        public void updateCollection(Intent intent) {
            if (intent.getStringExtra("type").equals(MyUpdateUI.LOCALMYPUBLISH)) {
                if(hireAndFindModel!=null){
                    isLoadMore=false;
                    initRecycler();
                }
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }

        initRecycler();
    }

    private void initRecycler() {

        ProgressDlgUtil.showProgressDlg("Loading...", activity);
        recyclerView.addOnScrollListener(mOnScrollListener);
        //if (PublishAdapter == null){
        publishAdapter = new PublishAdapter(activity, this, menuId);
        //}
        initAllDates();
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(publishAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(activity, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }

    private void initAllDates() {
        if (isLoadMore) {
            hireAndFindModel.initMyPublish(MyApplication.getInstance().self.getId(), currentPage,type, true);
        } else {
            publishAdapter.getList().clear();
            hireAndFindModel.initMyPublish(MyApplication.getInstance().self.getId(), currentPage,type, false);
        }

    }


    private MyHandler handler = new MyHandler();
    private int mCurrentCounter = 0;

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<HireFind> list) {

        publishAdapter.addItems(list);
        mCurrentCounter += list.size();
    }

    private static final int REQUEST_COUNT = 15;
    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(recyclerView);
            if (state == LoadingFooter.State.Loading) {
                Log.d("@Cundong", "the state is Loading, just wait..");
                return;
            }

            if (isMore) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(getActivity(), recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(getActivity(), recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }

    };

    @Override
    public void swipeRefreshLayoutOnRefresh() {
        currentPage = 1;
        isLoadMore = false;
        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
        isMore = true;
        initAllDates();
    }

    @Override
    public void itemRootOnClick(int position) {
        //重写PublishViewHolder的方法
        //每条item的点击事件
        //如果集合里面条目的isIssue等于1 表示是发布的 点击直接进入详情页面
        //如果集合里面条目的isIssue等于0 表示是保存的 点击进入编辑发布页面
        Intent intent = null;
        if(hireAndFindModel.getHireFindList().get(position).getIsIssue().equals("1")){
//            if (publishAdapter.getList().get(position).getListTypeId().equals(MyString.LOCAL_MENU2)) {
//                intent = new Intent(activity, RestaurantDetailActivity.class);
//                intent.putExtra("conId", publishAdapter.getList().get(position).getId());
//                activity.startActivity(intent);
//            } else {
                intent = new Intent(activity, HireAndFindDetailActivity.class);
                intent.putExtra("conId", publishAdapter.getList().get(position).getId());
            intent.putExtra("isform_fen", false);
            intent.putParcelableArrayListExtra("datas", hireAndFindModel.getHireFindList());
                activity.startActivity(intent);
//            }
        }else if(hireAndFindModel.getHireFindList().get(position).getIsIssue().equals("0")){
            intent=new Intent(activity, PublishMessageActivity.class);
            intent.putExtra("isSave",true);
            intent.putExtra("cityId",hireAndFindModel.getHireFindList().get(position).getCityId());
            intent.putExtra("parentId",hireAndFindModel.getHireFindList().get(position).getParentId());
            intent.putExtra("conId",hireAndFindModel.getHireFindList().get(position).getId());
            intent.putExtra("listTypeId",hireAndFindModel.getHireFindList().get(position).getListTypeId());
            activity.startActivity(intent);
        }


    }

    @Override
    public void LoadSuccess(String msg) {
        mHasLoadedOnce = true;
        ProgressDlgUtil.stopProgressDlg();
        if (swipeRefreshLayout!=null&&swipeRefreshLayout.isRefreshing())
            recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
        publishAdapter.addItems(hireAndFindModel.getHireFindList());
        publishAdapter.notifyDataSetChanged();
        if(hireAndFindModel.getHireFindList().size()==0){
            layoutNull.setVisibility(View.VISIBLE);
        }else {
            layoutNull.setVisibility(View.GONE);
        }
    }

    @Override
    public void LoadFailed(String msg) {

        if(msg.equals(MyString.NODATA)){
            mHasLoadedOnce = true;
            if(layoutNull!=null){
                if(hireAndFindModel.getHireFindList().size()==0){
                    layoutNull.setVisibility(View.VISIBLE);
                } else {
                    layoutNull.setVisibility(View.GONE);
                }
            }
        }
        if(msg.equals(MyString.LOADFAILED)){
            tRefresh.setVisibility(View.VISIBLE);
        }else {
            tRefresh.setVisibility(View.GONE);
        }
        ProgressDlgUtil.stopProgressDlg();
        if (swipeRefreshLayout!=null&&swipeRefreshLayout.isRefreshing())
            recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
    }

    @Override
    public void isMore(boolean state) {
        isMore = state;
        Log.i("zjz", "isMore=" + isMore);
    }

//---------------------全选删除功能未完成
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.rl_delete:
//                delete();
//                break;
//        }
//    }

//    private void delete() {
//        //删除所有选中的item
//        if (publishId.size() == 0) {
//            Toast.makeText(activity,"您还没有选中任何一项",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        final StringBuffer stringBuffer = new StringBuffer();
//        for (int i = 0; i < publishId.size(); i++) {
//            stringBuffer.append(publishId.get(i));
//            if (i != publishId.size() - 1) {
//                stringBuffer.append(",");
//            }
//        }
//        Log.i("xuke","删除选中的发布:"+stringBuffer.toString());
//        //提示是否删除选中的这些发布
//        new PromptDialog(activity, "确认删除这些发布？", new Complete() {
//            @Override
//            public void complete() {
//                ProgressDlgUtil.showProgressDlg("Loading...",activity);
//                HttpRequest.sendGet(TLUrl.getInstance().URL_local_delete_mypublish, "id=" + stringBuffer.toString(), new HttpRevMsg() {
//                    @Override
//                    public void revMsg(final String msg) {
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    JSONObject object = new JSONObject(msg);
//                                    if (object.getInt("status") == 1) {
//                                        Toast.makeText(activity,"删除成功",Toast.LENGTH_SHORT).show();
//                                        com.yixia.camera.util.Log.i("xuke", "hire_url,msg=" + msg);
//                                        ProgressDlgUtil.stopProgressDlg();
//                                        MyUpdateUI.sendUpdateCollection(activity, MyUpdateUI.LOCALMYPUBLISH);
//                                        btn_check_all.setChecked(false);
//                                    } else {
//                                        ProgressDlgUtil.stopProgressDlg();
//                                        android.util.Log.i("xuke", "hire_url,goodsDetail:参数校验失败");
//                                    }
//                                } catch (JSONException e) {
//                                    // TODO Auto-generated catch block
//                                    com.yixia.camera.util.Log.i("xuke", e.toString());
//                                    e.printStackTrace();
//                                    ProgressDlgUtil.stopProgressDlg();
//                                }
//                            }
//                        });
//                    }
//                });
//            }
//        }).show();
//    }


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    isLoadMore = true;
                    if (isMore && isLoadMore) {
                        currentPage += 1;
                        Log.i("zjz", "current=" + currentPage);
                        RecyclerViewStateUtils.setFooterViewState(recyclerView, LoadingFooter.State.Normal);
                        initAllDates();
                    }
                    break;
                case -2:
                    notifyDataSetChanged();
                    break;
                case -3:
                    RecyclerViewStateUtils.setFooterViewState(activity, recyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, mFooterClick);
                    break;
            }
        }
    }


    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
            requestData();
        }
    };

    /**
     * 模拟请求网络
     */
    private void requestData() {

        new Thread() {

            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //模拟一下网络请求失败的情况
                if (NetworkUtils.isNetAvailable(getContext())) {
                    Log.i("zjz", "有网络");
//                    mHandler.sendEmptyMessage(-1);
                    handler.sendEmptyMessage(-1);

                } else {
//                    mHandler.sendEmptyMessage(-3);
                    handler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myBroadCastReceiver.unRegister();
        ButterKnife.reset(this);
    }
}
