package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.PayWayActivity;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/14.
 */
public class YunGouAdapter extends BaseAdapter {
    ProgressDialog progressDialog;
    private ArrayList<Goods> countriesList;
    Activity activity;
    LayoutInflater inflater = null;
    public Handler handler = new Handler();

    public YunGouAdapter(Activity activity, ArrayList<Goods> countriesList
    ) {
        // TODO Auto-generated constructor stub

        this.activity = activity;
        this.countriesList = countriesList;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder mHolder = null;
        final Goods c = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(activity);
            convertView = mInflater.inflate(R.layout.fragment_shopping_yungou_item, null);

            mHolder = new ViewHolder();

            mHolder.t_goods_name = (TextView) convertView.findViewById(R.id.t_goods_name);
            mHolder.img_goods_icon = (ImageView) convertView.findViewById(R.id.img_goods_icon);
            mHolder.t_buy_num= (TextView) convertView.findViewById(R.id.t_buy_num);
            mHolder.t_total_num= (TextView) convertView.findViewById(R.id.t_total_num);
            mHolder.processbar= (ProgressBar) convertView.findViewById(R.id.processbar);
            mHolder.btn_buy= (Button) convertView.findViewById(R.id.btn_buy);
            convertView.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) convertView.getTag();

        }
        //图片加载
        LoadPicture loadPicture = new LoadPicture();
        loadPicture.initPicture(mHolder.img_goods_icon, c.getPicarr());
        mHolder.t_total_num.setText(c.getZongrenshu() + "");
        mHolder.t_buy_num.setText(c.getCanyurenshu() + "");
        mHolder.t_goods_name.setText(c.getTitle());
        int pro = (int) (Float.valueOf(c.getCanyurenshu())
                / Float.valueOf(c.getZongrenshu()) * 100);
        mHolder.processbar.setProgress(pro);
        mHolder.img_goods_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mHolder.btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getInstance().self == null) {
                    Intent intent = new Intent(activity, WXEntryActivity.class);
                    activity.startActivity(intent);
                    return;
                }
                Log.i("zjz","id="+c.getId());
                Log.i("zjz","qishu="+c.getQishu());

                Log.i("zjz","url="+TLUrl.getInstance().URL_GOODS_SHOPCAR+"?"+"uid=" + MyApplication.getInstance().self.getId() + "&id="
                        + c.getId() + "&num=1" + "&qishu=" + c.getQishu());
                HttpRequest.sendGet(TLUrl.getInstance().URL_GOODS_SHOPCAR, "uid=" + MyApplication.getInstance().self.getId() + "&id="
                        + c.getId() + "&num=1" + "&qishu=" + c.getQishu(), new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject(msg);
                                    if (object.getInt("status") == 1) {
                                        Log.i("zjz", "carFragment添加成功");
                                        Intent intent = new Intent(activity, PayWayActivity.class);
                                        intent.putExtra("yungou", true);
                                        activity.startActivity(intent);
                                    } else {
                                        Log.i("zjz", "carFragment添加失败");
                                    }
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    Log.i("zjz", e.toString());
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        });
        return convertView;

    }


    private static class ViewHolder {

        TextView t_goods_name;
        ImageView img_goods_icon;
        TextView t_total_num;
        TextView t_buy_num;
        ProgressBar processbar;
        Button btn_buy;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return countriesList == null ? 0 : countriesList.size();
    }


    @Override
    public Goods getItem(int position) {
        if (countriesList != null && countriesList.size() != 0) {
            if (position >= countriesList.size()) {
                return countriesList.get(0);
            }
            return countriesList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


}
