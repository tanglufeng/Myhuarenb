package com.abcs.haiwaigou.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/9/9.
 */
public class CreateBitmap {
    public static Bitmap create(String str,Activity activity){
        Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.img_hwg_head_default);
        Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);//创建一个宽度和高度都是400、32位ARGB图
        int mid_x=bitmap.getWidth()/2;
        int mid_y=bitmap.getHeight()/2;
        Canvas canvas =new Canvas(bitmap);//初始化画布绘制的图像到icon上
        Paint paint =new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);//创建画笔
        canvas.drawRect(150, 75, 250, 120, paint);
        canvas.drawColor(activity.getResources().getColor(R.color.tljr_statusbarcolor));//图层的背景

        paint.setTextSize(Util.dip2px(activity,20));//设置文字的大小
        paint.setStyle(Paint.Style.STROKE);
        paint.setTypeface(Typeface.DEFAULT_BOLD);//文字的样式(加粗)
        paint.setColor(Color.WHITE);//文字的颜色
//        canvas.drawBitmap(bmp,400,400,null);
        canvas.drawText(str,200, 200, paint);//将文字写入。这里面的（120，130）代表着文字在图层上的初始位置
        canvas.save(canvas.ALL_SAVE_FLAG);//保存所有图层
        canvas.restore();
        return bitmap;
    }
}
