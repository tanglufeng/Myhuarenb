package com.abcs.hqbtravel.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.hqbtravel.adapter.FindByTagAdapter;
import com.abcs.hqbtravel.adapter.YouJiAdapter;
import com.abcs.hqbtravel.biz.FindByTagBiz;
import com.abcs.hqbtravel.biz.YouJiListBiz;
import com.abcs.hqbtravel.donghua.JazzyHelper;
import com.abcs.hqbtravel.donghua.JazzyRecyclerViewScrollListener;
import com.abcs.hqbtravel.donghua.Utils;
import com.abcs.hqbtravel.entity.FindByTag;
import com.abcs.hqbtravel.entity.YouJi;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.sociax.android.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.Map;

public class SeachYouJiByTagActivity extends Activity implements  RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener  {

    private EditText ed_serche_context;
    private String pageSize="10";
    private String cityId;
    private int pageNo=1;
    private String type;
//    private  TextView tv_nodata;
    private EasyRecyclerView rv_bytag;
    JazzyRecyclerViewScrollListener jazzyScrollListener;
    private static final String KEY_TRANSITION_EFFECT = "transition_effect";

    private Map<String, Integer> mEffectMap;
    private int mCurrentTransitionEffect = JazzyHelper.GROW;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mEffectMap = Utils.buildEffectMap(this);
        Utils.populateEffectMenu(menu, new ArrayList<>(mEffectMap.keySet()), this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String strEffect = item.getTitle().toString();
        Toast.makeText(this, strEffect, Toast.LENGTH_SHORT).show();
        setupJazziness(mEffectMap.get(strEffect));
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TRANSITION_EFFECT, mCurrentTransitionEffect);
    }

    private void setupJazziness(int effect) {
        mCurrentTransitionEffect = effect;
        jazzyScrollListener.setTransitionEffect(mCurrentTransitionEffect);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_seach_you_ji_by_tag);

        type=getIntent().getStringExtra("type");
        cityId=getIntent().getStringExtra("cityId");

        Log.i("kkkkk:","cityId  "+cityId);
        Log.i("kkkkk:","type  "+type);


        ed_serche_context=(EditText) findViewById(R.id.ed_sercher);
//        ed_serche_context.setFocusable(true);
//        ed_serche_context.setFocusableInTouchMode(true);
//        ed_serche_context.requestFocus();
//        //打开软键盘
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInputFromInputMethod(ed_serche_context.getWindowToken(), 0);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);


