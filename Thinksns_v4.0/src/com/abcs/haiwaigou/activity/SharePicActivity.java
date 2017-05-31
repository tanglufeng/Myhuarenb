package com.abcs.haiwaigou.activity;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.ImageItem;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.PhotoDialog;
import com.abcs.haiwaigou.utils.PictureUtil;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.capturePhoto.CapturePhoto;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SharePicActivity extends BaseActivity implements View.OnClickListener {
    @InjectView(R.id.tljr_txt_news_title)
    TextView tljrTxtNewsTitle;
    @InjectView(R.id.tljr_hqss_news_titlebelow)
    TextView tljrHqssNewsTitlebelow;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.tljr_grp_goods_title)
    RelativeLayout tljrGrpGoodsTitle;
    @InjectView(R.id.img_goods_icon)
    ImageView imgGoodsIcon;
    @InjectView(R.id.relative_goods_icon)
    RelativeLayout relativeGoodsIcon;
    @InjectView(R.id.t_goods_name)
    TextView tGoodsName;
    @InjectView(R.id.ratingBar)
    RatingBar ratingBar;
    @InjectView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @InjectView(R.id.t_comment_info)
    TextView tCommentInfo;
    @InjectView(R.id.linear_pingjia)
    LinearLayout linearPingjia;
    @InjectView(R.id.gridview)
    GridView gridview;
    @InjectView(R.id.btn_commint)
    Button btnCommint;
    //    private GridView noScrollgridview;
    private GridAdapter adapter;
    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap;
    private CapturePhoto capture;

    String geval_id;
    String geval_goodsname;
    String geval_goodsimg;
    String geval_comment;
    String ac_id;
    int geval_scores;


    PhotoDialog photoDialog;
    int max = 0;
    public ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>();
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.img_add_pic);
        parentView = getLayoutInflater().inflate(R.layout.hwg_activity_share_pic, null);
        setContentView(parentView);
        ButterKnife.inject(this);
        mRequestQueue = Volley.newRequestQueue(this);
        capture = new CapturePhoto(this);
        photoDialog = new PhotoDialog(this);
        setOnListener();
        initView();
        initAcid();
        Init();
    }

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
        btnCommint.setOnClickListener(this);
    }

    private void initAcid() {
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, TLUrl.getInstance().URL_hwg_comment_share_step1 + "&geval_id=" + geval_id + "&key=" + MyApplication.getInstance().getMykey(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "share:连接成功");
                        Log.i("zjz", "response=" + response);
                        JSONObject object = response.getJSONObject("datas");
                        ac_id = object.optString("ac_id");
                    } else {
                        Log.i("zjz", "share:解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mRequestQueue.add(jr);
    }

    private void initView() {
        geval_id = getIntent().getStringExtra("geval_id");
        Log.i("zjz", "geval_id=" + geval_id);
        geval_goodsname = getIntent().getStringExtra("geval_goodsname");
        geval_goodsimg = getIntent().getStringExtra("geval_goodsimg");
        geval_comment = getIntent().getStringExtra("geval_comment");
        geval_scores = getIntent().getIntExtra("geval_scores", 0);
        LoadPicture loadPicture = new LoadPicture();
        loadPicture.initPicture(imgGoodsIcon, geval_goodsimg);
        tGoodsName.setText(geval_goodsname);
        ratingBar.setRating(geval_scores);
        tCommentInfo.setText(geval_comment);
        btnCommint.setVisibility(tempSelectBitmap.size() == 0 ? View.GONE : View.VISIBLE);

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
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        line_camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                camera();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        line_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

//        gridview = (GridView) findViewById(R.id.noScrollgridview);
//        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == tempSelectBitmap.size()) {
                    Log.i("ddddddd", "----------");
//                    ll_popup.startAnimation(AnimationUtils.loadAnimation(SharePicActivity.this, R.anim.hwg_dialog_enter));
                    photoDialog.show();
//                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
//                    Intent intent = new Intent(this, GalleryActivity.class);
//                    intent.putExtra("position", "1");
//                    intent.putExtra("ID", arg2);
//                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.btn_commint:
                commitPicture();
                break;
        }
    }

    private void commitPicture() {
        String param = null;
        if (tempSelectBitmap.size() == 0) {
            return;
        } else {
            switch (tempSelectBitmap.size()) {
                case 1:
                    param = "&evaluate_image[0]=" + tempSelectBitmap.get(0).getFile_name();
                    break;
                case 2:
                    param = "&evaluate_image[0]=" + tempSelectBitmap.get(0).getFile_name() + "&evaluate_image[1]=" + tempSelectBitmap.get(1).getFile_name();
                    break;
                case 3:
                    param = "&evaluate_image[0]=" + tempSelectBitmap.get(0).getFile_name() + "&evaluate_image[1]=" + tempSelectBitmap.get(1).getFile_name()
                            + "&evaluate_image[2]=" + tempSelectBitmap.get(2).getFile_name();
                    break;
                case 4:
                    param = "&evaluate_image[0]=" + tempSelectBitmap.get(0).getFile_name() + "&evaluate_image[1]=" + tempSelectBitmap.get(1).getFile_name()
                            + "&evaluate_image[2]=" + tempSelectBitmap.get(2).getFile_name() + "&evaluate_image[3]=" + tempSelectBitmap.get(3).getFile_name();
                    break;
                case 5:
                    param = "&evaluate_image[0]=" + tempSelectBitmap.get(0).getFile_name() + "&evaluate_image[1]=" + tempSelectBitmap.get(1).getFile_name()
                            + "&evaluate_image[2]=" + tempSelectBitmap.get(2).getFile_name() + "&evaluate_image[3]=" + tempSelectBitmap.get(3).getFile_name()
                            + "&evaluate_image[4]=" + tempSelectBitmap.get(4).getFile_name();
                    break;
            }
        }
        Log.i("zjz", "param=" + param);
        ProgressDlgUtil.showProgressDlg("正在上传中...", this);
        com.abcs.huaqiaobang.util.HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_comment_share_step3, "&geval_id=" + geval_id + param + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                if (object.optString("datas").contains("成功")) {
                                    showToast("晒单成功！");
                                    MyUpdateUI.sendUpdateCollection(SharePicActivity.this, MyUpdateUI.COMMENT);
                                    finish();
                                }
                            } else {
                                Log.i("zjz", "share:解析失败");
                            }
                            ProgressDlgUtil.stopProgressDlg();
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
            if (tempSelectBitmap.size() == 5) {
                return 5;
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
                    JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_comment_share_delete + "&id=" + tempSelectBitmap.get(position).getFile_id() + "&key=" + MyApplication.getInstance().getMykey(), null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.i("zjz", "response=" + response);
                                if (response.getInt("code") == 200) {
                                } else {
                                    Log.i("zjz", "share:解析失败");
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Log.i("zjz", e.toString());
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
                    mRequestQueue.add(jr);
                    tempSelectBitmap.remove(position);
                    btnCommint.setVisibility(tempSelectBitmap.size() == 0 ? View.GONE : View.VISIBLE);
                    Log.i("zjz", "tempSize=" + tempSelectBitmap.size());
                    adapter.notifyDataSetChanged();
                }
            });
            if (position == tempSelectBitmap.size()) {
                holder.delete.setVisibility(View.GONE);
                holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_add_pic));
                if (position == 5) {
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

    public void photo() {
        capture.dispatchTakePictureIntent(CapturePhoto.PICK_IMAGE,
                0);

    }

    public void camera() {
        capture.dispatchTakePictureIntent(CapturePhoto.SHOT_IMAGE,
                1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        byte[] bs = null;
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
//            Intent intent = photoDialog.actionCrop(data.getData());
//            //开启
//            startActivityForResult(intent, 3);
//            Log.i("zjz", "pic_images=" + photoDialog.getImageTempUri());
            mCurrentPhotoPath=cursor.getString(column_index);
//            mCurrentPhotoPath=photoDialog.getImageTempUri().getPath();
            save();
            Log.i("zjz", "pic_images=" + mCurrentPhotoPath);
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
            mCurrentPhotoPath=photoDialog.getImageTempUri().getPath();
            save();
            Log.i("zjz", "pic_camera=" + photoDialog.getImageTempUri().getPath());
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
        if (requestCode == 3 && resultCode == RESULT_OK) {
            Bitmap bitmap = photoDialog.getBitmap();
            if (bitmap != null) {
                uploadPictrue(photoDialog.getImageTempUri().getPath(), bitmap);
                photoDialog.cancel();
            }
        }

//        if (resultCode == RESULT_OK) {
//            //相册
//            if (capture.getActionCode() == CapturePhoto.PICK_IMAGE) {
////                Uri targetUri = data.getData();
//                Bitmap bitmap = capture.getBitmap(data.getData());
//
//                Uri contentUri = data.getData();
//                String[] proj = {MediaStore.Images.Media.DATA};
//                CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
//                Cursor cursor = loader.loadInBackground();
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                cursor.moveToFirst();
//                //这里需要不管是照相的图片，还是选择的图片，都需要统一使用file://的格式
//                String filePath = "file://" + cursor.getString(column_index);
////                Bitmap bitmap = capture.getSmallBitmap(filePath);
//                Log.i("zjz", "pic_images=" + filePath);
//                Bitmap scaledBitmap = Bitmap.createScaledBitmap(
//                        bitmap,
//                        120,
//                        120,
//                        true);
//                if (scaledBitmap != null) {
//
//                    uploadPictrue(filePath, scaledBitmap);
//
//                }
//                if (contentUri != null)
//                    bs = capture.handleMediaPhoto(contentUri);
//            }
//            //拍照
//            else {
//                bs = capture.handleCameraPhoto();
//                Bitmap bitmap = capture.getBitmap(capture.getImageTempUri());
////                Bitmap bitmap = capture.getSmallBitmap(capture.getImageTempUri().getPath());
//                Log.i("zjz", "pic_camera2=" + capture.getImageTempUri().getPath());
//                Bitmap scaledBitmap = Bitmap.createScaledBitmap(
//                        bitmap,
//                        120,
//                        120,
//                        true);
//                if (scaledBitmap != null) {
//
//                    uploadPictrue(capture.getImageTempUri().getPath(), scaledBitmap);
//                }
//            }
//            if (bs != null && bs.length > 0) {
//                try {
//                    Log.i("zjz", "bs=" + bs);
////                    uploadAvatar(bs);
//                } catch (Exception e) {
//
//                }
//            } else {
//                showToast("上传图片失败，无法找到该照片");
//            }
//        }

    }
    private String mCurrentPhotoPath;

    private void save() {
        if (mCurrentPhotoPath != null) {
//            PictureUtil.galleryAddPic(this, mCurrentPhotoPath);
            try {
                File f = new File(mCurrentPhotoPath);
                Bitmap bm = PictureUtil.getSmallBitmap(mCurrentPhotoPath);
                FileOutputStream fos = new FileOutputStream(new File(
                        PictureUtil.getAlbumDir(), "small_" + f.getName()));
                bm.compress(Bitmap.CompressFormat.JPEG, 40, fos);
                Log.i("zjz", "路径为：" + PictureUtil.getAlbumDir() +"/"+ "small_" + f.getName());
                if (bm != null) {

                    uploadPictrue(PictureUtil.getAlbumDir()+"/"+ "small_" + f.getName(), bm);
                    photoDialog.cancel();
                }
            } catch (Exception e) {
                Log.i("zjz", "error", e);
            }



        }
    }

    public Handler handler = new Handler();


    private void uploadPictrue(final String filePath, final Bitmap bitmap) {
        String path = filePath.replace("file:///", "/");
        Log.i("zjz", "path=" + path);
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        final String putURL = TLUrl.getInstance().URL_hwg_comment_share_step2;
        final HttpUtils hu = new HttpUtils(20000);
        final RequestParams params = new RequestParams();
        params.addBodyParameter("category_id", ac_id);
        params.addBodyParameter("key", MyApplication.getInstance().getMykey());
        params.addBodyParameter("file", new File(path));
        handler.post(new Runnable() {
            @Override
            public void run() {
                hu.send(HttpRequest.HttpMethod.POST, putURL, params,
                        new RequestCallBack<String>() {
                            @Override
                            public void onFailure(HttpException arg0, String arg1) {
                                ProgressDlgUtil.stopProgressDlg();
                                showToast("图片添加失败");
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> arg0) {
                                try {
                                    Log.i("zjz", "result=" + arg0.result);
                                    JSONObject jsonObject = new JSONObject(arg0.result);
                                    Log.i("zjz", "json=" + jsonObject);
                                    if (jsonObject.optString("state").equals("true")) {
                                        Log.i("zjz", "成功！");
                                        ImageItem takePhoto = new ImageItem();
                                        takePhoto.setFile_id(jsonObject.optString("file_id"));
                                        takePhoto.setFile_name(jsonObject.optString("file_name"));
                                        takePhoto.setOrigin_file_name(jsonObject.optString("origin_file_name"));
                                        takePhoto.setFile_url(jsonObject.optString("file_url"));
                                        takePhoto.setFile_path(jsonObject.optString("file_path"));


                                        takePhoto.setBitmap(bitmap);
//                                      takePhoto.setFile(Uri.parse(filePath));
                                        tempSelectBitmap.add(takePhoto);
                                        Log.i("zjz", "imageList=" + tempSelectBitmap.size());
                                        btnCommint.setVisibility(tempSelectBitmap.size() == 0 ? View.GONE : View.VISIBLE);
                                    } else {
                                        showToast("添加图片失败！请重新选择！");
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

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }
}
