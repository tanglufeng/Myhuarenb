package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CartActivity extends BaseFragmentActivity implements View.OnClickListener {
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
    private TextView mTvEdit;
    private View layoutEditBar;
    private View layoutPayBar;
    private ProgressBar mProgressBar;
    private TextView mTvAddUp;
    private ZrcListView listView;
    public Handler handler = new Handler();
    public static ArrayList<Goods> mData = new ArrayList<Goods>();
    private HashMap<Integer, Boolean> inCartMap = new HashMap<Integer, Boolean>();// 用于存放选中的项

    private double price; // 总价
    private int num; // 选中的商品数

    private boolean isEdit; // 是否正在编辑

    private TextView t_delete;
    MyBroadCastReceiver myBroadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_cart);
        ButterKnife.inject(this);
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();

        initView();
        setOnListener();
        initListView();
//        initData();
    }

    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {
            initDates();
        }

        @Override
        public void updateCarNum(Intent intent) {

        }

        @Override
        public void updateCollection(Intent intent) {

        }

        @Override
        public void update(Intent intent) {

        }

    };

    private CompoundButton.OnCheckedChangeListener checkAllListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            mBtnCheckAll.setChecked(isChecked);
            mBtnCheckAllEdit.setChecked(isChecked);
            if (isChecked) {
                checkAll();
                t_delete.setText("清空");
            } else {
                inCartMap.clear();
                t_delete.setText("删除");
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
        findViewById(R.id.tljr_img_news_back).setOnClickListener(this);
    }

    public void initView() {
        t_delete = (TextView) findViewById(R.id.btn_delete);
        layoutNull = findViewById(R.id.layout_null);
        mTvEdit = (TextView) findViewById(R.id.tv_edit_cart);
        mTvPrice = (TextView) findViewById(R.id.tv_price);
//        mTvTotal = (TextView) layout.findViewById(R.id.tv_total);
//        mTvCount = (TextView) layout.findViewById(R.id.tv_count);
        mBtnCheckAll = (CheckBox) findViewById(R.id.btn_check_all);
        mBtnCheckAll.setClickable(false);
        mBtnCheckAllEdit = (CheckBox) findViewById(R.id.btn_check_all_deit);
        mBtnCheckAllEdit.setClickable(false);
//        mViewLogin = layout.findViewById(R.id.layout_login_cart);
        layoutEditBar = findViewById(R.id.layout_edit_bar);
        layoutPayBar = findViewById(R.id.layout_pay_bar);
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
        for (int i = 0; i < mData.size(); i++) {
            Boolean isChecked = inCartMap.get(mData.get(i).getId());
            if (isChecked != null && isChecked) {
                Goods goods = mData.get(i);
                num += goods.getGoods_num();
                price += goods.getMoney() * goods.getGoods_num();
            }
        }
        mTvPrice.setText(NumberUtils.formatPrice(price));

//        mTvTotal.setText("总额：" + NumberUtils.formatPrice(price));
//        mTvCount.setText("(" + num + ")");
        mTvAddUp.setText("小计：" + NumberUtils.formatPrice(price));

    }

    /**
     * 通知更新购物车商品数量
     */
    public void notifyInCartNumChanged() {
        // 通知主页刷新购物车商品数
//        Intent intent = new Intent();
//        sendBroadcast(intent);
        //更新购物车数量
        MyUpdateUI.sendUpdateCarNum(CartActivity.this);
    }

    public void initListView() {
//        listView= (ZrcListView) findViewById(R.id.tljr_zListView);
        mListView = (SwipeMenuListView) findViewById(R.id.listView_cart);
        View foot = getLayoutInflater().inflate(
                R.layout.hwg_foot_cart_list, null);
        mTvAddUp = (TextView) foot.findViewById(R.id.tv_add_up);
        mListView.addFooterView(foot, null, false);

        initDates();

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(CartActivity.this);
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
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
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

    public void initDates() {
        ProgressDlgUtil.showProgressDlg("", this);
        Log.i("zjz", "cart_uid=" + MyApplication.getInstance().self.getId());
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_cart, "uid=" + MyApplication.getInstance().self.getId(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("status") == 1) {
                                Log.i("zjz", "cart:连接成功");
                                mData.clear();
                                JSONArray jsonArray = object.getJSONArray("msg");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    Goods g = new Goods();
                                    g.setId(object1.optInt("id"));
                                    g.setGoods_num(object1.optInt("count"));
                                    g.setTitle(object1.optString("title"));
                                    g.setMoney(object1.optDouble("money"));
                                    g.setPicarr(object1.optString("picarr"));
                                    g.setDismoney(object1.optDouble("dismoney"));
                                    g.setSid(object1.optInt("sid"));
                                    mData.add(g);
                                }
                                Log.i("zjz", "cartNum=" + mData.size());
//                                Editor editor = Util.preference.edit();
//                                editor.putInt("carNum", mData.size());
//                                editor.commit();
                                mBtnCheckAll.setClickable(true);
                                mBtnCheckAllEdit.setClickable(true);
                                mAdapter = new CartAdapter(mData);
                                mListView.setAdapter(mAdapter);
                                notifyInCartNumChanged();
                                ProgressDlgUtil.stopProgressDlg();
                            } else {
                                Log.i("zjz", "cart解析失败");
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
        ArrayList<Goods> mGoods = new ArrayList<Goods>();

        public CartAdapter(ArrayList<Goods> mGoods) {
            this.mGoods = mGoods;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = null;
            ViewHolder holder = null;
            final Goods goods = getItem(position);
            if (convertView == null) {
                // 复用乱序问题
                inflate = getLayoutInflater().inflate(
                        R.layout.hwg_item_activity_cart_list, null);
                holder = new ViewHolder();
                holder.btnCheck = (CheckBox) inflate.findViewById(R.id.btn_check);
                holder.btnReduce = (Button) inflate.findViewById(R.id.btn_cart_reduce);
                holder.btnAdd = (Button) inflate.findViewById(R.id.btn_cart_add);
                holder.btnNumEdit = (EditText) inflate.findViewById(R.id.btn_cart_num_edit);
                holder.imgGoods = (ImageView) inflate.findViewById(R.id.img_goods);
                holder.tvGoodsName = (TextView) inflate.findViewById(R.id.tv_goods_name);
                holder.tvGoodsPrice = (TextView) inflate.findViewById(R.id.tv_goods_price);
                inflate.setTag(holder);
            } else {
                inflate = convertView;
                holder = (ViewHolder) inflate.getTag();
            }
            holder.tvGoodsName.setText(goods.getTitle());
            holder.tvGoodsPrice.setText(NumberUtils.formatPrice(goods.getMoney()));
            holder.btnNumEdit.setText("" + goods.getGoods_num());
            //加载图片
            LoadPicture loadPicture = new LoadPicture();
            loadPicture.initPicture(holder.imgGoods, goods.getPicarr());
            holder.imgGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CartActivity.this, GoodsDetailActivity.class);
                    intent.putExtra("sid", goods.getSid());
                    CartActivity.this.startActivity(intent);
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
            Boolean isChecked = inCartMap.get(goods.getId());
            if (isChecked != null && isChecked) {
                holder.btnCheck.setChecked(true);
            } else {
                holder.btnCheck.setChecked(false);
            }
            final ViewHolder holder2 = holder;
            holder.btnReduce.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ProgressDlgUtil.showProgressDlg("", CartActivity.this);
                    HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_cart_btnreduce, "uid=" + MyApplication.getInstance().self.getId() + "&sid=" + goods.getSid(), new HttpRevMsg() {
                        @Override
                        public void revMsg(final String msg) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject object = new JSONObject(msg);
                                        if (object.getInt("status") == 1) {
                                            Log.i("zjz", "cartadd:连接成功");
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
                                            }
                                            ProgressDlgUtil.stopProgressDlg();
                                        } else {
                                            Log.i("zjz", "cartadd解析失败");
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
            });
            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDlgUtil.showProgressDlg("", CartActivity.this);
                    HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_cart_btnadd, "uid=" + MyApplication.getInstance().self.getId() + "&sid=" + goods.getSid(), new HttpRevMsg() {
                        @Override
                        public void revMsg(final String msg) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject object = new JSONObject(msg);
                                        if (object.getInt("status") == 1) {
                                            Log.i("zjz", "cartadd:连接成功");
                                            holder2.btnReduce.setEnabled(true);
                                            int num = getNum(holder2.btnNumEdit);
                                            num++;
                                            goods.setGoods_num(num);
                                            notifyInCartNumChanged();
                                            // 如果被选中，更新价格
                                            if (holder2.btnCheck.isChecked()) {
                                                notifyCheckedChanged();
                                            }
                                            Log.e("onClick", "holder2.btnCheck.isChecked() = "
                                                    + holder2.btnCheck.isChecked());
                                            holder2.btnNumEdit.setText("" + num);
                                            ProgressDlgUtil.stopProgressDlg();
                                        } else {
                                            Log.i("zjz", "cartadd解析失败");
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
            });
            holder.btnCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        inCartMap.put(goods.getId(), isChecked);
                        // 如果所有项都被选中，则点亮全选按钮
                        if (inCartMap.size() == mData.size()) {
                            mBtnCheckAll.setChecked(true);
                            mBtnCheckAllEdit.setChecked(true);
                            t_delete.setText("清空");
                        } else {
                            t_delete.setText("删除");
                        }
                    } else {
                        // 如果之前是全选状态，则取消全选状态
                        if (inCartMap.size() == mData.size()) {
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
                        inCartMap.remove(goods.getId());
                    }
                    notifyCheckedChanged();
                }
            });
            return inflate;
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
                isEdit = false;
            } else {
                mListView.setVisibility(View.VISIBLE);
                mTvEdit.setVisibility(View.VISIBLE);
                layoutNull.setVisibility(View.GONE);
                if (isEdit) {
                    layoutEditBar.setVisibility(View.VISIBLE);
                } else {
                    layoutPayBar.setVisibility(View.VISIBLE);
                }
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

    }

    class ViewHolder {
        CheckBox btnCheck;
        Button btnAdd;
        Button btnReduce;
        EditText btnNumEdit;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_login_cart: // 登录
//                gotoLogin();
//                break;
//            case R.id.btn_collect: // 移入关注
//                add2Collect();
//                break;
            case R.id.tljr_img_news_back:
                finish();
                break;
            case R.id.btn_delete: // 删除
                deleteInCart();
                break;
            case R.id.tv_edit_cart: // 编辑
                editInCart();
                break;
            case R.id.btn_pay: // 结算
                pay();
                break;
//            case R.id.btn_more: // 去秒杀
////                MainActivity activity = (MainActivity) getActivity();
////                activity.activeCategory();
//                break;
            case R.id.layout_null: // 去逛逛
//                MainActivity activity = (MainActivity) getActivity();
//                activity.activeCategory();


                break;

            default:
                break;
        }
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
            Log.i("zjz", "str=" + str);

            ProgressDlgUtil.showProgressDlg("", CartActivity.this);
            HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_pay_goods, "uid=" + MyApplication.getInstance().self.getId() + "&ajson=" + str, new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("status") == 1) {
                                    Log.i("zjz", "cart_to_pay:成功");

                                    String oid = object.getString("msg");
                                    Intent intent = new Intent(CartActivity.this, PayWayActivity.class);
                                    Log.i("zjz", "total_money=" + price);
                                    Log.i("zjz", "oid=" + oid);
                                    intent.putExtra("total_money", price);
                                    intent.putExtra("oid", oid);
                                    startActivity(intent);
                                } else {
                                    Log.i("zjz", "cart_to_pay:失败");
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
    }

    /**
     * 全选，将数据加入inCartMap
     */
    private void checkAll() {
        for (int i = 0; i < mData.size(); i++) {
            inCartMap.put(mData.get(i).getId(), true);
        }
    }

    /**
     * 删除列表项
     */
    private void deleteItem(final int position) {
        final Goods goods = mData.get(position);
        ProgressDlgUtil.showProgressDlg("", CartActivity.this);
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_cart_delone, "uid=" + MyApplication.getInstance().self.getId() + "&sid=" + goods.getSid(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("status") == 1) {
                                Log.i("zjz", "cartdel:连接成功");
                                inCartMap.remove(goods.getId());
                                mData.remove(position);
                                notifyCheckedChanged();
                                notifyInCartNumChanged();
                                mAdapter.notifyDataSetChanged();

                                ProgressDlgUtil.stopProgressDlg();
                            } else {
                                Log.i("zjz", "cartdel解析失败");
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
     * 编辑
     */
    private void editInCart() {
        isEdit = !isEdit;
        if (isEdit) {
            mTvEdit.setText("完成");
            layoutPayBar.setVisibility(View.GONE);
            layoutEditBar.setVisibility(View.VISIBLE);
        } else {
            mTvEdit.setText("编辑");
            layoutPayBar.setVisibility(View.VISIBLE);
            layoutEditBar.setVisibility(View.GONE);
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
            ProgressDlgUtil.showProgressDlg("", CartActivity.this);
            HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_cart_delall, "uid=" + MyApplication.getInstance().self.getId(), new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("status") == 1) {
                                    Log.i("zjz", "cartdelall:成功");
                                    mData.clear();
                                    inCartMap.clear();
                                    mBtnCheckAll.setChecked(false);
                                    mBtnCheckAllEdit.setChecked(false);
                                    notifyCheckedChanged();
                                    notifyInCartNumChanged();
                                    mAdapter.notifyDataSetChanged();
                                    ProgressDlgUtil.stopProgressDlg();
                                } else {
                                    Log.i("zjz", "cartdelall:失败");
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
        } else {
            com.alibaba.fastjson.JSONArray json = new com.alibaba.fastjson.JSONArray();
            for (int i = 0; i < mData.size(); i++) {
                Goods goods = mData.get(i);
                Boolean isChecked = inCartMap.get(mData.get(i).getId());
                if (isChecked != null && isChecked) {
                    com.alibaba.fastjson.JSONObject shopmsg = new com.alibaba.fastjson.JSONObject();
                    shopmsg.put("sid", mData.get(i).getSid());
                    json.add(shopmsg);
                }


            }
            String str = json.toJSONString();
            Log.i("zjz", "str=" + str);

            ProgressDlgUtil.showProgressDlg("", CartActivity.this);
            Log.i("zjz", TLUrl.getInstance().URL_hwg_cart_deltwos + "?uid=" + MyApplication.getInstance().self.getId() + "&ajson=" + str + "");

            HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_cart_deltwos, "uid=" + MyApplication.getInstance().self.getId() + "&ajson=" + str, new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("status") == 1) {
                                    Log.i("zjz", "cartdeltwos:成功");
//                                    mData.clear();
                                    inCartMap.clear();
                                    mBtnCheckAll.setChecked(false);
                                    mBtnCheckAllEdit.setChecked(false);
                                    notifyCheckedChanged();
                                    notifyInCartNumChanged();
                                    mAdapter.notifyDataSetChanged();
                                    ProgressDlgUtil.stopProgressDlg();
                                } else {
                                    Log.i("zjz", "cartdeltwos:失败");
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

    }

    @Override
    protected void onDestroy() {
        myBroadCastReceiver.unRegister();
        super.onDestroy();
    }
}
