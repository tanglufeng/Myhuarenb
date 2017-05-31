package com.abcs.hqbtravel.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/2/23.
 */

public class HqbViewHolder {
    private final SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    private HqbViewHolder(Context context, ViewGroup parent, int layoutId,
                          int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        // setTag
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static HqbViewHolder get(Context context, View convertView,
                                    ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new HqbViewHolder(context, parent, layoutId, position);
        }
        return (HqbViewHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public HqbViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public HqbViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }

    public HqbViewHolder setImageMoreResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.setImageResource(drawableId);

        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId

     * @return
     */
    public HqbViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @return
     */
    public HqbViewHolder setImageByUrl(int viewId, String url) {
        ImageLoader.getInstance().displayImage(url, (ImageView) getView(viewId), MyApplication.options);
//        ImageView imageView=getView(viewId);
//        LoadPicture loadPicture=new LoadPicture();
//        loadPicture.initPicture(imageView,url);
        return this;
    }

    public int getPosition() {
        return mPosition;
    }

}
