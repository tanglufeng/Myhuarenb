package com.abcs.haiwaigou.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/4/11.
 */
public class HotHeaderView extends LinearLayout {

    public HotHeaderView(Context context) {
        super(context);
        init(context);
    }

    public HotHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HotHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(final Context context) {
        inflate(context, R.layout.hwg_hot_header, this);
    }
}
