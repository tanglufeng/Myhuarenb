package com.abcs.haiwaigou.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.CartActivity2;
import com.abcs.haiwaigou.activity.GoodsSearchActivity2;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.utils.InitCarNum;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.FromPopBuildHuiYuanActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.ActivityHome;
import com.abcs.sociax.t4.android.fragment.FragmentSociax;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjz on 2016/9/13.
 */
public class HWGMainFragment2 extends FragmentSociax implements View.OnClickListener{
    @InjectView(R.id.statusbar)
    View statusbar;
    @InjectView(R.id.rb_goods)
    RadioButton rbGoods;
    @InjectView(R.id.rb_qingshe)
    RadioButton rbQingshe;
    @InjectView(R.id.rb_dudao)
    RadioButton rbDuDao;
    @InjectView(R.id.rb_wanle)
    RadioButton rbWanLe;
    @InjectView(R.id.radioGroup)
    RadioGroup radioGroup;
    @InjectView(R.id.relative_search)
    RelativeLayout relativeSearch;
    @InjectView(R.id.car_num)
    TextView carNum;
    @InjectView(R.id.relative_cart)
    RelativeLayout relativeCart;
    @InjectView(R.id.relative_title)
    RelativeLayout relativeTitle;
    @InjectView(R.id.content)
    FrameLayout content;
    private ActivityHome activity;
    private View view;
    public static TextView car_num;
    private MyBroadCastReceiver myBroadCastReceiver;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    HWGGoodsFragment hwgGoodsFragment;
    HWGLuxuryFragment hwgLuxuryFragment;
    HWGDuDaoFragment2 hwgDudaoFragment;
    HWGWanLeFragment hwgWanLeFragment;
    @Override
    public void onResume() {
        super.onResume();
        myBroadCastReceiver = new MyBroadCastReceiver(activity, updateUI);
        myBroadCastReceiver.register();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ActivityHome) getActivity();
        view = activity.getLayoutInflater().inflate(
                R.layout.hwg_main_fragment2, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////            window = getWindow();
//            view.findViewById(R.id.statusbar).setVisibility(View.VISIBLE);
        }
        ButterKnife.inject(this, view);
        manager = getChildFragmentManager();
        car_num = (TextView) view.findViewById(R.id.car_num);

