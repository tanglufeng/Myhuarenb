package com.abcs.huaqiaobang.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.activity.HeaderViewActivity;
import com.abcs.huaqiaobang.adapter.MyCenterAdapter;
import com.abcs.huaqiaobang.main.MainActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.presenter.UserDataInterface;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;



/**
 * Created by Administrator on 2016/1/21.
 */
public class MyFragment extends Fragment implements View.OnClickListener,UserDataInterface {

    @InjectView(R.id.circle_bj)
    ImageView circleBj;
    @InjectView(R.id.my_tv_name)
    TextView myTvName;
    @InjectView(R.id.my_userId)
    TextView myUserId;
    @InjectView(R.id.my_userHeader)
    ImageView myUserHeader;
    @InjectView(R.id.tv_lever)
    TextView tvLever;
    @InjectView(R.id.my_vlever)
    TextView myVlever;
    @InjectView(R.id.my_tv_experience)
    TextView myTvExperience;
    @InjectView(R.id.my_vexperience)
    TextView myVexperience;
    @InjectView(R.id.my_tv_integral)
    TextView myTvIntegral;
    @InjectView(R.id.my_vintegral)
    TextView myVintegral;
    @InjectView(R.id.my_tv_personal)
    TextView myTvPersonal;
    @InjectView(R.id.my_tv_finance)
    TextView myTvFinance;
    @InjectView(R.id.my_tv_shopping)
    TextView myTvShopping;
    @InjectView(R.id.my_tv_more)
    TextView myTvMore;
    @InjectView(R.id.navagition)
    ImageView navagition;
    @InjectView(R.id.mycenter_viewpager)
    ViewPager mycenterViewpager;
    @InjectView(R.id.my_img_vPersonal)
    ImageView myImgVPersonal;
    @InjectView(R.id.my_rl_personal)
    RelativeLayout myRlPersonal;
    @InjectView(R.id.my_img_vFinance)
    ImageView myImgVFinance;
    @InjectView(R.id.my_rl_finance)
    RelativeLayout myRlFinance;
    @InjectView(R.id.my_img_vShopping)
    ImageView myImgVShopping;
    @InjectView(R.id.my_rl_shopping)
    RelativeLayout myRlShopping;
    @InjectView(R.id.my_img_vMore)
    ImageView myImgVMore;
    @InjectView(R.id.my_rl_more)
    RelativeLayout myRlMore;
    @InjectView(R.id.pagertab)
    LinearLayout pagertab;
    @InjectView(R.id.linea)
    LinearLayout linea;
    private View view;

    private ArrayList<Fragment> fragments;
    private int currentIndex = 0;
    private ArrayList<TextView> tab_list;
    public MyPersonalFragment myPersonalFragment;
    public MyFinanceFragment myFinanceFragment;
    public MyShoppingFragment myShoppingFragment;
    public MyMoreFragment myMoreFragment;
    private MyApplication instance;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (view == null) {
            view = inflater.inflate(R.layout.fragment_my, null);
        }
        ButterKnife.inject(this, view);

