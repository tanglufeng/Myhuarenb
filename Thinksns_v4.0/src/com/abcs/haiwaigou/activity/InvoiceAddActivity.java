package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class InvoiceAddActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.t_inv_title)
    TextView tInvTitle;
    @InjectView(R.id.spinner_title)
    Spinner spinnerTitle;
    @InjectView(R.id.t_company_title)
    TextView tCompanyTitle;
    @InjectView(R.id.et_company)
    EditText etCompany;
    @InjectView(R.id.linear_company)
    LinearLayout linearCompany;
    @InjectView(R.id.t_inv_content)
    TextView tInvContent;
    @InjectView(R.id.spinner_content)
    Spinner spinnerContent;
    @InjectView(R.id.btn_save)
    Button btnSave;

    private Handler handler=new Handler();
    boolean isCompany=false;
    String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_invoice_add);
        ButterKnife.inject(this);
        initSpinner();
        setOnListener();
    }

    private void setOnListener() {
        btnSave.setOnClickListener(this);
    }


    private void initSpinner() {
        spinnerTitle.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerTitle.getSelectedItem().toString().equals("公司")) {
                    linearCompany.setVisibility(View.VISIBLE);
                    isCompany=true;
                } else {
                    isCompany=false;
                    linearCompany.setVisibility(View.GONE);
                }
//                showToast(spinnerTitle.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerContent.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                showToast(spinnerContent.getSelectedItem().toString());
                content=spinnerContent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                addInvoice();
                break;
        }
    }

    private void addInvoice() {
        String param = null;
        //个人
        if (!isCompany) {
            param = "&key=" + MyApplication.getInstance().getMykey() + "&inv_title_select=person" + "&inv_content="+content;
        } else {

            String company;
            company=etCompany.getText().toString();
            if(company.length()==0){
                showToast("公司名称不能为空！");
                return;
            }
            param = "&key=" + MyApplication.getInstance().getMykey() + "&inv_title_select=company" + "&inv_title="+company + "&inv_content="+content;
        }
        //公司
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_invoice_add, param, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg_invoice_add=" + msg);
                                JSONObject data = object.getJSONObject("datas");
                                if (data.has("inv_id") && data != null) {
                                    Intent intent=new Intent();
                                    intent.putExtra("result",true);
                                    setResult(2, intent);
                                    finish();
                                    showToast("添加成功！");
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
}
