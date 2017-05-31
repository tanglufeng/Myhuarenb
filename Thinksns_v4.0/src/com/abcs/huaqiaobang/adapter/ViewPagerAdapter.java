package com.abcs.huaqiaobang.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.abcs.haiwaigou.model.BannersBean;
import com.abcs.huaqiaobang.MyApplication;

import java.io.InputStream;
import java.util.List;


public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<BannersBean> list;

    public ViewPagerAdapter(Context context, List<BannersBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {

        return list == null ? 0 : list.size();
    }

    /**
     * �Ƿ�ʹ�ö������ҳ��
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView img = new ImageView(context);
        container.addView(img);
        // ImageLoader.getInstance().displayImage(MyConfig.imageUrl+list.get(position).getAdImage(),
        // img);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        if (list.get(position).type==1) {
            MyApplication.imageLoader.displayImage(list.get(position).img, img, MyApplication.getAvatorOptions());
        }
        return img;
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}
