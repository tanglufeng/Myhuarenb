package com.abcs.haiwaigou.local.huohang.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.CheckOrderActivity;
import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.PayWayActivity;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.local.evenbus.Notice;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.huaqiaobang.ytbt.common.utils.ToastUtil;
import com.abcs.sociax.android.R;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HuoHangCartActivity extends BaseFragmentActivity implements View.OnClickListener {
    @InjectView(R.id.layout_null)
    RelativeLayout cartNull;
    private View layoutNull;
    private CartAdapter mAdapter;
    private View mViewLogin;
    private SwipeMenuListView mListView;
    private TextView mTvPrice; // 合计
    private TextView mTvTotal; // 总额
    private TextView mTvCount; // 选中商品数
    public static CheckBox mBtnCheckAll;
    public static CheckBox mBtnCheckAllEdit;
    private RelativeLayout relative_network;
    private RelativeLayout relative_delall,btn_pay;
    private TextView mTvEdit;
    private TextView mTvTitle;
    private TextView t_tishi;
    private View layoutEditBar;
    private View layoutPayBar;
    private ProgressBar mProgressBar;
    private TextView mTvAddUp;
    private ZrcListView listView;
    public Handler handler = new Handler();
    public static ArrayList<Goods> mData = new ArrayList<Goods>();
    public static ArrayList<Goods> mDataSelect = new ArrayList<Goods>();
    public static ArrayList<Goods> cartDatas = new ArrayList<Goods>();
    public static ArrayList<Goods> cartNum = new ArrayList<Goods>();
    private HashMap<String, Boolean> inCartMap = new HashMap<String, Boolean>();// 用于存放选中的项
    private HashMap<String, Boolean> inCartMapSelect = new HashMap<String, Boolean>();// 用于存放选中的项

    private LinearLayout liner_edit;
    private RelativeLayout btn_delete_select;// 删除所选

    private double price; // 总价
    private int num; // 选中的商品数

    private boolean isEdit; // 是否正在编辑
    boolean isCheck = false;
    boolean isFirst = false;
    private TextView t_delete;
    MyBroadCastReceiver myBroadCastReceiver;
    private String goods;
//    private int carts=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_cart);
        ButterKnife.inject(this);
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();

        store_id=getIntent().getStringExtra("store_id");
        isCheck = getIntent().getBooleanExtra("isCheck", false);
        if (isCheck)
            goods = getIntent().getStringExtra("goods");
        initView();
        setOnListener();
        if (!NetworkUtils.isNetAvailable(this)) {
            t_tishi.setVisibility(View.GONE);
            if (relative_network != null)
                relative_network.setVisibility(View.VISIBLE);
        } else {
            initListView();
        }
//        initData();
    }

    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {
//            initDates();
            initListView();
        }


        @Override
        public void updateCollection(Intent intent) {
            if (intent.getSerializableExtra("type").equals(MyUpdateUI.CART)) {
                Log.i("zds", "更新购物车");
                initDates();
                inCartMap.clear();
                notifyCheckedChanged();
            }
        }

        @Override
        public void update(Intent intent) {

        }

    };

    private CompoundButton.OnCheckedChangeListener checkAllListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            mBtnCheckAll.setChecked(isChecked);
            mBtnCheckAllEdit.setChecked(isChecked);
            if (isChecked) {
                checkAll();
//                t_delete.setText("清空");
            } else {
                inCartMap.clear();
//                t_delete.setText("删除");
            }
            notifyCheckedChanged();
            mAdapter.notifyDataSetChanged();
        }

    };


    public void setOnListener() {
        mTvEdit.setOnClickListener(this);
        layoutEditBar.setOnClickListener(this);
        layoutPayBar.setOnClickListener(this);
        cartNull.setOnClickListener(this);
        mBtnCheckAll.setOnCheckedChangeListener(checkAllListener);
        mBtnCheckAllEdit.setOnCheckedChangeListener(checkAllListener);
//        layout.findViewById(R.id.btn_login_cart).setOnClickListener(this);
//        layout.findViewById(R.id.btn_collect).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_pay).setOnClickListener(this);
        findViewById(R.id.btn_more).setOnClickListener(this);
        findViewById(R.id.relative_back).setOnClickListener(this);
        findViewById(R.id.relative_network).setOnClickListener(this);
        findViewById(R.id.relative_delall).setOnClickListener(this);
        findViewById(R.id.btn_delete_select).setOnClickListener(this);

    }

    public void initView() {
        liner_edit = (LinearLayout) findViewById(R.id.liner_edit);
        btn_delete_select = (RelativeLayout) findViewById(R.id.btn_delete_select);
        relative_network = (RelativeLayout) findViewById(R.id.relative_network);
        relative_delall = (RelativeLayout) findViewById(R.id.relative_delall);
        btn_pay = (RelativeLayout) findViewById(R.id.btn_pay);

        mTvTitle = (TextView) findViewById(R.id.tljr_txt_country_title);
        t_tishi = (TextView) findViewById(R.id.t_tishi);

        t_delete = (TextView) findViewById(R.id.btn_delete);
        layoutNull = findViewById(R.id.layout_null);
        mTvEdit = (TextView) findViewById(R.id.tv_edit_cart);
        mTvPrice = (TextView) findViewById(R.id.tv_price);
//        mTvTotal = (TextView) layout.findViewById(R.id.tv_total);
        mTvCount = (TextView) findViewById(R.id.tv_pay);
        mBtnCheckAll = (CheckBox) findViewById(R.id.btn_check_all);
        mBtnCheckAll.setClickable(false);
        mBtnCheckAllEdit = (CheckBox) findViewById(R.id.btn_check_all_deit);
        mBtnCheckAllEdit.setClickable(false);
//        mViewLogin = layout.findViewById(R.id.layout_login_cart);
        layoutEditBar = findViewById(R.id.layout_edit_bar);
        layoutPayBar = findViewById(R.id.layout_pay_bar);

        try {
            Typeface type= Typeface.createFromAsset(getAssets(),"font/ltheijian.TTF");
            if(type!=null){
                mTvPrice.setTypeface(type) ;
                mTvAddUp.setTypeface(type) ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    private void initData() {
//        // 异步从数据库中获取数据
//        new InCartTask().execute();
//    }


    /**
     * 选中商品改变
     */
    public void notifyCheckedChanged() {
        price = 0;
        num = 0;
        for (int i = 0; i < cartDatas.size(); i++) {
            Boolean isChecked = inCartMap.get(cartDatas.get(i).getCart_id());
            if (isChecked != null && isChecked) {
                Goods goods = cartDatas.get(i);
                num++;
                price += goods.getMoney() * goods.getGoods_num();
            }
        }
        mTvPrice.setText(NumberUtils.formatPriceOuYuan(price));

//        mTvTotal.setText("总额：" + NumberUtils.formatPriceOuYuan(price));
        mTvCount.setText("结算(" + num + ")");
        mTvAddUp.setText("小计：" + NumberUtils.formatPriceOuYuan(price));

    }

    /**
     * 通知更新购物车商品数量
     */
    public void notifyInCartNumChanged() {
        // 通知主页刷新购物车商品数
//        Intent intent = new Intent();
//        sendBroadcast(intent);
        //更新购物车数量
        MyUpdateUI.sendUpdateCarNum(HuoHangCartActivity.this);
    }

    public void initListView() {
        mListView = (SwipeMenuListView) findViewById(R.id.listView_cart);
        View foot = getLayoutInflater().inflate(
                R.layout.hwg_foot_cart_list, null);
        mTvAddUp = (TextView) foot.findViewById(R.id.tv_add_up);
//        mListView.addFooterView(foot, null, false);

        initDates();

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(HuoHangCartActivity.this);
                // set item background
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
                deleteItem.setBackground(R.color.tljr_statusbarcolor);
                // set item width
                deleteItem.setWidth(Util.WIDTH / 4);
                // set item title
                deleteItem.setTitle("删除");
                // set item title fontsize
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);

            }
        };
        // set creator  设置侧滑
//        mListView.setMenuCreator(creator);

        // step 2. listener item click event  侧滑点击事件
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                // index是menu的菜单序号
                deleteItem(position);
            }
        });
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Goods goods = mData.get(position);
//                Intent intent = new Intent(CartActivity.this, GoodsDetailActivity.class);
//                intent.putExtra("sid", goods.getSid());
//                CartActivity.this.startActivity(intent);
//            }
//        });
    }

    private String store_id;
    private String paramas;
    public void initDates() {
        if (MyApplication.getInstance().getMykey() == null) {
            Intent intent = new Intent(this, WXEntryActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        if (!isFirst)
            ProgressDlgUtil.showProgressDlg("Loading...", this);
//        Log.i("zds", "cart_uid=" + MyApplication.getInstance().self.getId());
        Log.i("zds", "cart_key=" + MyApplication.getInstance().getMykey());
        if(!TextUtils.isEmpty(store_id)){
            Log.i("zds", "cart_store=" + TLUrl.getInstance().URL_hwg_cartlist+"&key=" + MyApplication.getInstance().getMykey()+"&store_id="+store_id);
            paramas="key=" + MyApplication.getInstance().getMykey()+"&store_id="+store_id;
        }else {
            Log.i("zds", "cart_store=" + TLUrl.getInstance().URL_hwg_cartlist+"&key=" + MyApplication.getInstance().getMykey());
            paramas="key=" + MyApplication.getInstance().getMykey();
        }

        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_cartlist, paramas, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object != null && object.has("code") && object.optInt("code") == 200) {
                                Log.i("zds", "cart:连接成功");
                                Log.i("zds", msg);
                                mData.clear();
                                cartDatas.clear();
                                cartNum.clear();
                                JSONObject jsonObject = object.getJSONObject("datas");
                                JSONArray jsonArray = jsonObject.getJSONArray("cart_list");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    Goods g = new Goods();
                                    if (object1.optString("bl_id").equals("0")) {
//                                    g.setId(object1.optInt("id"));
                                        g.setGoods_num(object1.optInt("goods_num"));
                                        g.setGood_en_name(object1.optString("goods_en_name"));
                                        g.setGoods_serial(object1.optString("goods_serial"));
                                        g.setTitle(object1.optString("goods_name"));
                                        g.setMoney(object1.optDouble("goods_price"));
                                        g.setPicarr(TLUrl.getInstance().URL_hwg_comment_goods + object1.getString("store_id") + "/" + object1.optString("goods_image"));
                                        g.setCart_id(object1.optString("cart_id"));
                                        g.setStore_id(object1.optString("store_id"));
                                        g.setGoods_id(object1.optString("goods_id"));
                                        g.setGoods_storage(object1.optInt("goods_storage"));
                                        g.setGoods_state(object1.optString("goods_state"));
                                        g.setLayoutType(0);
                                        if(object1.optString("goods_state").equals("1")){
                                            cartDatas.add(g);
                                        }
                                        cartNum.add(g);
                                        mData.add(g);
//                                    g.setDismoney(object1.optDouble("dismoney"));
//                                    g.setSid(object1.optInt("sid"));
                                    } else if (!object1.optString("bl_id").equals("0")) {
                                        g.setGoods_num(object1.optInt("goods_num"));
                                        g.setGood_en_name(object1.optString("goods_en_name"));
                                        g.setGoods_serial(object1.optString("goods_serial"));
                                        g.setTitle(object1.optString("goods_name"));
                                        g.setMoney(object1.optDouble("goods_price"));
                                        g.setCart_id(object1.optString("cart_id"));
                                        g.setStore_id(object1.optString("store_id"));
                                        g.setGoods_id(object1.optString("goods_id"));
                                        g.setGoods_storage(object1.optInt("goods_storage"));
                                        g.setLayoutType(1);
                                        mData.add(g);
                                        cartDatas.add(g);
                                        cartNum.add(g);
                                        JSONObject blObject = jsonObject.getJSONObject("suit");
                                        JSONObject goodsObject = blObject.getJSONObject(g.getCart_id());
                                        JSONArray blArray = goodsObject.getJSONArray("info");
                                        for (int j = 0; j < blArray.length(); j++) {
                                            JSONObject blgoodObject = blArray.getJSONObject(j);
                                            Goods goods = new Goods();
                                            goods.setLayoutType(17);
                                            goods.setTitle(blgoodObject.optString("goods_name"));
                                            goods.setMoney(blgoodObject.optDouble("bl_goods_price"));
                                            goods.setPicarr(TLUrl.getInstance().URL_hwg_comment_goods + blgoodObject.getString("store_id") + "/" + blgoodObject.optString("goods_image"));
                                            goods.setStore_id(blgoodObject.optString("store_id"));
                                            goods.setGoods_id(blgoodObject.optString("goods_id"));
                                            mData.add(goods);
                                        }
                                    }
                                }

                                Log.i("zds", "cartNum=" + mData.size());
                                Log.i("zds", "cartNum2=" + cartDatas.size());
                                mBtnCheckAll.setClickable(true);
                                mBtnCheckAllEdit.setClickable(true);
                                mAdapter = new CartAdapter(mData, cartDatas);
                                mListView.setAdapter(mAdapter);
                                mTvTitle.setText("购物车(" + cartNum.size() + ")");

                                //  默认全选
                                initQuanXuan(cartDatas);
                            } else {
                                Log.i("zds", "cart解析失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zds", e.toString());
                            Log.i("zds", msg);
                            e.printStackTrace();
                        } finally {
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
    }

    private void initQuanXuan(ArrayList<Goods> datas){
        if(datas!=null&&datas.size()>0){
            for(int i=0;i<datas.size();i++){

                Goods goods=datas.get(i);
                inCartMap.put(goods.getCart_id(), true);
                // 如果所有项都被选中，则点亮全选按钮
                mBtnCheckAll.setChecked(true);
                mBtnCheckAllEdit.setChecked(true);
//                t_delete.setText("清空");
            }
        }
    }

    /**
     * 获取数字
     *
     * @param tvNum
     * @return
     */
    private int getNum(TextView tvNum) {
        String num = tvNum.getText().toString().trim();
        return Integer.valueOf(num);

    }


    class CartAdapter extends BaseAdapter {
        private final int TYPE_ITEM_NOMAL = 0;
        private final int TYPE_ITEM_SPE = 1;
        private final int TYPE_ITEM_SPE_LIST = 2;
        ArrayList<Goods> mGoods = new ArrayList<Goods>();
        ArrayList<Goods> cartDatas = new ArrayList<Goods>();

        public CartAdapter(ArrayList<Goods> mGoods, ArrayList<Goods> cartDatas) {
            this.mGoods = mGoods;
            this.cartDatas = cartDatas;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
//            int currentType=getItemViewType(position);
            View nomalView = null;
            View speView = null;
            View speListView = null;
            final Goods goods = getItem(position);
            if (getItemViewType(position) == TYPE_ITEM_NOMAL) {
                ViewHolder holder = null;
                if (convertView == null) {
                    // 复用乱序问题
                    nomalView = getLayoutInflater().inflate(
                            R.layout.hwg_item_activity_cart_list, null);
                    holder = new ViewHolder();
                    holder.btnCheck = (CheckBox) nomalView.findViewById(R.id.btn_check);
                    holder.btnReduce = (Button) nomalView.findViewById(R.id.btn_cart_reduce);
                    holder.btnAdd = (Button) nomalView.findViewById(R.id.btn_cart_add);
                    holder.btnNumEdit = (EditText) nomalView.findViewById(R.id.btn_cart_num_edit);
                    holder.imgGoods = (ImageView) nomalView.findViewById(R.id.img_goods);
                    holder.tv_store = (TextView) nomalView.findViewById(R.id.tv_store);
                    holder.tv_en_title = (TextView) nomalView.findViewById(R.id.tv_en_title);
                    holder.img_delete = (ImageView) nomalView.findViewById(R.id.img_delete);
                    holder.tvGoodsName = (TextView) nomalView.findViewById(R.id.tv_goods_name);
                    holder.tvGoodsPrice = (TextView) nomalView.findViewById(R.id.tv_goods_price);
                    holder.tState= (TextView) nomalView.findViewById(R.id.t_state);
                    nomalView.setTag(holder);
                    convertView = nomalView;
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if(goods.getGoods_state().equals("0")){
                    holder.tState.setVisibility(View.VISIBLE);
                    holder.btnAdd.setVisibility(View.INVISIBLE);
                    holder.btnNumEdit.setVisibility(View.INVISIBLE);
                    holder.btnReduce.setVisibility(View.INVISIBLE);
                    holder.btnCheck.setClickable(false);
                }else {
                    holder.tState.setVisibility(View.GONE);
                    holder.btnAdd.setVisibility(View.VISIBLE);
                    holder.btnNumEdit.setVisibility(View.VISIBLE);
                    holder.btnReduce.setVisibility(View.VISIBLE);
                    holder.btnCheck.setClickable(true);
                }
                holder.img_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteItem(position);
                    }
                });
                holder.tvGoodsName.setText(goods.getTitle());
                holder.tv_en_title.setText(goods.getGood_en_name());
                holder.tv_store.setText("货号："+goods.getGoods_serial());
                holder.tvGoodsPrice.setText(NumberUtils.formatPriceOuYuan(goods.getMoney()));
                holder.btnNumEdit.setText("" + goods.getGoods_num());
                //加载图片
                LoadPicture loadPicture = new LoadPicture();
                loadPicture.initPicture(holder.imgGoods, goods.getPicarr());
                holder.imgGoods.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(goods.getStore_id())&&goods.getStore_id().equals("1")){
                            Intent intent = new Intent(HuoHangCartActivity.this, GoodsDetailActivity.class);
                            intent.putExtra("sid", goods.getGoods_id());
                            HuoHangCartActivity.this.startActivity(intent);
                        }
                    }
                });
                holder.tvGoodsName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(goods.getStore_id())&&goods.getStore_id().equals("1")){

                            Intent intent = new Intent(HuoHangCartActivity.this, GoodsDetailActivity.class);
                            intent.putExtra("sid", goods.getGoods_id());
                            HuoHangCartActivity.this.startActivity(intent);
                        }
                    }
                });
