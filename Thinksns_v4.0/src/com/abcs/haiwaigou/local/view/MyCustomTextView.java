package com.abcs.haiwaigou.local.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 项目名称：com.abcs.haiwaigou.local.view
 * 作者：zds
 * 时间：2017/5/20 10:28
 */

public class MyCustomTextView extends TextView {

    public MyCustomTextView(Context context) {
        super(context);
        init(context);
    }

    public MyCustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyCustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        try {
            Typeface type= Typeface.createFromAsset(context.getAssets(),"font/ltheijian.TTF");
            if(type!=null){
                setTypeface(type) ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