        initPopWindows();
        initInCartNum();
        initListener();
        initRadioGroup();

    }

    private Handler mHandler=new Handler();
    private void initPopWindows(){

            Log.i("zds", "ishuodong_url=" + TLUrl.getInstance().URL_vip_ishuodong + "&key=" + MyApplication.getInstance().getMykey());

            HttpRequest.sendPost(TLUrl.getInstance().URL_vip_ishuodong, "&key=" + MyApplication.getInstance().getMykey() , new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try{

                                if(!TextUtils.isEmpty(msg)){
//                                    {"state":1,"id":"1"}

                                    Log.e("zds_hdong",msg+"");

                                    final JSONObject respone=new JSONObject(msg);
                                    if (respone.getInt("state") == 1) {//搞活动

                                        View root_view=View.inflate(activity,R.layout.pop_christmas_view,null);
                                        ImageView img_close=(ImageView) root_view.findViewById(R.id.imge_christmas_close);
                                        ImageView img_tovip=(ImageView) root_view.findViewById(R.id.img_tovip);

                                        final PopupWindow popupWindow= new PopupWindow(root_view, Util.WIDTH, ViewGroup.LayoutParams.MATCH_PARENT, true);
                                        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                                        params.alpha = 0.5f;
                                        activity.getWindow().setAttributes(params);
                                        popupWindow.setTouchable(true);
                                        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {
                                                return false;
                                            }
                                        });
                                        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                            @Override
                                            public void onDismiss() {
                                                WindowManager.LayoutParams params = activity.getWindow()
                                                        .getAttributes();
                                                params.alpha = 1f;
                                                activity.getWindow().setAttributes(params);
                                            }
                                        });
                                        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#66000000")));
                                        popupWindow.showAtLocation(root_view, Gravity.CENTER, 0, 0);

                                        img_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                popupWindow.dismiss();
                                            }
                                        });
                                        img_tovip.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                popupWindow.dismiss();
                                                Intent intent=new Intent(activity, FromPopBuildHuiYuanActivity.class);
                                                intent.putExtra("huod_id", respone.optString("id"));
                                                activity.startActivity(intent);
                                            }
                                        });

                                    }else {

                                    }
                                }else {
                                    return;
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

    }
    public void initListener() {
        relativeSearch.setOnClickListener(this);
        relativeCart.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
//        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        ButterKnife.inject(this, rootView);
//        return rootView;

        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) view.getParent();

        if (p != null)
            p.removeAllViewsInLayout();
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public int getLayoutId() {
        return  R.layout.hwg_main_fragment2;
    }


    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {
            initInCartNum();
        }

        @Override
        public void updateCollection(Intent intent) {

        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initInCartNum() {
        if (MyApplication.getInstance().self != null) {
            new InitCarNum(car_num, activity,"");
        } else {
            car_num.setVisibility(View.GONE);
        }
    }


    private void initRadioGroup() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = manager.beginTransaction();
                hintFragment();
                switch (checkedId) {
                    case R.id.rb_goods:
                        if (hwgGoodsFragment == null) {
                            hwgGoodsFragment = new HWGGoodsFragment();
                            transaction.add(R.id.content, hwgGoodsFragment);
                        } else {
                            transaction.show(hwgGoodsFragment);
                        }
                        rbGoods.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                    case R.id.rb_qingshe:
                        if (hwgLuxuryFragment == null) {
                            hwgLuxuryFragment = new HWGLuxuryFragment();
                            transaction.add(R.id.content, hwgLuxuryFragment);
                        } else {
                            transaction.show(hwgLuxuryFragment);
                        }
                        rbQingshe.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                    case R.id.rb_dudao://独到  ddddd

                        if (hwgDudaoFragment == null) {
                            hwgDudaoFragment = new HWGDuDaoFragment2();
                            transaction.add(R.id.content, hwgDudaoFragment);
                        } else {
                            transaction.show(hwgDudaoFragment);
                        }
                        rbDuDao.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                    case R.id.rb_wanle:// 玩乐

                        if (hwgWanLeFragment == null) {
                            hwgWanLeFragment = new HWGWanLeFragment();
                            transaction.add(R.id.content, hwgWanLeFragment);
                        } else {
                            transaction.show(hwgWanLeFragment);
                        }
                        rbWanLe.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                    default:
                        break;
                }
                transaction.commit();
            }
        });
        ((RadioButton) radioGroup.findViewById(R.id.rb_goods)).setChecked(true);
    }

    private void hintFragment() {

        rbGoods.setTextColor(getResources().getColor(R.color.white));
        rbQingshe.setTextColor(getResources().getColor(R.color.white));
        rbDuDao.setTextColor(getResources().getColor(R.color.white));
        rbWanLe.setTextColor(getResources().getColor(R.color.white));
        if (hwgGoodsFragment != null)
            transaction.hide(hwgGoodsFragment);
        if (hwgLuxuryFragment != null)
            transaction.hide(hwgLuxuryFragment);
        if (hwgDudaoFragment != null)
            transaction.hide(hwgDudaoFragment);
        if (hwgWanLeFragment != null)
            transaction.hide(hwgWanLeFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        myBroadCastReceiver.unRegister();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initIntentData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.relative_search:
                intent = new Intent(getActivity(), GoodsSearchActivity2.class);
                startActivity(intent);
                break;
            case R.id.relative_cart:
                if (MyApplication.getInstance().self == null) {
                    intent = new Intent(activity, WXEntryActivity.class);
                    activity.startActivity(intent);
                    return;
                }
                intent = new Intent(activity, CartActivity2.class);
                intent.putExtra("store_id","");
                activity.startActivity(intent);
                break;
            default:
                break;
        }
    }
}