//            UILUtils.displayImage(getActivity(), inCart.getGoodsIcon(),
//                    holder.imgGoods);
                if (goods.getGoods_num() > 1) {
                    holder.btnReduce.setEnabled(true);
                } else {
                    holder.btnReduce.setEnabled(false);
                }

                // 避免由于复用触发onChecked()事件
                holder.btnCheck.setOnCheckedChangeListener(null);
                Boolean isChecked = inCartMap.get(goods.getCart_id());
                if (isChecked != null && isChecked) {
                    holder.btnCheck.setChecked(true);
                } else {
                    holder.btnCheck.setChecked(false);
                }
                final ViewHolder holder2 = holder;
                holder.btnReduce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (goods.getGoods_num() <= 1) {
                            ToastUtil.showMessage("商品数量不能为0");
                            return;
                        }

                        holder2.btnReduce.setEnabled(false);
                        int num = goods.getGoods_num() - 1;
                    ProgressDlgUtil.showProgressDlg("Loading...", HuoHangCartActivity.this);
                        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_cartnum_edit, "key=" + MyApplication.getInstance().getMykey() + "&cart_id=" + goods.getCart_id() + "&quantity=" + num, new HttpRevMsg() {
                            @Override
                            public void revMsg(final String msg) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject object = new JSONObject(msg);
                                            if (object.getInt("code") == 200) {
                                                Log.i("zds", "cartadd:连接成功");
                                                holder2.btnAdd.setEnabled(true);
                                                int num = getNum(holder2.btnNumEdit);
                                                num--;
                                                goods.setGoods_num(num);
//                                          inCart.save();
//                                                notifyInCartNumChanged();
                                                // 如果被选中，更新价格
                                                if (holder2.btnCheck.isChecked()) {
                                                    notifyCheckedChanged();
                                                }
                                                Log.e("onClick", "holder2.btnCheck.isChecked() = "
                                                        + holder2.btnCheck.isChecked());
                                                holder2.btnNumEdit.setText("" + num);
                                                if (num == 1) {
                                                    holder2.btnReduce.setEnabled(false);
                                                } else {
                                                    holder2.btnReduce.setEnabled(true);
                                                }
                                                ProgressDlgUtil.stopProgressDlg();
                                            } else {
                                                Log.i("zds", "cartadd解析失败");
                                            }
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            Log.i("zds", e.toString());
                                            Log.i("zds", msg);
                                            holder2.btnReduce.setEnabled(true);
                                            e.printStackTrace();
                                        } finally {
                                            ProgressDlgUtil.stopProgressDlg();
                                        }
                                    }
                                });

                            }
                        });

                    }
                });
                final int Storage = goods.getGoods_storage();
                if (goods.getGoods_num() >= Storage) {
                    holder.btnAdd.setEnabled(false);
                } else {
                    holder.btnAdd.setEnabled(true);
                }
                holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (goods.getGoods_num() >= Storage) {
                            showToast("商品库存不足！");
                            return;
                        }
                        holder2.btnAdd.setEnabled(false);
                        int num = goods.getGoods_num() + 1;
