package com.abcs.haiwaigou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.CartActivity2;
import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.HotActivity3;
import com.abcs.haiwaigou.adapter.HotItemAdapter;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.model.HotItem;
import com.abcs.haiwaigou.utils.InitCarNum;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.yixia.camera.util.Log;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class HotItemFragment extends BaseFragment implements View.OnClickListener,RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.recyclerView)
    EasyRecyclerView rv_dhdetials;
    @InjectView(R.id.btn_global)
    RelativeLayout btn_global;  //默认
    @InjectView(R.id.btn_salse_volume)
    RelativeLayout btn_salse_volume;  //销量
    @InjectView(R.id.btn_price)
    RelativeLayout btn_price;  //价格
    @InjectView(R.id.linear_sort)
    LinearLayout linearSort;
    @InjectView(R.id.img_overlay)
    ImageView imgOverlay;
    @InjectView(R.id.spinner_select)
    Spinner spinnerSelect;
    @InjectView(R.id.relative_spinner)
    RelativeLayout relativeSpinner;
    @InjectView(R.id.car_num)
    TextView carNum;
    @InjectView(R.id.relative_cart)
    RelativeLayout relativeCart;
    @InjectView(R.id.seperate)
    View seperate;
    @InjectView(R.id.img_cart)
    ImageView imgCart;
    @InjectView(R.id.img_volume)
    ImageView img_volume;  //销量
    @InjectView(R.id.img_price)
    ImageView img_price; //价格
    @InjectView(R.id.tv_global)
    TextView tv_global; //默认
    @InjectView(R.id.tv_salse_volume)
    TextView tv_salse_volume; //销量
    @InjectView(R.id.tv_price)
    TextView tv_price; //价格

    private Handler handler = new Handler();
    private View view;
    HotActivity3 activity;
    private String c_id;
    private String class_2,position;
    private String objectName;
    HotItemAdapter adapter;

    private boolean isXiaoLiang=true;
    private boolean isJiaGe=true;
    MyBroadCastReceiver myBroadCastReceiver;

    public static HotItemFragment newInstance(String c_id, String class_2,String position,String objectName) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("c_id", c_id);
        bundle.putSerializable("class_2", class_2);
        bundle.putSerializable("position", position);
        bundle.putSerializable("objectName", objectName);
        HotItemFragment fragment = new HotItemFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        activity = (HotActivity3)getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(
                    R.layout.hwg_fragment_hot3, null);
            ButterKnife.inject(this, view);

            Bundle bundle = getArguments();
            if (bundle != null) {
                c_id = bundle.getString("c_id");
                class_2 = bundle.getString("class_2");
                position = bundle.getString("position");
                objectName = bundle.getString("objectName");
            }
        }

        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null) {
            p.removeView(view);
        }

        ButterKnife.inject(this, view);

        myBroadCastReceiver = new MyBroadCastReceiver(activity, updateUI);
        myBroadCastReceiver.register();
        setOnListener();
        initRecyclerView();
        initInCartNum();

        return view;
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
        if (MyApplication.getInstance().getMykey() != null) {
            new InitCarNum(carNum, activity,"");
        }
    }

    private void setOnListener() {
        btn_global.setOnClickListener(this);
        btn_salse_volume.setOnClickListener(this);
        btn_price.setOnClickListener(this);
        relativeCart.setOnClickListener(this);

    }

    private void initRecyclerView(){

        GridLayoutManager manager = new GridLayoutManager(activity, 2);
//        rv_dhdetials.addItemDecoration(new SpaceItemDecoration(5));
        rv_dhdetials.setLayoutManager(manager);
        rv_dhdetials.setRefreshListener(this);
        adapter = new HotItemAdapter(activity);

        rv_dhdetials.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final  HotItem.DatasBean datas=adapter.getItem(position);
                Intent intent = new Intent(activity, GoodsDetailActivity.class);
                intent.putExtra("sid", datas.goodsId+"");
                intent.putExtra("pic", datas.goodsImage+"");
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

       /* if(Integer.valueOf(position)>0){
             getDatas();
        }*/

        getDatas();

    }

    String patamas=null;

    private boolean isRefresh = false;

    int page=1;
    private String order;
    private String order_key;

    public  void getDatas(){
//        c_id	分类id	number	和二级分类id二选一，传这个参数表示得到3级分类的商品
//        class_2	二级分类id	number	和分类id二选一，传这个参数表示得到二级分类全部的商品
//        order	排序参数	number	可传可不传，默认为商品id，1为商品id，2为销量，3为价格
//        order_key	正序或倒序	number	可传可不传，默认为倒序，1为倒序，2为正序
//        page	分页参数，第几页	number	可传可不传，默认为第一页
//        page_size	分类显示多少数据	number	可传可不传，默认为6个数据

//        http://www.huaqiaobang.com/mobile/index.php?act=goods_class&op=get_goods_by_class&c_id=1107

        patamas="act=goods_class&op=get_goods_by_class&c_id="+c_id+"&class_2="+class_2+"&order="+order+"&order_key="+order_key+"&page="+page+"&page_size=10";
       /* if(activity.mainPager.getCurrentItem()>0){
        }else {
            patamas="act=goods_class&op=get_goods_by_class&c_id=-1&class_2="+class_2+"&order="+order+"&order_key="+order_key+"&page="+page+"&page_size=10";
        }*/

        Log.i("zds","patamas="+patamas);

        ProgressDlgUtil.showProgressDlg("",activity);
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php", patamas, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgUtil.stopProgressDlg();
                        Log.i("zds","hotitem="+msg);

                        HotItem response=new Gson().fromJson(msg,HotItem.class);

                        if (isRefresh) {
                            adapter.clear();
                            isRefresh = false;
                        }

                        if(response!=null){
                            if(response.state==1){

                                if(response.datas!=null){
                                    if(response.datas.size()>0){
                                        adapter.addAll(response.datas);
                                        adapter.notifyDataSetChanged();
                                    }else {
                                        adapter.stopMore();
                                        showToast(activity,"很抱歉，服务器还没有相关的数！");
                                    }
                                }

                                page=response.page;

                            }else {
                                adapter.stopMore();
                            }
                        }
                    }
                });
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        myBroadCastReceiver.unRegister();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
          case R.id.relative_cart:
                Intent intent=null;
                if (MyApplication.getInstance().self == null) {
                    intent = new Intent(activity, WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(activity, CartActivity2.class);
                intent.putExtra("store_id","");
                activity.startActivity(intent);
                break;
            case R.id.btn_global:  //默认
                page=1;
                adapter.clear();
                tv_global.setTextColor(getResources().getColor(R.color.red));
                tv_salse_volume.setTextColor(getResources().getColor(R.color.darkgray));
                tv_price.setTextColor(getResources().getColor(R.color.darkgray));
                img_volume.setImageResource(R.drawable.sort_button);
                img_price.setImageResource(R.drawable.sort_button);
                order_key="";
                order="";
                getDatas();
                break;
            case R.id.btn_salse_volume:// 销量
                adapter.clear();
                tv_global.setTextColor(getResources().getColor(R.color.darkgray));
                tv_salse_volume.setTextColor(getResources().getColor(R.color.red));
                tv_price.setTextColor(getResources().getColor(R.color.darkgray));

                page=1;
                if(isXiaoLiang){
                    order_key="1";
                    order="2";
                    getDatas();
                    isXiaoLiang=false;
                    img_volume.setImageResource(R.drawable.sort_button_down);
                    img_price.setImageResource(R.drawable.sort_button);
                }else {
                    order_key="2";
                    order="2";
                    getDatas();
                    isXiaoLiang=true;
                    img_volume.setImageResource(R.drawable.sort_button_up);
                    img_price.setImageResource(R.drawable.sort_button);
                }

                break;
            case R.id.btn_price:  //价格
                page=1;
                adapter.clear();
                tv_global.setTextColor(getResources().getColor(R.color.darkgray));
                tv_salse_volume.setTextColor(getResources().getColor(R.color.darkgray));
                tv_price.setTextColor(getResources().getColor(R.color.red));

                if(isJiaGe){
                    order_key="1";
                    order="3";
                    getDatas();
                    isJiaGe=false;
                    img_volume.setImageResource(R.drawable.sort_button);
                    img_price.setImageResource(R.drawable.sort_button_down);
                }else {
                    order_key="2";
                    order="3";
                    getDatas();
                    isJiaGe=true;
                    img_volume.setImageResource(R.drawable.sort_button);
                    img_price.setImageResource(R.drawable.sort_button_up);
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        isRefresh = true;
        getDatas();
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               rv_dhdetials.setRefreshing(false);
           }
       }, 300);
    }

    @Override
    public void onLoadMore() {
        if (page == -1) {
            adapter.stopMore();
            return;
        }
        getDatas();
    }
}
