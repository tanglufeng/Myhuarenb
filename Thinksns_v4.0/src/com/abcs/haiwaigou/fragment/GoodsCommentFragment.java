package com.abcs.haiwaigou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.GoodsCommentActivity;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjz on 2016/6/22 0022.
 */
public class GoodsCommentFragment extends BaseFragment implements View.OnClickListener {
    GoodsCommentActivity activity;
    @InjectView(R.id.t_percent)
    TextView tPercent;
    @InjectView(R.id.t_all)
    TextView tAll;
    @InjectView(R.id.t_good)
    TextView tGood;
    @InjectView(R.id.t_middle)
    TextView tMiddle;
    @InjectView(R.id.t_bad)
    TextView tBad;
    @InjectView(R.id.t_image)
    TextView tImage;
    @InjectView(R.id.frame_comment)
    FrameLayout frameComment;
    @InjectView(R.id.linear_top)
    LinearLayout linearTop;
    private View view;

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    private RequestQueue mRequestQueue;
    private String goods_id;
    private String all_comment = "0";
    private String good_percent = "0";
    private String good_comment = "0";
    private String middle_comment = "0";
    private String bad_comment = "0";
    private String image_comment = "0";
    CommentItemFragment allFragment;
    CommentGoodFragment goodFragment;
    CommentMiddleFragment middleFragment;
    CommentBadFragment badFragment;
    CommentImageFragment commentImageFragment;
    private MyBroadCastReceiver myBroadCastReceiver;

