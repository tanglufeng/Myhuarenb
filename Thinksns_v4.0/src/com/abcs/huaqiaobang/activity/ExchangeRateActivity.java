package com.abcs.huaqiaobang.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.adapter.CommonAdapter;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Rate;
import com.abcs.huaqiaobang.view.HqbViewHolder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ExchangeRateActivity extends BaseActivity {

    @InjectView(R.id.titlebar)
    RelativeLayout titlebar;
    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.gridview)
    GridView gridview;
    private List<Rate> rates;
    private List<String> nums;
    private StringBuffer stringBuffer;
    private TextView shorthandName;
    private ImageView imageHL;
    private TextView name;
    private TextView price;
    private Rate defaultRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rate);
        ButterKnife.inject(this);
        rates = getIntent().getParcelableArrayListExtra("rates");

//        imageHL = (ImageView) findViewById(R.id.img_huilv);
//        shorthandName = (TextView) findViewById(R.id.shorthandName);
//        name = (TextView) findViewById(R.id.name);
//        price = (TextView) findViewById(R.id.price);
        Rate defaultRate = new Rate();
        defaultRate.setName("中国(yen)");
        defaultRate.setShorthandName("RMB");
        defaultRate.setRate(1.0);
        rates.add(0, defaultRate);

        nums = new ArrayList<>();
        nums.add("7");
        nums.add("8");
        nums.add("9");
        nums.add("4");
        nums.add("5");
        nums.add("6");
        nums.add("1");
        nums.add("2");
        nums.add("3");
        nums.add("0");
        nums.add(".");
        nums.add("C");
        gridview.setAdapter(new CommonAdapter<String>(this, nums, R.layout.grid_calculator) {
            @Override
            public void convert(HqbViewHolder helper, String item, int position) {

                if (position == nums.size() - 1) {
                    helper.getConvertView().setBackgroundColor(getResources().getColor(R.color.key_bule));
                    ((TextView) helper.getConvertView().findViewById(R.id.num)).setTextColor(getResources().getColor(R.color.white));
                }
                helper.setText(R.id.num, item);

            }
        });
        stringBuffer = new StringBuffer();
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (stringBuffer.length() == 14) {
                    return;
                }

                if (position == nums.size() - 1) {

                    if (stringBuffer.length() != 0)
                        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                } else if (position == nums.size() - 2) {
                    if ("".equals(stringBuffer.toString())) {
                        stringBuffer.append("0");
                        stringBuffer.append(nums.get(position));
                    } else if (stringBuffer.indexOf(nums.get(position)) <= -1) {
                        stringBuffer.append(nums.get(position));
                    }
                } else if (position < nums.size() - 2) {
                    stringBuffer.append(nums.get(position));
                }
                rates.get(0).setInPut(stringBuffer.toString());
                ((CommonAdapter) listview.getAdapter()).notifyDataSetChanged();

            }
        });

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == nums.size() - 1 && !"".equals(stringBuffer.toString())) {
                    stringBuffer.delete(0, stringBuffer.length());
                }
                rates.get(0).setInPut(stringBuffer.toString());
                ((CommonAdapter) listview.getAdapter()).notifyDataSetChanged();
                return true;
            }
        });


//        for (int i = 0; i < 15; i++) {
//            Rate rate = new Rate();
//            rate.setName("中国");
//            if (i == 0) {
//                rate.setShorthandName("RMB");
//            } else {
//                rate.setShorthandName("USD");
//            }
//            rate.setRate(i + 0.1);
//            rates.add(rate);
//        }

        listview.setAdapter(new CommonAdapter<Rate>(this, rates, R.layout.rate_item) {
            @Override
            public void convert(HqbViewHolder helper, Rate item, int position) {

                if (position == 0) {
                    helper.getConvertView().setBackgroundColor(getResources().getColor(R.color.hwgbg));
                    TextView priceView = helper.getView(R.id.price);
                    priceView.setTextColor((getResources().getColor(R.color.colorPrimary)));
                    priceView.setText(item.getInPut() == "" ? "0" : item.getInPut());
                } else {
                    helper.getConvertView().setBackgroundColor(getResources().getColor(R.color.white));
                    TextView priceView = helper.getView(R.id.price);
                    priceView.setTextColor((getResources().getColor(R.color.black)));

                    helper.setText(R.id.price,
                            rates.get(0).getInPut() == "" ? "0" : getRateString(item));
                }
                if (item.getCountryUrl() == null) {
                    helper.setImageResource(R.id.img_huilv, R.drawable.img_country_cn);
                } else {
//                    ((ImageView) helper.getView(R.id.img_huilv)).setImageResource(0);
                    helper.setImageByUrl(R.id.img_huilv, item.getCountryUrl(),0);
                }
                helper.setText(R.id.shorthandName, item.getShorthandName());
                helper.setText(R.id.name, item.getName());

            }
        });

        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                gridview.setVisibility(View.GONE);
                return false;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0)
                    gridview.setVisibility(gridview.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                Rate rate = rates.get(position);
//                rates.get(0).setInPut("");
                if (!"".equals(stringBuffer))
                    stringBuffer.delete(0, stringBuffer.length());
                rates.set(position, rates.get(0));
                rates.set(0, rate);
                ((CommonAdapter) listview.getAdapter()).notifyDataSetChanged();

            }
        });

    }

    @NonNull
    private String getRateString(Rate item) {
        String s;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
//        if (!rates.get(0).getShorthandName().equals("RMB")) {
//            s = nf.format(Double.parseDouble(rates.get(0).getInPut()) *
//                    item.getRate() * rates.get(0).getRate());
//
//        } else {
        s = nf.format(Double.parseDouble(rates.get(0).getInPut().equals("") ? "0" : rates.get(0).getInPut()) * rates.get(0).getRate() /
                item.getRate());
//        }
        return s;
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.back:

                finish();
                break;

        }
    }
}
