package com.abcs.hqbtravel.view.activity;

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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.model.ImageItem;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.PhotoDialog;
import com.abcs.haiwaigou.utils.PictureUtil;
import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.wedgt.PromptDialog;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.CancelComplete;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.capturePhoto.CapturePhoto;
import com.abcs.sociax.android.R;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChuanTuActivity2 extends BaseActivity implements View.OnClickListener {
    @InjectView(R.id.tljr_txt_news_title)
    TextView tljrTxtNewsTitle;
    @InjectView(R.id.tljr_hqss_news_titlebelow)
    TextView tljrHqssNewsTitlebelow;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.relative_yes)
    RelativeLayout relative_yes;
    @InjectView(R.id.tljr_grp_goods_title)
    RelativeLayout tljrGrpGoodsTitle;
//    @InjectView(R.id.img_goods_icon)
//    ImageView imgGoodsIcon;
//    @InjectView(R.id.relative_goods_icon)
//    RelativeLayout relativeGoodsIcon;
//    @InjectView(R.id.t_goods_name)
//    TextView tGoodsName;
//    @InjectView(R.id.ratingBar)
//    RatingBar ratingBar;
//    @InjectView(R.id.linearLayout2)
//    LinearLayout linearLayout2;
//    @InjectView(R.id.t_comment_info)
//    TextView tCommentInfo;
//    @InjectView(R.id.linear_pingjia)
//    LinearLayout linearPingjia;
    @InjectView(R.id.gridview)
    GridView gridview;
    @InjectView(R.id.img_logo)
    ImageView img_logo;
    @InjectView(R.id.tv_name)
    TextView tv_name;
//    @InjectView(R.id.btn_commint)
//    Button btnCommint;
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

    private String logo,title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.img_add_pic);
        parentView = getLayoutInflater().inflate(R.layout.travel_activity_chuan_tu2, null);
        setContentView(parentView);
        ButterKnife.inject(this);
        mRequestQueue = Volley.newRequestQueue(this);
        capture = new CapturePhoto(this);
        photoDialog = new PhotoDialog(this);

        detailId=getIntent().getStringExtra("detialsId");
        type=getIntent().getStringExtra("type");
        logo=getIntent().getStringExtra("logo");
        title=getIntent().getStringExtra("title");

        if(!TextUtils.isEmpty(logo)){
            MyApplication.imageLoader.displayImage(logo,img_logo,MyApplication.options);
        }
        if(!TextUtils.isEmpty(title)){
            tv_name.setText(title);
        }
        setOnListener();
        initView();
        initAcid();
        Init();
    }

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
        relative_yes.setOnClickListener(this);
