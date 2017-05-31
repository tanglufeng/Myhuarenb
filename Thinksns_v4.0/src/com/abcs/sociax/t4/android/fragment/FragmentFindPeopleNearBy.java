package com.abcs.sociax.t4.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.abcs.sociax.android.R;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.abcs.sociax.api.Api;
import com.abcs.sociax.t4.adapter.AdapterNearByGridUser;
import com.abcs.sociax.t4.adapter.AdapterSociaxList;
import com.abcs.sociax.t4.android.user.ActivityUserInfo_2;
import com.abcs.sociax.t4.model.ModelSearchUser;
import com.abcs.sociax.t4.service.UpdateLocationService;
import com.thinksns.sociax.thinksnsbase.activity.widget.EmptyLayout;
import com.thinksns.sociax.thinksnsbase.bean.ListData;
import com.thinksns.sociax.thinksnsbase.bean.SociaxItem;
import com.thinksns.sociax.thinksnsbase.utils.ActivityStack;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * 类说明： 附近的人
 * @author hedong
 * @version 1.0
 * @date 2016.3.1
 */
public class FragmentFindPeopleNearBy extends FragmentSociax
        implements   AdapterView.OnItemClickListener, AbsListView.OnScrollListener,
        OnRefreshListener2<GridView>{

//    private AMapLocationClient locationClient = null;
//    private AMapLocationClientOption locationOption = null;

    private PullToRefreshGridView pullToRefreshGridView;
    private GridView mGridView;
    private AdapterNearByGridUser mAdapter;
    private EmptyLayout emptyLayout;
    private boolean isRecommend = false;


    // 定位相关
    LocationClient mLocClient;
     MyLocationListenner myListener = new MyLocationListenner();

    public static FragmentFindPeopleNearBy newInstance(boolean isRecommend) {
        FragmentFindPeopleNearBy fragmentFindPeopleNearBy = new FragmentFindPeopleNearBy();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_recommend", isRecommend);
        fragmentFindPeopleNearBy.setArguments(bundle);
        return fragmentFindPeopleNearBy;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if(getArguments() != null) {
            isRecommend = getArguments().getBoolean("is_recommend", false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!isRecommend) {
             initOptions();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().startService(new Intent(getActivity(), UpdateLocationService.class));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().stopService(new Intent(getActivity(), UpdateLocationService.class));
    }

    /**
     * 初始化定位参数
     */
    public void initOptions() {
        mLocClient = new LocationClient(getActivity().getApplicationContext());
        mLocClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setCoorType("wgs84"); // 设置坐标类型
        option.setIsNeedAddress(true);
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);
        mLocClient.start();
//        mLocClient.registerLocationListener(ne);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pull_refresh_gridview;
    }

    @Override
    public void onDestroy() {
        if (null != mLocClient) {
            mLocClient.stop();
        }
        super.onDestroy();
    }

    @Override
    public void initView() {
        pullToRefreshGridView = (PullToRefreshGridView)findViewById(R.id.pull_refresh_grid);
        pullToRefreshGridView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshGridView.setOnRefreshListener(this);
        mGridView = pullToRefreshGridView.getRefreshableView();
        mGridView.setNumColumns(3);
        mGridView.setAdapter(getListAdapter());

        mGridView.setOnItemClickListener(this);
        mGridView.setOnScrollListener(this);

        //空置页面设置
        emptyLayout = (EmptyLayout)findViewById(R.id.error_layout);
        emptyLayout.setNoDataContent(getResources().getString(R.string.empty_content));

    }

    @Override
    public void initIntentData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        if(isRecommend) {
            //只允许下拉刷新
            pullToRefreshGridView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            getRecommendUsers(true);
        }else {

        }
    }

    /**
     * 获取推荐用户
     * @param refreshHeader
     */
    private void getRecommendUsers(final boolean refreshHeader) {
        new Api.Users().searchUser(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    ListData<SociaxItem> listData = new ListData<SociaxItem>();
                    if(result.startsWith("{") && result.endsWith("}")) {
                    }else {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                ModelSearchUser follow = new ModelSearchUser(
                                        jsonArray.getJSONObject(i));
                                listData.add(follow);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    if(refreshHeader) {
                        mAdapter.clearList();
                        mAdapter.addHeader(listData);
                    }else {
                        mAdapter.addFooter(listData);
                    }
                }catch(JSONException e) {
                    e.printStackTrace();
                }

                //刷新完毕
                getPullRefreshView().onRefreshComplete();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (getActivity().isFinishing()){
                    return;
                }
                getPullRefreshView().onRefreshComplete();
                Toast.makeText(getActivity(), "网络连接错误", Toast.LENGTH_SHORT).show();
            }
        }, 50);
    }

    protected AdapterSociaxList getListAdapter() {
        mAdapter = new AdapterNearByGridUser(this, new ListData<SociaxItem>());
        return mAdapter;
    }

    @Override
    public PullToRefreshBase getPullRefreshView() {
        return pullToRefreshGridView;
    }

    @Override
    public EmptyLayout getEmptyLayout() {
        return emptyLayout;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ModelSearchUser modelSearchUser = (ModelSearchUser) mAdapter.getItem((int) id);
        if(modelSearchUser != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("uid", modelSearchUser.getUid());
            ActivityStack.startActivity(getActivity(), ActivityUserInfo_2.class, bundle);
        }
    }

    int firstVisibleItem = 0;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState != SCROLL_STATE_IDLE) {
            return;
        }

        if (firstVisibleItem <= 1)
            FragmentHomePeople.getInstance().animatorShow(true);
        else {
            FragmentHomePeople.getInstance().animatorHide();
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    @Override
    public void executeDataSuccess(ListData<SociaxItem> data) {
        try {
            if (getActivity().isFinishing()){
                return;
            }
            emptyLayout.setNoDataContent(getResources().getString(R.string.empty_user));
        }catch (Exception e){
            e.printStackTrace();
        }

        super.executeDataSuccess(data);
    }

//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation != null) {
//            if (aMapLocation.getErrorCode() == 0) {
//                mAdapter.setLatitudeLngitude(aMapLocation.getLatitude(), aMapLocation.getLongitude());
//                mAdapter.loadInitData();
//            } else {
//                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
//                Log.e("FragmentFindPeopleNearBy", errText);
//              //  Toast.makeText(getActivity(), "定位失败:" + aMapLocation.getErrorInfo(), Toast.LENGTH_SHORT).show();
//                mAdapter.loadInitData();
//            }
//        }
//    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            // LogUtil.i("locationbaidu","latitude"+location.getLatitude() + "longtitude"+location.getLongitude());
            if (location.getLatitude() == 4.9e-324) {
                return;
            }
            mAdapter.setLatitudeLngitude(location.getLatitude(), location.getLongitude());
            mAdapter.loadInitData();



        }

        public void onReceivePoi(BDLocation poiLocation) {


        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
        if(!isRecommend) {
            mAdapter.doRefreshFooter();
        }else {
            getRecommendUsers(true);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
        if(isRecommend) {
            getRecommendUsers(false);
        }else {
            mAdapter.doRefreshHeader();
        }
    }
}
