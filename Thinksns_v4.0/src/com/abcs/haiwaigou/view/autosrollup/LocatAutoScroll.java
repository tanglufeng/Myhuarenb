package com.abcs.haiwaigou.view.autosrollup;

/**
 * Created by Administrator on 2016/11/30.
 */

import android.content.Context;
import android.util.AttributeSet;

import com.abcs.haiwaigou.local.beans.Msg;

/**
 * User: zds
 * Date: 2016-11-30
 * Time: 09:56
 * FIXME
 */
public class LocatAutoScroll extends BaseAutoScrollUpTextView<Msg> {

    public LocatAutoScroll(Context context, AttributeSet attrs,
                                         int defStyle) {
        super(context, attrs, defStyle);
    }

    public LocatAutoScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LocatAutoScroll(Context context) {
        super(context);
    }

    @Override
    public String getTextTitle(Msg data) {
        return data.getTitle();
    }

    @Override
    public String getIcon(Msg data) {
        return data.getImgUrl();
    }
    @Override
    public String getTextIcon(Msg data) {
        return data.getTitle();
    }
    @Override
    public String getTips(Msg data) {
        return data.getTypeName();
    }
    @Override
    public long getTime(Msg data) {
        return data.getCreateTime();
    }

    /**
     * 这里面的高度应该和你的xml里设置的高度一致
     */
    @Override
    protected int getAdertisementHeight() {
        return 40;
    }

}
