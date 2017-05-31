package com.abcs.haiwaigou.local.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.local.beans.City;
import com.abcs.haiwaigou.local.beans.NewHireFind;
import com.abcs.haiwaigou.model.ImageItem;
import com.abcs.haiwaigou.utils.PhotoDialog;
import com.abcs.haiwaigou.utils.PictureUtil;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.tljr.news.widget.InputTools;
import com.abcs.huaqiaobang.util.AvatarRev;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.util.capturePhoto.CapturePhoto;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.data.StaticInApp;
import com.abcs.sociax.t4.android.img.Bimp;
import com.abcs.sociax.t4.android.temp.SelectImageListener;
import com.abcs.sociax.t4.unit.UnitSociax;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.entity.InputStreamUploadEntity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class PublishMessageActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.t_title)
    TextView tTitle;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.linear_take_photo)
    LinearLayout linearTakePhoto;
    @InjectView(R.id.ed_title)
    EditText edTitle;
    @InjectView(R.id.ed_content)
    EditText edContent;
    @InjectView(R.id.ed_name)
    EditText edName;
    @InjectView(R.id.ed_phone)
    EditText edPhone;
    @InjectView(R.id.t_save)
    TextView tSave;
    @InjectView(R.id.t_publish)
    TextView tPublish;
    @InjectView(R.id.relative_pic)
    RelativeLayout relativePic;
    @InjectView(R.id.gv_preview)
    GridView gvPreview;
    @InjectView(R.id.imageHoriScroll)
    HorizontalScrollView imageHoriScroll;
    @InjectView(R.id.spinner_type)
    Spinner spinnerType;
    @InjectView(R.id.linear_type)
    LinearLayout linearType;
    @InjectView(R.id.spinner_select_city)  // 城市
    Spinner spinnerSelectCity;
    @InjectView(R.id.linear_select_city)
    LinearLayout linearSelectCity;

    //    private String countryId;
    private String cityId;
    private int position;
    private String menuId;
    private Handler handler = new Handler();
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    private View view;
    private CapturePhoto capture;
    protected GridAdapter adapter;   //图片显示适配器
    protected SelectImageListener listener_selectImage;   //拍照工具
    PhotoDialog photoDialog;
    private ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>();
    int max = 0;
    ArrayAdapter<String> typeAdapter;
    ArrayAdapter<String> cityAdapter;
    ArrayList<City> lists = new ArrayList<City>();
    ArrayList<NewHireFind.MsgBean> lists_city = new ArrayList<NewHireFind.MsgBean>();
    String listType;
    String listCity;
    String conId;
    int selected;
    private static final String PUBLISH = "1";
    private static final String SAVE = "0";
    boolean isFromSave;
    String[] goods_images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.local_activity_publish_message, null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiscCache();
        System.gc();
        initListener();
        initPicviews();
        Init();
        capture = new CapturePhoto(this);
