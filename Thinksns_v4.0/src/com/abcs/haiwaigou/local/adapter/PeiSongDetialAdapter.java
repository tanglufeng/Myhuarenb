//package com.abcs.haiwaigou.local.adapter;
//
//import android.content.Context;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.AccelerateInterpolator;
//import android.view.animation.Animation;
//import android.view.animation.AnimationSet;
//import android.view.animation.LinearInterpolator;
//import android.view.animation.TranslateAnimation;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.abcs.haiwaigou.adapter.GridLocalTagAdapter;
//import com.abcs.haiwaigou.local.activity.BenDiPeiSongActivity2;
//import com.abcs.haiwaigou.local.beans.ActivityArr;
//import com.abcs.haiwaigou.local.beans.HuoHangItem;
//import com.abcs.huaqiaobang.MyApplication;
//import com.abcs.sociax.android.R;
//import com.jude.easyrecyclerview.adapter.BaseViewHolder;
//import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
//import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// */
//public class PeiSongDetialAdapter extends RecyclerArrayAdapter<HuoHangItem>{
//    public PeiSongDetialAdapter(Context context,TextView shopCart) {
//        super(context);
//        this.shopCart = shopCart;
//    }
//
//    @Override
//    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
//        return new PeiSongDetialHoder(parent);
//    }
//
//
//    public ImageView buyImg;
//    public ViewGroup animMaskLayout;
//    public int[] goodsNum;
//    public static Map<Integer,HuoHangItem> datas=new HashMap<>(); //用来保存添加到购物车的对象
//    public static Map<Integer,Double> goodsPri=new HashMap<>(); //用来保存价格总数
//    public  static final int CONNT=500;
//    public int buyNum;
//    public TextView shopCart,goods_price;
//    public BenDiPeiSongActivity2 mActivity;
//    public List<HuoHangItem> goodscatrgoryEntities=new ArrayList<>();
//
//    public Map<Integer, HuoHangItem> getDatas() {
//        return datas;
//    }
//    public Map<Integer, Double> getGoodsPrice() {
//        return goodsPri;
//    }
//
//    /**
//     * 初始化各个商品的购买数量
//     */
//    public void initGoodsNum() {
//        goodsNum = new int[CONNT];
//        for (int i = 0; i < CONNT; i++) {
//            goodsNum[i] = 0;
//        }
//    }
//
//
//
//    public void setGoodscatrgoryEntities(List<HuoHangItem> goodscatrgoryEntities) {
//        this.goodscatrgoryEntities = goodscatrgoryEntities;
//    }
//
//
//    public void setmActivity(BenDiPeiSongActivity2 mActivity) {
//        this.mActivity = mActivity;
//    }
//
//    public interface OnShopCartGoodsChangeListener {
//        public void onNumChange(int num,HuoHangItem data);
//    }
//
//    public PeiSongDetialAdapter.OnShopCartGoodsChangeListener mOnGoodsNunChangeListener = null;
//
//    public void setOnShopCartGoodsChangeListener(PeiSongDetialAdapter.OnShopCartGoodsChangeListener e){
//        mOnGoodsNunChangeListener = e;
//    }
//
//    /**
//     * 判断商品是否有添加到购物车中
//     *
//     * @param i  条目下标
//     * @param vh ViewHolder
//     */
//    public void isSelected(int i, PeiSongDetialHoder vh) {
//        if (i == 0) {
//            vh.tvGoodsSelectNum.setVisibility(View.GONE);
//            vh.ivGoodsMinus.setVisibility(View.GONE);
//            datas.remove(vh.getAdapterPosition());
//            goodsPri.remove(vh.getAdapterPosition());
//        } else {
//            vh.tvGoodsSelectNum.setVisibility(View.VISIBLE);
//            vh.tvGoodsSelectNum.setText(i + "");
//            vh.ivGoodsMinus.setVisibility(View.VISIBLE);
//            datas.put(vh.getAdapterPosition(),getItem(vh.getAdapterPosition()));
//            goodsPri.put(vh.getAdapterPosition(),getItem(vh.getAdapterPosition()).goodsPrice*i);
//        }
//    }
//
//    /**
//     * 设置动画
//     *
//     * @param v              要显示的内容
//     * @param start_location 坐标
//     */
//    public void setAnim(final View v, int[] start_location) {
//        animMaskLayout = null;
//        animMaskLayout = createAnimLayout();
//        animMaskLayout.addView(v);// 把动画小球添加到动画层
//        final View view = addViewToAnimLayout(animMaskLayout, v,
//                start_location);
//        int[] end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标
//        shopCart.getLocationInWindow(end_location);// shopCart是那个购物车
//        // 计算位移
//        int endX = 0 - start_location[0] + 40;// 动画位移的X坐标
//        int endY = end_location[1] - start_location[1];// 动画位移的y坐标
//        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
//                endX, 0, 0);
//        translateAnimationX.setInterpolator(new LinearInterpolator());
//        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
//        translateAnimationX.setFillAfter(true);
//        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
//                0, endY);
//        translateAnimationY.setInterpolator(new AccelerateInterpolator());
//        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
//        translateAnimationX.setFillAfter(true);
//        AnimationSet set = new AnimationSet(false);
//        set.setFillAfter(false);
//        set.addAnimation(translateAnimationY);
//        set.addAnimation(translateAnimationX);
//        set.setDuration(500);// 动画的执行时间
//        view.startAnimation(set);
//        // 动画监听事件
//        set.setAnimationListener(new Animation.AnimationListener() {
//            // 动画的开始
//            @Override
//            public void onAnimationStart(Animation animation) {
//                v.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                // TODO Auto-generated method stub
//            }
//            // 动画的结束
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                v.setVisibility(View.GONE);
//            }
//        });
//    }
//
//    /**
//     * 修改购物车状态
//     */
//    public void changeShopCart(int buyNum) {
//        if (buyNum > 0) {
//            shopCart.setVisibility(View.VISIBLE);
//            if(mActivity.cart_num>0){
//                shopCart.setText((buyNum +mActivity.cart_num)+ "");
//            }else {
//                shopCart.setText(buyNum + "");
//            }
//
//        } else {
//            if(mActivity.cart_num>0){
//                shopCart.setText(mActivity.cart_num+ "");
//            }else {
//                shopCart.setVisibility(View.GONE);
//            }
//        }
//    }
//
//    /**
//     * 初始化动画图层
//     *
//     * @return
//     */
//    public ViewGroup createAnimLayout() {
//        ViewGroup rootView = (ViewGroup) mActivity.getWindow().getDecorView();
//        LinearLayout animLayout = new LinearLayout(mActivity);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        animLayout.setLayoutParams(lp);
//        animLayout.setBackgroundResource(android.R.color.transparent);
//        rootView.addView(animLayout);
//        return animLayout;
//    }
//
//
//    /**
//     * 将View添加到动画图层
//     *
//     * @param vg
//     * @param view
//     * @param location
//     * @return
//     */
//    public View addViewToAnimLayout(final ViewGroup vg, final View view,
//                                     int[] location) {
//        int x = location[0];
//        int y = location[1];
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.leftMargin = x;
//        lp.topMargin = y;
//        view.setLayoutParams(lp);
//        return view;
//    }
//
//
//    /**
//     * Created by Administrator on 2016/9/8.
//     */
//    public class PeiSongDetialHoder extends BaseViewHolder<HuoHangItem>{
//
//        public TextView tv_title,tv_store,tv_en_title;
//        public TextView tv_price,tv_sui;
//        public ImageView img_logo;
//
//        public  ImageView ivGoodsMinus;
//        public  TextView tvGoodsSelectNum;
//        public  ImageView ivGoodsAdd;
//
//        LinearLayout lin_tianjai,liner_hdong;
//        GridView liner_tag;
//
//        public PeiSongDetialHoder(ViewGroup parent) {
//            super(parent, R.layout.item_local_peisong2);
//
//            img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
//            lin_tianjai=(LinearLayout) itemView.findViewById(R.id.lin_tianjai);
//            liner_hdong=(LinearLayout) itemView.findViewById(R.id.liner_hdong);
////            liner_tag=(LinearLayout) itemView.findViewById(R.id.liner_tag);
//             liner_tag=(GridView) itemView.findViewById(R.id.liner_tag);
//            tv_en_title=(TextView) itemView.findViewById(R.id.tv_en_title);
//            tv_title=(TextView) itemView.findViewById(R.id.tv_title);
//            tv_store=(TextView) itemView.findViewById(R.id.tv_store);
//            tv_price=(TextView) itemView.findViewById(R.id.tv_price);
//            tv_sui=(TextView) itemView.findViewById(R.id.tv_sui);
//
//            ivGoodsMinus = (ImageView) itemView.findViewById(R.id.ivGoodsMinus);
//            tvGoodsSelectNum = (TextView) itemView.findViewById(R.id.tvGoodsSelectNum);
//            ivGoodsAdd = (ImageView) itemView.findViewById(R.id.ivGoodsAdd);
//
//        }
//
//        @Override
//        public void setData(final HuoHangItem data) {
//
//            if(data!=null){
//
//                liner_hdong.removeAllViews();
////                liner_tag.removeAllViews();
//
//                lin_tianjai.setVisibility(View.VISIBLE);
//                if(!TextUtils.isEmpty(data.goodsImage)){
////                http://www.huaqiaobang.com/data/upload/shop/store/goods/11/11_05375327290881075.jpg
//                    MyApplication.imageLoader.displayImage(TLUrl.getInstance().URL_hwg_base+"/data/upload/shop/store/goods/11/"+data.goodsImage,img_logo,MyApplication.options);
//                }
//                tv_title.setText(data.goodsName+"");
//                tv_en_title.setText(data.goodsEnName+"");
//                tv_store.setText("货号:"+data.goodsSerial);
//                tv_price.setText("€ "+data.goodsPrice);
//                tv_sui.setText(data.tax_rate+"");
//
//                if(data.activityArr!=null){
//
//                    if(data.activityArr.size()>0){
//                        liner_hdong.setVisibility(View.VISIBLE);
//                        for (int i = 0; i <data.activityArr.size() ; i++) {
//
//                            ActivityArr bean_hd=data.activityArr.get(i);
//                            View view=View.inflate(getContext(),R.layout.item_hd_text2,null);
//
//                            ViewGroup parent = (ViewGroup) view.getParent();
//                            if (parent != null) {
//                                parent.removeAllViews();
//                            }
//
//                            ImageView img=(ImageView) view.findViewById(R.id.img);
//                            TextView tv=(TextView) view.findViewById(R.id.tv);
//
//                            if(bean_hd!=null){
//                                if(!TextUtils.isEmpty(bean_hd.img)){
////                http://www.huaqiaobang.com/data/upload/shop/store/goods/11/11_05375327290881075.jpg
//                                    MyApplication.imageLoader.displayImage(bean_hd.img,img,MyApplication.options);
//                                }
//
//                                if(!TextUtils.isEmpty(bean_hd.activityName)){
//                                    tv.setText(bean_hd.activityName);
//                                }
//                            }
//
//                            liner_hdong.addView(view);
//                        }
//                    }else {
//                        liner_hdong.setVisibility(View.GONE);
//                    }
//                }
//
//                if(data.tagArr!=null){
//
//                    if(data.tagArr.size()>0){
//                        liner_tag.setAdapter(new GridLocalTagAdapter(mActivity,data.tagArr));
//                        liner_tag.setVisibility(View.VISIBLE);
//             /*           for (int i = 0; i <data.tagArr.size() ; i++) {
//
//                            TagArr bean_hd=data.tagArr.get(i);
//                            View view_tab=View.inflate(getContext(),R.layout.item_textview_t,null);
//
//                            ViewGroup parent = (ViewGroup) view_tab.getParent();
//                            if (parent != null) {
//                                parent.removeAllViews();
//                            }
//
//                            TextView tv=(TextView) view_tab.findViewById(R.id.tv_tab);
//
//                            if(bean_hd!=null){
//                                if(!TextUtils.isEmpty(bean_hd.tagName)){
//                                    tv.setText(bean_hd.tagName);
//                                }
//                            }
//
//                            liner_tag.addView(view_tab);
//                        }*/
//                    }else {
//                        liner_tag.setVisibility(View.GONE);
//                    }
//                }
//
//                isSelected(goodsNum[getAdapterPosition()], this);
//
//                ivGoodsAdd.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        goodsNum[getAdapterPosition()]++;
//                        // TODO:  添加购物车
//                        int[] start_location = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
//                        v.getLocationInWindow(start_location);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
//                        buyImg = new ImageView(getContext());
//                        buyImg.setBackgroundResource(R.drawable.icon_goods_add_item);// 设置buyImg的图片
//                        buyNum++;
//                        setAnim(buyImg, start_location);// 开始执行动画
//                        changeShopCart(buyNum);
//                        data.numbers=goodsNum[getAdapterPosition()];
//                        isSelected(goodsNum[getAdapterPosition()], PeiSongDetialHoder.this);
//                        mOnGoodsNunChangeListener.onNumChange(buyNum,data);
//
//                    }
//                });
//                ivGoodsMinus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (goodsNum[getAdapterPosition()] > 0) {
//                            goodsNum[getAdapterPosition()]--;
//                            buyNum--;
//                            changeShopCart(buyNum);
//                            data.numbers=goodsNum[getAdapterPosition()];
//                            //  删除购物车内容
//                            isSelected(goodsNum[getAdapterPosition()], PeiSongDetialHoder.this);
//                            mOnGoodsNunChangeListener.onNumChange(buyNum,data);
//                        }
//                    }
//                });
//            }
//        }
//    }
//}
