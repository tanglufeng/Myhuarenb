package com.abcs.haiwaigou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.sociax.android.R;

/**
 * Created by Administrator on 2016/2/18.
 */
public class GoodCartFragment extends Fragment {

    MyBroadCastReceiver myBroadCastReceiver;
    private LinearLayout linear_tuijian_root;
    private TextView t_tuijian_goods_name;
    private TextView t_tuijian_goods_money;
    private TextView t_tuijian_buy;
    private ImageView img_tuijian_goods_icon;
    public ImageView img_mohu;
    Goods dataList;
    int position;


    public static GoodCartFragment getInstance(Goods data, int position) {
        GoodCartFragment f = new GoodCartFragment();
        Bundle b = new Bundle();
        b.putSerializable("data", data);
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    MyBroadCastReceiver.UpdateUI updateUI=new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        myBroadCastReceiver=new MyBroadCastReceiver(getContext(),updateUI);
        myBroadCastReceiver.register();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.hwg_item_country_tuijian2, null);
        img_mohu = (ImageView) view.findViewById(R.id.img_mohu);
        img_tuijian_goods_icon = (ImageView) view.findViewById(R.id.img_tuijian_goods_icon);
        LoadPicture loadPicture = new LoadPicture();
        dataList = (Goods) getArguments().getSerializable("data");
        loadPicture.initPicture(img_tuijian_goods_icon, dataList.getPicarr());
//            loadPicture.initPicture(img_tuijian_goods_icon,goodsList.get(i).getPicarr());
        linear_tuijian_root = (LinearLayout) view.findViewById(R.id.linear_tuijian_root);
        linear_tuijian_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra("sid", dataList.getSid());
                intent.putExtra("pic", dataList.getPicarr());
                startActivity(intent);
            }
        });
        t_tuijian_goods_name = (TextView) view.findViewById(R.id.t_tuijian_goods_name);
        t_tuijian_goods_name.setText(dataList.getTitle());
//            t_tuijian_goods_name.setText(goodsList.get(i).getTitle());
        t_tuijian_goods_money = (TextView) view.findViewById(R.id.t_tuijian_goods_money);
        t_tuijian_goods_money.setText(NumberUtils.formatPrice(dataList.getMoney()));
//            t_tuijian_goods_money.setText(NumberUtils.formatPrice(goodsList.get(i).getMoney()));
        t_tuijian_buy = (TextView) view.findViewById(R.id.t_tuijian_buy);
        t_tuijian_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showToast("购买成功！");
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        myBroadCastReceiver.unRegister();
        super.onDestroy();
    }
}