//        countryId = getIntent().getStringExtra("countryId");
        isFromSave = getIntent().getBooleanExtra("isSave", false);
        if (!isFromSave) {
            position = getIntent().getIntExtra("position",0);
            cityId = getIntent().getStringExtra("cityId");
            menuId = getIntent().getStringExtra("menuId");
            tTitle.setText(getIntent().getStringExtra("title"));
            initListType();
            initListCity();
        } else {
            cityId = getIntent().getStringExtra("cityId");
            menuId = getIntent().getStringExtra("parentId");
            conId = getIntent().getStringExtra("conId");
            listType = getIntent().getStringExtra("listTypeId");
            tTitle.setText("");
            if (menuId != null)
                initListType();
                initListCity();
            if (conId != null)
                initPublishMsg();
        }

        if (listener_selectImage == null)
            listener_selectImage = new SelectImageListener(this);
        photoDialog = new PhotoDialog(this);

    }

    private void initPublishMsg() {
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_detail, "conId=" + conId, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject mainObj = new JSONObject(msg);
                            Log.i("zjz", "local_Detail_msg=" + msg);
                            if (mainObj.optString("status").equals("1")) {
                                JSONObject msgObj = mainObj.optJSONObject("msg");
                                goods_images = msgObj.optString("imgUrls").split(",");
                                if (edContent != null)
                                    edContent.setText(msgObj.optString("content"));
                                if (edName != null)
                                    edName.setText(msgObj.optString("contactMan"));
                                if (edPhone != null)
                                    edPhone.setText(msgObj.optString("contact"));
                                if (edTitle != null)
                                    edTitle.setText(msgObj.optString("title"));

                                if (goods_images != null && goods_images.length > 0) {
                                    for (int i = 0; i < goods_images.length; i++) {
                                        final ImageItem takePhoto = new ImageItem();
                                        final int finalI = i;
                                        Log.i("zjz", "url=" + Util.myTrim(goods_images[i]));
                                        Util.getLocalImage(Util.myTrim(goods_images[i]), new AvatarRev() {
                                            @Override
                                            public void revBtype(byte[] b, int bytelength) {
                                                Log.i("zjz", "b=" + b);
                                                if (b != null) {
                                                    final Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                                                    handler.post(new Runnable() {

                                                        @Override
                                                        public void run() {
                                                            // TODO Auto-generated method stub
                                                            takePhoto.setFile_url(Util.myTrim(goods_images[finalI]));
                                                            takePhoto.setBitmap(bitmap);
                                                            tempSelectBitmap.add(takePhoto);
                                                            imageHoriScroll.setVisibility(View.VISIBLE);
                                                            relativePic.setVisibility(View.GONE);
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            ProgressDlgUtil.stopProgressDlg();
                        }

                    }
                });
            }
        });
    }

    private void initListType() {
        typeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        lists.clear();
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_subTitle, "menuId=" + menuId, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject mainObj = new JSONObject(msg);
                            if (mainObj.optString("status").equals("1")) {
                                JSONArray msgArray = mainObj.optJSONArray("msg");
                                City v1 = new City();
                                v1.setName("请选择类型");
                                lists.add(v1);
                                typeAdapter.add(v1.getName());
                                if (msgArray.length() != 0) {
                                    for (int i = 0; i < msgArray.length(); i++) {
                                        JSONObject object = msgArray.getJSONObject(i);
                                        City list = new City();
                                        list.setName(object.optString("menuName"));
                                        list.setAreaId(object.optString("menuId"));
                                        if (listType != null && listType.equals(object.optString("menuId"))) {
                                            selected = i + 1;
                                        }
                                        lists.add(list);
                                        typeAdapter.add(list.getName());
                                    }
                                  /*  if (lists.size() > 2) {
                                        initSpinner();
                                    } else {
                                        listType = menuId;
                                    }*/

                                    initSpinner();

                                } else {
                                    listType = menuId;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    private void initListCity() {
        cityAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        lists_city.clear();
        //        https://japi.tuling.me/hrq/conListDetail/getConListByConType.json?cityId=6561&menuId=8010&version=v1.0
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_menuList, "cityId="+cityId+"&menuId="+menuId+"&version=v1.0", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        NewHireFind mBean=new Gson().fromJson(msg,NewHireFind.class);
                        if(mBean!=null){
                            if(mBean.status.equals("1")){
                                linearSelectCity.setVisibility(View.VISIBLE);
                                if(mBean.msg!=null&&mBean.msg.size()>0){
                                    NewHireFind.MsgBean v1 = new NewHireFind.MsgBean();
                                    v1.cateName="请选择城市";
                                    lists_city.add(v1);
                                    cityAdapter.add(v1.cateName);

                                    for (int i=0;i<mBean.msg.size();i++){
                                        NewHireFind.MsgBean list = new NewHireFind.MsgBean();
                                        list.cateName=mBean.msg.get(i).cateName;
                                        list.qyerCityId=mBean.msg.get(i).qyerCityId;
                                        lists_city.add(list);
                                        cityAdapter.add(list.cateName);
                                    }
                                    initCitySpinner();
                                }
                            }else {
                                linearSelectCity.setVisibility(View.GONE);
                            }
                        }
                    }
                });
            }
        });
    }

    private void initSpinner() {
        linearType.setVisibility(View.VISIBLE);
        typeAdapter.setDropDownViewResource(R.layout.hwg_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);
        if (isFromSave) {  // 我的保存过来的
            spinnerType.setSelection(selected);
        }else {
            try {
                spinnerType.setSelection(position+1);
                listType = lists.get(position+1).getAreaId();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        spinnerType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    listType = "0";
                } else {
                    listType = lists.get(position).getAreaId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*选择城市*/
    private void initCitySpinner() {
        cityAdapter.setDropDownViewResource(R.layout.hwg_spinner_dropdown_item);
        spinnerSelectCity.setAdapter(cityAdapter);
        try {
            spinnerSelectCity.setSelection(1);
            listCity = lists_city.get(position+1).qyerCityId+"";
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        spinnerSelectCity.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.i("zds", "onItemSelected: "+lists_city.get(position).qyerCityId);
                Log.i("zds", "onItemSelected: "+lists_city.get(position).cateName);

                if (position == 0) {

                } else {
                    listCity = lists_city.get(position).qyerCityId+"";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initListener() {
        relativeBack.setOnClickListener(this);
        tSave.setOnClickListener(this);
        tPublish.setOnClickListener(this);
        linearTakePhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_back:
                InputTools.HideKeyboard(v);
                finish();

                break;
            case R.id.t_save:
//                actCommit(SAVE);
                tSave.setEnabled(false);
                break;
            case R.id.t_publish:
                if(TextUtils.isEmpty(MyApplication.getInstance().getMykey())){
                    Intent ty=new Intent(PublishMessageActivity.this,WXEntryActivity.class);
                    ty.putExtra("isthome",true);
                    startActivity(ty);
                }else {
                    actCommit(PUBLISH);
                    tPublish.setEnabled(false);
                }
                break;
            case R.id.linear_take_photo:
                InputTools.HideKeyboard(v);
//                ll_popup.startAnimation(AnimationUtils.loadAnimation(this, R.anim.hwg_dialog_enter));
//                pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                photoDialog.show();
                break;
        }
    }

    private void actCommit(final String type) {
        String pics = null;
        if (tempSelectBitmap.size() != 0) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < tempSelectBitmap.size(); i++) {
              /*  if (i == 0) {
                    stringBuffer.append("[");
                }*/
                stringBuffer.append(tempSelectBitmap.get(i).getFile_url());
                if (i != tempSelectBitmap.size() - 1) {
                    stringBuffer.append(",");
                }
             /*   if (i == tempSelectBitmap.size() - 1) {
                    stringBuffer.append("]");
                }*/
            }
            pics = stringBuffer.toString();
            Log.i("zjz", "pics=" + pics);
        }

        String title = "";
        String content = "";
        String name = "";
        String phone = "";

        try {
            title = URLEncoder.encode(edTitle.getText().toString(), "utf-8");
            content = URLEncoder.encode(edContent.getText().toString(), "utf-8");
            name = URLEncoder.encode(edName.getText().toString(), "utf-8");
            phone = URLEncoder.encode(edPhone.getText().toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (modify()) {
            ProgressDlgUtil.showProgressDlg("Loading...", this);
            final String putURL = TLUrl.getInstance().URL_local_publish;
            final HttpUtils hu = new HttpUtils(20000);
            final RequestParams params = new RequestParams();
            params.addBodyParameter("cityId", listCity);
//            params.addBodyParameter("cityId", cityId);
            params.addBodyParameter("title", title);
            params.addBodyParameter("content", content);
            params.addBodyParameter("contactMan", name);
            params.addBodyParameter("contact", phone);
            params.addBodyParameter("listTypeId", listType);
            params.addBodyParameter("issueUser", MyApplication.getInstance().self.getId());
            params.addBodyParameter("imgUrls", pics);
            params.addBodyParameter("isIssue", type);
            if (isFromSave) {
                params.addBodyParameter("conId", conId);
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    hu.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, putURL, params,
                            new RequestCallBack<String>() {
                                @Override
                                public void onFailure(HttpException arg0, String arg1) {
                                    ProgressDlgUtil.stopProgressDlg();
                                    showToast("发布失败！请重试！");
                                }

                                @Override
                                public void onSuccess(ResponseInfo<String> arg0) {
                                    Log.i("zjz", "publish_msg=" + arg0.result);
                                    try {
                                        JSONObject mainObj = new JSONObject(arg0.result);
                                        if (mainObj.optString("status").equals("1")) {
                                            if (type.equals(SAVE)) {
                                                showToast("保存成功！");
                                            } else {
                                                showToast("发布成功！");
                                            }
                                            if (isFromSave) {
                                                MyUpdateUI.sendUpdateCollection(PublishMessageActivity.this, MyUpdateUI.LOCALMYPUBLISH);
                                            } else {
                                                MyUpdateUI.sendUpdateCollection(PublishMessageActivity.this, MyUpdateUI.LOCALFRAGMENT);
                                            }
                                            finish();
                                        } else {
                                            if (type.equals(SAVE)) {
                                                showToast("保存失败，请重试！");
                                            } else {
                                                showToast("发布失败，请重试！");
                                            }
                                        }
                                        ProgressDlgUtil.stopProgressDlg();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            });
        }
    }

    @Override
    protected void onRestart() {
        if (needPicture()) {
            adapter.update();
        }
        super.onRestart();
    }

    private boolean modify() {
        if (listType == null || listType.equals("0")) {
            showToast("请选择类型");
            return false;
        }
        if (TextUtils.isEmpty(edTitle.getText().toString())) {
            showToast("标题不能为空！");
            return false;
        }
        if (TextUtils.isEmpty(edContent.getText().toString())) {
            showToast("内容不能为空！");
            return false;
        }
//        if (TextUtils.isEmpty(edName.getText().toString())) {
//            showToast("联系人不能为空！");
//            return false;
//        }
        if (TextUtils.isEmpty(edPhone.getText().toString())) {
            showToast("联系方式不能为空！");
            return false;
        }
//        if (!Util.isValidMobile(edPhone.getText().toString())) {
//            showToast("请输入正确的手机号！");
//            return false;
//        }
        return true;
    }

    //初始化图片控件
    private void initPicviews() {
        adapter = new GridAdapter(this, gvPreview);
        adapter.update();
        gvPreview.setAdapter(adapter);
        gvPreview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
//                SociaxUIUtils.hideSoftKeyboard(ActivityCreateBase.this, et_content);
                InputTools.HideKeyboard(view);
                if (arg2 == tempSelectBitmap.size()) {
                    //选择图片
//                    SociaxUIUtils.hideSoftKeyboard(getApplicationContext(), et_content);
//                    showSelectImagePopUpWindow(view);
                    InputTools.HideKeyboard(view);
//                    ll_popup.startAnimation(AnimationUtils.loadAnimation(PublishMessageActivity.this, R.anim.hwg_dialog_enter));
//                    pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                    photoDialog.show();
                } else {
//                    Intent intent = new Intent(PublishMessageActivity.this, PhotoActivity.class);
//                    intent.putExtra("ID", arg2);
//                    //预览选择的照片
//                    startActivityForResult(intent, StaticInApp.UPLOAD_WEIBO);
                }
            }
        });

    }

    /**
     * 图片加载
     */
    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;
        private GridView mgridViwew;

        private int imgWidth = 0;   //图片显示宽高
        private int horizontalSpacing;  //图片左右间隙
        private int gridViewHeight;     //列表高度

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context, GridView gridView) {
            inflater = LayoutInflater.from(context);
            this.mgridViwew = gridView;
            imgWidth = UnitSociax.dip2px(context, 68);
            horizontalSpacing = UnitSociax.dip2px(context, 3);
            gridViewHeight = UnitSociax.dip2px(context, 70);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            int count = 0;
            if (tempSelectBitmap.size() == 0) {
                imageHoriScroll.setVisibility(View.GONE);
                relativePic.setVisibility(View.VISIBLE);
                count = 1;
            } else if (tempSelectBitmap.size() == 9) {
                imageHoriScroll.setVisibility(View.VISIBLE);
                relativePic.setVisibility(View.GONE);
                count = 9;
            } else {
                imageHoriScroll.setVisibility(View.VISIBLE);
                relativePic.setVisibility(View.GONE);
                count = tempSelectBitmap.size() + 1;
            }
            //计算列表的宽度
            int gridviewWidth = count * imgWidth + (count - 1) * horizontalSpacing;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    gridviewWidth, gridViewHeight);
            mgridViwew.setLayoutParams(params);         // 重点
            mgridViwew.setNumColumns(count);
            return count;
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.hwg_item_grida_image,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
                holder.delete = (ImageView) convertView.findViewById(R.id.img_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imgWidth, imgWidth);
            holder.image.setLayoutParams(params);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tempSelectBitmap.remove(position);
                    Log.i("zjz", "tempSize=" + tempSelectBitmap.size());
                    if (tempSelectBitmap.size() == 0) {
                        imageHoriScroll.setVisibility(View.GONE);
                        relativePic.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            if (position == tempSelectBitmap.size()) {
                holder.delete.setVisibility(View.GONE);
                holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_add_pic));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(tempSelectBitmap.get(position).getBitmap());
                holder.delete.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
            public ImageView delete;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (max == tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }

    }

    public void Init() {

        pop = new PopupWindow(this);

        View view = getLayoutInflater().inflate(R.layout.hwg_item_popwindow, null);

        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        pop.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.photostyle);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        LinearLayout line_images = (LinearLayout) view.findViewById(R.id.line_images);
        LinearLayout line_camera = (LinearLayout) view.findViewById(R.id.line_camera);
        LinearLayout line_cancel = (LinearLayout) view.findViewById(R.id.line_cancel);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

        line_images.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                selectPhoto();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        line_camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        line_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                camera();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

    }

    public void camera() {
        capture.dispatchTakePictureIntent(CapturePhoto.SHOT_IMAGE,
                1);
    }

    //选择相册
    private void selectPhoto() {
        Intent getImage = new Intent(this, MultiImageSelectorActivity.class);
        getImage.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        getImage.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
        getImage.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        getImage.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
                Bimp.address);
        startActivityForResult(getImage, StaticInApp.LOCAL_IMAGE);
    }

    private void photo() {
        listener_selectImage.cameraImage();
//        Intent intent = new Intent(this, TakePhotoActivity.class);
//        startActivityForResult(intent, 1);
    }

    private ArrayList<String> filePath = new ArrayList<String>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = null;
