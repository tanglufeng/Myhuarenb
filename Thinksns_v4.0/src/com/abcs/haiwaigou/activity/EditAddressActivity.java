package com.abcs.haiwaigou.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Addres;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.sociax.android.R;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nineoldandroids.animation.ObjectAnimator;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EditAddressActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.tljr_txt_news_title)
    TextView tljrTxtNewsTitle;
    @InjectView(R.id.tljr_hqss_news_titlebelow)
    TextView tljrHqssNewsTitlebelow;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.tljr_grp_goods_title)
    RelativeLayout tljrGrpGoodsTitle;
    @InjectView(R.id.ed_name)
    EditText edName;
    @InjectView(R.id.ed_phone)
    EditText edPhone;
    @InjectView(R.id.t_province)
    TextView tProvince;
    @InjectView(R.id.zlist_province)
    ZrcListView zlistProvince;
    @InjectView(R.id.t_city)
    TextView tCity;
    @InjectView(R.id.zlist_city)
    ZrcListView zlistCity;
    @InjectView(R.id.t_area)
    TextView tArea;
    @InjectView(R.id.zlist_area)
    ZrcListView zlistArea;
    @InjectView(R.id.ed_detail)
    EditText edDetail;
    @InjectView(R.id.t_ok)
    TextView tOk;
    @InjectView(R.id.img_province)
    ImageView imgProvince;
    @InjectView(R.id.img_city)
    ImageView imgCity;
    @InjectView(R.id.img_area)
    ImageView imgArea;
    @InjectView(R.id.spinner_province)
    Spinner spinnerProvince;
    @InjectView(R.id.relative_province)
    RelativeLayout relativeProvince;
    @InjectView(R.id.relative_city)
    RelativeLayout relativeCity;
    @InjectView(R.id.relative_area)
    RelativeLayout relativeArea;
    @InjectView(R.id.btn_isdefault)
    ToggleButton btnIsdefault;
    @InjectView(R.id.ed_id_card)
    EditText edIdCard;
    private RequestQueue mRequestQueue;

    ArrayList<Addres> provinceList = new ArrayList<Addres>();
    ArrayList<Addres> cityList = new ArrayList<Addres>();
    ArrayList<Addres> areaList = new ArrayList<Addres>();
    ProvinceAdapter provinceAdapter;
    CityAdapter cityAdapter;
    AreaAdapter areaAdapter;
    public Handler handler = new Handler();
    private int durationRotate = 200;// 旋转动画时间
    private int durationAlpha = 200;// 透明度动画时间
    String name;
    String phone;
    String param;
    String detailAdd;
    String addInfo;
    String cityId;
    String areaId;
    String idCard;
    String telPhone = "";

    String title_name;
    boolean isEdit;
    boolean isDefault = false;
    String address_id;
    String isdefault = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_edit_address);
        ButterKnife.inject(this);
        mRequestQueue = Volley.newRequestQueue(this);
        title_name = (String) getIntent().getSerializableExtra("title");
        isEdit = (boolean) getIntent().getSerializableExtra("isEdit");
        address_id = (String) getIntent().getSerializableExtra("address_id");
        tljrTxtNewsTitle.setText(title_name);
        edDetail.setEnabled(false);
        if (isEdit) {
            initEdit();
            edDetail.setEnabled(true);
        } else {
            initProvince();
            relativeCity.setVisibility(View.GONE);
            relativeArea.setVisibility(View.GONE);
        }
        setOnListener();

    }

    ArrayAdapter<String> typeAdapter;
    ArrayList<String> provinceId = new ArrayList<>();

    private void initSpinner() {
//        typeAdapter  = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(R.layout.hwg_spinner_dropdown_item);
//        initProvince();

        if (spinnerProvince != null)
            spinnerProvince.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//               showToast(provinceId.get(position));
                    if (position == 0) {
                        showToast("请选择省份");
                    } else
                        showToast(provinceList.get(position - 1).getProvince_id());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
    }

    String[] strings;

    private void initEdit() {
        Log.i("zjz", "address_id=" + address_id);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_address_info, "&key=" + MyApplication.getInstance().getMykey() + "&address_id=" + address_id, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                // TODO Auto-generated method stub
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(msg);
                            if (json != null && json.has("code")) {
                                int code = json.getInt("code");
                                Log.i("zjz", "edit_msg=" + msg);
                                if (code == 200) {
                                    Log.i("zjz", "修改中");
                                    JSONObject object = json.getJSONObject("datas");

                                    JSONObject object1 = object.getJSONObject("address_info");
                                    Addres a = new Addres();
                                    a.setTrue_name(object1.optString("true_name"));
                                    a.setDetail_address(object1.optString("address"));
                                    a.setPhone(object1.optString("mob_phone"));
                                    a.setT_phone(object1.optString("tel_phone"));
                                    if (object1.optString("area_info").contains("\t")) {
                                        strings = object1.optString("area_info").split("\t");
                                    } else {
                                        strings = object1.optString("area_info").split(" ");
                                    }
                                    if (edName != null)
                                        edName.setText(object1.optString("true_name"));
                                    if (edPhone != null)
                                        edPhone.setText(object1.optString("mob_phone"));

                                    try {
                                        if(strings!=null&&strings.length>0){
                                            if(!TextUtils.isEmpty(strings[0])){
                                                if (tProvince != null)
                                                    tProvince.setText(strings[0]);
                                            }
                                            if(!TextUtils.isEmpty(strings[1])){
                                                if (tCity != null)
                                                    tCity.setText(strings[1]);
                                            }
                                            if(!TextUtils.isEmpty(strings[2])){
                                                if (tArea != null)
                                                    tArea.setText(strings[2]);
                                            }
                                        }
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }

                                    isDefault = object1.optString("is_default").equals("1") ? true : false;
                                    if (btnIsdefault != null)
                                        btnIsdefault.setChecked(isDefault);
                                    if (edDetail != null)
                                        edDetail.setText(object1.optString("address"));
                                    if (edIdCard != null)
                                        edIdCard.setText(object1.optString("id_card"));

                                    initProvince();
//                                    initArea(object1.optString("city_id"));
                                } else {
                                    Log.i("zjz", "goodsActivity解析失败");
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", "登录失败！");
                            Log.i("zjz", msg);
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void setOnListener() {
        tProvince.setOnClickListener(this);
        tCity.setOnClickListener(this);
        tArea.setOnClickListener(this);
        relativeBack.setOnClickListener(this);
        tOk.setOnClickListener(this);
        edDetail.setOnClickListener(this);
        btnIsdefault.setOnClickListener(this);
    }


    private void initProvince() {
        typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_province, "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                // TODO Auto-generated method stub
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(msg);
                            if (json != null && json.has("code")) {
                                int code = json.getInt("code");
                                Log.i("zjz", msg);
                                if (code == 200) {
                                    JSONObject object = json.getJSONObject("datas");
                                    JSONArray jsonArray = object.getJSONArray("area_list");
                                    provinceList.clear();
                                    typeAdapter.add("请选择省份");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object1 = jsonArray.getJSONObject(i);
                                        Addres a = new Addres();
                                        a.setProvince_name(object1.optString("area_name"));
                                        a.setProvince_id(object1.optString("area_id"));
                                        if (isEdit) {
                                            if (object1.optString("area_name").equals(strings[0])) {
                                                initCity(object1.optString("area_id"));

                                            }
                                        }
                                        provinceList.add(a);
                                        typeAdapter.add(a.getProvince_name());
                                        provinceId.add(a.getProvince_id());
                                    }
                                    provinceId.add(0, "0");
                                    Log.i("zjz", "provinceList=" + provinceList.size());
                                    if (spinnerProvince != null && typeAdapter != null)
                                        spinnerProvince.setAdapter(typeAdapter);
                                    provinceAdapter = new ProvinceAdapter(EditAddressActivity.this, provinceList, zlistProvince);
                                    if (zlistProvince != null) {
                                        zlistProvince.setAdapter(provinceAdapter);
                                        setListViewHeight(zlistProvince);
                                    }
                                    provinceAdapter.notifyDataSetChanged();

//                                    initSpinner();
                                } else {
                                    Log.i("zjz", "goodsActivity解析失败");
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", "登录失败！");
                            Log.i("zjz", msg);
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    class ProvinceAdapter extends BaseAdapter {
        private ArrayList<Addres> provinceList;
        Activity activity;
        LayoutInflater inflater = null;
        ZrcListView listView;
        //    MyListView listView;
        public Handler handler = new Handler();


        public ProvinceAdapter(Activity activity, ArrayList<Addres> provinceList,
                               ZrcListView listView) {
            // TODO Auto-generated constructor stub

            this.activity = activity;
            this.provinceList = provinceList;
            this.listView = listView;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            // TODO Auto-generated method stub
            ProvinceViewHolder mHolder = null;
            final Addres address = getItem(position);
            if (convertView == null) {
                LayoutInflater mInflater = LayoutInflater.from(activity);
                convertView = mInflater.inflate(R.layout.hwg_item_province, null);
                mHolder = new ProvinceViewHolder();
                mHolder.t_province = (TextView) convertView.findViewById(R.id.t_province);
                mHolder.linear_root = (LinearLayout) convertView.findViewById(R.id.linear_root);

                convertView.setTag(mHolder);

            } else {
                mHolder = (ProvinceViewHolder) convertView.getTag();

            }
            mHolder.t_province.setText(address.getProvince_name());
            mHolder.linear_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isProvince = false;
                    ObjectAnimator.ofFloat(imgProvince, "rotation", 180, 360).setDuration(durationRotate).start();
                    zlistProvince.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            zlistProvince.setVisibility(View.GONE);
                        }
                    }, durationAlpha);
                    tProvince.setText(address.getProvince_name());
                    relativeCity.setVisibility(View.VISIBLE);
                    tCity.setText("");
                    tArea.setText("");
                    edDetail.setText("");
                    edDetail.setEnabled(false);
                    initCity(address.getProvince_id());
                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return provinceList == null ? 0 : provinceList.size();
        }

        @Override
        public Addres getItem(int position) {
            if (provinceList != null && provinceList.size() != 0) {
                if (position >= provinceList.size()) {
                    return provinceList.get(0);
                }
                return provinceList.get(position);
            }
            return null;
        }


        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
    }

    class ProvinceViewHolder {
        TextView t_province;
        LinearLayout linear_root;
    }

    private void initCity(String areaID) {
        Log.i("zjz", "area_id=" + areaID);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_province, "&key=" + MyApplication.getInstance().getMykey() + "&area_id=" + areaID, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                // TODO Auto-generated method stub
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(msg);
                            if (json != null && json.has("code")) {
                                int code = json.getInt("code");
                                Log.i("zjz", msg);
                                if (code == 200) {
                                    JSONObject object = json.getJSONObject("datas");
                                    JSONArray jsonArray = object.getJSONArray("area_list");
                                    cityList.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object1 = jsonArray.getJSONObject(i);
                                        Addres a = new Addres();
                                        a.setCity_name(object1.optString("area_name"));
                                        a.setCity_id(object1.optString("area_id"));
                                        if (isEdit) {
                                            if (object1.optString("area_name").equals(strings[1])) {
                                                initArea(object1.optString("area_id"));
                                                cityId = object1.optString("area_id");
                                                Log.i("zjz", "edit_cityid=" + cityId);
                                            }
                                        }
                                        cityList.add(a);
                                    }

                                    Log.i("zjz", "cityList=" + cityList.size());
                                    cityAdapter = new CityAdapter(EditAddressActivity.this, cityList, zlistCity);
                                    if (zlistCity != null) {

                                        zlistCity.setAdapter(cityAdapter);
                                        setListViewHeight(zlistCity);
                                    }
                                    cityAdapter.notifyDataSetChanged();
                                } else {
                                    Log.i("zjz", "goodsActivity解析失败");
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", "登录失败！");
                            Log.i("zjz", msg);
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }

    class CityAdapter extends BaseAdapter {
        private ArrayList<Addres> cityList;
        Activity activity;
        LayoutInflater inflater = null;
        ZrcListView listView;
        //    MyListView listView;
        public Handler handler = new Handler();


        public CityAdapter(Activity activity, ArrayList<Addres> cityList,
                           ZrcListView listView) {
            // TODO Auto-generated constructor stub

            this.activity = activity;
            this.cityList = cityList;
            this.listView = listView;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            // TODO Auto-generated method stub
            CityViewHolder mHolder = null;
            final Addres address = getItem(position);
            if (convertView == null) {
                LayoutInflater mInflater = LayoutInflater.from(activity);
                convertView = mInflater.inflate(R.layout.hwg_item_province, null);
                mHolder = new CityViewHolder();
                mHolder.t_province = (TextView) convertView.findViewById(R.id.t_province);
                mHolder.linear_root = (LinearLayout) convertView.findViewById(R.id.linear_root);

                convertView.setTag(mHolder);

            } else {
                mHolder = (CityViewHolder) convertView.getTag();

            }
            mHolder.t_province.setText(address.getCity_name());
            mHolder.linear_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ObjectAnimator.ofFloat(imgCity, "rotation", 180, 360).setDuration(durationRotate).start();

                    isCity = false;
                    zlistCity.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            zlistCity.setVisibility(View.GONE);
                        }
                    }, durationAlpha);

                    tCity.setText(address.getCity_name());
                    relativeArea.setVisibility(View.VISIBLE);
                    tArea.setText("");
                    edDetail.setText("");
                    edDetail.setEnabled(false);
                    initArea(address.getCity_id());
                    cityId = address.getCity_id();
                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return cityList == null ? 0 : cityList.size();
        }

        @Override
        public Addres getItem(int position) {
            if (cityList != null && cityList.size() != 0) {
                if (position >= cityList.size()) {
                    return cityList.get(0);
                }
                return cityList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
    }

    class CityViewHolder {
        TextView t_province;
        LinearLayout linear_root;
    }


    private void initArea(String areaID) {
        Log.i("zjz", "area_id=" + areaID);

        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_province, "&key=" + MyApplication.getInstance().getMykey() + "&area_id=" + areaID, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                // TODO Auto-generated method stub
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(msg);
                            if (json != null && json.has("code")) {
                                int code = json.getInt("code");
                                Log.i("zjz", msg);
                                if (code == 200) {
                                    JSONObject object = json.getJSONObject("datas");
                                    JSONArray jsonArray = object.getJSONArray("area_list");
                                    areaList.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object1 = jsonArray.getJSONObject(i);
                                        Addres a = new Addres();
                                        a.setArea_name(object1.optString("area_name"));
                                        a.setArea_id(object1.optString("area_id"));
                                        if (isEdit) {
                                            if (object1.optString("area_name").equals(strings[2])) {
                                                areaId = object1.optString("area_id");
                                                Log.i("zjz", "edit_areaid=" + areaId);
                                            }
                                        }
                                        areaList.add(a);
                                    }
                                    Log.i("zjz", "areaList=" + areaList.size());
                                    areaAdapter = new AreaAdapter(EditAddressActivity.this, areaList, zlistArea);
                                    if (zlistArea != null) {
                                        zlistArea.setAdapter(areaAdapter);
                                        setListViewHeight(zlistArea);
                                    }
                                    areaAdapter.notifyDataSetChanged();
                                } else {
                                    Log.i("zjz", "goodsActivity解析失败");
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", "登录失败！");
                            Log.i("zjz", msg);
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    class AreaAdapter extends BaseAdapter {
        private ArrayList<Addres> areaList;
        Activity activity;
        LayoutInflater inflater = null;
        ZrcListView listView;
        //    MyListView listView;
        public Handler handler = new Handler();


        public AreaAdapter(Activity activity, ArrayList<Addres> areaList,
                           ZrcListView listView) {
            // TODO Auto-generated constructor stub

            this.activity = activity;
            this.areaList = areaList;
            this.listView = listView;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            // TODO Auto-generated method stub
            AreaViewHolder mHolder = null;
            final Addres address = getItem(position);
            if (convertView == null) {
                LayoutInflater mInflater = LayoutInflater.from(activity);
                convertView = mInflater.inflate(R.layout.hwg_item_province, null);
                mHolder = new AreaViewHolder();
                mHolder.t_province = (TextView) convertView.findViewById(R.id.t_province);
                mHolder.linear_root = (LinearLayout) convertView.findViewById(R.id.linear_root);

                convertView.setTag(mHolder);

            } else {
                mHolder = (AreaViewHolder) convertView.getTag();

            }
            mHolder.t_province.setText(address.getArea_name());
            mHolder.linear_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ObjectAnimator.ofFloat(imgArea, "rotation", 180, 360).setDuration(durationRotate).start();
                    isArea = false;
                    zlistArea.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            zlistArea.setVisibility(View.GONE);
                        }
                    }, durationAlpha);

                    tArea.setText(address.getArea_name());
                    edDetail.setText("");
                    areaId = address.getArea_id();
                    edDetail.setEnabled(true);
                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return areaList == null ? 0 : areaList.size();
        }

        @Override
        public Addres getItem(int position) {
            if (areaList != null && areaList.size() != 0) {
                if (position >= areaList.size()) {
                    return areaList.get(0);
                }
                return areaList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
    }

    class AreaViewHolder {
        TextView t_province;
        LinearLayout linear_root;
    }

    public void setListViewHeight(ZrcListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    boolean isProvince;
    boolean isCity;
    boolean isArea;

    @Override
    public void onClick(View v) {
        InputMethodManager imm;
        switch (v.getId()) {
            case R.id.relative_back:
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                finish();
                break;
            case R.id.t_province:
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                isCity = false;
                isArea = false;
                isProvince = !isProvince;
                if (isProvince) {
                    ObjectAnimator.ofFloat(imgProvince, "rotation", 0, 180).setDuration(durationRotate).start();
                    zlistProvince.setVisibility(View.VISIBLE);
                    ObjectAnimator.ofFloat(zlistProvince, "alpha", 0, 1).setDuration(durationAlpha).start();
                } else {
                    ObjectAnimator.ofFloat(imgProvince, "rotation", 180, 360).setDuration(durationRotate).start();

                    zlistProvince.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            zlistProvince.setVisibility(View.GONE);
                        }
                    }, durationAlpha);
                }
                zlistArea.setVisibility(View.GONE);
                zlistCity.setVisibility(View.GONE);
                break;
            case R.id.t_city:

                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if (tProvince.getText().equals("")) {
                    showToast("请先选择省份！");
                    return;
                }
                isProvince = false;
                isArea = false;
                isCity = !isCity;
                if (isCity) {
                    ObjectAnimator.ofFloat(imgCity, "rotation", 0, 180).setDuration(durationRotate).start();
                    zlistCity.setVisibility(View.VISIBLE);
                    ObjectAnimator.ofFloat(zlistCity, "alpha", 0, 1).setDuration(durationAlpha).start();
                } else {
                    ObjectAnimator.ofFloat(imgCity, "rotation", 180, 360).setDuration(durationRotate).start();
                    ObjectAnimator.ofFloat(zlistCity, "alpha", 1, 0).setDuration(durationAlpha).start();
                    zlistCity.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            zlistCity.setVisibility(View.GONE);
                        }
                    }, durationAlpha);
                }
                zlistProvince.setVisibility(View.GONE);
                zlistArea.setVisibility(View.GONE);
                break;
            case R.id.t_area:

                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if (tCity.getText().equals("")) {
                    showToast("请先选择城市！");
                    return;
                }
                isProvince = false;
                isCity = false;
                isArea = !isArea;
                if (isArea) {
                    ObjectAnimator.ofFloat(imgArea, "rotation", 0, 180).setDuration(durationRotate).start();
                    zlistArea.setVisibility(View.VISIBLE);
                    ObjectAnimator.ofFloat(zlistArea, "alpha", 0, 1).setDuration(durationAlpha).start();
                } else {
                    ObjectAnimator.ofFloat(imgArea, "rotation", 180, 360).setDuration(durationRotate).start();

                    zlistArea.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            zlistArea.setVisibility(View.GONE);
                        }
                    }, durationAlpha);
                }
                zlistProvince.setVisibility(View.GONE);
                zlistCity.setVisibility(View.GONE);
                break;
            case R.id.ed_detail:
                if (tCity.getText().equals("")) {
                    showToast("请先选择区县！");
                    return;
                }
                break;
            case R.id.t_ok:
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                comfirm();
                break;
            case R.id.btn_isdefault:
                isDefault = !isDefault;
                btnIsdefault.setChecked(isDefault);
                isdefault = isDefault ? "1" : "0";
                break;
        }
    }

    private void comfirm() {
//        try {
        String phone = edPhone.getText().toString().trim();
        String idcard = edIdCard.getText().toString().trim();
        if (edName.getText().toString().trim().equals("")) {
            showToast("收货人不能为空！");
        } else if (phone.length() == 0 || !isValidMobile(phone)) {
            showToast("请输入正确手机号码！");
        } else if (tProvince.getText().toString().trim().equals("")) {
            showToast("地址信息不完整！");
        } else if (tCity.getText().toString().trim().equals("")) {
            showToast("地址信息不完整！");
        } else if (tArea.getText().toString().trim().equals("")) {
            showToast("地址信息不完整！");
        } else if (edDetail.getText().toString().trim().equals("")) {
            showToast("地址信息不完整！");
        }
//            else if (idcard.length() == 0 || !Util.IDCardValidate(idcard)) {
//                showToast("请输入正确的身份证号码！");
//            }
        else {
            saveAddr();
        }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    private boolean isValidMobile(String mobiles) {
//        Pattern p = Pattern
//                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,3,5-9]))\\d{8}$");
        Pattern p = Pattern
                .compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }

    private void saveAddr() {

        name = edName.getText().toString();
        phone = edPhone.getText().toString();
        detailAdd = edDetail.getText().toString();
        addInfo = tProvince.getText().toString() + " " + tCity.getText().toString() + " " + tArea.getText().toString();
        idCard = edIdCard.getText().toString();
        Log.i("zjz", "addInfo=" + addInfo);
        Log.i("zjz", "cityId=" + cityId);
        Log.i("zjz", "areaId=" + areaId);
        Log.i("zjz", "idCard=" + idCard);


        param = "key=" + MyApplication.getInstance().getMykey() + "&true_name="
                + name + "&mob_phone=" + phone + "&city_id=" + cityId + "&area_id=" +
                areaId + "&address=" + detailAdd + "&area_info=" + addInfo + "&is_default=" + isdefault + "&id_card=";

        Log.i("zjz", "param=" + param);
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        if (isEdit) {
            HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_address_addedit + "&id=" + address_id, param + "&address_id=" + address_id, new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    // TODO Auto-generated method stub
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(msg);
                                if (json != null && json.has("code")) {
                                    int code = json.getInt("code");
                                    Log.i("zjz", "修改成功");
                                    Log.i("zjz", "address_edit" + msg);
                                    if (code == 200) {
                                        if (json.optString("datas").contains("成功")) {
                                            showToast("成功修改收货地址！");
                                            MyUpdateUI.sendUpdateCollection(EditAddressActivity.this, MyUpdateUI.ADDRESS);
                                            ProgressDlgUtil.stopProgressDlg();
                                            finish();
                                        } else {
                                            showToast("修改地址失败！");
                                            ProgressDlgUtil.stopProgressDlg();
                                        }
                                    } else {
                                        showToast("修改地址失败！");
                                        ProgressDlgUtil.stopProgressDlg();
                                    }
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Log.i("zjz", "登录失败！");
                                Log.i("zjz", msg);
                                e.printStackTrace();
                            }
                        }
                    });
                }


            });
        } else {
            HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_address_addedit, param, new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    // TODO Auto-generated method stub
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(msg);
                                if (json != null && json.has("code")) {
                                    int code = json.getInt("code");
                                    Log.i("zjz", "code=" + code);
                                    Log.i("zjz", "address_add" + msg);
                                    if (code == 200) {
//                                        JSONObject object = json.getJSONObject("datas");
                                        if (json.optString("datas").contains("成功")) {
                                            showToast("成功添加收货地址！");
                                            MyUpdateUI.sendUpdateCollection(EditAddressActivity.this, MyUpdateUI.ADDRESS);
                                            ProgressDlgUtil.stopProgressDlg();
                                            finish();
                                        } else {
                                            showToast("失败咯！");
                                            ProgressDlgUtil.stopProgressDlg();
                                        }
                                    } else {
                                        showToast("失败咯！");
                                        ProgressDlgUtil.stopProgressDlg();
                                    }
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Log.i("zjz", "登录失败！");
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
        ButterKnife.reset(this);
        super.onDestroy();
    }
}
