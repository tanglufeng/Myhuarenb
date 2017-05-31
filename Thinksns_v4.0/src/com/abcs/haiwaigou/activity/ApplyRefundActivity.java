package com.abcs.haiwaigou.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.ImageItem;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.PhotoDialog;
import com.abcs.haiwaigou.utils.PictureUtil;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.activity.NoticeDialogActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ApplyRefundActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.tljr_txt_comment_title)
    TextView tljrTxtCommentTitle;
    @InjectView(R.id.tljr_hqss_news_titlebelow)
    TextView tljrHqssNewsTitlebelow;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.tljr_grp_goods_title)
    RelativeLayout tljrGrpGoodsTitle;
    @InjectView(R.id.img_goods)
    ImageView imgGoods;
    @InjectView(R.id.t_goods_name)
    TextView tGoodsName;
    @InjectView(R.id.t_goods_price)
    TextView tGoodsPrice;
    @InjectView(R.id.t_num)
    TextView tNum;
    @InjectView(R.id.t_need1)
    TextView tNeed1;
    @InjectView(R.id.t_text)
    TextView tText;
    @InjectView(R.id.spinner_type)
    Spinner spinnerType;
    @InjectView(R.id.relative_type)
    RelativeLayout relativeType;
    @InjectView(R.id.t_need2)
    TextView tNeed2;
    @InjectView(R.id.t_reason)
    TextView tReason;
    @InjectView(R.id.spinner_refund)
    Spinner spinnerRefund;
    @InjectView(R.id.relative_return)
    RelativeLayout relativeReturn;
    @InjectView(R.id.t_need_reason)
    TextView tNeedReason;
    @InjectView(R.id.t_reason2)
    TextView tReason2;
    @InjectView(R.id.spinner_reason)
    Spinner spinnerReason;
    @InjectView(R.id.relative_refund)
    RelativeLayout relativeRefund;
    @InjectView(R.id.t_default1)
    TextView tDefault1;
    @InjectView(R.id.relative_order_money)
    RelativeLayout relativeOrderMoney;
    @InjectView(R.id.t_need4)
    TextView tNeed4;
    @InjectView(R.id.t_default)
    TextView tDefault;
    @InjectView(R.id.ed_money)
    EditText edMoney;
    @InjectView(R.id.t_yuan)
    TextView tYuan;
    @InjectView(R.id.relative_money)
    RelativeLayout relativeMoney;
    @InjectView(R.id.t_need5)
    TextView tNeed5;
    @InjectView(R.id.t_default_num)
    TextView tDefaultNum;
    @InjectView(R.id.ed_num)
    EditText edNum;
    @InjectView(R.id.relative_num)
    RelativeLayout relativeNum;
    @InjectView(R.id.ed_comment)
    EditText edComment;
    @InjectView(R.id.gridview)
    GridView gridview;
    @InjectView(R.id.btn_commit)
    Button btnCommit;
    @InjectView(R.id.t_order_money)
    TextView tOrderMoney;
    @InjectView(R.id.t_total_money)
    TextView tTotalMoney;
    @InjectView(R.id.t_cancel_order)
    TextView tCancelOrder;

    boolean isOrder;
    String title;
    String order_money;
    String goods_name;
    String goods_price;
    String goods_img;
    String goods_num;
    String order_id;
    String goods_id;
    int num;
    int tempNum;
    double price;
    double tempPrice;

    public static final String REFUND = "1";
    public static final String RETURN = "2";
    public static final String CHECK = "777";
    public static final String NOINTIME = "99";
    public static final String FAKE = "98";
    public static final String BAOZHIQI = "97";
    public static final String POSUN = "96";
    public static final String NOGOOD = "95";
    public static final String OTHERS = "0";
    @InjectView(R.id.img_reason)
    ImageView imgReason;
    @InjectView(R.id.linear_goods)
    LinearLayout linearGoods;
    @InjectView(R.id.ed_mobile)
    EditText edMobile;
    private Handler handler = new Handler();
    String type;
    String reason_id;
    private GridAdapter adapter;
    public ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>();
    PhotoDialog photoDialog;
    int max = 0;

    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_apply_refund);
        ButterKnife.inject(this);
        photoDialog = new PhotoDialog(this);
        isOrder = getIntent().getBooleanExtra("isOrder", false);
        title = getIntent().getStringExtra("title");
        tljrTxtCommentTitle.setText(title);
        order_id = getIntent().getStringExtra("order_id");
        if (isOrder) {
            order_money = getIntent().getStringExtra("order_money");
            tOrderMoney.setText(order_money);
        } else {
            goods_id = getIntent().getStringExtra("goods_id");
            goods_name = getIntent().getStringExtra("goods_name");
            goods_price = getIntent().getStringExtra("goods_price");
            price = Double.parseDouble(goods_price);
            tempPrice = Double.parseDouble(goods_price);
            goods_img = getIntent().getStringExtra("goods_img");
            goods_num = getIntent().getStringExtra("goods_num");
            num = Integer.parseInt(goods_num);
            tempNum = Integer.parseInt(goods_num);
            edMoney.setText(price * num + "");
            edMoney.setSelection(edMoney.getText().length());
            edNum.setText(num + "");
        }
        initView();
        initSpinner();
        setOnListener();

        initGridView();
    }

    public void initGridView() {

        adapter = new GridAdapter(this);
        adapter.update();
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == tempSelectBitmap.size()) {
                    Log.i("ddddddd", "----------");
                    photoDialog.show();
                } else {

                }
            }
        });

    }

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if (tempSelectBitmap.size() == 3) {
                return 3;
            }
            return (tempSelectBitmap.size() + 1);
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

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tempSelectBitmap.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
            if (position == tempSelectBitmap.size()) {
                holder.delete.setVisibility(View.GONE);
                holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_add_pic));
                if (position == 3) {
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

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    private void initTextChange() {
        edMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    double temp = Double.valueOf(edMoney.getText().toString());
                    tempPrice = temp;
                    edMoney.setSelection(edMoney.getText().length());
                    if (temp > price * num) {
                        edMoney.setText(price * num + "");
                        edMoney.setSelection(edMoney.getText().length());
                    }
                } catch (Exception e) {
                    showToast("退款金额不能为空");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int temp = Integer.parseInt(edNum.getText().toString());
                    tempNum = temp;
                    edNum.setSelection(edNum.getText().length());
                    if (temp > num) {
                        edNum.setText(num + "");
                    }
                } catch (Exception e) {
                    showToast("退货数量不能为空");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initSpinner() {
        final ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        typeAdapter.add("仅退款");
        typeAdapter.add("退货退款");
        typeAdapter.setDropDownViewResource(R.layout.hwg_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);
        spinnerType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerType.getSelectedItem().toString().equals("仅退款")) {
                    type = REFUND;
                    relativeReturn.setVisibility(View.GONE);
                    relativeNum.setVisibility(View.GONE);
                    relativeRefund.setVisibility(View.VISIBLE);
                    tCancelOrder.setVisibility(View.GONE);
                    spinnerReason.setVisibility(View.VISIBLE);
                    relativeReturn.setVisibility(View.GONE);
                    edMoney.setText(price * num + "");
                    tTotalMoney.setText(price * num + "");
                } else if (spinnerType.getSelectedItem().toString().equals("退货退款")) {
                    type = RETURN;
                    relativeReturn.setVisibility(View.VISIBLE);
                    relativeNum.setVisibility(View.VISIBLE);
                    relativeRefund.setVisibility(View.GONE);
                    relativeReturn.setVisibility(View.VISIBLE);
                    edMoney.setText(price * num + "");
                    tTotalMoney.setText(price * num + "");
                    edNum.setText(num + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> refundAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        refundAdapter.add("请选择退货退款原因");
        refundAdapter.add("不能按时发货");
        refundAdapter.add("认为是假货");
        refundAdapter.add("保质期不符");
        refundAdapter.add("商品破损、有污渍");
        refundAdapter.add("效果不好不喜欢");
        refundAdapter.add("其他");
        refundAdapter.setDropDownViewResource(R.layout.hwg_spinner_dropdown_item);
        spinnerRefund.setAdapter(refundAdapter);
        spinnerRefund.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        reason_id = CHECK;
                        break;
                    case 1:
                        reason_id = NOINTIME;
                        break;
                    case 2:
                        reason_id = FAKE;
                        break;
                    case 3:
                        reason_id = BAOZHIQI;
                        break;
                    case 4:
                        reason_id = POSUN;
                        break;
                    case 5:
                        reason_id = NOGOOD;
                        break;
                    case 6:
                        reason_id = OTHERS;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> reasonAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        reasonAdapter.add("请选择退款原因");
        reasonAdapter.add("不能按时发货");
        reasonAdapter.add("认为是假货");
        reasonAdapter.add("保质期不符");
        reasonAdapter.add("商品破损、有污渍");
        reasonAdapter.add("效果不好不喜欢");
        reasonAdapter.add("其他");
        reasonAdapter.setDropDownViewResource(R.layout.hwg_spinner_dropdown_item);
        spinnerReason.setAdapter(reasonAdapter);
        spinnerReason.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        reason_id = CHECK;
                        break;
                    case 1:
                        reason_id = NOINTIME;
                        break;
                    case 2:
                        reason_id = FAKE;
                        break;
                    case 3:
                        reason_id = BAOZHIQI;
                        break;
                    case 4:
                        reason_id = POSUN;
                        break;
                    case 5:
                        reason_id = NOGOOD;
                        break;
                    case 6:
                        reason_id = OTHERS;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
    }

    private void initView() {
        if (isOrder) {
//            relativeGoods.setVisibility(View.GONE);
            linearGoods.setVisibility(View.GONE);
            relativeType.setVisibility(View.GONE);
            relativeReturn.setVisibility(View.GONE);
            relativeRefund.setVisibility(View.VISIBLE);
            tCancelOrder.setVisibility(View.VISIBLE);
            imgReason.setVisibility(View.GONE);
            spinnerReason.setVisibility(View.GONE);
            relativeNum.setVisibility(View.GONE);
            relativeMoney.setVisibility(View.GONE);
            relativeOrderMoney.setVisibility(View.VISIBLE);
        } else {
//            relativeGoods.setVisibility(View.VISIBLE);
            LoadPicture loadPicture = new LoadPicture();
            loadPicture.initPicture(imgGoods, goods_img);
            tGoodsName.setText(goods_name);
            tGoodsPrice.setText("¥" + goods_price);
            tNum.setText("X" + goods_num);

            imgReason.setVisibility(View.VISIBLE);
            relativeType.setVisibility(View.VISIBLE);
            relativeReturn.setVisibility(View.GONE);
            relativeRefund.setVisibility(View.VISIBLE);
            tCancelOrder.setVisibility(View.GONE);
            spinnerReason.setVisibility(View.VISIBLE);
            relativeNum.setVisibility(View.GONE);
            relativeMoney.setVisibility(View.VISIBLE);
            relativeOrderMoney.setVisibility(View.GONE);
        }
        initTextChange();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            mCurrentPhotoPath = cursor.getString(column_index);
//            mCurrentPhotoPath = photoDialog.getImageTempUri().getPath();
//            Intent intent = photoDialog.actionCrop(data.getData());
//            //开启
//            startActivityForResult(intent, 3);
            Log.i("zjz", "pic_images=" + filePath);
            if (bitmap != null) {
//                ImageItem takePhoto = new ImageItem();
//                takePhoto.setBitmap(bitmap);
//                tempSelectBitmap.add(takePhoto);
//                takePhoto.setFile(Uri.parse(filePath));
//                takePhoto.setFile_url(filePath);
                save();
//                uploadPictrue(filePath, bitmap);
                photoDialog.cancel();
            }

        }
        if (requestCode == PhotoDialog.CAMERA && resultCode == RESULT_OK) {
//            Intent intent = photoDialog.actionCrop(photoDialog.getImageTempUri());
            //开启
//            startActivityForResult(intent, 3);
            Bitmap bitmap = photoDialog.getBitmap(photoDialog.getImageTempUri());
            Log.i("zjz", "pic_camera=" + photoDialog.getImageTempUri().getPath());
            mCurrentPhotoPath = photoDialog.getImageTempUri().getPath();
            if (bitmap != null) {
//                ImageItem takePhoto = new ImageItem();
//                takePhoto.setBitmap(bitmap);
//                tempSelectBitmap.add(takePhoto);
//                takePhoto.setFile_url(photoDialog.getImageTempUri().getPath());
//                takePhoto.setFile(photoDialog.getImageTempUri());
                save();

//                uploadPictrue(photoDialog.getImageTempUri().getPath(),bitmap);
                photoDialog.cancel();
            }

        }
        if (requestCode == 3 && resultCode == RESULT_OK) {
            Bitmap bitmap = photoDialog.getBitmap();
            if (bitmap != null) {
                photoDialog.cancel();
            }
        }
    }


    private void save() {
        if (mCurrentPhotoPath != null) {
//            PictureUtil.galleryAddPic(this, mCurrentPhotoPath);
            try {
                File f = new File(mCurrentPhotoPath);
                Bitmap bm = PictureUtil.getSmallBitmap(mCurrentPhotoPath);
                FileOutputStream fos = new FileOutputStream(new File(
                        PictureUtil.getAlbumDir(), "small_" + f.getName()));
                bm.compress(Bitmap.CompressFormat.JPEG, 40, fos);
                Log.i("zjz", "路径为：" + PictureUtil.getAlbumDir() + "/" + "small_" + f.getName());
                if (bm != null) {

                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    tempSelectBitmap.add(takePhoto);
                    takePhoto.setFile(Uri.parse(PictureUtil.getAlbumDir() + "/" + "small_" + f.getName()));
                    takePhoto.setFile_url(PictureUtil.getAlbumDir() + "/" + "small_" + f.getName());
                    photoDialog.cancel();
                }
            } catch (Exception e) {
                Log.i("zjz", "error", e);
            }


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.btn_commit:
//                commit();
                commitAndPic();
                break;
        }
    }

    private void commitAndPic() {
        File refund_pic1 = null;
        File refund_pic2 = null;
        File refund_pic3 = null;
        String temp1 = null;
        String temp2 = null;
        String temp3 = null;
        String path1 = "";
        String path2 = "";
        String path3 = "";
        final RequestParams params = new RequestParams();
        switch (tempSelectBitmap.size()) {
            case 0:
                params.addBodyParameter("refund_pic1", "");
                params.addBodyParameter("refund_pic2", "");
                params.addBodyParameter("refund_pic3", "");
                break;
            case 1:
                temp1 = tempSelectBitmap.get(0).getFile_url();
                path1 = temp1.replace("file:///", "/");
                refund_pic1 = new File(path1);
                Log.i("zjz", "refund_pic1=" + refund_pic1);
                params.addBodyParameter("refund_pic1", new File(path1));
                params.addBodyParameter("refund_pic2", "");
                params.addBodyParameter("refund_pic3", "");
                break;
            case 2:

                temp1 = tempSelectBitmap.get(0).getFile_url();
                temp2 = tempSelectBitmap.get(1).getFile_url();
                path1 = temp1.replace("file:///", "/");
                path2 = temp2.replace("file:///", "/");
                refund_pic1 = new File(path1);
                refund_pic2 = new File(path2);
                params.addBodyParameter("refund_pic1", new File(path1));
                params.addBodyParameter("refund_pic2", new File(path2));
                params.addBodyParameter("refund_pic3", "");
                Log.i("zjz", "refund_pic1=" + refund_pic1);
                Log.i("zjz", "refund_pic2=" + refund_pic2);
                break;
            case 3:
                temp1 = tempSelectBitmap.get(0).getFile_url();
                temp2 = tempSelectBitmap.get(1).getFile_url();
                temp3 = tempSelectBitmap.get(2).getFile_url();
                path1 = temp1.replace("file:///", "/");
                path2 = temp2.replace("file:///", "/");
                path3 = temp3.replace("file:///", "/");
                refund_pic1 = new File(path1);
                refund_pic2 = new File(path2);
                refund_pic3 = new File(path3);
                params.addBodyParameter("refund_pic1", new File(path1));
                params.addBodyParameter("refund_pic2", new File(path2));
                params.addBodyParameter("refund_pic3", new File(path3));
                Log.i("zjz", "refund_pic1=" + refund_pic1);
                Log.i("zjz", "refund_pic2=" + refund_pic2);
                Log.i("zjz", "refund_pic3=" + refund_pic3);
                break;
        }
        String phone = edMobile.getText().toString().trim();
        if (phone.length() == 0 || !isValidMobile(phone)) {
            showToast("请输入正确的手机号码");
            return;
        }
        if (edComment.getText().toString().length() == 0) {
            showToast("退款说明不能为空！");
            return;
        }
        String url = null;
        String param = null;
        final HttpUtils hu = new HttpUtils(20000);

        if (isOrder) {
            url = TLUrl.getInstance().URL_hwg_refund + "&order_id=" + order_id;
            param = "&key=" + MyApplication.getInstance().getMykey() + "&refund_pic1=" + refund_pic1 + "&refund_pic2=" + refund_pic2 + "&refund_pic3=" + refund_pic3 + "&buyer_message=" + edComment.getText().toString();
            params.addBodyParameter("key", MyApplication.getInstance().getMykey());
//            params.addBodyParameter("refund_pic1", new File(path1));
//            params.addBodyParameter("refund_pic2", new File(path2));
//            params.addBodyParameter("refund_pic3", new File(path3));
            params.addBodyParameter("buyer_message", edComment.getText().toString());
            params.addBodyParameter("mobile_phone", edMobile.getText().toString());
        } else {
            if (type.equals(REFUND)) {
                if (reason_id.equals(CHECK)) {
                    showToast("请选择退款原因！");
                    return;
                } else if (edMoney.getText().toString().length() == 0) {
                    showToast("退款金额不能为空！");
                    return;
                }
                url = TLUrl.getInstance().URL_hwg_goods_refund + "&order_id=" + order_id + "&goods_id=" + goods_id;
                param = "&key=" + MyApplication.getInstance().getMykey() + "&refund_pic1=" + refund_pic1 + "&refund_pic2=" + refund_pic2 + "&refund_pic3=" + refund_pic3 +
                        "&goods_num=" + num + "&refund_amount=" + tempPrice + "&reason_id=" + reason_id + "&refund_type=1" + "&buyer_message=" + edComment.getText().toString();
                params.addBodyParameter("key", MyApplication.getInstance().getMykey());
//                params.addBodyParameter("refund_pic1", new File(path1));
//                params.addBodyParameter("refund_pic2", new File(path2));
//                params.addBodyParameter("refund_pic3", new File(path3));
                params.addBodyParameter("goods_num", num + "");
                params.addBodyParameter("refund_amount", tempPrice + "");
                params.addBodyParameter("reason_id", reason_id);
                params.addBodyParameter("refund_type", "1");
                params.addBodyParameter("buyer_message", edComment.getText().toString());
                params.addBodyParameter("mobile_phone", edMobile.getText().toString());
            } else {
                if (reason_id.equals(CHECK)) {
                    showToast("请选择退货退款原因！");
                    return;
                } else if (edMoney.getText().toString().length() == 0) {
                    showToast("退款金额不能为空！");
                    return;
                } else if (edNum.getText().toString().length() == 0) {
                    showToast("退款数量不能为空！");
                    return;
                }
                url = TLUrl.getInstance().URL_hwg_goods_refund + "&order_id=" + order_id + "&goods_id=" + goods_id;
                param = "&key=" + MyApplication.getInstance().getMykey() + "&refund_pic1=" + refund_pic1 + "&refund_pic2=" + refund_pic2 + "&refund_pic3=" + refund_pic3 +
                        "&goods_num=" + tempNum + "&refund_amount=" + tempPrice + "&reason_id=" + reason_id + "&refund_type=2" + "&buyer_message=" + edComment.getText().toString();
                params.addBodyParameter("key", MyApplication.getInstance().getMykey());
//                params.addBodyParameter("refund_pic1", new File(path1));
//                params.addBodyParameter("refund_pic2", new File(path2));
//                params.addBodyParameter("refund_pic3", new File(path3));
                params.addBodyParameter("goods_num", tempNum + "");
                params.addBodyParameter("refund_amount", tempPrice + "");
                params.addBodyParameter("reason_id", reason_id);
                params.addBodyParameter("refund_type", "2");
                params.addBodyParameter("buyer_message", edComment.getText().toString());
                params.addBodyParameter("mobile_phone", edMobile.getText().toString());
            }
        }
        ProgressDlgUtil.showProgressDlg("上传中...请稍等...", this);
        final String finalUrl = url;
        handler.post(new Runnable() {
            @Override
            public void run() {
                hu.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, finalUrl, params,
                        new RequestCallBack<String>() {
                            @Override
                            public void onFailure(HttpException arg0, String arg1) {
                                ProgressDlgUtil.stopProgressDlg();
                                Log.i("zjz", "failed_msg=" + arg0);
                                Log.i("zjz", "failed_msg_str=" + arg1);
                                MyUpdateUI.sendUpdateCollection(ApplyRefundActivity.this, MyUpdateUI.ORDER);
                                MyUpdateUI.sendUpdateCollection(ApplyRefundActivity.this, MyUpdateUI.MYORDERNUM);
                                showToast("出现错误，请重新申请！");
                                finish();
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> arg0) {
                                try {
                                    Log.i("zjz", "result=" + arg0.result);
                                    JSONObject jsonObject = new JSONObject(arg0.result);
                                    Log.i("zjz", "json=" + jsonObject);
                                    if (jsonObject.getInt("code") == 200) {
//                                        showToast(jsonObject.optString("datas"));
                                        if (jsonObject.optString("datas").contains("成功")) {
//                                            showToast("提交成功");
                                            startActivity(new Intent(ApplyRefundActivity.this, NoticeDialogActivity.class).putExtra("msg", "提交成功，客服将会在2-3个工作日内为您处理退款，可在我的卡包—预存款中查看记录详情。"));
                                            MyUpdateUI.sendUpdateCollection(ApplyRefundActivity.this, MyUpdateUI.ORDER);
                                            MyUpdateUI.sendUpdateCollection(ApplyRefundActivity.this, MyUpdateUI.MYORDERNUM);
                                            finish();
                                        }
                                        ProgressDlgUtil.stopProgressDlg();
                                    } else {
                                        ProgressDlgUtil.stopProgressDlg();
                                    }
                                    adapter.notifyDataSetChanged();
                                    ProgressDlgUtil.stopProgressDlg();
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
    }

    private boolean isValidMobile(String mobiles) {
//        Pattern p = Pattern
//                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,3,5-9]))\\d{8}$");
        Pattern p = Pattern
                .compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }
    private void commit() {
        File refund_pic1 = null;
        File refund_pic2 = null;
        File refund_pic3 = null;
        String temp1 = null;
        String temp2 = null;
        String temp3 = null;
        String path1 = null;
        String path2 = null;
        String path3 = null;
        switch (tempSelectBitmap.size()) {
            case 0:
                Log.i("zjz", "refund_pic1=" + refund_pic1);
                Log.i("zjz", "refund_pic2=" + refund_pic2);
                Log.i("zjz", "refund_pic3=" + refund_pic3);
                break;
            case 1:
                temp1 = tempSelectBitmap.get(0).getFile_url();
                path1 = temp1.replace("file:///", "/");
                refund_pic1 = new File(path1);
                Log.i("zjz", "refund_pic1=" + refund_pic1);
                break;
            case 2:

                temp1 = tempSelectBitmap.get(0).getFile_url();
                temp2 = tempSelectBitmap.get(1).getFile_url();
                path1 = temp1.replace("file:///", "/");
                path2 = temp2.replace("file:///", "/");
                refund_pic1 = new File(path1);
                refund_pic2 = new File(path2);
                Log.i("zjz", "refund_pic1=" + refund_pic1);
                Log.i("zjz", "refund_pic2=" + refund_pic2);
                break;
            case 3:
                temp1 = tempSelectBitmap.get(0).getFile_url();
                temp2 = tempSelectBitmap.get(1).getFile_url();
                temp3 = tempSelectBitmap.get(2).getFile_url();
                path1 = temp1.replace("file:///", "/");
                path2 = temp2.replace("file:///", "/");
                path3 = temp3.replace("file:///", "/");
                refund_pic1 = new File(path1);
                refund_pic2 = new File(path2);
                refund_pic3 = new File(path3);
                Log.i("zjz", "refund_pic1=" + refund_pic1);
                Log.i("zjz", "refund_pic2=" + refund_pic2);
                Log.i("zjz", "refund_pic3=" + refund_pic3);
                break;
        }
        if (edComment.getText().toString().length() == 0) {
            showToast("退款说明不能为空！");
            return;
        }
        String url = null;
        String param = null;
        if (isOrder) {
            url = TLUrl.getInstance().URL_hwg_refund + "&order_id=" + order_id;
            param = "&key=" + MyApplication.getInstance().getMykey() + "&refund_pic1=" + refund_pic1 + "&refund_pic2=" + refund_pic2 + "&refund_pic3=" + refund_pic3 + "&buyer_message=" + edComment.getText().toString();
        } else {
            if (type.equals(REFUND)) {
                if (reason_id.equals(CHECK)) {
                    showToast("请选择退款原因！");
                    return;
                } else if (edMoney.getText().toString().length() == 0) {
                    showToast("退款金额不能为空！");
                    return;
                }
                url = TLUrl.getInstance().URL_hwg_goods_refund + "&order_id=" + order_id + "&goods_id=" + goods_id;
                param = "&key=" + MyApplication.getInstance().getMykey() + "&refund_pic1=" + refund_pic1 + "&refund_pic2=" + refund_pic2 + "&refund_pic3=" + refund_pic3 +
                        "&goods_num=" + num + "&refund_amount=" + tempPrice + "&reason_id=" + reason_id + "&refund_type=1" + "&buyer_message=" + edComment.getText().toString();
            } else {
                if (reason_id.equals(CHECK)) {
                    showToast("请选择退货退款原因！");
                    return;
                } else if (edMoney.getText().toString().length() == 0) {
                    showToast("退款金额不能为空！");
                    return;
                } else if (edNum.getText().toString().length() == 0) {
                    showToast("退款数量不能为空！");
                    return;
                }
                url = TLUrl.getInstance().URL_hwg_goods_refund + "&order_id=" + order_id + "&goods_id=" + goods_id;
                param = "&key=" + MyApplication.getInstance().getMykey() + "&refund_pic1=" + refund_pic1 + "&refund_pic2=" + refund_pic2 + "&refund_pic3=" + refund_pic3 +
                        "&goods_num=" + tempNum + "&refund_amount=" + tempPrice + "&reason_id=" + reason_id + "&refund_type=2" + "&buyer_message=" + edComment.getText().toString();
            }
        }

        ProgressDlgUtil.showProgressDlg("Loading...", this);
        HttpRequest.sendPost(url, param, new HttpRevMsg() {
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
                                    MyUpdateUI.sendUpdateCollection(ApplyRefundActivity.this, MyUpdateUI.ORDER);
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
    }
}
