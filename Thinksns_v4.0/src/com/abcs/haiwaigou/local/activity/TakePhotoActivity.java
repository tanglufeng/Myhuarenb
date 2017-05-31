package com.abcs.haiwaigou.local.activity;

import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TakePhotoActivity extends BaseActivity {

    private CameraView cv = null;// 继承surfaceView的自定义view 用于存放照相的图片
    private Camera camera;
    private Camera.Parameters parameters = null;
    private Button cameraPhoto;
    private Button cameraOk;
    private LinearLayout linearLayout;
    private LinearLayout linearLayoutCamera;
    private String cameraPath;

    private ArrayList<String> listPath = new ArrayList<String>();//存放图片地址的集合
    int i = 0;  // 删除的图片的tag 从0开始
    private Button cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_take_photo);
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiscCache();
        System.gc();
        linearLayout = (LinearLayout) findViewById(R.id.linearlayout_images);
        linearLayoutCamera = (LinearLayout) findViewById(R.id.preciew);
        cameraPhoto = (Button) findViewById(R.id.camera_photo);
        cameraOk = (Button) findViewById(R.id.camera_ok);
        cancle = (Button) findViewById(R.id.cancle);
        cameraOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击拍照
                Intent intent = new Intent(TakePhotoActivity.this, PublishMessageActivity.class);
//                Intent intent = getIntent();
                intent.putStringArrayListExtra("listPath", listPath);
                setResult(1, intent);
                finish();
            }
        });
        cameraPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, picture);
            }
        });
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //初始化camera数据
        openCamera();
    }

    private void getImageView(String path) {
        int j = i++;
        final View view = getLayoutInflater().inflate(R.layout.local_item_camera, null);
        final ImageView imageView = (ImageView) view.findViewById(R.id.photoshare_item_image);
        final Button button = (Button) view.findViewById(R.id.photoshare_item_delete);
        final TextView t_delete = (TextView) view.findViewById(R.id.t_delete);
        LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = Util.dip2px(this, 90);
        layoutParams.height = Util.dip2px(this, 110);
        imageView.setLayoutParams(layoutParams);
        try {
            imageView.setImageBitmap(getImageBitmap(path));
            //做一个旋转,因为竖屏拍照时,下面显示的缩略图方向与预览时不一致
            RotateAnimation animation = new RotateAnimation(0f, 90f, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(0);
            animation.setFillAfter(true);
            imageView.startAnimation(animation);
            t_delete.setTag(j);
            button.setTag(j);// 给删除button设置tag
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        t_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.removeView(view);

                int k = Integer.parseInt(t_delete.getTag().toString());
                listPath.set(k, "NOIMAGE");//如果点击删除,将存放path的集合中的值改为"NOIMAGE"
//                listPath.remove(k);
            }
        });
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                linearLayout.removeView(view);

                int k = Integer.parseInt(button.getTag().toString());
                listPath.set(k, "NOIMAGE");//如果点击删除,将存放path的集合中的值改为"NOIMAGE"
            }
        });

        linearLayout.addView(view);
        listPath.add(path);
    }

    // 根据路径获取图片
    private Bitmap getImageBitmap(String path) throws FileNotFoundException, IOException {
        Bitmap bmp = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        opts.inSampleSize = Util.computeSampleSize(opts, -1, 150 * 150);
        opts.inJustDecodeBounds = false;
        try {
            bmp = BitmapFactory.decodeFile(path, opts);
        } catch (OutOfMemoryError e) {
        }
        return bmp;
    }

    // 主要的surfaceView，负责展示预览图片，camera的开关
    class CameraView extends SurfaceView {

        private SurfaceHolder holder = null;

        public CameraView(Context context) {
            super(context);
            holder = this.getHolder();
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            holder.setKeepScreenOn(true);//设置屏幕长亮
            holder.addCallback(new SurfaceCallback());
//            holder.addCallback(new SurfaceHolder.Callback() {
//                @Override
//                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                    parameters= camera.getParameters();
//                    parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
//                    camera.setDisplayOrientation(90);
//                    parameters.setPreviewSize(width, height); // 设置预览大小
////                    parameters.setPreviewFrameRate(5);  //设置每秒显示4帧
//                    parameters.setPictureSize(1024, 768); // 设置保存的图片尺寸
//
////                    parameters.setJpegQuality(80); // 设置照片质量
//                    camera.setParameters(parameters);
//                    camera.startPreview();
//                }
//
//                @SuppressLint("NewApi")
//                @Override
//                public void surfaceCreated(SurfaceHolder holder) {
//                    camera = Camera.open();
//                    try {
//                        //设置预览角度
//                        camera.setDisplayOrientation(90);
//                        // 设置holder主要是用于surfaceView的图片的实时预览，以及获取图片等功能
//                        camera.setPreviewDisplay(holder);
//                    } catch (IOException e) {
//                        camera.release();
//                        camera = null;
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void surfaceDestroyed(SurfaceHolder holder) {
//                    camera.stopPreview();
//                    camera.release();
//                    camera = null;
//                }
//            });
        }
    }

    private final class SurfaceCallback implements SurfaceHolder.Callback {

        // 拍照状态变化时调用该方法
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            parameters = camera.getParameters(); // 获取各项参数
//            parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
//            parameters.setPreviewSize(width, height); // 设置预览大小
//            parameters.setPreviewFrameRate(5);  //设置每秒显示4帧
//            parameters.setPictureSize(1024, 768); // 设置保存的图片尺寸
//            parameters.setJpegQuality(80); // 设置照片质量
            List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
            Camera.Size previewSize = supportedPreviewSizes.get(0);
            parameters.setPreviewSize(previewSize.width, previewSize.height);
            List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
            Camera.Size pictureSize = supportedPictureSizes.get(0);
            parameters.setPictureSize(pictureSize.width, pictureSize.height);
            parameters.set("orientation", "portrait");
            parameters.setRotation(90);
            camera.setParameters(parameters);
        }

        // 开始拍照时调用该方法
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera = Camera.open(); // 打开摄像头
                camera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象
                camera.setDisplayOrientation(90);
                camera.startPreview(); // 开始预览
            } catch (Exception e) {
                camera.release();
                camera = null;
                e.printStackTrace();
            }

        }

        // 停止拍照时调用该方法
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                camera.stopPreview();
                camera.release(); // 释放照相机
                camera = null;
            }
        }
    }


    private void openCamera() {
        if (cv == null) {
            linearLayoutCamera.removeAllViews();
            cv = new CameraView(TakePhotoActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT);

            linearLayoutCamera.addView(cv, params);
        } else {
            try {
                if (camera != null) {
                    camera.startPreview();
                }
            } catch (Exception e) {
                Log.i("zjz",
                        "Error starting camera preview: " + e.getMessage());
            }

        }
    }

    // 回调用的picture，实现里边的onPictureTaken方法，其中byte[]数组即为照相后获取到的图片信息
    private Camera.PictureCallback picture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File picture = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
