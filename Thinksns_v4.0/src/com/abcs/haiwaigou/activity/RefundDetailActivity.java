package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Refund;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.tljr.news.ShowWebImageActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RefundDetailActivity extends BaseActivity implements View.OnClickListener {


    @InjectView(R.id.tljr_txt_news_title)
    TextView tljrTxtNewsTitle;
    @InjectView(R.id.tljr_hqss_news_titlebelow)
    TextView tljrHqssNewsTitlebelow;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.tljr_grp_goods_title)
    RelativeLayout tljrGrpGoodsTitle;
    @InjectView(R.id.t_tishi)
    TextView tTishi;
    @InjectView(R.id.spinner_company)
    Spinner spinnerCompany;
    @InjectView(R.id.img_style)
    ImageView imgStyle;
    @InjectView(R.id.ed_code)
    EditText edCode;
    @InjectView(R.id.linear_deliver_code)
    LinearLayout linearDeliverCode;
    @InjectView(R.id.btn_commit)
    Button btnCommit;
    @InjectView(R.id.linear_deliver)
    LinearLayout linearDeliver;
    @InjectView(R.id.t_refund_money)
    TextView tRefundMoney;
    @InjectView(R.id.t_refund_time)
    TextView tRefundTime;
    @InjectView(R.id.t_finish_time)
    TextView tFinishTime;
    @InjectView(R.id.linear_finish_time)
    LinearLayout linearFinishTime;
    @InjectView(R.id.t_refund_code)
    TextView tRefundCode;
    @InjectView(R.id.t_refund_reason)
    TextView tRefundReason;
    @InjectView(R.id.t_refund_explain)
    TextView tRefundExplain;
    @InjectView(R.id.t_deliver_detail)
    TextView tDeliverDetail;
    @InjectView(R.id.t_deliver_sn)
    TextView tDeliverSn;
    @InjectView(R.id.linear_my_deliver_detail)
    LinearLayout linearMyDeliverDetail;
    @InjectView(R.id.img_1)
    ImageView img1;
    @InjectView(R.id.relative_pic1)
    RelativeLayout relativePic1;
    @InjectView(R.id.img_2)
    ImageView img2;
    @InjectView(R.id.relative_pic2)
    RelativeLayout relativePic2;
    @InjectView(R.id.img_3)
    ImageView img3;
    @InjectView(R.id.relative_pic3)
    RelativeLayout relativePic3;
    @InjectView(R.id.linear_pic)
    LinearLayout linearPic;
    @InjectView(R.id.t_store_state)
    TextView tStoreState;
    @InjectView(R.id.t_store_beizhu)
    TextView tStoreBeizhu;
    @InjectView(R.id.linear_store_beizhu)
    LinearLayout linearStoreBeizhu;


    String refund_id;
    String pic1 = "";
    String pic2 = "";
    String pic3 = "";
    @InjectView(R.id.t_no)
    TextView tNo;
    private RequestQueue mRequestQueue;
    Refund refund;
    boolean isRefund;
    String express_id;
    ArrayList<String> arrayList = new ArrayList<>();

    private Handler handler = new Handler();
    private ArrayList<String> uList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_refund_detail);
        ButterKnife.inject(this);
        mRequestQueue = Volley.newRequestQueue(this);
        isRefund = getIntent().getBooleanExtra("isRefund", false);
        refund_id = getIntent().getStringExtra("refund_id");
        refund = new Refund();
        if (isRefund) {
            tljrTxtNewsTitle.setText("退款详情");
            linearDeliver.setVisibility(View.GONE);
            linearMyDeliverDetail.setVisibility(View.GONE);
            initRefundDate();
        } else {
            tljrTxtNewsTitle.setText("退货详情");
            initReturnDate();
        }
        initExpressID();
        setOnListener();
        initSpinner();
    }

    ArrayAdapter<CharSequence> companyAdapter = null;

    private void initSpinner() {

        companyAdapter = ArrayAdapter.createFromResource(this, R.array.deliver_company, android.R.layout.simple_spinner_item);
        companyAdapter.setDropDownViewResource(R.layout.hwg_spinner_dropdown_item);
        spinnerCompany.setAdapter(companyAdapter);
        spinnerCompany.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                express_id = arrayList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void initReturnDate() {
        ProgressDlgUtil.showProgressDlg("Loading...",this);
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_return_list_detail + "&return_id=" + refund_id + "&key=" + MyApplication.getInstance().getMykey(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "good_refund:连接成功");
                        Log.i("zjz", "response=" + response);
                        JSONObject object = response.getJSONObject("datas");
                        JSONObject refundObject = object.getJSONObject("return");
                        refund.setAdd_time(refundObject.optLong("add_time"));
                        refund.setAdmin_time(refundObject.optLong("admin_time"));
                        refund.setReason_info(refundObject.optString("reason_info"));
                        refund.setBuyer_message(refundObject.optString("buyer_message"));
                        refund.setRefund_state(refundObject.optString("refund_state"));
                        refund.setSeller_message(refundObject.optString("seller_message"));
                        refund.setAdmin_message(refundObject.optString("admin_message"));
                        refund.setRefund_sn(refundObject.optString("refund_sn"));
                        refund.setSeller_state(refundObject.optString("seller_state"));
                        refund.setGoods_state(refundObject.optString("goods_state"));
                        refund.setRefund_amount(refundObject.optString("refund_amount"));
                        refund.setReturn_e_name(object.optString("return_e_name"));
                        refund.setInvoice_no(refundObject.optString("invoice_no"));
                        refund.setExpress_id(refundObject.optString("express_id"));
                        JSONObject picObject = object.optJSONObject("pic_list");
                        uList.clear();
                        if (picObject != null) {
                            pic1 = TLUrl.getInstance().URL_hwg_return_pic_head + picObject.optString("1", "");
                            pic2 = TLUrl.getInstance().URL_hwg_return_pic_head + picObject.optString("2", "");
                            pic3 = TLUrl.getInstance().URL_hwg_return_pic_head + picObject.optString("3", "");
                            uList.add(pic1);
                            uList.add(pic2);
                            uList.add(pic3);
                            linearPic.setVisibility(View.VISIBLE);
                        }else {
                            tNo.setVisibility(View.VISIBLE);
                        }
                        initReturnView();
                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
                }finally {
                    ProgressDlgUtil.stopProgressDlg();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDlgUtil.stopProgressDlg();
            }
        });
        mRequestQueue.add(jr);
    }


    private void initRefundDate() {
        ProgressDlgUtil.showProgressDlg("Loading...",this);
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_refund_list_detail + "&refund_id=" + refund_id + "&key=" + MyApplication.getInstance().getMykey(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "good_refund:连接成功");
                        Log.i("zjz", "response=" + response);
                        JSONObject object = response.getJSONObject("datas");
                        JSONObject refundObject = object.getJSONObject("refund");
                        refund.setAdd_time(refundObject.optLong("add_time"));
                        refund.setAdmin_time(refundObject.optLong("admin_time"));
                        refund.setReason_info(refundObject.optString("reason_info"));
                        refund.setBuyer_message(refundObject.optString("buyer_message"));
                        refund.setRefund_state(refundObject.optString("refund_state"));
                        refund.setSeller_message(refundObject.optString("seller_message"));
                        refund.setAdmin_message(refundObject.optString("admin_message"));
                        refund.setRefund_sn(refundObject.optString("refund_sn"));
                        refund.setRefund_amount(refundObject.optString("refund_amount"));
                        refund.setSeller_state(refundObject.optString("seller_state"));
                        refund.setGoods_state(refundObject.optString("goods_state"));
                        JSONObject picObject = object.optJSONObject("pic_list");
                        uList.clear();
                        if (picObject != null) {
                            pic1 = TLUrl.getInstance().URL_hwg_return_pic_head + picObject.optString("1", "");
                            pic2 = TLUrl.getInstance().URL_hwg_return_pic_head + picObject.optString("2", "");
                            pic3 = TLUrl.getInstance().URL_hwg_return_pic_head + picObject.optString("3", "");
                            uList.add(pic1);
                            uList.add(pic2);
                            uList.add(pic3);
                            linearPic.setVisibility(View.VISIBLE);
                        }else {
                            tNo.setVisibility(View.VISIBLE);
                        }

                        initRefundView();
                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
                }finally {
                    ProgressDlgUtil.stopProgressDlg();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDlgUtil.stopProgressDlg();
            }
        });
        mRequestQueue.add(jr);
    }

    private void initReturnView() {
        switch (refund.getRefund_state()) {
            //待商家处理
            case "1":

                linearFinishTime.setVisibility(View.GONE);
                if (refund.getSeller_state().equals("1")) {
                    //商家未处理
                    tTishi.setText("等待客服处理退货申请");
                    tStoreState.setText("待审核");
                    linearStoreBeizhu.setVisibility(View.GONE);
                    linearMyDeliverDetail.setVisibility(View.GONE);
                    linearDeliver.setVisibility(View.GONE);
                } else if (refund.getSeller_state().equals("2")) {
                    //商家已处理
                    tStoreState.setText("待审核");
                    tStoreBeizhu.setText(refund.getSeller_message());
                    if (refund.getGoods_state().equals("1")) {
                        //商家已同意，待发货
                        tTishi.setText("客服已受理并同意退货");
                        linearDeliver.setVisibility(View.VISIBLE);
                        linearMyDeliverDetail.setVisibility(View.GONE);
                    } else if (refund.getGoods_state().equals("2")) {
                        //已发货
                        tTishi.setText("用户已退货,等待客服处理退货申请");
                        if (!refund.getExpress_id().equals("null")) {
                            tDeliverDetail.setText(refund.getReturn_e_name());
                            tDeliverSn.setText(refund.getInvoice_no());
                            linearMyDeliverDetail.setVisibility(View.VISIBLE);
                        } else {
                            linearMyDeliverDetail.setVisibility(View.GONE);
                        }
                        linearDeliver.setVisibility(View.GONE);
                    }
                }

                break;
            case "2":
                tTishi.setText("等待客服处理退货申请");
                tStoreState.setText("待审核");
                tStoreBeizhu.setText(refund.getSeller_message());
                linearDeliver.setVisibility(View.GONE);
                tDeliverDetail.setText(refund.getReturn_e_name());
                tDeliverSn.setText(refund.getInvoice_no());
                linearFinishTime.setVisibility(View.GONE);
                break;
            case "3":

                if (refund.getSeller_state().equals("2")) {
                    tTishi.setText("退货退款成功");
                    tStoreState.setText("已完成");
                    tStoreBeizhu.setText(refund.getSeller_message());
                    tDeliverDetail.setText(refund.getReturn_e_name());
                    tDeliverSn.setText(refund.getInvoice_no());
                    linearDeliver.setVisibility(View.GONE);
                    tFinishTime.setText(Util.format.format(refund.getAdmin_time() * 1000) + "");
                    linearFinishTime.setVisibility(View.VISIBLE);
                } else if (refund.getSeller_state().equals("3")) {
                    tTishi.setText("商家不同意退货申请");
                    tStoreState.setText("不同意");
                    tStoreBeizhu.setText(refund.getSeller_message());
                    linearDeliver.setVisibility(View.GONE);
                    linearMyDeliverDetail.setVisibility(View.GONE);
                    linearFinishTime.setVisibility(View.GONE);
                }

                break;
        }
        tRefundTime.setText(Util.format.format(refund.getAdd_time() * 1000) + "");
        tRefundCode.setText(refund.getRefund_sn());
        tRefundMoney.setText("¥" + refund.getRefund_amount());
        tRefundReason.setText(refund.getReason_info());
        tRefundExplain.setText(refund.getBuyer_message());
        LoadPicture loadPicture = new LoadPicture();
        if (!pic1.equals("")) {
            relativePic1.setVisibility(View.VISIBLE);
            relativePic1.setOnClickListener(this);
            String url1 = TLUrl.getInstance().URL_hwg_return_pic_head + pic1;
            loadPicture.initPicture(img1, pic1);
        }
        if (!pic2.equals("")) {
            relativePic2.setVisibility(View.VISIBLE);
            relativePic2.setOnClickListener(this);
            String url2 = TLUrl.getInstance().URL_hwg_return_pic_head + pic2;
            loadPicture.initPicture(img2, pic2);
        }
        if (!pic3.equals("")) {
            relativePic3.setVisibility(View.VISIBLE);
            relativePic3.setOnClickListener(this);
            String url3 = TLUrl.getInstance().URL_hwg_return_pic_head + pic3;
            loadPicture.initPicture(img3, pic3);
        }
    }

    private void initRefundView() {
        switch (refund.getRefund_state()) {
            case "1":
                //待商家审核
                tTishi.setText("等待客服处理退款申请");
                tStoreState.setText("待审核");
                linearStoreBeizhu.setVisibility(View.GONE);
                linearFinishTime.setVisibility(View.GONE);
                break;
            case "2":
                //商家已审核,待商城处理
                tTishi.setText("等待客服处理退款申请");
                tStoreState.setText("待审核");
                tStoreBeizhu.setText(refund.getSeller_message());
                linearFinishTime.setVisibility(View.GONE);
                break;
            case "3":
                //商城已处理
                if (refund.getSeller_state().equals("2")) {
                    tTishi.setText("退款成功");
                    tStoreState.setText("已完成");
                    tStoreBeizhu.setText(refund.getSeller_message());
                    tFinishTime.setText(Util.format.format(refund.getAdmin_time() * 1000) + "");
                    linearFinishTime.setVisibility(View.VISIBLE);
                } else if (refund.getSeller_state().equals("3")) {
                    tTishi.setText("商家不同意退款申请");
                    tStoreState.setText("不同意");
                    tStoreBeizhu.setText(refund.getSeller_message());
                    linearFinishTime.setVisibility(View.GONE);
                }

                break;
        }
        tRefundCode.setText(refund.getRefund_sn());
        tRefundTime.setText(Util.format.format(refund.getAdd_time() * 1000) + "");
        tRefundMoney.setText("¥" + refund.getRefund_amount());
        tRefundReason.setText(refund.getReason_info());
        tRefundExplain.setText(refund.getBuyer_message());
        LoadPicture loadPicture = new LoadPicture();
        if (!pic1.equals("")) {
            relativePic1.setVisibility(View.VISIBLE);
            relativePic1.setOnClickListener(this);
            String url1 = TLUrl.getInstance().URL_hwg_return_pic_head + pic1;
            loadPicture.initPicture(img1, pic1);
        }
        if (!pic2.equals("")) {
            relativePic2.setVisibility(View.VISIBLE);
            relativePic2.setOnClickListener(this);
            String url2 = TLUrl.getInstance().URL_hwg_return_pic_head + pic2;
            loadPicture.initPicture(img2, pic2);
        }
        if (!pic3.equals("")) {
            relativePic3.setVisibility(View.VISIBLE);
            relativePic3.setOnClickListener(this);
            String url3 = TLUrl.getInstance().URL_hwg_return_pic_head + pic3;
            loadPicture.initPicture(img3, pic3);
        }
    }

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.btn_commit:
                commitDeliver();
                break;
            case R.id.relative_pic1:
                intent = new Intent();
                intent.putExtra("image", pic1);
                intent.putExtra("ulist", uList);
                intent.setClass(this, ShowWebImageActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_pic2:
                intent = new Intent();
                intent.putExtra("image", pic2);
                intent.putExtra("ulist", uList);
                intent.setClass(this, ShowWebImageActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_pic3:
                intent = new Intent();
                intent.putExtra("image", pic3);
                intent.putExtra("ulist", uList);
                intent.setClass(this, ShowWebImageActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void commitDeliver() {
        if (express_id.equals("0")) {
            showToast("请选择物流公司！");
            return;
        }
        if (edCode.getText().toString().length() == 0) {
            showToast("物流单号不能为空！");
            return;
        }
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_return_deliver + "&return_id=" + refund_id, "&express_id=" + express_id + "&invoice_no=" + edCode.getText().toString() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg_commit=" + msg);
                                showToast(object.optString("datas"));
                                if (object.optString("datas").contains("成功")) {
                                    showToast("提交成功");
                                    MyUpdateUI.sendUpdateCollection(RefundDetailActivity.this, MyUpdateUI.RETURN);
                                    finish();
                                }
                                ProgressDlgUtil.stopProgressDlg();
                            } else {
                                ProgressDlgUtil.stopProgressDlg();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
//        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, TLUrl.getInstance().URL_hwg_return_deliver+refund_id+ "&express_id="+express_id+ "&invoice_no="+edCode.getText().toString()+ "&key=" + MyApplication.getInstance().getMykey(), null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    if (response.getInt("code") == 200) {
//                        Log.i("zjz","response="+response);
//                        showToast(response.optString("datas"));
//                        if (response.optString("datas").contains("成功")) {
//                            showToast("提交成功");
//                            MyUpdateUI.sendUpdateCollection(RefundDetailActivity.this, MyUpdateUI.RETURN);
//                            finish();
//                        }
//                        ProgressDlgUtil.stopProgressDlg();
//                    } else {
//                        Log.i("zjz", "goodsActivity解析失败");
//                        ProgressDlgUtil.stopProgressDlg();
//                    }
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    Log.i("zjz", e.toString());
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });
//        mRequestQueue.add(jr);
    }

    private void initExpressID() {
        arrayList.add("0");
        arrayList.add("29");
        arrayList.add("40");
        arrayList.add("41");
        arrayList.add("44");
        arrayList.add("2");
        arrayList.add("4");
        arrayList.add("3");
        arrayList.add("7");
        arrayList.add("5");
        arrayList.add("6");
        arrayList.add("8");
        arrayList.add("9");
        arrayList.add("12");
        arrayList.add("11");
        arrayList.add("10");
        arrayList.add("15");
        arrayList.add("13");
        arrayList.add("14");
        arrayList.add("17");
        arrayList.add("16");
        arrayList.add("18");
        arrayList.add("20");
        arrayList.add("19");
        arrayList.add("21");
        arrayList.add("22");
        arrayList.add("24");
        arrayList.add("23");
        arrayList.add("27");
        arrayList.add("26");
        arrayList.add("25");
        arrayList.add("28");
        arrayList.add("30");
        arrayList.add("33");
        arrayList.add("32");
        arrayList.add("31");
        arrayList.add("35");
        arrayList.add("34");
        arrayList.add("39");
        arrayList.add("38");
        arrayList.add("37");
        arrayList.add("36");
        arrayList.add("42");
        arrayList.add("43");
        arrayList.add("45");
        arrayList.add("46");
        arrayList.add("47");
    }
}
