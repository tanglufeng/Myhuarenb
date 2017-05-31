package com.abcs.haiwaigou.HDong;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.haiwaigou.model.ReXiaoItem;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ReXiaoActivity extends AppCompatActivity {


    CFViewPagerAdapter viewPagerAdapter;

    @InjectView(R.id.tljr_txt_news_title)
    TextView tljrTxtNewsTitle;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.top)
    RelativeLayout top;
    @InjectView(R.id.iv_logo)
    ImageView ivLogo;
    @InjectView(R.id.iv_shishi_rexiao)
    ImageView ivShishiRexiao;
    @InjectView(R.id.tv_shishi_rexiao)
    TextView tvShishiRexiao;
    @InjectView(R.id.liner_shishi_rexiao)
    LinearLayout linerShishiRexiao;
    @InjectView(R.id.iv_meizhou_rexiao)
    ImageView ivMeizhouRexiao;
    @InjectView(R.id.tv_meizhou_rexiao)
    TextView tvMeizhouRexiao;
    @InjectView(R.id.liner_meizhou_rexiao)
    LinearLayout linerMeizhouRexiao;
    @InjectView(R.id.line)
    View line;
    @InjectView(R.id.lin_middle)
    LinearLayout linMiddle;
    @InjectView(R.id.viewpager)
    ViewPager viewPager;


    private String refId;
    private String refImg;
    int COUNT=2;
    private TextView[] title=new TextView[COUNT];
    private ImageView[] imges=new ImageView[COUNT];
    LinearLayout[] relative_n=new LinearLayout[COUNT];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_xiao);
        ButterKnife.inject(this);

        title[0]=tvShishiRexiao;
        title[1]=tvMeizhouRexiao;
        imges[0]=ivShishiRexiao;
        imges[1]=ivMeizhouRexiao;
        relative_n[0]=linerShishiRexiao;
        relative_n[1]=linerMeizhouRexiao;

        refId = getIntent().getStringExtra("refId");
        refImg = getIntent().getStringExtra("refImg");

        if (!TextUtils.isEmpty(refImg)) {
            MyApplication.imageLoader.displayImage(refImg, ivLogo, MyApplication.getListOptions());
        }

        getDatas();
        initViewPager();

    }

    boolean first = false;
    Handler handler = new Handler();


    private void initViewPager() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(position);
                setUI(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int position) {
            }
        });
    }


    private void getDatas() {

//        ProgressDlgUtil.showProgressDlg("Loading...", this);

//        http://www.huaqiaobang.com/mobile/index.php?act=activity&op=get_avtivity_ref&ref_id=1
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php","act=activity&op=get_avtivity_ref&ref_id="+ refId, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.e("msg", "msg==" + msg);
//                        ProgressDlgUtil.stopProgressDlg();
                        if(TextUtils.isEmpty(msg)){
                            return;
                        }else {

                            ReXiaoItem item=new Gson().fromJson(msg, ReXiaoItem.class);
                            if(item!=null&&item.state==1){
                                if(item.datas!=null&&item.datas.size()>0){

                                    viewPagerAdapter = new CFViewPagerAdapter(getSupportFragmentManager());

                                  for(int i=0;i<item.datas.size();i++){
                                      ReXiaoItem.DatasEntry bean=item.datas.get(i);
                                      final int k=i;
                                      if(i<2){
                                          if(bean!=null){

                                              imges[i].setVisibility(View.VISIBLE);

                                              Log.i("zds", "run: "+bean.refType+"  ");
                                              Log.i("zds", "run: "+bean.refParam);

                                              viewPagerAdapter.getDatas().add(ReXiaoShiShiFragment.newInstance(bean.refType, bean.refParam));
                                              viewPagerAdapter.getTitle().add(i, "" + i);

                                              if(!TextUtils.isEmpty(bean.title)){
                                                  title[i].setText(bean.title);
                                              }

                                              relative_n[i].setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      setUI(k);
                                                      viewPager.setCurrentItem(k);
                                                  }
                                              });
                                          }
                                      }
                                  }
                                    //滑动的viewpager
                                    viewPager.setAdapter(viewPagerAdapter);
                                    viewPager.setOffscreenPageLimit(1);
                                    viewPager.setCurrentItem(0);

                                }
                            }
                        }
                    }
                });
            }
        });
    }

    public void setUI(int position) {

        for (int i = 0; i < COUNT; i++) {
            if(position==0){
                imges[0].setImageResource(R.drawable.iv_hwg_rexiao_shishi);
                title[0].setTextColor(getResources().getColor(R.color.red));
                imges[1].setImageResource(R.drawable.iv_hwg_rexiao_everyweek_no);
                title[1].setTextColor(getResources().getColor(R.color.title_color));
            }else if(position==1){
                imges[0].setImageResource(R.drawable.iv_hwg_rexiao_shishi_no);
                title[0].setTextColor(getResources().getColor(R.color.title_color));
                imges[1].setImageResource(R.drawable.iv_hwg_rexiao_everyweek);
                title[1].setTextColor(getResources().getColor(R.color.red));
            }
            continue;
        }
    }


    @OnClick({R.id.relative_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_back:
                finish();
                break;
          /*  case R.id.liner_shishi_rexiao:  //  实时热销
                imges[0].setImageResource(R.drawable.iv_hwg_rexiao_shishi);
                title[0].setTextColor(getResources().getColor(R.color.red));
                imges[1].setImageResource(R.drawable.iv_hwg_rexiao_everyweek_no);
                title[1].setTextColor(getResources().getColor(R.color.title_color));

                viewPager.setCurrentItem(0);

                break;
            case R.id.liner_meizhou_rexiao:  //  每周热销
                imges[0].setImageResource(R.drawable.iv_hwg_rexiao_shishi_no);
                title[0].setTextColor(getResources().getColor(R.color.title_color));
                imges[1].setImageResource(R.drawable.iv_hwg_rexiao_everyweek);
                title[1].setTextColor(getResources().getColor(R.color.red));
                viewPager.setCurrentItem(1);
                break;*/
        }
    }
}