        IntentFilter filter = new IntentFilter("com.abcs.huaqiaobang.changeuser");
        getContext().registerReceiver(broadcastReceiver, filter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) linea.getLayoutParams();
            layoutParams.setMargins(0, ((MainActivity) getActivity()).getStatusBarHeight(), 0, 0);
        }
        initView();
        instance = MyApplication.getInstance();
        loadData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void loadData() {
        myUserHeader.setClickable("".equals(Util.preference.getString("logintype", "")) ? true : false);

        instance.imageLoader.displayImage(instance.self.getAvatarUrl(), myUserHeader, Options.getCircleListOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (bitmap != null)
                    myPersonalFragment.loadImage(bitmap);

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        myUserId.setText("ID:" + instance.self.getId());
        myTvName.setText(instance.self.getNickName());


        String param = "act=pointgrade&op=index&key=" + MyApplication.getInstance().getMykey();
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php", param, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.optInt("code") == 200) {

                                JSONObject datas = object.getJSONObject("datas");
                                JSONObject menber = datas.getJSONObject("result").getJSONObject("member_info");
                                myVlever.setText(menber.optInt("level") + "");
                                double ex = menber.optDouble("member_exppoints") / menber.optDouble("upgrade_exppoints");
                                //获取格式化对象
                                NumberFormat nt = NumberFormat.getPercentInstance();
                                //设置百分数精确度2即保留两位小数
                                nt.setMinimumFractionDigits(2);
                                myVexperience.setText(nt.format(ex));
                                myVintegral.setText(menber.optString("member_points"));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        if (myFinanceFragment != null) {
            myFinanceFragment.initUser();
        }
//        myPersonalFragment.loadData();


    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadData();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(broadcastReceiver);
    }


    private void initView() {
        fragments = new ArrayList<Fragment>();
        myPersonalFragment = new MyPersonalFragment();

        myShoppingFragment = new MyShoppingFragment();
        myMoreFragment = new MyMoreFragment();
        fragments.add(myPersonalFragment);
        if (Util.preference.getString("yclicai", "1").equals("1")) {
            myFinanceFragment = new MyFinanceFragment();
            fragments.add(myFinanceFragment);
        }
        fragments.add(myShoppingFragment);

        fragments.add(myMoreFragment);

//        tab_list = new ArrayList<TextView>();
//        tab_list.add(myTvPersonal);
//        tab_list.add(myTvFinance);
//        tab_list.add(myTvShopping);
//        tab_list.add(myTvMore);

        myRlFinance.setVisibility(Util.preference.getString("yclicai", "1").equals("1") ? View.VISIBLE : View.GONE);

        HttpRequest.sendGet(TLUrl.getInstance().URL_bind_base+"/im/rest/User/updatestate", null, new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                try {
                    JSONObject object = new JSONObject(msg);
                    int anInt = object.optInt("status", 0);
                    if (anInt == 1) {
                        String lcShow = object.optString("msg");
                        Util.preference.edit().putString("yclicai", lcShow).commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        myRlPersonal.setOnClickListener(this);
        myRlFinance.setOnClickListener(this);
        myRlShopping.setOnClickListener(this);
        myRlMore.setOnClickListener(this);
        myUserHeader.setOnClickListener(this);

//            Util.isThirdLogin = false;
//            myUserHeader.setClickable(false);
//        } else {
//            myUserHeader.setClickable(true);
//        }

//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Util.WIDTH / tab_list.size(), 3);
//        navagition.setLayoutParams(params);


        mycenterViewpager.setOffscreenPageLimit(4);
        MyCenterAdapter adapter = new MyCenterAdapter(getActivity().getSupportFragmentManager(), fragments);
        mycenterViewpager.setAdapter(adapter);

        mycenterViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) navagition
//                        .getLayoutParams();
//                currentIndex = position;
//
//                for (int i = 0; i < tab_list.size(); i++) {
//                    if (i != position) {
//                        tab_list.get(i).setTextColor(Color.parseColor("#666666"));
//                    } else {
//                        tab_list.get(position).setTextColor(Color.parseColor("#fa5759"));
//                    }
//                }
//
//
//                if (currentIndex == position) {
//                    lp.leftMargin = (int) (positionOffset * (Util.WIDTH * 1.0 / tab_list.size()) + currentIndex
//                            * (Util.WIDTH / tab_list.size()));
//                } else {
//                    lp.leftMargin = (int) (-(1 - positionOffset)
//                            * (Util.WIDTH * 1.0 / tab_list.size()) + currentIndex
//                            * (Util.WIDTH / tab_list.size()));
//                }
////                if (currentIndex == 1 && position == 0) // 1->0
////                } else if (currentIndex == 1 && position == 1) // 1->2
////                {
////                    lp.leftMargin = (int) (positionOffset * (Util.WIDTH * 1.0 / tab_list.size()) + currentIndex
////                            * (Util.WIDTH / tab_list.size()));
////                } else if (currentIndex == 2 && position == 1) // 2->1
////                {
////                    lp.leftMargin = (int) (-(1 - positionOffset)
////                            * (Util.WIDTH * 1.0 / tab_list.size()) + currentIndex
////                            * (Util.WIDTH / tab_list.size()));
////                }
//                navagition.setLayoutParams(lp);
                //设置图片变化

            }

            @Override
            public void onPageSelected(int position) {
                changePic(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changePic(int position) {

        defaultPic();

        if (fragments.get(position) instanceof MyPersonalFragment) {
            myImgVPersonal.setImageDrawable(getResources().getDrawable(R.drawable.my_img_geren2));
        } else if (fragments.get(position) instanceof MyFinanceFragment) {
            myImgVFinance.setImageDrawable(getResources().getDrawable(R.drawable.my_img_licai2));
        } else if (fragments.get(position) instanceof MyShoppingFragment) {
            myImgVShopping.setImageDrawable(getResources().getDrawable(R.drawable.my_img_gouwu2));
        } else if (fragments.get(position) instanceof MyMoreFragment) {
            myImgVMore.setImageDrawable(getResources().getDrawable(R.drawable.my_img_gengduo2));
        }

//        switch (position) {
//            case 0:
//                myImgVPersonal.setImageResource(R.drawable.my_img_geren2);
//
//                break;
//            case 1:
//                myImgVFinance.setImageResource(R.drawable.my_img_licai2);
//                break;
//            case 2:
//
//                myImgVShopping.setImageResource(R.drawable.my_img_gouwu2);
//                break;
//            case 3:
//                myImgVMore.setImageResource(R.drawable.my_img_gengduo2);
//                break;
//        }

    }

    private void defaultPic() {

        myImgVFinance.setImageDrawable(getResources().getDrawable(R.drawable.my_img_licai1));
        myImgVShopping.setImageDrawable(getResources().getDrawable(R.drawable.my_img_gouwu1));
        myImgVPersonal.setImageDrawable(getResources().getDrawable(R.drawable.my_img_geren1));
        myImgVMore.setImageDrawable(getResources().getDrawable(R.drawable.my_img_gengduo1));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.my_rl_personal:
                mycenterViewpager.setCurrentItem(0);
                break;
            case R.id.my_rl_finance:
                mycenterViewpager.setCurrentItem(1);
                break;
            case R.id.my_rl_shopping:
                if (myFinanceFragment == null) {
                    mycenterViewpager.setCurrentItem(1);
                } else {
                    mycenterViewpager.setCurrentItem(2);
                }
                break;
            case R.id.my_rl_more:
                mycenterViewpager.setCurrentItem(fragments.size() - 1);
                break;
            case R.id.my_userHeader:

                startActivity(new Intent(getActivity(), HeaderViewActivity.class));
//                startActivity(new Intent(getActivity(), HWQActivity.class));
                break;
        }
    }

    public void refreshNickName() {
        myTvName.setText(MyApplication.getInstance().self.getNickName());
    }

    public void refreshImgHeader(int i) {
        myUserHeader.setImageResource(R.drawable.img_avatar);
        myPersonalFragment.refreshImgHeader(1);
    }

    public void refreshImgHeader() {
        instance.imageLoader.displayImage(instance.self.getAvatarUrl(), myUserHeader, Options.getCircleListOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                myPersonalFragment.loadImage(bitmap);

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    public void refreshcash() {
        myFinanceFragment.refreshcash();
    }

    @Override
    public void loginSuccess() {

    }
}
