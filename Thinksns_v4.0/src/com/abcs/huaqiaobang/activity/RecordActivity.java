package com.abcs.huaqiaobang.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.BlurUtils;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.TimePopupWindow;
import com.abcs.huaqiaobang.view.TimePopupWindow.OnTimeSelectListener;
import com.abcs.huaqiaobang.view.TimePopupWindow.Type;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author xbw
 * @version 创建时间：2015年10月23日 下午7:16:03
 */
public class RecordActivity extends BaseActivity {
    private Handler handler = new Handler();
    private TextView from, to;
    private RadioGroup radioGroup;
    private ListView lv;
    private ArrayList<JSONObject> list = new ArrayList<JSONObject>();
    private TimePopupWindow pwTime;
    private TextView nowText;

    /*
     * 透明模糊
     */
    private View overlayLayout;
    private View contentFrame;
    private Bitmap scaled;

    public void setBlurImage() {
        scaled = null;
        contentFrame.setDrawingCacheEnabled(true);
        scaled = contentFrame.getDrawingCache();
        Bitmap blured = BlurUtils.apply(this, scaled, 10);
        contentFrame.setDrawingCacheEnabled(false);
        Drawable drawable = new BitmapDrawable(blured);
        overlayLayout.setBackgroundDrawable(drawable);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        contentFrame = View.inflate(this, R.layout.occft_activity_record, null);
        setContentView(contentFrame);
        from = (TextView) findViewById(R.id.record_edit_form);
        to = (TextView) findViewById(R.id.record_edit_to);
        lv = (ListView) findViewById(R.id.record_lv);
        radioGroup = (RadioGroup) findViewById(R.id.rg);
        findViewById(R.id.tljr_img_regiest_back).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.week:
                        getTime(1);
                        break;
                    case R.id.month:
                        getTime(2);
                        break;
                    case R.id.threemonth:
                        getTime(3);
                        break;

                    default:
                        break;
                }
            }
        });
        findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getRecordFromTime(from.getText().toString(), to.getText()
                        .toString());
            }
        });
        from.setText(Util.getDateOnlyDat(System.currentTimeMillis() - 86400000l * 7));
        from.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    pwTime.setTime(Util.format1
                            .parse(from.getText().toString()));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                nowText = from;
                pwTime.showAtLocation(from, Gravity.BOTTOM, 0, 0, new Date());
                setBlurImage();
            }
        });
        to.setText(Util.getDateOnlyDat(System.currentTimeMillis()));
        to.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    pwTime.setTime(Util.format1.parse(to.getText().toString()));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                nowText = to;
                pwTime.showAtLocation(to, Gravity.BOTTOM, 0, 0, new Date());
                setBlurImage();
            }
        });
        // 时间选择器
        pwTime = new TimePopupWindow(this, Type.YEAR_MONTH_DAY);
        overlayLayout = pwTime.getBj();
        // 时间选择后回调
        pwTime.setOnTimeSelectListener(new OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                nowText.setText(Util.getDateOnlyDat(date.getTime()));
                getRecordFromTime(from.getText().toString(), to.getText()
                        .toString());
            }
        });
        getTime(1);
        lv.setAdapter(new BaseAdapter() {

            @Override
            public View getView(int position, View v, ViewGroup parent) {
                JSONObject object = list.get(position);
                if (v == null) {
                    v = View.inflate(RecordActivity.this,
                            R.layout.occft_item_record, null);
                    AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                            Util.WIDTH, Util.HEIGHT / 10);
                    v.setLayoutParams(params);
                }
                try {
                    ((TextView) v.findViewById(R.id.record_item_title))
                            .setText(object.getString("name"));
                    ((TextView) v.findViewById(R.id.record_item_time))
                            .setText(Util.getDate(object.getLong("date")));
                    ((TextView) v.findViewById(R.id.record_item_money)).setText(Util.df
                            .format(object.getDouble("money") / 100) + "元");
                    if (object.getInt("status") != 1) {
                        ((TextView) v.findViewById(R.id.record_item_info))
                                .setText(object.getString("errMsg"));
                    } else {
                        ((TextView) v.findViewById(R.id.record_item_info))
                                .setText("交易成功");
                    }
                    if (object.getBoolean("vip")) {
                        v.findViewById(R.id.item_product_bj).setVisibility(
                                View.VISIBLE);
                    } else {
                        v.findViewById(R.id.item_product_bj).setVisibility(
                                View.GONE);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return v;
            }

            @Override
            public long getItemId(int position) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public Object getItem(int position) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return list.size();
            }
        });
    }

    private void getTime(int type) {
        long from = System.currentTimeMillis();
        if (type == 1) {
            from = System.currentTimeMillis() - 86400000l * 7;
        } else if (type == 2) {
            from = System.currentTimeMillis() - 86400000l * 30;
        } else if (type == 3) {
            from = System.currentTimeMillis() - 86400000l * 90;
        }
        getRecordFromTime(Util.getDateOnlyDat(from),
                Util.getDateOnlyDat(System.currentTimeMillis() + 86400000l));
    }

    // &startDate=2015-10-12&endDate=2015-10-24
    private void getRecordFromTime(String from, String to) {
        try {
            if (Util.format1.parse(from).getTime() > Util.format1.parse(to)
                    .getTime()) {
                showToast("时间格式错误，请重试");
                return;
            }
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        LogUtil.e("getRecordFromTime", from + "   " + to);
        handler.post(new Runnable() {

            @Override
            public void run() {
                list.clear();
                ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
            }
        });
        ProgressDlgUtil.showProgressDlg("", this);
        LogUtil.e("getRecordFromTime", "method=getTradingList&uid="
                + MyApplication.getInstance().self.getId() + "&startDate="
                + from + "&endDate=" + to + "&token=" + Util.token);
        HttpRequest.sendPost(
                TLUrl.getInstance().URL_productServlet,
                "method=getTradingList&uid="
                        + MyApplication.getInstance().self.getId()
                        + "&startDate=" + from + "&endDate=" + to + "&token="
                        + Util.token, new HttpRevMsg() {

                    @Override
                    public void revMsg(String msg) {
                        LogUtil.e("getRecordFromTime", msg);
                        ProgressDlgUtil.stopProgressDlg();
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.getInt("status") == 1) {
                                showListUi(jsonObject.getJSONArray("msg"));
                            } else {
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        showToast("获取失败，请重试");
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    showToast("获取失败，请检查日期是否正确");
                                }
                            });
                        }
                    }
                });
    }

    private void showListUi(final JSONArray array) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                list.clear();
                try {
                    for (int i = 0; i < array.length(); i++) {
                        list.add(array.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
                if (list.size() == 0) {
                    showToast("暂无交易记录!");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(scaled!=null&&!scaled.isRecycled())
        scaled.recycle();
    }
}