//        tv_nodata=(TextView) findViewById(R.id.tv_nodata);
        rv_bytag=(EasyRecyclerView) findViewById(R.id.rv_bytag);

        rv_bytag.setOnScrollListener(jazzyScrollListener=new JazzyRecyclerViewScrollListener());
        if (savedInstanceState != null) {
            mCurrentTransitionEffect = savedInstanceState.getInt(KEY_TRANSITION_EFFECT, JazzyHelper.GROW);
            setupJazziness(mCurrentTransitionEffect);
        }

        rv_bytag.setLayoutManager(new LinearLayoutManager(this));
        rv_bytag.setRefreshListener(this);

        /*输入框监听事件*/
        ed_serche_context.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				/*判断是否是“GO”键*/
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
					/*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }

                    loadData();
//                    ed_serche_context.setText("");
                    return true;
                }
                return false;
            }
        });
    }

    private FindByTagAdapter adapter;
    private boolean isRefresh = false;
    private boolean isFirst = true;

    protected void loadingMain(final String ser_context){
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        FindByTagBiz.getInstance(this).getList(cityId,pageNo,pageSize,ser_context,new Response.Listener<FindByTag>(){
            @Override
            public void onResponse(FindByTag response) {
                if(response!=null){    //  result  1  成功   ；1002   参数错误
                    ProgressDlgUtil.stopProgressDlg();

                    if (isRefresh) {
                        adapter.clear();
                        isRefresh = false;
                    }

                    if(response.result==1){   //  响应正确

                        if(response.body.list!=null){
                            if(response.body.list.size()>0){
                                adapter.addAll(response.body.list);
                                adapter.notifyDataSetChanged();
                                pageNo = response.body.next;
                            }else {
                                adapter.stopMore();
                                Toast.makeText(SeachYouJiByTagActivity.this,"无数据!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        pageNo = 1;
                        Toast.makeText(SeachYouJiByTagActivity.this,"抱歉！该字段无数据", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDlgUtil.stopProgressDlg();
            }
        }, null);
    }
    protected void loadingOthers(final String ser_context){
        ProgressDlgUtil.showProgressDlg("Loading...", this);

        YouJiListBiz.getInstance(this).getYouJiList(cityId, Integer.valueOf(pageSize),pageNo,ser_context,new Response.Listener<YouJi>(){
            @Override
            public void onResponse(YouJi response) {
                if(response!=null){
                    ProgressDlgUtil.stopProgressDlg();

                    if (isRefresh) {
                        youjiAdapter.clear();
                        isRefresh = false;
                    }

                    if(response.result==1){   //  响应正确

                        if(response.body.items!=null){
                            if(response.body.items.size()>0){
                                youjiAdapter.addAll(response.body.items);
                                youjiAdapter.notifyDataSetChanged();
                                pageNo = response.body.next;
                            }else {
//                                if(pageNo==1){
//                                    youjiAdapter.clear();
//                                    youjiAdapter.notifyDataSetChanged();
//                                }
//                                else if(response.body.next==-1){
//                                    pageNo = 1;
//                                    return;
//                                }
//                                else if(response.body.next>0){
//                                    pageNo = 1;
//                                }
                                youjiAdapter.stopMore();
                                Toast.makeText(SeachYouJiByTagActivity.this,"无数据!", Toast.LENGTH_SHORT).show();
//                                pageNo = 1;
                            }
                        }
                    }else {
                        youjiAdapter.clear();
                        youjiAdapter.notifyDataSetChanged();
                        pageNo = 1;
                        Toast.makeText(SeachYouJiByTagActivity.this,"抱歉！该字段无数据", Toast.LENGTH_SHORT).show();
                    }


//
//                    /********************旧版带数据返回********************/
//                    dialog.dismiss();
//
//                    if(response.result==1){
//                        Intent it=new Intent(SeachYouJiByTagActivity.this,YouJiActivity.class);
//                        HQBApplication.KEY_YOUJI_SARCHER=new Gson().toJson(response);
////                        HQBApplication.KEY_YOUJI_SARCHER=KEY_TESTJSON;
////                        Bundle bundle=new Bundle();.
////                        bundle.putSerializable("respone",response);
////
////                        it.putExtra("bundle",bundle);
//                        it.putExtra("ser_context",ser_context);
//                        setResult(YouJiActivity.YOU_JI_REPONSE_CODE,it);
//                        SeachYouJiByTagActivity.this.finish();
//                    }else {
//                        Toast.makeText(SeachYouJiByTagActivity.this,"抱歉！该字段无数据",Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(SeachYouJiByTagActivity.this,error.toString(), Toast.LENGTH_LONG).show();
                ProgressDlgUtil.stopProgressDlg();
            }
        }, null);

    }
    private String ser_context;
    private  YouJiAdapter youjiAdapter;
    /**加载数据**/
    private void loadData(){

          pageNo=1;
          ser_context=ed_serche_context.getText().toString().trim();

        if(!TextUtils.isEmpty(ser_context)&&!TextUtils.isEmpty(cityId)){

            if(!TextUtils.isEmpty(type)&&type.equals("ismeishi")){  //搜索美食  景点
                rv_bytag.setVisibility(View.VISIBLE);
                rv_bytag.setAdapter(adapter = new FindByTagAdapter(this));

                adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        FindByTag.BodyEntity.ListEntity itemsEntity = adapter.getAllData().get(position);
                          // type  1 必玩  2  必吃  3 必买

                        if(itemsEntity.type==1){
                            Intent it = new Intent(SeachYouJiByTagActivity.this, BiWanDetialsActivity.class);
                            it.putExtra("cityId", cityId);
                            it.putExtra("bwanId", itemsEntity.id);
                            it.putExtra("photo", itemsEntity.photo);
                            startActivity(it);
                        }else if(itemsEntity.type==2){
                            Intent it = new Intent(SeachYouJiByTagActivity.this, BiChiDetialsActivity.class);
                            it.putExtra("chiId", itemsEntity.id);
                            it.putExtra("cityId", cityId);
                            it.putExtra("chiphoto", itemsEntity.photo);
                            startActivity(it);
                        }else {
                            Intent it = new Intent(SeachYouJiByTagActivity.this, BiMaiDetialsActivity.class);
                            it.putExtra("cityId", cityId);
                            it.putExtra("shopId", itemsEntity.id);
                            it.putExtra("photo", itemsEntity.photo);
                            startActivity(it);
                        }
                    }
                });
                adapter.setNoMore(R.layout.view_nomore);
                adapter.setMore(R.layout.view_more,this);
                adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.resumeMore();
                    }
                });

                loadingMain(ser_context);

            }else if(!TextUtils.isEmpty(type)&&type.equals("youji")){//搜索游记
                rv_bytag.setVisibility(View.VISIBLE);
                rv_bytag.setAdapter(youjiAdapter = new YouJiAdapter(this));
                youjiAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        YouJi.BodyEntity.ItemsEntity itemsEntity = youjiAdapter.getAllData().get(position);
                        Intent it = new Intent(SeachYouJiByTagActivity.this, YouJiDetialsActivity.class);
                        it.putExtra("youjiId", itemsEntity.id);
                        it.putExtra("readNumber", itemsEntity.read+"");
                        startActivity(it);
                    }
                });
                youjiAdapter.setNoMore(R.layout.view_nomore);
                youjiAdapter.setMore(R.layout.view_more,this);
                youjiAdapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        youjiAdapter.resumeMore();
                    }
                });

                loadingOthers(ser_context);
            }
        }
    }

    /****返回****/
    public void onCancle(View view){
        this.finish();
    }
    /**取消*/
    public void onCancle2(View view){
        if(adapter!=null){
            adapter.clear();
            adapter.notifyDataSetChanged();
        }else if(youjiAdapter!=null){
            youjiAdapter.clear();
            youjiAdapter.notifyDataSetChanged();
        }

        ed_serche_context.setText("");
    }



    @Override
    public void onLoadMore() {

        if(adapter!=null){
            if (pageNo == -1) {
                adapter.stopMore();
                return;
            }

            loadingMain(ser_context);
        }else if(youjiAdapter!=null){
            if (pageNo == -1) {
                youjiAdapter.stopMore();
                return;
            }
            loadingOthers(ser_context);
        }
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        isRefresh = true;

        if(adapter!=null){
            loadingMain(ser_context);
        }else if(youjiAdapter!=null){
            loadingOthers(ser_context);
        }
    }

    private static final String KEY_YY = "{\n" +
            "  \"body\": {\n" +
            "    \"list\": [\n" +
            "      {\n" +
            "        \"id\": \"4143\",\n" +
            "        \"detail_id\": \"86659\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"翠华餐厅(中环威灵顿店)\",\n" +
            "        \"english_name\": \"\",\n" +
            "        \"grade\": 8.29,\n" +
            "        \"photo\": \"http://pic.qyer.com/album/user/389/84/QkFcSh4EYw/index/300x300\",\n" +
            "        \"been_number\": \"4313\",\n" +
            "        \"rank\": \"美食第2名\",\n" +
            "        \"cate_name\": \"粤菜港点\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-09-23 11:42:28.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4143.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"4144\",\n" +
            "        \"detail_id\": \"81661\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"澳洲牛奶公司\",\n" +
            "        \"english_name\": \"\",\n" +
            "        \"grade\": 8.36,\n" +
            "        \"photo\": \"http://pic1.qyer.com/album/1eb/09/1068225/index/300x300\",\n" +
            "        \"been_number\": \"2837\",\n" +
            "        \"rank\": \"美食第3名\",\n" +
            "        \"cate_name\": \"快餐简餐\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-09-23 11:42:29.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4144.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"4145\",\n" +
            "        \"detail_id\": \"62089\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"庙街\",\n" +
            "        \"english_name\": \"Temple Street\",\n" +
            "        \"grade\": 8.06,\n" +
            "        \"photo\": \"http://pic1.qyer.com/album/user/1228/15/QEtXShsCZ0E/index/300x300\",\n" +
            "        \"been_number\": \"3858\",\n" +
            "        \"rank\": \"美食第4名\",\n" +
            "        \"cate_name\": \"中餐\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-09-23 11:42:30.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4145.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"4146\",\n" +
            "        \"detail_id\": \"86657\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"九记牛腩\",\n" +
            "        \"english_name\": \"\",\n" +
            "        \"grade\": 8.74,\n" +
            "        \"photo\": \"http://pic4.qyer.com/album/user/408/14/RUldQx4AYg/index/300x300\",\n" +
            "        \"been_number\": \"1442\",\n" +
            "        \"rank\": \"美食第5名\",\n" +
            "        \"cate_name\": \"粤菜港点\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-09-23 11:42:30.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4146.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"4147\",\n" +
            "        \"detail_id\": \"86713\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"港澳义顺牛奶公司（旺角店）\",\n" +
            "        \"english_name\": \"\",\n" +
            "        \"grade\": 8.42,\n" +
            "        \"photo\": \"http://pic.qyer.com/album/user/330/64/QkpVRB4PYg/index/300x300\",\n" +
            "        \"been_number\": \"903\",\n" +
            "        \"rank\": \"美食第6名\",\n" +
            "        \"cate_name\": \"甜点,粤菜港点\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-09-23 11:42:30.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4147.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"4148\",\n" +
            "        \"detail_id\": \"39032\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"兰芳园(中环总店)\",\n" +
            "        \"english_name\": \"Lan Fong Yuen(Central)\",\n" +
            "        \"grade\": 8.19,\n" +
            "        \"photo\": \"http://pic4.qyer.com/album/user/715/12/RkhQQxgAYQ/index/300x300\",\n" +
            "        \"been_number\": \"998\",\n" +
            "        \"rank\": \"美食第7名\",\n" +
            "        \"cate_name\": \"粤菜港点\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-09-23 11:42:31.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4148.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"4149\",\n" +
            "        \"detail_id\": \"112088\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"翠华餐厅（尖沙咀店）\",\n" +
            "        \"english_name\": \"Tsui Wah Restaurant（尖沙咀店）\",\n" +
            "        \"grade\": 8.36,\n" +
            "        \"photo\": \"http://pic.qyer.com/album/user/407/99/RUlSSxMPaQ/index/300x300\",\n" +
            "        \"been_number\": \"669\",\n" +
            "        \"rank\": \"美食第8名\",\n" +
            "        \"cate_name\": \"粤菜港点\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-09-23 11:42:31.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4149.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"4150\",\n" +
            "        \"detail_id\": \"86653\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"添好运点心专门店(中环)\",\n" +
            "        \"english_name\": \"\",\n" +
            "        \"grade\": 8.56,\n" +
            "        \"photo\": \"http://pic4.qyer.com/album/user/634/62/R0pRRBgGYA/index/300x300\",\n" +
            "        \"been_number\": \"482\",\n" +
            "        \"rank\": \"美食第9名\",\n" +
            "        \"cate_name\": \"粤菜港点\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-09-23 11:42:32.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4150.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"4151\",\n" +
            "        \"detail_id\": \"73985\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"珍妮家手工曲奇(尖沙咀店)\",\n" +
            "        \"english_name\": \"Jenny Bakery, Tsim Sha Tsui Store\",\n" +
            "        \"grade\": 8.36,\n" +
            "        \"photo\": \"http://pic1.qyer.com/album/user/716/12/RkhTQxgPYQ/index/300x300\",\n" +
            "        \"been_number\": \"540\",\n" +
            "        \"rank\": \"购物第19名\",\n" +
            "        \"cate_name\": \"面包糕点,食品店/零食店\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-09-23 11:42:32.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_food_img/4151.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3848\",\n" +
            "        \"detail_id\": \"83130\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"太平山\",\n" +
            "        \"english_name\": \"Victoria Peak\",\n" +
            "        \"grade\": 8.93,\n" +
            "        \"photo\": \"http://pic3.qyer.com/album/user/367/89/Qk9SShMGZg/index/300x300\",\n" +
            "        \"been_number\": \"18305\",\n" +
            "        \"rank\": \"景点观光第1名\",\n" +
            "        \"cate_name\": \"自然风光,观景台\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-10-12 16:26:35.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_sight_img/3848.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3849\",\n" +
            "        \"detail_id\": \"59070\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"香港海洋公园\",\n" +
            "        \"english_name\": \"Ocean Park\",\n" +
            "        \"grade\": 8.92,\n" +
            "        \"photo\": \"http://pic3.qyer.com/album/user/1544/52/QExRRh8FZU0/index/300x300\",\n" +
            "        \"been_number\": \"15367\",\n" +
            "        \"rank\": \"景点观光第2名\",\n" +
            "        \"cate_name\": \"主题公园/游乐场/民俗村,公园/植物园,水族馆\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-10-12 16:07:16.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_sight_img/3849.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3850\",\n" +
            "        \"detail_id\": \"59068\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"香港迪士尼乐园\",\n" +
            "        \"english_name\": \"Hong Kong Disneyland\",\n" +
            "        \"grade\": 8.99,\n" +
            "        \"photo\": \"http://pic.qyer.com/album/153/65/1841653/index/300x300\",\n" +
            "        \"been_number\": \"13290\",\n" +
            "        \"rank\": \"景点观光第3名\",\n" +
            "        \"cate_name\": \"主题公园/游乐场/民俗村\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-10-12 16:07:16.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_sight_img/3850.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3851\",\n" +
            "        \"detail_id\": \"56501\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"星光大道\",\n" +
            "        \"english_name\": \"the Stars Avenue\",\n" +
            "        \"grade\": 7.98,\n" +
            "        \"photo\": \"http://pic1.qyer.com/album/user/213/10/Q0hWQxoEZw/index/300x300\",\n" +
            "        \"been_number\": \"21533\",\n" +
            "        \"rank\": \"景点观光第4名\",\n" +
            "        \"cate_name\": \"街区\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-10-12 15:56:34.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_sight_img/3851.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3852\",\n" +
            "        \"detail_id\": \"37686\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"弥敦道\",\n" +
            "        \"english_name\": \"Nathan Road\",\n" +
            "        \"grade\": 8.35,\n" +
            "        \"photo\": \"http://pic3.qyer.com/album/user/357/21/QkxSQBsGYA/index/300x300\",\n" +
            "        \"been_number\": \"7744\",\n" +
            "        \"rank\": \"景点观光第5名\",\n" +
            "        \"cate_name\": \"街区\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-10-12 15:56:34.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_sight_img/3852.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3853\",\n" +
            "        \"detail_id\": \"56522\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"中环至半山自动扶梯\",\n" +
            "        \"english_name\": \"Central-Mid-Levels Escalator and Walkway System\",\n" +
            "        \"grade\": 8.4,\n" +
            "        \"photo\": \"http://pic2.qyer.com/album/user/499/25/RUBcQB8EZg/index/300x300\",\n" +
            "        \"been_number\": \"6975\",\n" +
            "        \"rank\": \"景点观光第6名\",\n" +
            "        \"cate_name\": \"建筑\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-10-12 16:08:20.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_sight_img/3853.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3854\",\n" +
            "        \"detail_id\": \"94436\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"香港杜莎夫人蜡像馆\",\n" +
            "        \"english_name\": \"Madame Tussauds Hong Kong\",\n" +
            "        \"grade\": 8.43,\n" +
            "        \"photo\": \"http://pic1.qyer.com/album/user/1660/44/QE9TQh4DZUk/index/300x300\",\n" +
            "        \"been_number\": \"5482\",\n" +
            "        \"rank\": \"景点观光第7名\",\n" +
            "        \"cate_name\": \"艺术馆/展览中心,博物馆\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-10-12 15:12:21.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_sight_img/3854.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3855\",\n" +
            "        \"detail_id\": \"94785\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"湾仔\",\n" +
            "        \"english_name\": \"Wan Chai\",\n" +
            "        \"grade\": 8.31,\n" +
            "        \"photo\": \"http://pic3.qyer.com/album/user/367/77/Qk9SRR0FYQ/index/300x300\",\n" +
            "        \"been_number\": \"6336\",\n" +
            "        \"rank\": \"景点观光第8名\",\n" +
            "        \"cate_name\": \"街区\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-10-12 15:56:34.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_sight_img/3855.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3856\",\n" +
            "        \"detail_id\": \"94404\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"兰桂坊\",\n" +
            "        \"english_name\": \"Lan Kwai Fong\",\n" +
            "        \"grade\": 8.16,\n" +
            "        \"photo\": \"http://pic2.qyer.com/album/user/373/8/Qk5WQhIOZA/index/300x300\",\n" +
            "        \"been_number\": \"7793\",\n" +
            "        \"rank\": \"景点观光第9名\",\n" +
            "        \"cate_name\": \"街区\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-10-12 15:56:34.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_sight_img/3856.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"3857\",\n" +
            "        \"detail_id\": \"36513\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"中银大厦\",\n" +
            "        \"english_name\": \"Bank of China Tower\",\n" +
            "        \"grade\": 8.11,\n" +
            "        \"photo\": \"http://pic4.qyer.com/album/user/617/91/R0hSSxsBYA/index/300x300\",\n" +
            "        \"been_number\": \"8512\",\n" +
            "        \"rank\": \"景点观光第10名\",\n" +
            "        \"cate_name\": \"建筑\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-10-12 15:06:45.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_sight_img/3857.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"13244\",\n" +
            "        \"detail_id\": \"94444\",\n" +
            "        \"city_id\": \"50\",\n" +
            "        \"chinese_name\": \"浅水湾\",\n" +
            "        \"english_name\": \"Repulse Bay\",\n" +
            "        \"grade\": 8.35,\n" +
            "        \"photo\": \"http://pic3.qyer.com/album/user/352/28/QkxXQBIPYg/index/300x300\",\n" +
            "        \"been_number\": \"4581\",\n" +
            "        \"rank\": \"景点观光第11名\",\n" +
            "        \"cate_name\": \"海滩,公园/植物园,自然风光\",\n" +
            "        \"category_id\": null,\n" +
            "        \"create_time\": \"2016-10-12 17:44:33.0\",\n" +
            "        \"oss_url\": \"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/Img/qyer_sight_img/13244.jpg\",\n" +
            "        \"lng\": null,\n" +
            "        \"lat\": null\n" +
            "      }\n" +
            "    ],\n" +
            "    \"next\": 0,\n" +
            "    \"pageCount\": 0\n" +
            "  },\n" +
            "  \"result\": 1,\n" +
            "  \"timestamp\": \"1477627315716\",\n" +
            "  \"version\": \"1.0\",\n" +
            "  \"transactionid\": null\n" +
            "}";
}
