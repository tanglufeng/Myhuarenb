package com.abcs.haiwaigou.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.activity.StartActivity;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.abcs.huaqiaobang.tljr.news.Options;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by zjz on 2016/1/20.
 */
public class LoadPicture {

    /*
	 * 图片地址
	 */
    private String defaultPicture = "default";
    String imageUri_Local = "file:///sdcard/huaqiaobang/"; // 离线状态时加载图片使用下载到本地的地址
    public static String imageUri_moren = "drawable://" + R.drawable.img_huaqiao_default;

    private ImageView imageView;
    private String Url;
    private ImageLoader imageLoader;

    public void initPicture(ImageView imageView, final String Url) {
        final ImageAware imageAware = new ImageViewAware(imageView, false);
        if (!Constent.noPictureMode) {
				/*
				 * 离线图片加载
				 */

            if (Constent.netType.equals("未知")) {
                if (!TextUtils.isEmpty(Url)) {

                    StartActivity.imageLoader.displayImage(Url, imageAware, Options.getListOptions(),
                            new ImageLoadingListener() {

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
										/*
										 * 若联网时imageloader没有下载的图片，将使用从离线下载获取到的图片
										 */
                                    System.out.println("onLoadingFailed");
//                                    Url =imageUri_Local + goods.getId() + ".png";
                                    StartActivity.imageLoader.displayImage(imageUri_moren, imageAware);
                                }

                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                }

                                @Override
                                public void onLoadingCancelled(String imageUri, View view) {
                                }
                            });

                } else {
                    if (defaultPicture.equals("default")) {

                        StartActivity.imageLoader.displayImage(imageUri_moren, imageAware);
                        imageView.setTag(imageUri_moren);
                    } else {
                        StartActivity.imageLoader.displayImage(defaultPicture, imageAware);
                        imageView.setTag(defaultPicture);
                    }

                }
            } else {
					/*
					 * 在线图片加载
					 */
                if (!TextUtils.isEmpty(Url)) {

                    // 防止重复加载闪烁错位
                    if (imageView.getTag() == null
                            || !imageView.getTag().equals(Url)) {
                        StartActivity.imageLoader
                                .displayImage(Url, imageAware, Options.getListOptions());
                        imageView.setTag(Url);
                    }

                } else {
                    if (defaultPicture.equals("default")) {

                        StartActivity.imageLoader.displayImage(imageUri_moren, imageAware);
                        imageView.setTag(imageUri_moren);
                    } else {
                        StartActivity.imageLoader.displayImage(defaultPicture, imageAware);
                        imageView.setTag(defaultPicture);
                    }
                }
            }
        } else {
            StartActivity.imageLoader.displayImage(imageUri_moren, imageAware);
            imageView.setTag(imageUri_moren);
        }
    }


    public void initPicture(ImageView imageView, boolean isLocal, final String Url, final int id) {
        imageLoader = ImageLoader.getInstance();
        final ImageAware imageAware = new ImageViewAware(imageView, false);
        if (isLocal) {
            imageLoader.displayImage(Url, imageAware, com.abcs.huaqiaobang.tljr.news.Options.getListOptions(),
                    new ImageLoadingListener() {

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                        /*
                                         * 若联网时imageloader没有下载的图片，将使用从离线下载获取到的图片
										 */
                            System.out.println("onLoadingFailed");
                            String temp="" + id + ".png";
                            StartActivity.imageLoader.displayImage(temp, imageAware);
                        }

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                        }
                    });
        } else

        {

            imageLoader.displayImage(Url, imageAware, StartActivity.options);
        }
    }


}
