package com.abcs.haiwaigou.HDong;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;

import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.component.LazyViewPager;
import com.abcs.sociax.t4.component.SmallDialog;

import java.util.ArrayList;

public class HdongListActivity extends FragmentActivity {

    private TabLayout tb_information;
    private LazyViewPager vp_information;
    private SmallDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hdong_list);
        initView();
        initListener();
        initData();

    }


    private void initView() {
        tb_information = (TabLayout) findViewById(R.id.tb_information);
        vp_information = (LazyViewPager)findViewById(R.id.vp_information);
        vp_information.setOffscreenPageLimit(0);

        dialog = new SmallDialog(this, getString(R.string.please_wait));

        initTab();//初始化tab
    }

    //初始化TabLayout的设置
    public void initTab(){
        tb_information.setTabMode(TabLayout.MODE_SCROLLABLE);
        vp_information.setOffscreenPageLimit(0);
    }

    public void initListener(){
        vp_information.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tb_information));
        tb_information.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp_information.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    Handler handler=new Handler();

    public void initData(){
        if (!dialog.isShowing()){
            dialog.setContent("请稍后...");
            dialog.show();
        }
        getCate();
    }

    /**
     * 获取分类
     */
    public void getCate() {

//        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base + "/mobile/index.php", "act=activity&op=get_flash_sale_times&times=" +, new HttpRevMsg() {
//            @Override
//            public void revMsg(final String msg) {
//
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
////                        ProgressDlgUtil.stopProgressDlg();
//                        if (TextUtils.isEmpty(msg)) {
//                            return;
//                        } else {
//                      /*      Log.d("zds", "tags" + msg);
//
//                            QiangGouTag data = new Gson().fromJson(msg, QiangGouTag.class);
//
//                            if (data != null && data.state == 1) {
//                                if (data.datas != null) {
//                                    if (data.datas.size() > 0) {
//
//                                        for(int i=0;i<data.datas.size();i++){
//                                            final QiangGouTag.DatasEntry entity=data.datas.get(i);
//                                            final int k=i;
//                                        }
//
//                                    }
//                                }
//                            }*/
//                        }
//                    }
//                });
//            }
//        });


        dialog.dismiss();
        cateList.clear();
        for (int i = 0; i < 20; i++) {
            tb_information.addTab(tb_information.newTab().setText(i+"纸尿布"));
            cateList.add(i+"");
        }
        setPager(cateList);
    }

    ArrayList<String > cateList=new ArrayList<>();

    public void setPager(ArrayList<String> tables) {
//        HWGHDongListPagerAdapter adapter = new HWGHDongListPagerAdapter(getSupportFragmentManager(), tables);
//        vp_information.setAdapter(adapter);
    }
}
