package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BankListActivity extends BaseActivity {
    @InjectView(R.id.linear_content)
    LinearLayout linearContent;
    private ArrayList<CityAcitivity.PopBean> list;
    private ListView bankListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);
        ButterKnife.inject(this);

//        bankListview = (ListView) findViewById(R.id.bank_listview);
        list = new ArrayList<>();

        String[] array = getResources().getStringArray(R.array.bank_list);
        String[] bank = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            String[] split = array[i].split("-");
            CityAcitivity.PopBean popBean = new CityAcitivity.PopBean();
            popBean.setId(split[0]);
            popBean.setName(split[1]);
            bank[i] = split[1];
            list.add(popBean);
        }
        findViewById(R.id.tljr_btn_lfanhui).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, bank);
////        PopAdapter adapter=new PopAdapter(this,list);
//        bankListview.setAdapter(adapter);
//        bankListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent();
//                intent.putExtra("bank", list.get(position));
//                setResult(2, intent);
//                finish();
//            }
//        });


        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.bank_item, null);
            TextView bankName = (TextView) view.findViewById(R.id.bank_name);

            final int index = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("bank", list.get(index));
                    setResult(2, intent);
                    finish();
                }
            });
            bankName.setText(list.get(i).getName());
            linearContent.addView(view);
        }

    }
}
