package com.abcs.haiwaigou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.BenDiSearchAdapter;
import com.abcs.haiwaigou.model.BenDiSearch;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AllBenDiGoodsActivity extends BaseFragmentActivity implements View.OnClickListener,RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.et_search)
    EditText textView2;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.relative_search)
    RelativeLayout relativeSearch;
    @InjectView(R.id.btn_global)
    RelativeLayout btnGlobal;
    @InjectView(R.id.btn_price)
    RelativeLayout btnPrice;
    @InjectView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @InjectView(R.id.tv_global)
    TextView tv_global; //默认
    @InjectView(R.id.tv_price)
    TextView tv_price; //价格
    @InjectView(R.id.img_price)
    ImageView img_price; //价格

    public Handler handler = new Handler();
    private int currentPage = 1;
    boolean first = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_bendigoods_recycler);

        ButterKnife.inject(this);
        initView();
        setOnListener();
        if(!TextUtils.isEmpty(getIntent().getStringExtra("search_keyword"))){
            textView2.setText(getIntent().getStringExtra("search_keyword"));
            initAllDates();
        }

        watchSearch();
    }


    public void watchSearch() {
        textView2.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) textView2.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(AllBenDiGoodsActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    initAllDates();
                    return true;
                }
                return false;
            }
        });
    }

    private void setOnListener() {
        btnGlobal.setOnClickListener(this);
        btnPrice.setOnClickListener(this);
        relativeBack.setOnClickListener(this);
        relativeSearch.setOnClickListener(this);
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setRefreshListener(this);
        recyclerView.setAdapter(adapter=new BenDiSearchAdapter(this));
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                BenDiSearch.DatasBean itemsEntity = adapter.getAllData().get(position);
                Intent intent = new Intent(AllBenDiGoodsActivity.this, GoodsDetailActivity.class);
                intent.putExtra("sid", itemsEntity.goodsId);
                intent.putExtra("pic", itemsEntity.goodsImage);
                startActivity(intent);
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
       
    }

    private String order;  // 1为商品id排序，2为价格
    private String order_key; //默认为倒序，1为倒序，2为正序

    BenDiSearchAdapter adapter;
    private boolean isRefresh = false;

    private void initAllDates() {
        if(TextUtils.isEmpty(textView2.getText().toString().trim())){
            showToast("请输入要搜索的内容");
            return;
        }
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", this);
        }
        Log.i("zds", "currentPage=" + currentPage);
        Log.i("zds", "keyword=" + textView2.getText().toString().trim());

        HttpRequest.sendPost(TLUrl.getInstance().URL_bendi_goods, "&key=" + MyApplication.getInstance().getMykey() + "&keyword=" + textView2.getText().toString().trim() + "&page=" + currentPage + "&page_size=10&order_key=" + order_key + "&order=" + order, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgUtil.stopProgressDlg();
                        Log.i("zds", "msg=" + msg);
                        if(TextUtils.isEmpty(msg)){
                            return;
                        }else {

                            if (isRefresh) {
                                adapter.clear();
                                isRefresh = false;
                            }

                            BenDiSearch benDiSearch=new Gson().fromJson(msg, BenDiSearch.class);

                            if(benDiSearch.state==1){
                                if(benDiSearch.datas!=null){
                                    if(benDiSearch.datas.size()>0){
                                        adapter.addAll(benDiSearch.datas);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }else {
                                adapter.clear();
                                adapter.notifyDataSetChanged();
                                adapter.stopMore();
                                currentPage=-1;
                                showToast(benDiSearch.msg);

                            }
                        }
                    }
                });
            }
        });
    }

    private boolean isJiaGe=true;

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()) {
            case R.id.relative_back:
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                finish();
                break;
            case R.id.relative_search:
                textView2.setText("");
                break;
            case R.id.btn_global: // 默认
                first = true;
                adapter.clear();
                tv_global.setTextColor(getResources().getColor(R.color.red));
                tv_price.setTextColor(getResources().getColor(R.color.darkgray));
                img_price.setImageResource(R.drawable.sort_button);
                order_key="";
                order="";
                initAllDates();
                break;
            case R.id.btn_price: // 价格排序
                first = true;
                adapter.clear();
                tv_global.setTextColor(getResources().getColor(R.color.darkgray));
                tv_price.setTextColor(getResources().getColor(R.color.red));

                if(isJiaGe){
                    order_key="1";
                    order="2";
                    isJiaGe=false;
                    img_price.setImageResource(R.drawable.sort_button_down);
                    initAllDates();
                }else {
                    order_key="2";
                    order="2";
                    isJiaGe=true;
                    img_price.setImageResource(R.drawable.sort_button_up);
                    initAllDates();
                }

                break;
            default:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        isRefresh = true;

        initAllDates();

    }

    @Override
    public void onLoadMore() {
        if (currentPage == -1) {
            adapter.stopMore();
            return;
        }
        currentPage=currentPage+1;
        initAllDates();
    }
}
