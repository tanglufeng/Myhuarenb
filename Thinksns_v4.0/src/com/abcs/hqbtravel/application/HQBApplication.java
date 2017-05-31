package com.abcs.hqbtravel.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Administrator on 2016/9/6.
 */
public class HQBApplication extends Application{

    public static String KEY_YOUJI_SARCHER=null;
    public static ImageLoader imageLoader;
    public static Context instance;
    public static final String LOCATION="location";
    public static final String LOCATION_NAME="locationsd";
    private static final String  CITY_ID="cityid";
    private static final String  CITY_ID_NAME="cityids";

    public static void saveLocation(String location){
        SharedPreferences sp=instance.getSharedPreferences(LOCATION,MODE_PRIVATE);
        sp.edit().putString(LOCATION_NAME,location).commit();

    }
    public static String getLocation(){
        SharedPreferences sp=instance.getSharedPreferences(LOCATION,MODE_PRIVATE);
        return  sp.getString(LOCATION_NAME,null);

    }
    public static void saveCityId(String cityId){
        SharedPreferences sp=instance.getSharedPreferences(CITY_ID,MODE_PRIVATE);
        sp.edit().putString(CITY_ID_NAME,cityId).commit();

    }
    public static String getCityId(){
        SharedPreferences sp=instance.getSharedPreferences(CITY_ID,MODE_PRIVATE);
        return  sp.getString(CITY_ID_NAME,null);

    }
    public static void saveCityName(String cityname){
        SharedPreferences sp=instance.getSharedPreferences("CityNames",MODE_PRIVATE);
        sp.edit().putString("CityName",cityname).commit();

    }
    public static String getCityName(){
        SharedPreferences sp=instance.getSharedPreferences("CityNames",MODE_PRIVATE);
        return  sp.getString("CityName",null);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
//        PgyCrashManager.register(this);
        instance=getApplicationContext();
        inniImageLoder();

    }

    public void inniImageLoder(){
        imageLoader = ImageLoader.getInstance();

        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800)
                // max width, max height，即保存的每个缓存文件的最大长宽
                // .discCacheExtraOptions(480, 800, CompressFormat.PNG, 75,
                // null)
                // Can slow ImageLoader, use it carefully (Better don't use
                // it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(2)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 3).denyCacheImageMultipleSizesInMemory()
                // .memoryCache(new WeakMemoryCache())
                .memoryCache(new LruMemoryCache(8 * 1024 * 1024))

                // You can pass your own memory cache
                // implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(9 * 1024 * 1024).discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // .discCacheFileCount(200)
                // 缓存的文件数量

                //  .discCache(new  UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout
                // (5
                // ss),
                // readTimeout
                // (30
                // ss)超时时间
                .writeDebugLogs() // Remove for release app
                .build();// 开始构建
        imageLoader.init(config);
    }

    public static DisplayImageOptions getListOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
              /*  // // 设置图片在下载期间显示的图片
                .showImageOnLoading(R.drawable.img_huaqiao_default)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.drawable.img_huaqiao_default)
                // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.drawable.img_huaqiao_default)*/
                .cacheInMemory(true)
                // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)
                // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型
                // .decodingOptions(android.graphics.BitmapFactory.Options
                // decodingOptions)//设置图片的解码配置
                .considerExifParams(true)

                // 设置图片下载前的延迟
                // .delayBeforeLoading(int delayInMillis)//int
                // delayInMillis为你设置的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // 。preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))// 淡入

                .build();
        return options;
    }

    public static DisplayImageOptions getAvatorOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
               /* // // 设置图片在下载期间显示的图片
                .showImageOnLoading(R.drawable.img_huaqiao_default)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.drawable.img_huaqiao_default)
                // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.drawable.img_huaqiao_default)*/
                .cacheInMemory(true)
                // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)
                // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型
                // .decodingOptions(android.graphics.BitmapFactory.Options
                // decodingOptions)//设置图片的解码配置
                .considerExifParams(true)

                // 设置图片下载前的延迟
                // .delayBeforeLoading(int delayInMillis)//int
                // delayInMillis为你设置的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // 。preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))// 淡入

                .build();
        return options;
    }
}
