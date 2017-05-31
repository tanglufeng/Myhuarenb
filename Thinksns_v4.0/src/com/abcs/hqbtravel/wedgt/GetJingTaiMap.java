//package com.abcs.hqbtravel.wedgt;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.text.TextUtils;
//
//import com.abcs.hqbtravel.entity.RestauransBean;
//import com.abcs.sociax.android.R;
//import com.baidu.mapapi.map.BitmapDescriptor;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.Marker;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.map.OverlayOptions;
//import com.baidu.mapapi.model.LatLng;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.List;
//
///**
// * Created by Administrator on 2016/11/17.
// */
//
//public class GetJingTaiMap {
//
//    /**
//     * 通过GET方式发送的请求
//     * @param lng
//     * @param lat
//     * 坐标格式：lng<经度>，lat<纬度>，例如116.43213,38.76623。
//     */
//
//
//    public Bitmap locationByGet(String lng, String lat) throws IOException {
//
//        InputStream is=null;
//        try {
//            // 设置请求的地址 通过URLEncoder.encode(String sd, String enc)
//            // 使用指定的编码机制将字符串转换为 application/x-www-form-urlencoded 格式
//            // 根据地址创建URL对象(网络访问的url)
//            String spec="http://api.map.baidu.com/staticimage/v2?ak="+ Config.BAIDU_AK+"&mcode="+Config.BAIDU_MCODE+"&center="+lng+","+lat+"&width=570&height=200&zoom=9&markers="+lng+","+lat+""+"&markers="+;
//            URL url = new URL(spec);
//            // url.openConnection()打开网络链接
//            HttpURLConnection urlConnection = (HttpURLConnection) url
//                    .openConnection();
//            urlConnection.setRequestMethod("GET");// 设置请求的方式
//            urlConnection.setReadTimeout(5000);// 设置超时的时间
//            urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
//            urlConnection.setDoInput(true);
//            urlConnection.connect();
////            // 设置请求的头
////            urlConnection
////                    .setRequestProperty("User-Agent",
////                            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
//            // 获取响应的状态码 404 200 505 302
//            if (urlConnection.getResponseCode() == 200) {
//                // 获取响应的输入流对象
//                is = urlConnection.getInputStream();
//
//                final Bitmap bitmap = BitmapFactory.decodeStream(is);
//
//                // 释放资源
//                is.close();
////                // 创建字节输出流对象
////                ByteArrayOutputStream os = new ByteArrayOutputStream();
////                // 定义读取的长度
////                int len = 0;
////                // 定义缓冲区
////                byte buffer[] = new byte[1024];
////                // 按照缓冲区的大小，循环读取
////                while ((len = is.read(buffer)) != -1) {
////                    // 根据读取的长度写入到os对象中
////                    os.write(buffer, 0, len);
////                }
////                os.close();
//                // 返回字符串
////                String result = new String(os.toByteArray());
////                System.out.println("***************" + result
////                        + "******************");
//
//                return bitmap;
//            } else {
//                System.out.println("------------------链接失败-----------------");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