//        if (requestCode == 1 && resultCode == 1 && data != null) {
//            Log.i("zjz", "listPic=" + data.getStringArrayListExtra("listPath"));
//            filePath = data.getStringArrayListExtra("listPath");
//            intent = new Intent(this, PicturesActivity.class);
//            intent.putExtra("listPath", filePath);
//            startActivity(intent);
//            uploadFiles(true);
//        }
//        if (requestCode == StaticInApp.LOCAL_IMAGE && resultCode == RESULT_OK) {
//            List<String> photoList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//            filePath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//            boolean original = data.getBooleanExtra(MultiImageSelectorActivity.EXTRA_SELECT_ORIGIANL, false);
//            intent = new Intent(this, PicturesActivity.class);
//            intent.putExtra("listPath", filePath);
//            startActivity(intent);
////            uploadFiles(original);
//            Log.i("zjz", "photoList=" + photoList);
//        }

//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case StaticInApp.CAMERA_IMAGE:
//                    String path = listener_selectImage.getImagePath();
//                    if (path != null && Bimp.address.size() < 9) {
////                        uploadPictrue(path);
//                        save(path);
//                    }
//                    break;
//                case StaticInApp.LOCAL_IMAGE:
//                    List<String> photoList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//                    boolean original = data.getBooleanExtra(MultiImageSelectorActivity.EXTRA_SELECT_ORIGIANL, false);
//                    for (String addr : photoList) {
//                        if (Bimp.address.size() == 9)
//                            break;
//                        if (!Bimp.address.contains(addr)) {
//                            uploadPictrue(addr);
////                            Bimp.address.add(addr);
//                        }
//                    }
//                    break;
//            }
//        }

        if (requestCode == PhotoDialog.IMAGES && resultCode == RESULT_OK && data != null) {


            Bitmap bitmap = photoDialog.getBitmap(data.getData());
            Uri contentUri = data.getData();

            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            //这里需要不管是照相的图片，还是选择的图片，都需要统一使用file://的格式
            String filePath = "file://" + cursor.getString(column_index);
            photoDialog.setImageTempUri(Uri.parse(filePath));
//            Intent intent = photoDialog.actionCrop(data.getData());
//            //开启
//            startActivityForResult(intent, 3);
//            Log.i("zjz", "pic_images=" + photoDialog.getImageTempUri());
//            mCurrentPhotoPath=photoDialog.getImageTempUri().getPath();
            Log.i("zjz", "pic_images=" + photoDialog.getImageTempUri().getPath());
            save(cursor.getString(column_index));
//            save(photoDialog.getImageTempUri().getPath());
//            if (bitmap != null) {
//
//                uploadPictrue(filePath, bitmap);
//                File file=new File(filePath);
//                try {
//                    Log.i("zjz","image_size="+getFolderSize(file));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                photoDialog.cancel();
//            }

        }
        if (requestCode == PhotoDialog.CAMERA && resultCode == RESULT_OK) {
//            Intent intent = photoDialog.actionCrop(photoDialog.getImageTempUri());
            //开启
//            startActivityForResult(intent, 3);
            Bitmap bitmap = photoDialog.getBitmap(photoDialog.getImageTempUri());
//            mCurrentPhotoPath=photoDialog.getImageTempUri().getPath();
            Log.i("zjz", "pic_camera=" + photoDialog.getImageTempUri().getPath());
            save(photoDialog.getImageTempUri().getPath());
//            if (bitmap != null) {
//
//                uploadPictrue(photoDialog.getImageTempUri().getPath(), bitmap);
//                File file=new File(photoDialog.getImageTempUri().getPath());
//                try {
//                    Log.i("zjz","image_size="+getFolderSize(file));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                photoDialog.cancel();
//            }

        }

    }