    public static GoodsCommentFragment newInstance(String goods_id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("goods_id", goods_id);
        GoodsCommentFragment fragment = new GoodsCommentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private FragmentManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        activity = (GoodsCommentActivity) getActivity();

        mRequestQueue = Volley.newRequestQueue(activity);
        manager = activity.getSupportFragmentManager();
        myBroadCastReceiver = new MyBroadCastReceiver(activity, updateUI);
        myBroadCastReceiver.register();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(
                    R.layout.hwg_goods_fragment_comment, null);
            ButterKnife.inject(this, view);
            Bundle bundle = getArguments();
            if (bundle != null) {
                goods_id = bundle.getString("goods_id");
            }
            isPrepared = true;
            lazyLoad();
        }
        setOnListener();
        ButterKnife.inject(this, view);
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);

        return view;
    }

    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {

        }

        @Override
        public void updateCollection(Intent intent) {
            if (intent.getStringExtra("type").equals(MyUpdateUI.SHOWCOMMENTTITLE)) {
                if (linearTop != null) {
                    linearTop.setVisibility(View.VISIBLE);
                }
            } else if (intent.getStringExtra("type").equals(MyUpdateUI.HIDECOMMENTTITLE)) {
                if (linearTop != null) {
                    linearTop.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void setOnListener() {
        tAll.setOnClickListener(this);
        tGood.setOnClickListener(this);
        tMiddle.setOnClickListener(this);
        tBad.setOnClickListener(this);
        tImage.setOnClickListener(this);
    }

    private void initAllDates() {

        Bundle bundle = new Bundle();
        bundle.putSerializable("goods_id", goods_id);
        bundle.putSerializable("isShow", true);
        allFragment = new CommentItemFragment();
        allFragment.setArguments(bundle);
        manager.beginTransaction().add(R.id.frame_comment, allFragment)
//                .add(R.id.frame_comment, goodFragment).add(R.id.frame_comment, middleFragment)
//                .add(R.id.frame_comment, badFragment).add(R.id.frame_comment, commentImageFragment)
//                .hide(goodFragment).hide(middleFragment).hide(badFragment).hide(commentImageFragment)
                .show(allFragment)
                .commitAllowingStateLoss();
        Log.i("zjz", "goods_id=" + goods_id);
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, TLUrl.getInstance().URL_hwg_goods_comment2 + "&goods_id=" + goods_id + "&type=1" + "&curpage=" + 1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        LogUtil.e("zjz", "comment_title:连接成功");
                        LogUtil.e("zjz", "response=" + response);
                        if (response.getInt("page_total") != 0) {
                            JSONObject object = response.getJSONObject("datas");
                            JSONObject jsonObject = object.getJSONObject("goods_evaluate_info");
                            good_percent = jsonObject.optString("good_percent");
                            all_comment = jsonObject.optString("all");
                            good_comment = jsonObject.optString("good");
                            middle_comment = jsonObject.optString("normal");
                            bad_comment = jsonObject.optString("bad");
                            image_comment = jsonObject.optString("imagecount");
                        }
                        LogUtil.e("zjz", "好评率:" + good_percent + "全部：" + all_comment + "好评：" + good_comment + "中评：" + middle_comment + "差评：" + bad_comment + "有图：" + image_comment);
                        initView();
                        mHasLoadedOnce = true;
                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mRequestQueue.add(jr);
    }

    private void initView() {
        if (tPercent != null)
            tPercent.setText(good_percent + "%");
        if (tAll != null){
            tAll.setText("全部(" + all_comment + ")");
            tAll.setTextColor(activity.getResources().getColor(R.color.white));
            tAll.setBackground(activity.getResources().getDrawable(R.drawable.img_hwg_commet_bg3));
        }
        if (tGood != null)
            tGood.setText("好评(" + good_comment + ")");
        if (tMiddle != null)
            tMiddle.setText("中评(" + middle_comment + ")");
        if (tBad != null)
            tBad.setText("差评(" + bad_comment + ")");
        if (tImage != null)
            tImage.setText("晒图(" + image_comment + ")");


//        goodFragment = new CommentGoodFragment();
//        goodFragment.setArguments(bundle);
//        middleFragment = new CommentMiddleFragment();
//        middleFragment.setArguments(bundle);
//        badFragment = new CommentBadFragment();
//        badFragment.setArguments(bundle);
//        commentImageFragment = new CommentImageFragment();
//        commentImageFragment.setArguments(bundle);



    }

    @Override
    protected void lazyLoad() {
//        if (isVisible) {
//            GoodsDetailActivity.setIsVisible(GoodsDetailActivity.COMMENT);
//        }
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }

        initAllDates();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        myBroadCastReceiver.unRegister();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (allFragment != null)
            transaction.hide(allFragment);
        if (goodFragment != null)
            transaction.hide(goodFragment);
        if (middleFragment != null)
            transaction.hide(middleFragment);
        if (badFragment != null)
            transaction.hide(badFragment);
        if (commentImageFragment != null)
            transaction.hide(commentImageFragment);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("goods_id", goods_id);
        bundle.putSerializable("isShow", true);
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        tAll.setTextColor(activity.getResources().getColor(R.color.noselect));
        tAll.setBackground(activity.getResources().getDrawable(R.drawable.img_hwg_commet_bg2));
        tGood.setTextColor(activity.getResources().getColor(R.color.noselect));
        tGood.setBackground(activity.getResources().getDrawable(R.drawable.img_hwg_commet_bg2));
        tMiddle.setTextColor(activity.getResources().getColor(R.color.noselect));
        tMiddle.setBackground(activity.getResources().getDrawable(R.drawable.img_hwg_commet_bg2));
        tBad.setTextColor(activity.getResources().getColor(R.color.noselect));
        tBad.setBackground(activity.getResources().getDrawable(R.drawable.img_hwg_commet_bg1));
        tImage.setTextColor(activity.getResources().getColor(R.color.noselect));
        tImage.setBackground(activity.getResources().getDrawable(R.drawable.img_hwg_commet_bg2));
        switch (v.getId()) {
            case R.id.t_all:
                tAll.setTextColor(activity.getResources().getColor(R.color.white));
                tAll.setBackground(activity.getResources().getDrawable(R.drawable.img_hwg_commet_bg3));
                if (allFragment != null) {
                    transaction.show(allFragment);
                } else {
                    Log.i("zjz", "all为空");
                    allFragment = new CommentItemFragment();
                    allFragment.setArguments(bundle);
                    transaction.add(R.id.frame_comment, allFragment);
                }
                break;
            case R.id.t_good:
                tGood.setTextColor(activity.getResources().getColor(R.color.white));
                tGood.setBackground(activity.getResources().getDrawable(R.drawable.img_hwg_commet_bg3));
                if (goodFragment != null) {
                    transaction.show(goodFragment);
                } else {
                    Log.i("zjz", "goods为空");
                    goodFragment = new CommentGoodFragment();
                    goodFragment.setArguments(bundle);
                    transaction.add(R.id.frame_comment, goodFragment);
                }
                break;
            case R.id.t_middle:
                tMiddle.setTextColor(activity.getResources().getColor(R.color.white));
                tMiddle.setBackground(activity.getResources().getDrawable(R.drawable.img_hwg_commet_bg3));
                if (middleFragment != null) {
                    transaction.show(middleFragment);
                } else {
                    Log.i("zjz", "middle为空");
                    middleFragment = new CommentMiddleFragment();
                    middleFragment.setArguments(bundle);
                    transaction.add(R.id.frame_comment, middleFragment);
                }

                break;
            case R.id.t_bad:
                tBad.setTextColor(activity.getResources().getColor(R.color.white));
                tBad.setBackground(activity.getResources().getDrawable(R.drawable.img_hwg_commet_bg3));
                if (badFragment != null) {
                    transaction.show(badFragment);
                } else {
                    Log.i("zjz", "bad为空");
                    badFragment = new CommentBadFragment();
                    badFragment.setArguments(bundle);
                    transaction.add(R.id.frame_comment, badFragment);
                }
                break;
            case R.id.t_image:
                tImage.setTextColor(activity.getResources().getColor(R.color.white));
                tImage.setBackground(activity.getResources().getDrawable(R.drawable.img_hwg_commet_bg3));
                if (commentImageFragment != null) {
                    transaction.show(commentImageFragment);
                } else {
                    Log.i("zjz", "image为空");
                    commentImageFragment = new CommentImageFragment();
                    commentImageFragment.setArguments(bundle);
                    transaction.add(R.id.frame_comment, commentImageFragment);
                }
                break;

        }
        transaction.commitAllowingStateLoss();
    }
}
