package com.abcs.hqbtravel.view.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.hqbtravel.adapter.DHangDetials2Adapter;
import com.abcs.hqbtravel.biz.DaoHangDetialsBiz;
import com.abcs.hqbtravel.donghua.JazzyHelper;
import com.abcs.hqbtravel.donghua.JazzyRecyclerViewScrollListener;
import com.abcs.hqbtravel.donghua.Utils;
import com.abcs.hqbtravel.entity.DaohangDetials2;
import com.abcs.hqbtravel.wedgt.SpaceItemDecoration;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.sociax.android.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.abcs.sociax.android.R.id.rv_youji;

public class DaoHangDetialsActivity2 extends Activity implements View.OnClickListener,RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{

    private ImageView back;
    private ImageView img_logo;
    private RelativeLayout play;
    private LinearLayout liner_yuyin_num;
    private TextView tv_youji_cn;
    private TextView tv_english;
    private TextView tv_yuyin_numbers;

    private DHangDetials2Adapter adapter;

    private EasyRecyclerView rv_dhdetials;

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
        setContentView(R.layout.activity_dao_hang_detials21);

        initViews();
        setListen();

        rv_dhdetials.setOnScrollListener(jazzyScrollListener=new JazzyRecyclerViewScrollListener());
        if (savedInstanceState != null) {
            mCurrentTransitionEffect = savedInstanceState.getInt(KEY_TRANSITION_EFFECT, JazzyHelper.GROW);
            setupJazziness(mCurrentTransitionEffect);
        }


    }

    public void setListen(){
        back.setOnClickListener(this);
        play.setOnClickListener(this);
    }
    private String dahangId;
    public void initViews(){

        dahangId=getIntent().getStringExtra("dahangid");

        rv_dhdetials=(EasyRecyclerView)findViewById(R.id.rv_dhdetials);


        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rv_dhdetials.addItemDecoration(new SpaceItemDecoration(5));

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                /**
                 *  分几列，普通的只需要1，而headerview则需要1
                 */
                if (position == 0) {
                    return 3;
                } else {
                    return 1;
                }
            }
        });

        rv_dhdetials.setLayoutManager(manager);
        rv_dhdetials.setRefreshListener(this);
         final View view = View.inflate(this, R.layout.item_daohang_detials21_top, null);
        back=(ImageView) view.findViewById(R.id.back);
        play=(RelativeLayout) view.findViewById(R.id.play);
        liner_yuyin_num=(LinearLayout) view.findViewById(R.id.liner_yuyin_num);
        img_logo=(ImageView) view.findViewById(R.id.img_logo);
        tv_youji_cn=(TextView) view.findViewById(R.id.name_cn);
        tv_yuyin_numbers=(TextView) view.findViewById(R.id.tv_yuyin_numbers);
        tv_english=(TextView) view.findViewById(R.id.name_en);

        adapter = new DHangDetials2Adapter(this);
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return view;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });

        rv_dhdetials.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final  DaohangDetials2.BodyEntity.GuideEntity.PicturesEntity picturesEntity=adapter.getItem(position);
                Intent t=new Intent(DaoHangDetialsActivity2.this,DaoHangPlayActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("guide_item",picturesEntity);
                t.putExtra("audio_url_item",bundle);
                t.putExtra("type","two");
//                t.putExtra("enName",guideEntity.enName);
//                t.putExtra("cnName",guideEntity.name);
                startActivity(t);
              
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

        if(!TextUtils.isEmpty(dahangId)){
            ProgressDlgUtil.showProgressDlg("Loading...", this);
            getDaoHangDetials();
        }

    }

    private boolean isRefresh = false;
    private int pageNO=1;
    private Handler mhandle=new Handler();
    private DaohangDetials2.BodyEntity.GuideEntity guideEntity;
    List<DaohangDetials2.BodyEntity.GuideEntity.PicturesEntity> datas=new ArrayList<>();

    private void getDaoHangDetials() {

        DaoHangDetialsBiz.getInstance(this).getDaoHangDetialsList(dahangId,pageNO+"",new Response.Listener<DaohangDetials2>(){
            @Override
            public void onResponse(final DaohangDetials2 response) {
                if(response!=null){
                    ProgressDlgUtil.stopProgressDlg();

                    if (isRefresh) {
                        adapter.clear();
                        isRefresh = false;
                    }

                    if(response.result==1){
                        if(response.body!=null){

                            if(response.body.pageCount>0){
                                tv_yuyin_numbers.setText("含"+response.body.pageCount+"处语音介绍");
                            }else {
                                liner_yuyin_num.setVisibility(View.GONE);
                            }

                            if(response.body.guide!=null){

                                guideEntity=response.body.guide;
//                            if(!TextUtils.isEmpty(response.body.guide.audioUrl)){
//                                audio_url=response.body.guide.audioUrl;
//                            }


                                if(!TextUtils.isEmpty(response.body.guide.picUrl)){
                                    MyApplication.imageLoader.displayImage(response.body.guide.picUrl, img_logo, MyApplication.getListOptions());
                                }

                                if(!TextUtils.isEmpty(response.body.guide.name)){
                                    tv_youji_cn.setText(response.body.guide.name);
                                }else {
                                    tv_youji_cn.setText("");
                                }

                                if(!TextUtils.isEmpty(response.body.guide.enName)){
                                    tv_english.setText(response.body.guide.enName);
                                }else {
                                    tv_english.setText("");
                                }

                                if(response.body.guide.pictures!=null){

                                    if(response.body.guide.pictures.size()>0){
                                        adapter.addAll(response.body.guide.pictures);
                                        adapter.notifyDataSetChanged();

                                    }
                                }
                            }
                            pageNO=response.body.next;
                        }
                    }

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDlgUtil.stopProgressDlg();
//                Toast.makeText(DaoHangDetialsActivity2.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.play:  //播放
                Intent t=new Intent(this,DaoHangPlayActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("guide",guideEntity);
                t.putExtra("audio_url",bundle);
                t.putExtra("type","one");
                startActivity(t);
                break;
        }
    }

    @Override
    public void onLoadMore() {

        if (pageNO == -1) {
            adapter.stopMore();
            return;
        }
        getDaoHangDetials();
    }

    @Override
    public void onRefresh() {

        pageNO = 1;
        isRefresh = true;
        getDaoHangDetials();
        mhandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                rv_dhdetials.setRefreshing(false);
            }
        },1000);
    }
}