//                    ProgressDlgUtil.showProgressDlg("Loading...", CartActivity2.this);
                        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_cartnum_edit, "key=" + MyApplication.getInstance().getMykey() + "&cart_id=" + goods.getCart_id() + "&quantity=" + num, new HttpRevMsg() {
                            @Override
                            public void revMsg(final String msg) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject object = new JSONObject(msg);
                                            if (object.getInt("code") == 200) {
                                                Log.i("zds", "cartadd:连接成功");
                                                holder2.btnReduce.setEnabled(true);
                                                int num = getNum(holder2.btnNumEdit);
                                                num++;
                                                goods.setGoods_num(num);
//                                                notifyInCartNumChanged();
                                                // 如果被选中，更新价格
                                                if (holder2.btnCheck.isChecked()) {
                                                    notifyCheckedChanged();
                                                }
                                                if (num >= Storage) {
                                                    holder2.btnAdd.setEnabled(false);
                                                } else {
                                                    holder2.btnAdd.setEnabled(true);
                                                }
                                                Log.e("onClick", "holder2.btnCheck.isChecked() = "
                                                        + holder2.btnCheck.isChecked());
                                                holder2.btnNumEdit.setText("" + num);
                                                ProgressDlgUtil.stopProgressDlg();
                                            } else {
                                                Log.i("zds", "cartadd解析失败");
                                            }
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            Log.i("zds", e.toString());
                                            Log.i("zds", msg);
                                            holder2.btnAdd.setEnabled(true);
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                        });

                    }
                });
                holder.btnCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            inCartMap.put(goods.getCart_id(), isChecked);
                            // 如果所有项都被选中，则点亮全选按钮
                            if (inCartMap.size() == cartDatas.size()) {
                                mBtnCheckAll.setChecked(true);
                                mBtnCheckAllEdit.setChecked(true);
                                t_delete.setText("清空");
                            } else {
                                t_delete.setText("删除");
                            }
                        } else {
                            // 如果之前是全选状态，则取消全选状态
                            if (inCartMap.size() == cartDatas.size()) {
                                mBtnCheckAll
                                        .setOnCheckedChangeListener(null);
                                mBtnCheckAllEdit
                                        .setOnCheckedChangeListener(null);
                                mBtnCheckAll.setChecked(false);
                                mBtnCheckAllEdit.setChecked(false);
                                mBtnCheckAll
                                        .setOnCheckedChangeListener(checkAllListener);
                                mBtnCheckAllEdit
                                        .setOnCheckedChangeListener(checkAllListener);
                                t_delete.setText("删除");
                            }
                            inCartMap.remove(goods.getCart_id());
                        }
                        notifyCheckedChanged();
                    }
                });
            } else if (getItemViewType(position) == TYPE_ITEM_SPE) {
                ViewHolderSpe holderSpe = null;
                if (convertView == null) {
                    // 复用乱序问题
                    speView = getLayoutInflater().inflate(
                            R.layout.hwg_item_activity_cart_spe, null);
                    holderSpe = new ViewHolderSpe();
                    holderSpe.btnCheck = (CheckBox) speView.findViewById(R.id.btn_check);
                    holderSpe.btnReduce = (Button) speView.findViewById(R.id.btn_cart_reduce);
                    holderSpe.btnAdd = (Button) speView.findViewById(R.id.btn_cart_add);
                    holderSpe.btnNumEdit = (EditText) speView.findViewById(R.id.btn_cart_num_edit);
                    holderSpe.img_delete = (ImageView) speView.findViewById(R.id.img_delete);
                    holderSpe.tvGoodsName = (TextView) speView.findViewById(R.id.tv_goods_name);
                    holderSpe.tvGoodsPrice = (TextView) speView.findViewById(R.id.tv_goods_price);
                    speView.setTag(holderSpe);
                    convertView = speView;
                } else {
//                    inflate = convertView;
                    holderSpe = (ViewHolderSpe) convertView.getTag();
                }
                holderSpe.img_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteItem(position);
                    }
                });
                holderSpe.tvGoodsName.setText(goods.getTitle());
                holderSpe.tvGoodsPrice.setText(NumberUtils.formatPriceOuYuan(goods.getMoney()));
                holderSpe.btnNumEdit.setText("" + goods.getGoods_num());