//    private String mCurrentPhotoPath;


//    private void uploadFiles(boolean original) {
//        if (filePath.size() != 0) {
//            for (int i = 0; i < filePath.size(); i++) {
//                byte[] bs = null;
//                bs=capture.handleMediaPhoto(Uri.parse(filePath.get(i)));
//                if (bs != null && bs.length > 0) {
//                    try {
//                        uploadAvatar(bs);
//                    } catch (Exception e) {
//
//                    }
//                }
//                if (!filePath.get(i).equals("NOIMAGE")) {
//                    if (original) {
//                        uploadPictrue(filePath.get(i));
//                    } else {
//                        save(filePath.get(i));
//                    }
//                }
//
//            }
//        }
//    }

    private void save(String path) {
        if (path != null) {
//            PictureUtil.galleryAddPic(this, path);
            try {
                File f = new File(path);
                Bitmap bm = PictureUtil.getSmallBitmap(path);
                FileOutputStream fos = new FileOutputStream(new File(
                        PictureUtil.getAlbumDir(), "small_" + f.getName()));
                bm.compress(Bitmap.CompressFormat.JPEG, 40, fos);
                Log.i("zjz", "路径为：" + PictureUtil.getAlbumDir() + "/" + "small_" + f.getName());

                uploadPictrue(PictureUtil.getAlbumDir() + "/" + "small_" + f.getName(), bm);
//                uploadPictrue(PictureUtil.getAlbumDir() + "/" + "small_" + f.getName());
                photoDialog.cancel();
            } catch (Exception e) {
                Log.i("zjz", "error", e);
            }


        }
    }

    private void uploadAvatar(final byte[] bs) {
        HttpRequest.sendGet(TLUrl.getInstance().URL_getAvatar, "", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zjz", "upload_msg=" + msg);
                        if (msg.length() == 0) {
                            ProgressDlgUtil.stopProgressDlg();
                            showToast("上传头像失败");
                            return;
                        }
                        if (msg.equals("error")) {
                            ProgressDlgUtil.stopProgressDlg();
                            showToast("上传头像失败");
                        } else {
                            com.alibaba.fastjson.JSONObject obj = com.alibaba.fastjson.JSONObject.parseObject(msg);
                            final String putURL = obj.getString("puturl");
                            final String avatar = obj.getString("objname");

                            final HttpUtils hu = new HttpUtils(10000);
                            final RequestParams params = new RequestParams();
                            InputStream fStream = new ByteArrayInputStream(bs);
                            hu.configTimeout(20000);
                            params.addQueryStringParameter("Content-Length", bs.length
                                    + "");
                            params.setBodyEntity(new InputStreamUploadEntity(fStream,
                                    bs.length));
                            params.setContentType("image/jpeg");
                            hu.configResponseTextCharset("utf-8");
                            hu.configRequestRetryCount(5);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    hu.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.PUT, putURL, params,
                                            new RequestCallBack<String>() {
                                                @Override
                                                public void onFailure(
                                                        HttpException arg0, String arg1) {
                                                    ProgressDlgUtil.stopProgressDlg();
                                                    showToast("修改头像失败");
                                                }

                                                @Override
                                                public void onSuccess(ResponseInfo<String> arg0) {
                                                    String postUrl = TLUrl.getInstance().URL_user
                                                            + "changeavatar?iou=1";
                                                    String params = "token="
                                                            + Util.token + "&avatar="
                                                            + TLUrl.getInstance().URL_avatar + avatar;
                                                    Log.i("zjz", "成功！" + arg0);

                                                }
                                            });
                                }
                            });

                        }
                    }
                });
            }
        });
    }

    private void uploadPictrue(final String filePath, final Bitmap bitmap) {
        final String path = filePath.replace("file:///", "/");
        Log.i("zjz", "path=" + path);
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        final String putURL = TLUrl.getInstance().URL_local_upload_pic;
        final HttpUtils hu = new HttpUtils(20000);
        final RequestParams params = new RequestParams();
        params.addBodyParameter("file[]", new File(path));
        handler.post(new Runnable() {
            @Override
            public void run() {
                hu.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, putURL, params,
                        new RequestCallBack<String>() {
                            @Override
                            public void onFailure(HttpException arg0, String arg1) {
                                ProgressDlgUtil.stopProgressDlg();
                                showToast("上传图片失败");
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> arg0) {
                                Log.i("zjz", "result=" + arg0.result);
                                ImageItem takePhoto = new ImageItem();
                                takePhoto.setFile_url(arg0.result);
                                takePhoto.setBitmap(bitmap);
                                tempSelectBitmap.add(takePhoto);
                                Log.i("zjz", "imageList=" + tempSelectBitmap.size());
                                imageHoriScroll.setVisibility(View.VISIBLE);
                                relativePic.setVisibility(View.GONE);
                                ProgressDlgUtil.stopProgressDlg();
                                adapter.notifyDataSetChanged();
                            }
                        });
            }
        });

    }

    //是否需要拍照、选相册
    protected boolean needPicture() {
        return true;
    }
}