//            try {
//                cameraPath = picture.getPath();
//                FileOutputStream fos = new FileOutputStream(cameraPath);// 获得文件输出流
//                fos.write(data);// 写入文件
//                fos.close();// 关闭文件流
//                openCamera();// 重新打开相机
//                getImageView(cameraPath);// 获取照片
//            } catch (Exception e) {
//
//            }
            Bitmap bMap;
            try {// 获得图片
                cameraPath = picture.getPath();
                Configuration config = getResources().getConfiguration();
                if (config.orientation == 1) {
                    Log.i("zjz","竖拍");
                    bMap = BitmapFactory.decodeByteArray(data, 0, data.length);

                    Bitmap bMapRotate;
                    Matrix matrix = new Matrix();
                    matrix.reset();
                    matrix.postRotate(90);
                    bMapRotate = Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(),
                            bMap.getHeight(), matrix, true);
                    bMap = bMapRotate;

                    // Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);

                    File file = new File(cameraPath);
                    BufferedOutputStream bos =
                            new BufferedOutputStream(new FileOutputStream(file));
                    bMap.compress(Bitmap.CompressFormat.JPEG, 800, bos);//将图片压缩到流中
                    bos.flush();//输出
                    bos.close();//关闭
                }else {
                    Log.i("zjz","横拍");
                    FileOutputStream fos = new FileOutputStream(cameraPath);// 获得文件输出流
                    fos.write(data);// 写入文件
                    fos.close();// 关闭文件流
                    openCamera();// 重新打开相机
                    getImageView(cameraPath);// 获取照片
                }
                openCamera();// 重新打开相机
                getImageView(cameraPath);// 获取照片

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                getImageView(Util.getAbsoluteImagePath(data.getData(), TakePhotoActivity.this));
            }
        }
    }
}