//            UILUtils.displayImage(getActivity(), inCart.getGoodsIcon(),
//                    holder.imgGoods);
                if (goods.getGoods_num() > 1) {
                    holderSpe.btnReduce.setEnabled(true);
                } else {
                    holderSpe.btnReduce.setEnabled(false);
                }

                // 避免由于复用触发onChecked()事件
                holderSpe.btnCheck.setOnCheckedChangeListener(null);
                Boolean isChecked = inCartMap.get(goods.getCart_id());
                if (isChecked != null && isChecked) {
                    holderSpe.btnCheck.setChecked(true);
                } else {
                    holderSpe.btnCheck.setChecked(false);
                }
                final ViewHolderSpe holder2 = holderSpe;
                holderSpe.btnReduce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (goods.getGoods_num() <= 1) {
                            showToast("商品数量不能为0");
                            return;
                        }

                        holder2.btnReduce.setEnabled(false);
                        int num = goods.getGoods_num() - 1;
//                    ProgressDlgUtil.showProgressDlg("Loading...", CartActivity2.this);
                        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_cartnum_edit, "key=" + MyApplication.getInstance().getMykey() + "&cart_id=" + goods.getCart_id() + "&quantity=" + num, new HttpRevMsg() {
                            @Override
                            public void revMsg(final String msg) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject object = new JSONObject(msg);
                                            if (object.getInt("code") == 200) {
                                                Log.i("zds", "cartadd:连接成功");
                                                holder2.btnAdd.setEnabled(true);
                                                int num = getNum(holder2.btnNumEdit);
                                                num--;
                                                goods.setGoods_num(num);
//                                          inCart.save();
                                                notifyInCartNumChanged();
                                                // 如果被选中，更新价格
                                                if (holder2.btnCheck.isChecked()) {
                                                    notifyCheckedChanged();
                                                }
                                                Log.e("onClick", "holder2.btnCheck.isChecked() = "
                                                        + holder2.btnCheck.isChecked());
                                                holder2.btnNumEdit.setText("" + num);
                                                if (num == 1) {
                                                    holder2.btnReduce.setEnabled(false);
                                                } else {
                                                    holder2.btnReduce.setEnabled(true);
                                                }
                                                ProgressDlgUtil.stopProgressDlg();
                                            } else {
                                                Log.i("zds", "cartadd解析失败");
                                            }
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            Log.i("zds", e.toString());
                                            Log.i("zds", msg);
                                            holder2.btnReduce.setEnabled(true);
                                            e.printStackTrace();
                                        } finally {

                                            ProgressDlgUtil.stopProgressDlg();
                                        }
                                    }
                                });

                            }
                        });

                    }
                });
                final int Storage = goods.getGoods_storage();
                if (goods.getGoods_num() >= Storage) {
                    holderSpe.btnAdd.setEnabled(false);
                } else {
                    holderSpe.btnAdd.setEnabled(true);
                }
                holderSpe.btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (goods.getGoods_num() >= Storage) {
                            showToast("商品库存不足！");
                            return;
                        }
                        holder2.btnAdd.setEnabled(false);
                        int num = goods.getGoods_num() + 1;
                    ProgressDlgUtil.showProgressDlg("Loading...", HuoHangCartActivity.this);
                        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_cartnum_edit, "key=" + MyApplication.getInstance().getMykey() + "&cart_id=" + goods.getCart_id() + "&quantity=" + num, new HttpRevMsg() {
                            @Override
                            public void revMsg(final String msg) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject object = new JSONObject(msg);
                                            if (object.getInt("code") == 200) {
                                                Log.i("zds", "cartadd:连接成功");
                                                holder2.btnReduce.setEnabled(true);
                                                int num = getNum(holder2.btnNumEdit);
                                                num++;
                                                goods.setGoods_num(num);
                                                notifyInCartNumChanged();
                                                // 如果被选中，更新价格
                                                if (holder2.btnCheck.isChecked()) {
                                                    notifyCheckedChanged();
                                                }
                                                if (num >= Storage) {
                                                    holder2.btnAdd.setEnabled(false);
                                                } else {
                                                    holder2.btnAdd.setEnabled(true);
                                                }
                                                Log.e("onClick", "holder2.btnCheck.isChecked() = "
                                                        + holder2.btnCheck.isChecked());
                                                holder2.btnNumEdit.setText("" + num);

                                            } else {
                                                Log.i("zds", "cartadd解析失败");
                                            }
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            Log.i("zds", e.toString());
                                            Log.i("zds", msg);
                                            holder2.btnAdd.setEnabled(true);
                                            e.printStackTrace();
                                        }
                                        ProgressDlgUtil.stopProgressDlg();
                                    }
                                });

                            }
                        });

                    }
                });
                holderSpe.btnCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            inCartMap.put(goods.getCart_id(), isChecked);
                            // 如果所有项都被选中，则点亮全选按钮
                            if (inCartMap.size() == cartDatas.size()) {
                                mBtnCheckAll.setChecked(true);
                                mBtnCheckAllEdit.setChecked(true);
                                t_delete.setText("清空");
                            } else {
                                t_delete.setText("删除");
                            }
                        } else {
                            // 如果之前是全选状态，则取消全选状态
                            if (inCartMap.size() == cartDatas.size()) {
                                mBtnCheckAll
                                        .setOnCheckedChangeListener(null);
                                mBtnCheckAllEdit
                                        .setOnCheckedChangeListener(null);
                                mBtnCheckAll.setChecked(false);
                                mBtnCheckAllEdit.setChecked(false);
                                mBtnCheckAll
                                        .setOnCheckedChangeListener(checkAllListener);
                                mBtnCheckAllEdit
                                        .setOnCheckedChangeListener(checkAllListener);
                                t_delete.setText("删除");
                            }
                            inCartMap.remove(goods.getCart_id());
                        }
                        notifyCheckedChanged();
                    }
                });
            } else if (getItemViewType(position) == TYPE_ITEM_SPE_LIST) {
                ViewHolderSpeList holderSpeList = null;
                if (convertView == null) {
                    // 复用乱序问题
                    speListView = getLayoutInflater().inflate(
                            R.layout.hwg_item_activity_cart_spe_list, null);
                    holderSpeList = new ViewHolderSpeList();
                    holderSpeList.imgGoods = (ImageView) speListView.findViewById(R.id.img_goods);
                    holderSpeList.tvGoodsName = (TextView) speListView.findViewById(R.id.tv_goods_name);
                    holderSpeList.tvGoodsPrice = (TextView) speListView.findViewById(R.id.tv_goods_price);
                    speListView.setTag(holderSpeList);
                    convertView = speListView;
                } else {
                    holderSpeList = (ViewHolderSpeList) convertView.getTag();
                }
                holderSpeList.tvGoodsName.setText(goods.getTitle());
                holderSpeList.tvGoodsPrice.setText(NumberUtils.formatPriceOuYuan(goods.getMoney()));
                //加载图片
                LoadPicture loadPicture = new LoadPicture();
                loadPicture.initPicture(holderSpeList.imgGoods, goods.getPicarr());
                holderSpeList.imgGoods.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(goods.getStore_id())&&goods.getStore_id().equals("1")){

                            Intent intent = new Intent(HuoHangCartActivity.this, GoodsDetailActivity.class);
                            intent.putExtra("sid", goods.getGoods_id());
                            HuoHangCartActivity.this.startActivity(intent);
                        }
                    }
                });
            }

            return convertView;
        }

        @Override
        public int getCount() {

            // 若mData.size() == 0，显示layoutNull
            if (mGoods.size() == 0) {
                mListView.setVisibility(View.GONE);
                mTvEdit.setVisibility(View.GONE);
                layoutEditBar.setVisibility(View.GONE);
                layoutPayBar.setVisibility(View.GONE);
                layoutNull.setVisibility(View.VISIBLE);
                relative_delall.setVisibility(View.GONE);
                t_tishi.setVisibility(View.GONE);
                isEdit = false;
            } else {
                layoutPayBar.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.VISIBLE);
                mTvEdit.setVisibility(View.VISIBLE);
                layoutNull.setVisibility(View.GONE);
//                relative_delall.setVisibility(View.VISIBLE);
                t_tishi.setVisibility(View.VISIBLE);
//                if (isEdit) {
//                    layoutEditBar.setVisibility(View.VISIBLE);
//                } else {
//                    layoutPayBar.setVisibility(View.VISIBLE);
//                }
            }
            return mGoods.size();
        }

        @Override
        public Goods getItem(int position) {
            if (mGoods != null && mGoods.size() != 0) {
                if (position >= mGoods.size()) {
                    return mGoods.get(0);
                }
                return mGoods.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (getItem(position).getLayoutType() == 1) {
                return TYPE_ITEM_SPE;
            } else if (getItem(position).getLayoutType() == 17) {
                return TYPE_ITEM_SPE_LIST;
            } else {
                return TYPE_ITEM_NOMAL;
            }
//            return super.getItemViewType(position);
        }

        //
        @Override
        public int getViewTypeCount() {
            return 3;
        }
    }

    class ViewHolder {
        CheckBox btnCheck;
        Button btnAdd;
        Button btnReduce;
        EditText btnNumEdit;
        ImageView imgGoods;
        ImageView img_delete;
        TextView tvGoodsName;
        TextView tvGoodsPrice;
        TextView tState,tv_en_title,tv_store;

    }

    class ViewHolderSpe {
        CheckBox btnCheck;
        Button btnAdd;
        Button btnReduce;
        EditText btnNumEdit;
        ImageView img_delete;
        TextView tvGoodsName;
        TextView tvGoodsPrice;
    }

    class ViewHolderSpeList {
        ImageView imgGoods;
        TextView tvGoodsName;
        TextView tvGoodsPrice;
    }

    class InCartTask extends AsyncTask<Void, Void, List<Goods>> {

        @Override
        protected List<Goods> doInBackground(Void... params) {
//            return DBUtils.getInCart();
            return null;
        }

        @Override
        protected void onPostExecute(List<Goods> result) {
            super.onPostExecute(result);
            mData.clear();
            mData.addAll(result);
            if (mBtnCheckAll.isChecked()) {
                checkAll();
            }
            mAdapter.notifyDataSetChanged();
            notifyCheckedChanged();
            if (mData.size() == 0) {
                mListView.setVisibility(View.GONE);
            } else {
                mListView.setVisibility(View.VISIBLE);
            }
            mProgressBar.setVisibility(View.GONE);
        }

    }
 private int Good_num=0;
 private double price_total=0;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_login_cart: // 登录
