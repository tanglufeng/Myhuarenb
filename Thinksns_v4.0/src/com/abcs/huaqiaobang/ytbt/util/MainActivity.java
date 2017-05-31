package com.abcs.huaqiaobang.ytbt.util;
//package com.im.xbb.util;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.NetworkImageView;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class MainActivity extends Activity {
//
//    public static final String TAG = "MainActivity";
//    public static final String URL_GET = "http://apis.juhe" +
//            ".cn/mobile/get?phone=13812345678&key=daf8fa858c330b22e342c882bcbac622";
//    public static final String URL_IMG = "https://ss0.bdstatic" +
//            ".com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png";
//    public static final String URL_IMG2 = "http://img2.3lian.com/2014/f7/5/d/22.jpg";
//
//    public static final String URL_POST = "http://apis.juhe.cn/mobile/get ";
//    private RequestQueue requestQueue;
//    private TextView tv_response;
//    private TextView tv_error;
//    private ImageView imageView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        tv_response = (TextView) findViewById(R.id.tv_response);
//        tv_error = (TextView) findViewById(R.id.tv_error);
//
//        //1. 创建一个RequestQueue
//        requestQueue = Volley.newRequestQueue(this);
//    }
//
//    private void imgViewDemo() {
//        imageView = (ImageView) findViewById(R.id.imageView);
//        ImageLoader imageLoader = new ImageLoader(requestQueue, VolleyFactroy.getInstance(this).getImgCache());
//        imageLoader.get(URL_IMG, ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, R
//                .mipmap.ic_launcher));
//    }
//
//    private void imgViewDemo1(ImageLoader imageLoader) {
//        imageLoader.get(URL_IMG2, new ImageLoader.ImageListener() {
//            @Override
//            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
//                Bitmap bitmap = response.getBitmap();
//                imageView.setImageBitmap(bitmap);
//            }
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                tv_error.setText(error.toString());
//            }
//        });
//    }
//
//    private void netWorkImageDemo() {
//        NetworkImageView networkImageView = (NetworkImageView) findViewById(R.id.networkImageView);
//
//        networkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
//        networkImageView.setErrorImageResId(R.mipmap.ic_launcher);
//        networkImageView.setTag(TAG);
//        networkImageView.setImageUrl(URL_IMG, new ImageLoader(requestQueue, new ImageLoader
//                .ImageCache() {
//
//            @Override
//            public Bitmap getBitmap(String url) {
//                return null;
//            }
//
//            @Override
//            public void putBitmap(String url, Bitmap bitmap) {
//
//            }
//        }));
//    }
//
//    private void postDemo() {
//        StringRequest request = new StringRequest(Request.Method.POST, URL_POST, new Response
//                .Listener<String>() {
//
//
//            @Override
//            public void onResponse(String response) {
//                tv_response.setText(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                tv_error.setText(error.toString());
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> map = new HashMap<>();
//                map.put("phone", "18607550755");
//                map.put("key", "daf8fa858c330b22e342c882bcbac622");
//                return map;
//            }
//        };
//
//        request.setTag(TAG);
//        requestQueue.add(request);
//    }
//
//    private void getDemo() {
//        //2. 创建Request
//        StringRequest request = new StringRequest(URL_GET, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                tv_response.setText(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                tv_error.setText(error.toString());
//            }
//        });
//        // 为请求添加标志位
//        request.setTag(TAG);
//        //3. 将请求添加到队列中
//        requestQueue.add(request);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (requestQueue != null)
//            requestQueue.cancelAll(TAG);
//    }
//}