//        btnCommint.setOnClickListener(this);
    }

    private void initAcid() {
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, TLUrl.getInstance().URL_hwg_comment_share_step1 + "&geval_id=" + geval_id + "&key=" + MyApplication.getInstance().getMykey(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zds", "share:连接成功");
                        Log.i("zds", "response=" + response);
                        JSONObject object = response.getJSONObject("datas");
                        ac_id = object.optString("ac_id");
                    } else {
                        Log.i("zds", "share:解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zds", e.toString());
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
        Log.i("zds", "geval_id=" + geval_id);
        geval_goodsname = getIntent().getStringExtra("geval_goodsname");
        geval_goodsimg = getIntent().getStringExtra("geval_goodsimg");
        geval_comment = getIntent().getStringExtra("geval_comment");
        geval_scores = getIntent().getIntExtra("geval_scores", 0);
        LoadPicture loadPicture = new LoadPicture();
//        loadPicture.initPicture(imgGoodsIcon, geval_goodsimg);
//        tGoodsName.setText(geval_goodsname);
//        ratingBar.setRating(geval_scores);
//        tCommentInfo.setText(geval_comment);
//        btnCommint.setVisibility(tempSelectBitmap.size() == 0 ? View.GONE : View.VISIBLE);

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
                closePop();
                break;
            case R.id.relative_yes://上传图片
                commitPicture();
                break;
        }
    }


    private void closePop(){
        new PromptDialog(this, "", new CancelComplete() {
            @Override
            public void cancel() {
                finish();
            }
        }, new Complete() {
            @Override
            public void complete() {

            }
        }).show();
    }

    private String pics=null;
    private String detailId=null;
    private String type=null;
    private void commitPicture() {
        if (tempSelectBitmap.size() == 0) {
            showToast("请选择需要上传的图片！");
            return;
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < tempSelectBitmap.size(); i++) {
                stringBuffer.append(tempSelectBitmap.get(i).getFile_url());
                if (i != tempSelectBitmap.size() - 1) {
                    stringBuffer.append(",");
                }
            }
            pics = stringBuffer.toString();
            Log.i("zds", "pics=" + pics);


            ProgressDlgUtil.showProgressDlg("正在上传中...", this);
            com.abcs.huaqiaobang.util.HttpRequest.sendGet(Contonst.HOST+"/addImg", "detailId=" + detailId + "&imgs="+pics + "&type=" + type+ "&uid=" + MyApplication.getInstance().getUid()+"", new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.i("zds","relect=="+msg);
//                                {"timestamp":"1483753515616","body":{},"result":1,"transactionid":"","version":"1.0","info":"OK"}
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("result") == 1) {
                                    showToast("上传成功！");
                                    finish();
                                } else {
                                    Log.i("zds", "share:解析失败");
                                }
                                ProgressDlgUtil.stopProgressDlg();
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Log.i("zds", e.toString());
                                Log.i("zds", msg);
                                e.printStackTrace();
                                ProgressDlgUtil.stopProgressDlg();
                            }
                        }
                    });

                }
            });

        }
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
                    tempSelectBitmap.remove(position);
                    Log.i("zds", "tempSize=" + tempSelectBitmap.size());
                    if (tempSelectBitmap.size() == 0) {
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
//            Log.i("zds", "pic_images=" + photoDialog.getImageTempUri());
            mCurrentPhotoPath=cursor.getString(column_index);
//            mCurrentPhotoPath=photoDialog.getImageTempUri().getPath();
            save();
            Log.i("zds", "pic_images=" + mCurrentPhotoPath);
//            if (bitmap != null) {
//
//                uploadPictrue(filePath, bitmap);
//                File file=new File(filePath);
//                try {
//                    Log.i("zds","image_size="+getFolderSize(file));
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
            Log.i("zds", "pic_camera=" + photoDialog.getImageTempUri().getPath());
//            if (bitmap != null) {
//
//                uploadPictrue(photoDialog.getImageTempUri().getPath(), bitmap);
//                File file=new File(photoDialog.getImageTempUri().getPath());
//                try {
//                    Log.i("zds","image_size="+getFolderSize(file));
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
//                Log.i("zds", "pic_images=" + filePath);
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
//                Log.i("zds", "pic_camera2=" + capture.getImageTempUri().getPath());
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
//                    Log.i("zds", "bs=" + bs);
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
                Log.i("zds", "路径为：" + PictureUtil.getAlbumDir() +"/"+ "small_" + f.getName());
                if (bm != null) {

                    uploadPictrue(PictureUtil.getAlbumDir()+"/"+ "small_" + f.getName(), bm);
                    photoDialog.cancel();
                }
            } catch (Exception e) {
                Log.i("zds", "error", e);
            }

        }
    }

    public Handler handler = new Handler();

    private void uploadPictrue(final String filePath, final Bitmap bitmap) {
        final String path = filePath.replace("file:///", "/");
        Log.i("zds", "path=" + path);
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
                                Log.i("zds", "result=" + arg0.result);
                                ImageItem takePhoto = new ImageItem();
                                takePhoto.setFile_url(arg0.result);
                                takePhoto.setBitmap(bitmap);
                                tempSelectBitmap.add(takePhoto);
                                Log.i("zds", "imageList=" + tempSelectBitmap.size());
                                ProgressDlgUtil.stopProgressDlg();
                                adapter.notifyDataSetChanged();
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
