package com.abcs.haiwaigou.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.capturePhoto.CapturePhoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PhotoDialog implements View.OnClickListener {
    /**
     * 从图库里面选择
     */
    public static final int IMAGES = 1;

    /**
     * 使用摄像头
     */
    public static final int CAMERA = 2;

    /**
     * 临时存储的sdcard路径
     */
    private Uri imageTempUri = Uri.parse("file:///sdcard/huaqiaobang.jpg");
//    private Uri imageTempUri = Uri.parse("huaqiaobang"+System.currentTimeMillis()+".jpg");

    /**
     * 裁剪框X比例
     */
    private int aspectX = 1;
    /**
     * 裁剪框Y比例
     */
    private int aspectY = 1;

    /**
     * 裁剪输出宽【像素】
     */
    private int outputX = 400;

    /**
     * 裁剪输出高【像素】
     */
    private int outputY = 400;

    /**
     * 裁剪输出图片类型【】
     */
    private String outputFormat = Bitmap.CompressFormat.JPEG.toString();


    private LinearLayout line_images;
    private LinearLayout line_camera;
    private LinearLayout line_cancel;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private BaseActivity activity;

    CapturePhoto capturePhoto;


    public Uri getImageTempUri() {
        return imageTempUri;
    }

    public void setImageTempUri(Uri imageTempUri) {
        this.imageTempUri = imageTempUri;
    }


    public int getAspectX() {
        return aspectX;
    }

    public void setAspectX(int aspectX) {
        this.aspectX = aspectX;
    }

    public int getAspectY() {
        return aspectY;
    }

    public void setAspectY(int aspectY) {
        this.aspectY = aspectY;
    }

    public int getOutputX() {
        return outputX;
    }

    public void setOutputX(int outputX) {
        this.outputX = outputX;
    }

    public int getOutputY() {
        return outputY;
    }

    public void setOutputY(int outputY) {
        this.outputY = outputY;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public PhotoDialog(BaseActivity activity) {
        this.activity = activity;
        builder = new AlertDialog.Builder(activity, R.style.photodialog);

        View view = LayoutInflater.from(activity).inflate(R.layout.hwg_photo_dialog, null);
        line_images = (LinearLayout) view.findViewById(R.id.line_images);
        line_images.setOnClickListener(this);
        line_camera = (LinearLayout) view.findViewById(R.id.line_camera);
        line_camera.setOnClickListener(this);
        line_cancel = (LinearLayout) view.findViewById(R.id.line_cancel);
        line_cancel.setOnClickListener(this);
        //设置布局
        builder.setView(view);
        //生成dialog
        alertDialog = builder.create();


    }


    //设置dialog的方位
    private void setDialogGravity() {
        //获取dialog的window对象
        Window window = alertDialog.getWindow();
        //设置dialog的位置
        window.setGravity(Gravity.BOTTOM);
        //添加动画
        window.setWindowAnimations(R.style.photostyle);
        //设置宽度
        WindowManager windowManager = activity.getWindowManager();
        //获取手机屏幕display对象
        Display display = windowManager.getDefaultDisplay();
        //获取window的属性对象
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置属性的宽度
        lp.width = (int) (display.getWidth());
        //重新设置dialog的window的属性
        window.setAttributes(lp);
    }

    /**
     * 调度图库
     *
     * @return 返回意图
     */
    public Intent actionImages() {
        //调度图库活动
        Intent intent = new Intent(Intent.ACTION_PICK);
        //设置数据【内部存储的图片】
        intent.setData(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //设置类型
        intent.setType("image/*");
        return intent;
    }

    /**
     * 调度相册
     *
     * @return 返回意图
     */
    public Intent actionCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        try {
            //调度摄像头
            //设置输出的配置
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            //设置输出的位置
        String path = "file:///sdcard/huaqiaobang" + System.currentTimeMillis() + ".jpg";
        imageTempUri = Uri.parse(path);

//            File f = createImageFile();
            intent.putExtra("output", imageTempUri);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return intent;
    }

    private String mCurrentPhotoPath;

    @SuppressLint("SimpleDateFormat")
    private File createImageFile() throws IOException {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = format.format(new Date());
        String imageFileName = "huaqiaobang_" + timeStamp + ".jpg";

        File image = new File(PictureUtil.getAlbumDir(), imageFileName);
        mCurrentPhotoPath = image.getAbsolutePath();
        save();
        return image;
    }

    private void save() {
        if (mCurrentPhotoPath != null) {
            PictureUtil.galleryAddPic(activity, mCurrentPhotoPath);
            try {
                File f = new File(mCurrentPhotoPath);

                Bitmap bm = PictureUtil.getSmallBitmap(mCurrentPhotoPath);

                FileOutputStream fos = new FileOutputStream(new File(
                        PictureUtil.getAlbumDir(), "small_huaqiaobang_" + f.getName()));

                bm.compress(Bitmap.CompressFormat.JPEG, 40, fos);

                imageTempUri=Uri.parse(PictureUtil.getAlbumDir()+"small_huaqiaobang_" + f.getName());

            } catch (Exception e) {
                Log.i("zjz", "error", e);
            }

        }
    }

    public void getSaveUrl(){

    }


    /**
     * 调度裁剪
     *
     * @param resData 源图片文件的uri
     * @return 返回意图
     */
    public Intent actionCrop(Uri resData) {
        Intent intent = new Intent();
        //系统裁剪活动
        intent.setAction("com.android.camera.action.CROP");
        //设置裁剪的源图片和类型
        intent.setDataAndType(resData, "image/*");// mUri是已经选择的图片 Uri
        //打开裁剪
        intent.putExtra("crop", "true");
        // 裁剪框比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        // 输出图片大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        //返回结果,但是大图片很有可能直接内存溢出
        //intent.putExtra("return-data", true);
        //黑边【可以缩放】
        intent.putExtra("scale", true);
        //黑边【可以缩放】
        intent.putExtra("scaleUpIfNeeded", true);
        //输出设置【直接输出到sdcard里面】
        intent.putExtra("output", imageTempUri);
        //输出格式设置
        intent.putExtra("outputFormat", outputFormat);
        return intent;
    }

    /**
     * 返回bitmap
     *
     * @return 返回裁剪好的图片
     */
    public Bitmap getBitmap() {
        InputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            //从sdcard里面读取文件流
            inputStream = activity.getContentResolver().openInputStream(imageTempUri);
            //文件流转换成为bitmap
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 给定一个uri返回bitmap
     *
     * @return 返回bitmap
     */
    public Bitmap getBitmap(Uri data) {
        InputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            //从sdcard里面读取文件流
            inputStream = activity.getContentResolver().openInputStream(data);
            //文件流转换成为bitmap
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 显示dialog
     */
    public void show() {
        //设置dialog的方位
        setDialogGravity();
        //然后显示
        alertDialog.show();
    }

    /**
     * 取消dialog
     */
    public void cancel() {
        alertDialog.cancel();
    }


    @Override
    public void onClick(View v) {
        if (v == line_images) {
            //开启

            activity.startActivityForResult(actionImages(), IMAGES);
        }
        if (v == line_camera) {
            //开启
            activity.startActivityForResult(actionCamera(), CAMERA);
        }
        if (v == line_cancel) {
            cancel();
        }
    }


}
