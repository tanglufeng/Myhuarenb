package com.abcs.haiwaigou.HDong;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.haiwaigou.local.fragment.HWGHDQiangFragment;
import com.abcs.haiwaigou.model.find.QiangGouTag;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HDongQiangGouActivity extends AppCompatActivity {


    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.tv_bottom_qiang_ing1)
    TextView tvBottomQiangIng1;
    @InjectView(R.id.txt_yikaiqiang_one)
    TextView txtYikaiqiangOne;
    @InjectView(R.id.re_qiang1)
    RelativeLayout reQiang1;
    @InjectView(R.id.tv_bottom_qiang_ing2)
    TextView tvBottomQiangIng2;
    @InjectView(R.id.txt_yikaiqiang_two)
    TextView txtYikaiqiangTwo;
    @InjectView(R.id.re_qiang2)
    RelativeLayout reQiang2;
    @InjectView(R.id.tv_bottom_qiang_ing3)
    TextView tvBottomQiangIng3;
    @InjectView(R.id.txt_yikaiqiang_three)
    TextView txtYikaiqiangThree;
    @InjectView(R.id.re_qiang3)
    RelativeLayout reQiang3;
    @InjectView(R.id.tv_bottom_qiang_ing4)
    TextView tvBottomQiangIng4;
    @InjectView(R.id.txt_yikaiqiang_four)
    TextView txtYikaiqiangFour;
    @InjectView(R.id.re_qiang4)
    RelativeLayout reQiang4;
    @InjectView(R.id.tv_bottom_qiang_ing5)
    TextView tvBottomQiangIng5;
    @InjectView(R.id.txt_yikaiqiang_five)
    TextView txtYikaiqiangFive;
    @InjectView(R.id.re_qiang5)
    RelativeLayout reQiang5;
    @InjectView(R.id.tv_bottom_qiang_ing6)
    TextView tvBottomQiangIng6;
    @InjectView(R.id.txt_yikaiqiang_sive)
    TextView txtYikaiqiangSive;
    @InjectView(R.id.re_qiang6)
    RelativeLayout reQiang6;
    @InjectView(R.id.tv_bottom_qiang_ing7)
    TextView tvBottomQiangIng7;
    @InjectView(R.id.txt_yikaiqiang_seven)
    TextView txtYikaiqiangSeven;
    @InjectView(R.id.re_qiang7)
    RelativeLayout reQiang7;
    @InjectView(R.id.main_pager)
    ViewPager mainPager;


    long time;
    private int COUNT = 7;
    RelativeLayout[] relative_n=new RelativeLayout[COUNT];
    TextView[] tv_n= new TextView[COUNT];
    TextView[] txt_n= new TextView[COUNT];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_qianggou);
        ButterKnife.inject(this);

        time = getIntent().getLongExtra("time", 0);
        Log.i("zds", "onCreate: " + time);

        initView();
        initDatas(time);
    }

    Handler handler = new Handler();
    CFViewPagerAdapter viewPagerAdapter;
    int yes_qing=0;

    private void initDatas(long time) {
//        ProgressDlgUtil.showProgressDlg("",this);
//        http://www.huaqiaobang.com/mobile/index.php?act=activity&op=get_flash_sale_times&times=1490954400
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base + "/mobile/index.php", "act=activity&op=get_flash_sale_times&times=" + time, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        ProgressDlgUtil.stopProgressDlg();
                        if (TextUtils.isEmpty(msg)) {
                            return;
                        } else {
                            Log.d("zds", "tags" + msg);

                            QiangGouTag data = new Gson().fromJson(msg, QiangGouTag.class);

                            viewPagerAdapter = new CFViewPagerAdapter(getSupportFragmentManager());

                            if (data != null && data.state == 1) {
                                if (data.datas != null) {
                                    if (data.datas.size() > 0) {

                                        for(int i=0;i<data.datas.size();i++){
                                            final QiangGouTag.DatasEntry entity=data.datas.get(i);
                                            final int k=i;
                                            if(i<7){

                                                relative_n[i].setVisibility(View.VISIBLE);
                                                viewPagerAdapter.getDatas().add(HWGHDQiangFragment.newInstance("" + i, entity.times));
                                                viewPagerAdapter.getTitle().add(i, "" + i);

                                                if(!TextUtils.isEmpty(entity.title)){
                                                    txt_n[i].setText(entity.title);
                                                    if(entity.title.equals("抢购中")){
                                                        yes_qing=i;
                                                    }
                                                }

                                                if(!TextUtils.isEmpty(entity.time)){
                                                    tv_n[i].setText(entity.time);
                                                }

                                                relative_n[i].setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        setUI(k);
                                                        mainPager.setCurrentItem(k);
                                                    }
                                                });
                                            }
                                        }

                                        if(data.datas.size()<5){
                                            for(int p=data.datas.size();p<5;p++){
                                                relative_n[p].setVisibility(View.VISIBLE);
                                            }
                                        }

                                        initViewPager(yes_qing);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    private void initViewPager(int yes_qing) {

        //滑动的viewpager
        mainPager.setAdapter(viewPagerAdapter);
        mainPager.setOffscreenPageLimit(1);
        setUI(yes_qing);
        mainPager.setCurrentItem(yes_qing);

        mainPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                Log.i("zds", "postion===" + position);
                setUI(position);
                mainPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int position) {

                System.out.println("Change Posiont:" + position);

                // TODO Auto-generated method stub

            }
        });
    }



    public void setUI(int position) {

        for (int i = 0; i < COUNT; i++) {
            if (position == i) {
                relative_n[i].setBackgroundResource(R.drawable.bg_qiang_selected);
                tv_n[i].setTextColor(this.getResources().getColor(R.color.white));
                txt_n[i].setTextColor(this.getResources().getColor(R.color.white));
            } else {
                relative_n[i].setBackgroundResource(R.drawable.transparent);
                tv_n[i].setTextColor(this.getResources().getColor(R.color.bg_black));
                txt_n[i].setTextColor(this.getResources().getColor(R.color.translucent_background_80));
            }
            continue;
        }
    }

    private void initView() {

        relative_n[0]=reQiang1;
        relative_n[1]=reQiang2;
        relative_n[2]=reQiang3;
        relative_n[3]=reQiang4;
        relative_n[4]=reQiang5;
        relative_n[5]=reQiang6;
        relative_n[6]=reQiang7;

        tv_n[0]=tvBottomQiangIng1;
        tv_n[1]=tvBottomQiangIng2;
        tv_n[2]=tvBottomQiangIng3;
        tv_n[3]=tvBottomQiangIng4;
        tv_n[4]=tvBottomQiangIng5;
        tv_n[5]=tvBottomQiangIng6;
        tv_n[6]=tvBottomQiangIng7;

        txt_n[0]=txtYikaiqiangOne;
        txt_n[1]=txtYikaiqiangTwo;
        txt_n[2]=txtYikaiqiangThree;
        txt_n[3]=txtYikaiqiangFour;
        txt_n[4]=txtYikaiqiangFive;
        txt_n[5]=txtYikaiqiangSive;
        txt_n[6]=txtYikaiqiangSeven;

    }

    @OnClick(R.id.relative_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_back:
                finish();
                break;
        }
    }
}
