package com.abcs.haiwaigou.local.huohang.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.abcs.hqbtravel.adapter.NoticeAdapter;
import com.abcs.hqbtravel.entity.Notice;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NoticeActivity extends BaseActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.rv_notice)
    EasyRecyclerView rvNotice;

    private int pageNo = 1;
    NoticeAdapter adapter;
    private boolean isRefresh = false;
    private boolean isFirst=true;
    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.inject(this);

        EventBus.getDefault().register(this);

        rvNotice.setLayoutManager(new LinearLayoutManager(this));
        rvNotice.setRefreshListener(this);
        rvNotice.setAdapter(adapter = new NoticeAdapter(this));
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Notice.DatasEntry itemsEntity = adapter.getAllData().get(position);

                Log.i("zds", "onItemClick: itemsEntity.messageTitle"+itemsEntity.messageTitle);

                Intent it = new Intent(NoticeActivity.this, NoticeDetialsActivity.class);
                it.putExtra("messageTitle", itemsEntity.messageTitle);
                it.putExtra("messageDetails", itemsEntity.messageDetails);
                it.putExtra("addTime", itemsEntity.addTime);
                it.putExtra("id", itemsEntity.id);
                startActivity(it);
            }
        });

        adapter.setNoMore(R.layout.view_nomore);
        adapter.setMore(R.layout.view_more, this);
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });
        loading();
    }

    private void loading(){

        if(isFirst){
            ProgressDlgUtil.showProgressDlg("加载中...",this);
        }
//        http://www.huaqiaobang.com/mobile/index.php?act=native_message&op=get_native_message&key=939f6c2c1ad7199187be733cc714955a&page=1&page_size=10

        HttpRequest.sendGet(com.thinksns.sociax.thinksnsbase.utils.TLUrl.getInstance().URL_hwg_base + "/mobile/index.php", "act=native_message&op=get_native_message&page_size=10" + "&page="+pageNo+"&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "run: " + msg);

                        if(isFirst){
                            ProgressDlgUtil.stopProgressDlg();
                            isFirst=false;
                        }

                        if (isRefresh) {
                            adapter.clear();
                            isRefresh = false;
                        }

                        Notice notice=new Gson().fromJson(msg, Notice.class);

                        if(notice!=null){
                            if(notice.state==1){
                                if(notice.datas!=null){
                                    if(notice.datas.size()>0){
                                        adapter.addAll(notice.datas);
                                    }else {
                                        adapter.stopMore();
                                        showToast("暂时还没有数据！");
                                    }
                                }
                            }else {
                                adapter.stopMore();
                                if(!isUpdata){
                                    showToast("已加载全部数据！");
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    @OnClick({R.id.relative_back, R.id.activity_notice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.activity_notice:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upDatas(String read_msg){
        LogUtil.i("zds",read_msg);

        if(read_msg.length()>0&&read_msg.equals("readmsg")){
            isUpdata=true;
            isRefresh = true;
            pageNo = 1;
            loading();
        }
    }

    private boolean isUpdata=false;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLoadMore() {
       pageNo=pageNo+1;
       loading();
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        isRefresh = true;
        loading();
    }
}