//                gotoLogin();
//                break;
//            case R.id.btn_collect: // 移入关注
//                add2Collect();
//                break;
            case R.id.relative_back:
                for (int i = 0; i < cartDatas.size(); i++) {
                    Good_num += cartDatas.get(i).getGoods_num();
                    price_total += cartDatas.get(i).getMoney() * cartDatas.get(i).getGoods_num();
                }

                Log.i("zds", "onClick: Good_num"+Good_num);
                Log.i("zds", "onClick: price_total"+price_total);

                EventBus.getDefault().post(new Notice(Good_num,price_total));
                finish();
                break;
            case R.id.btn_delete: // 删除
                deleteInCart();
                break;
            case R.id.tv_edit_cart: // 编辑
                editInCart();
                break;
            case R.id.btn_pay: // 结算
//                pay();
                checkOrder();
                break;
//            case R.id.btn_more: // 去秒杀
////                MainActivity activity = (MainActivity) getActivity();
////                activity.activeCategory();
//                break;
            case R.id.layout_null: // 去逛逛
//                MainActivity activity = (MainActivity) getActivity();
//                activity.activeCategory();


                break;
            case R.id.relative_network:
                Intent intent=new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
//                intent = new Intent("/");
//                ComponentName cm = new ComponentName("com.android.settings",
//                        "com.android.settings.Settings");
//                intent.setComponent(cm);
//                intent.setAction("android.intent.action.VIEW");
//                startActivity(intent);
                break;

            case R.id.relative_delall:// 清空所有
                if(MyApplication.getInstance().getMykey()==null){
                    startActivity(new Intent(this,WXEntryActivity.class));
                }else {
                    deleteAll();
                }
                break;
            case R.id.btn_delete_select:  // 删除所选
                if(MyApplication.getInstance().getMykey()==null){
                    startActivity(new Intent(this,WXEntryActivity.class));
                }else {
                    deleteSelect();
                }
                break;
            default:
                break;
        }
    }

    private void deleteSelect() {

        mDataSelect.clear();
        inCartMapSelect.clear();
        if (num == 0) {
            showToast("您还没有选择商品！");
            return;
        }
        Log.i("zds", "choose=" + inCartMap.size());
        CheckOrderActivity.stores.clear();
        String cart_id = null;
        StringBuffer srtbuffer = new StringBuffer();
        for (int i = 0; i < cartDatas.size(); i++) {
            Boolean isChecked = inCartMap.get(cartDatas.get(i).getCart_id());
            if (isChecked != null && isChecked) {
                Goods goods = cartDatas.get(i);
                Log.i("zds", "cart_id=" + goods.getCart_id());
                StringBuffer buffer = new StringBuffer();
                buffer.append(goods.getCart_id());
                if (i != cartDatas.size() - 1) {
                    buffer.append(",");
                }

                srtbuffer.append(buffer);

                mDataSelect.add(goods);
                inCartMapSelect.put(goods.getCart_id(), isChecked);
            }
        }

        cart_id = srtbuffer.toString();


        Log.i("zds", "cart_id=" + cart_id);

        ProgressDlgUtil.showProgressDlg("Loading...", HuoHangCartActivity.this);
//        key=939f6c2c1ad7199187be733cc714955a&cart_id=11014,10902
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_cart_del_select ,"&key=" + MyApplication.getInstance().getMykey()+ "&cart_id="+cart_id, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i("zds", "cartdelselect_msg=" + msg);

                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("state") == 1) {

                                for(int i=0;i<inCartMapSelect.size();i++){
                                    inCartMap.remove(inCartMapSelect.get(i));
                                }

                                mData.removeAll(mDataSelect);

                                isFirst = true;

                                mTvPrice.setText(NumberUtils.formatPriceOuYuan(0.00));
                                mTvCount.setText("结算");
                                mTvAddUp.setText("小计：" + NumberUtils.formatPriceOuYuan(0.00));

                                notifyInCartNumChanged();
                                mAdapter.notifyDataSetChanged();
                                showToast(object.optString("msg"));
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zds", e.toString());
                            Log.i("zds", msg);
                            e.printStackTrace();
                        } finally {
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
    }

    private void deleteAll() {
        new PromptDialog(HuoHangCartActivity.this, "确定清空购物车中的商品么？", new Complete() {
            @Override
            public void complete() {
                ProgressDlgUtil.showProgressDlg("Loading...", HuoHangCartActivity.this);
                HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_cart_del_all + "&key=" + MyApplication.getInstance().getMykey(), null, new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject(msg);
                                    if (object.getInt("code") == 200) {
                                        Log.i("zds", "cartdelall:连接成功");
                                        Log.i("zds", "cartdelall_msg=" + msg);
                                        inCartMap.clear();
                                        mData.clear();
                                        isFirst = true;
                                        notifyCheckedChanged();
                                        notifyInCartNumChanged();
                                        mAdapter.notifyDataSetChanged();
                                        showToast("购物车清空成功！");
                                    }
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    Log.i("zds", e.toString());
                                    Log.i("zds", msg);
                                    e.printStackTrace();
                                } finally {
                                    ProgressDlgUtil.stopProgressDlg();
                                }
                            }
                        });

                    }
                });
            }
        }).show();
    }

    private void checkOrder() {
        if (num == 0) {
//            ToastUtils.showToast(getActivity(), "您还没有选择商品哦！");
            showToast("您还没有选择商品！");
            return;
        }
        Log.i("zds", "choose=" + inCartMap.size());
        CheckOrderActivity.stores.clear();
        HuoHangCheckOrderActivity.stores.clear();
        String cart_id = null;
        String storeTemp;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer srtbuffer = new StringBuffer();
        ArrayList<String> strings = new ArrayList<String>();
        for (int i = 0; i < cartDatas.size(); i++) {
            Boolean isChecked = inCartMap.get(cartDatas.get(i).getCart_id());
            if (isChecked != null && isChecked) {
                Goods goods = cartDatas.get(i);
                Log.i("zds", "cart_id=" + goods.getCart_id());
                Log.i("zds", "num=" + goods.getGoods_num());
                storeTemp = goods.getStore_id();
                if (!strings.contains(storeTemp)) {
                    CheckOrderActivity.stores.add(storeTemp);
                    HuoHangCheckOrderActivity.stores.add(storeTemp);
                }


                StringBuffer buffer = new StringBuffer();

                buffer.append("&cart_id[" + i + "]=");
                buffer.append(goods.getCart_id() + "|" + goods.getGoods_num());
                srtbuffer.append(buffer);

                //zds
                strings.add(goods.getStore_id());
                stringBuffer.append(goods.getCart_id() + "|" + goods.getGoods_num());
                if (i != cartDatas.size() - 1) {
                    stringBuffer.append(",");
                }
            }
        }
        Log.i("zds", "store_ids=" + CheckOrderActivity.stores.size());
        Log.i("zds", "store_ids=" + HuoHangCheckOrderActivity.stores.size());
        Log.i("zds", "srt_ids=" + srtbuffer.toString());


//        cart_id=stringBuffer.toString();
        cart_id = srtbuffer.toString();


        Log.i("zds", "cart_id=" + cart_id);

        initCheckDatas(cart_id);

    }

    /*判断商品的库存是否为零*/


    private void initCheckDatas(final String cart_id) {

        Log.d("zds", "initCheckDatas: " + TLUrl.getInstance().URL_hwg_buy_step1 + "&key=" + MyApplication.getInstance().getMykey() + cart_id + "&ifcart=1" + "&userId=" + MyApplication.getInstance().self.getId());

        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_buy_step1, "key=" + MyApplication.getInstance().getMykey() + cart_id + "&ifcart=1" + "&userId=" + MyApplication.getInstance().self.getId(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg_buy_step1=" + msg);
                                JSONObject object1 = object.getJSONObject("datas");
                                if (object1.optBoolean("state")) {
                                    JSONObject data = object1.getJSONObject("data");
                                    JSONObject storeObject = data.getJSONObject("store_cart_list");
                                    if(storeObject.has("11")){
                                        JSONArray jsonArray = storeObject.optJSONArray("11");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject goods = jsonArray.getJSONObject(i);

                                            if (!goods.optBoolean("state")) {
                                                Toast.makeText(HuoHangCartActivity.this, "所选商品中[" + goods.optString("goods_name").substring(0, 12) + "]已经下架，无法购买，请重新下单！", Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                            if (!goods.optBoolean("storage_state")) {
                                                Toast.makeText(HuoHangCartActivity.this, "所选商品中[" + goods.optString("goods_name").substring(0, 12) + "]库存不足，无法购买，请重新下单！", Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                        }

                                        MyUpdateUI.sendUpdateCarNum(HuoHangCartActivity.this);
                                        Intent intent=null;
                                        if(!TextUtils.isEmpty(store_id)&&store_id.equals("11")){ // 本地
                                            intent = new Intent(HuoHangCartActivity.this, HuoHangCheckOrderActivity.class);
                                            intent.putExtra("cart_id",cart_id);
                                        }else {  // 海外购
                                            intent = new Intent(HuoHangCartActivity.this, CheckOrderActivity.class);
                                            intent.putExtra("cart_id",cart_id);
                                        }

                                        startActivity(intent);

                                    }
                                }
                            } else {
                                Log.i("zjz", "goodsDetail:解析失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /**
     * 结算
     */
    private void pay() {
        if (num == 0) {
//            ToastUtils.showToast(getActivity(), "您还没有选择商品哦！");
            showToast("您还没有选择商品！");
        } else {
//            ToastUtils.showToast(getActivity(), "恭喜，付款成功！");
            com.alibaba.fastjson.JSONArray json = new com.alibaba.fastjson.JSONArray();
            for (int i = 0; i < mData.size(); i++) {
                Boolean isChecked = inCartMap.get(mData.get(i).getId());
                if (isChecked != null && isChecked) {
                    com.alibaba.fastjson.JSONObject shopmsg = new com.alibaba.fastjson.JSONObject();
                    shopmsg.put("sid", mData.get(i).getSid());
                    json.add(shopmsg);
                }
            }
            String str = json.toJSONString();
            Log.i("zds", "str=" + str);

            ProgressDlgUtil.showProgressDlg("Loading...", HuoHangCartActivity.this);
            HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_pay_goods, "uid=" + MyApplication.getInstance().self.getId() + "&ajson=" + str, new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("status") == 1) {
                                    Log.i("zds", "cart_to_pay:成功");

                                    String oid = object.getString("msg");
                                    Intent intent = new Intent(HuoHangCartActivity.this, PayWayActivity.class);
                                    Log.i("zds", "total_money=" + price);
                                    Log.i("zds", "oid=" + oid);
                                    intent.putExtra("total_money", price);
                                    intent.putExtra("oid", oid);
                                    startActivity(intent);
                                } else {
                                    Log.i("zds", "cart_to_pay:失败");
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Log.i("zds", e.toString());
                                Log.i("zds", msg);
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });

        }
    }

    /**
     * 全选，将数据加入inCartMap
     */
    private void checkAll() {
        for (int i = 0; i < cartDatas.size(); i++) {
            inCartMap.put(cartDatas.get(i).getCart_id(), true);
        }
    }

    /**
     * 删除列表项
     */
    private void deleteItem(final int position) {
        new PromptDialog(HuoHangCartActivity.this, "确定从购物车中移除该商品么？", new Complete() {
            @Override
            public void complete() {
                final Goods goods = mData.get(position);
                ProgressDlgUtil.showProgressDlg("Loading...", HuoHangCartActivity.this);
                Log.i("zds", "cart_id=" + goods.getCart_id());
                HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_cart_del, "key=" + MyApplication.getInstance().getMykey() + "&cart_id=" + goods.getCart_id(), new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject(msg);
                                    if (object.getInt("code") == 200) {
                                        Log.i("zds", "cartdel:连接成功");
                                        Log.i("zds", msg);
                                        inCartMap.remove(goods.getCart_id());
//                                        inCartMap.remove(position);
                                        mData.remove(position);
                                        isFirst=true;
                                        notifyCheckedChanged();
                                        notifyInCartNumChanged();
//                                        initListView();
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                        Log.i("zds", "cartdel解析失败");
                                    }
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    Log.i("zds", e.toString());
                                    Log.i("zds", msg);
                                    e.printStackTrace();
                                } finally {
                                    ProgressDlgUtil.stopProgressDlg();
                                }
                            }
                        });

                    }
                });
            }
        }).show();
    }

    /**
     * 编辑
     */
    private void editInCart() {
        isEdit = !isEdit;
        if (isEdit) {
            mTvEdit.setText("完成");
            btn_pay.setVisibility(View.GONE);
            liner_edit.setVisibility(View.VISIBLE);
//            layoutPayBar.setVisibility(View.GONE);
//            layoutEditBar.setVisibility(View.VISIBLE);
        } else {
            mTvEdit.setText("编辑");
           /* layoutPayBar.setVisibility(View.VISIBLE);
            layoutEditBar.setVisibility(View.GONE);*/
            btn_pay.setVisibility(View.VISIBLE);
            liner_edit.setVisibility(View.GONE);
        }
    }

    /**
     * 删除选中项
     */
    private void deleteInCart() {
        // TODO Auto-generated method stub
        if (inCartMap.size() == 0) {
            showToast("您还没有选择商品哦！");
            return;
        }
        if (inCartMap.size() == mData.size()) {
            ProgressDlgUtil.showProgressDlg("Loading...", HuoHangCartActivity.this);
            HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_cart_delall, "uid=" + MyApplication.getInstance().self.getId(), new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("status") == 1) {
                                    Log.i("zds", "cartdelall:成功");
                                    mData.clear();
                                    inCartMap.clear();
                                    mBtnCheckAll.setChecked(false);
                                    mBtnCheckAllEdit.setChecked(false);
                                    notifyCheckedChanged();
                                    notifyInCartNumChanged();
                                    mAdapter.notifyDataSetChanged();
                                    ProgressDlgUtil.stopProgressDlg();
                                } else {
                                    Log.i("zds", "cartdelall:失败");
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Log.i("zds", e.toString());
                                Log.i("zds", msg);
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });
        } else {
            com.alibaba.fastjson.JSONArray json = new com.alibaba.fastjson.JSONArray();
            for (int i = 0; i < cartDatas.size(); i++) {
                Goods goods = cartDatas.get(i);
                Boolean isChecked = inCartMap.get(cartDatas.get(i).getId());
                if (isChecked != null && isChecked) {
                    com.alibaba.fastjson.JSONObject shopmsg = new com.alibaba.fastjson.JSONObject();
                    shopmsg.put("sid", cartDatas.get(i).getSid());
                    json.add(shopmsg);
                }


            }
            String str = json.toJSONString();
            Log.i("zds", "str=" + str);

            ProgressDlgUtil.showProgressDlg("Loading...", HuoHangCartActivity.this);
            Log.i("zds", TLUrl.getInstance().URL_hwg_cart_deltwos + "?uid=" + MyApplication.getInstance().self.getId() + "&ajson=" + str + "");

            HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_cart_deltwos, "uid=" + MyApplication.getInstance().self.getId() + "&ajson=" + str, new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("status") == 1) {
                                    Log.i("zds", "cartdeltwos:成功");
//                                    mData.clear();
                                    inCartMap.clear();
                                    mBtnCheckAll.setChecked(false);
                                    mBtnCheckAllEdit.setChecked(false);
                                    notifyCheckedChanged();
                                    notifyInCartNumChanged();
                                    mAdapter.notifyDataSetChanged();
                                    ProgressDlgUtil.stopProgressDlg();
                                } else {
                                    Log.i("zds", "cartdeltwos:失败");
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Log.i("zds", e.toString());
                                Log.i("zds", msg);
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        myBroadCastReceiver.unRegister();
        ButterKnife.reset(this);
        super.onDestroy();
    }
}
