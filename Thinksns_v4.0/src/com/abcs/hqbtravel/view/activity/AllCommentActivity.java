package com.abcs.hqbtravel.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.adapter.AllCommentAdapter;
import com.abcs.hqbtravel.donghua.JazzyHelper;
import com.abcs.hqbtravel.donghua.JazzyRecyclerViewScrollListener;
import com.abcs.hqbtravel.donghua.Utils;
import com.abcs.hqbtravel.entity.Comment;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AllCommentActivity extends BaseActivity implements View.OnClickListener,RecyclerArrayAdapter.OnLoadMoreListener{

    @InjectView(R.id.img_back)
    ImageView img_back;
    @InjectView(R.id.pb_five_star)
    ProgressBar pb_five_star;
    @InjectView(R.id.pb_four_star)
    ProgressBar pb_four_star;
    @InjectView(R.id.pb_three_star)
    ProgressBar pb_three_star;
    @InjectView(R.id.pb_two_star)
    ProgressBar pb_two_star;
    @InjectView(R.id.pb_one_star)
    ProgressBar pb_one_star;

    @InjectView(R.id.tv_five_star)
    TextView tv_five_star;
    @InjectView(R.id.tv_four_star)
    TextView tv_four_star;
    @InjectView(R.id.tv_three_star)
    TextView tv_three_star;
    @InjectView(R.id.tv_two_star)
    TextView tv_two_star;
    @InjectView(R.id.tv_one_star)
    TextView tv_one_star;
    @InjectView(R.id.tv_grade)
    TextView tv_grade;
    @InjectView(R.id.ratingBar)
    RatingBar ratingBar;

    @InjectView(R.id.rv_all_comments)
    EasyRecyclerView rvAllComment;

    Comment comment;

    List<TextView> pbs_num=new ArrayList<>();
    List<ProgressBar> pbs=new ArrayList<>();


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
        setContentView(R.layout.hqb_travle_all_comments);
        ButterKnife.inject(this);

        pbs_num.add(tv_five_star);
        pbs_num.add(tv_four_star);
        pbs_num.add(tv_three_star);
        pbs_num.add(tv_two_star);
        pbs_num.add(tv_one_star);

        pbs.add(pb_five_star);
        pbs.add(pb_four_star);
        pbs.add(pb_three_star);
        pbs.add(pb_two_star);
        pbs.add(pb_one_star);

        rvAllComment.setOnScrollListener(jazzyScrollListener=new JazzyRecyclerViewScrollListener());
        if (savedInstanceState != null) {
            mCurrentTransitionEffect = savedInstanceState.getInt(KEY_TRANSITION_EFFECT, JazzyHelper.GROW);
            setupJazziness(mCurrentTransitionEffect);
        }


        detailId=getIntent().getStringExtra("detialsId");
        type=getIntent().getStringExtra("type");
        comment=(Comment) getIntent().getSerializableExtra("progerss");
        if(comment!=null){
            if(comment.result==1){

                if(comment.body.score!=null){
                    tv_grade.setText(comment.body.score.scoreFloat);
                    ratingBar.setRating(Float.valueOf(comment.body.score.scoreFloat));

                    if(comment.body.score.percentage!=null&&comment.body.score.percentage.size()>0){   //否则无数据
                        for (int i=0;i<comment.body.score.percentage.size();i++){

                            if(i<5){
                                pbs_num.get(i).setText(comment.body.score.percentage.get(i)+"%");

                                double d1=comment.body.score.percentage.get(i);

                                pbs.get(i).setProgress((int)d1);

                            }
                        }

                    }
                }
            }else {
                showToast("解析出错！");
            }
        }

        if(!TextUtils.isEmpty(detailId)&&!TextUtils.isEmpty(type)){
//            Util.checkNetUsable()
            getComment();
        }
        iniListne();
        
    }

    private AllCommentAdapter adapter;
    private void iniListne(){
        img_back.setOnClickListener(this);


        rvAllComment.setLayoutManager(new LinearLayoutManager(this));
//        rvAllComment.setRefreshListener(this);
        rvAllComment.setRefreshing(false);
        rvAllComment.setAdapter(adapter = new AllCommentAdapter(this));
//        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//
//            }
//        });

        adapter.setNoMore(R.layout.view_nomore);
        adapter.setMore(R.layout.view_more, this);
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });
        
    }


    private Handler mhandle=new Handler();

    private int pageNo=1;
    private boolean isRefresh = false;
    String detailId;
    String type;

    private void getComment(){

        com.abcs.huaqiaobang.util.HttpRequest.sendGet(Contonst.HOST+"/getComments", "detailId=" + detailId + "&pageNo="+pageNo +"&pageSize=10"+ "&type=" + type, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                mhandle.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds","getallcomment=="+msg);
                        try {

                            ProgressDlgUtil.stopProgressDlg();
                            if (isRefresh) {
                                adapter.clear();
                                isRefresh = false;
                            }

                            Comment comment=  new Gson().fromJson(msg, Comment.class);
                            if(comment!=null){
                                if(comment.result==1){

                                    if(comment.body.items!=null){

                                        if(comment.body.items.size()>0){
                                            adapter.addAll(comment.body.items);
                                        }else {
                                            adapter.stopMore();
                                            return;
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                    pageNo = pageNo+1;
                                }else {
                                    showToast("解析出错！");
                                }
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Log.i("zds", e.toString());
                            Log.i("zds", msg);
                            e.printStackTrace();
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }

//    @Override
//    public void onRefresh() {
//        pageNo = 1;
//        isRefresh = true;
//        getComment();
//    }

    @Override
    public void onLoadMore() {
        if (pageNo == -1) {
            adapter.stopMore();
            return;
        }
        getComment();
    }
}
