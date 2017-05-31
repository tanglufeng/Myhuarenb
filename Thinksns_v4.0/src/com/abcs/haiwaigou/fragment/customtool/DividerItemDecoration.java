package com.abcs.haiwaigou.fragment.customtool;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView分隔线
 * This class is from the v7 samples of the Android SDK. It's not by me!
 * <p/>
 * See the license above for details.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;

    private int mOrientation;


    private int space;

    public DividerItemDecoration(Context context, int orientation, int space) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        this.space=space;
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }


    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }
    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView v = new RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
        if(parent.getChildPosition(view) != 0)
            outRect.top = space;
    }


//    /**
//     * 水平方向
//     */
//    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
//
//    /**
//     * 垂直方向
//     */
//    public static final int VERTICAL = LinearLayoutManager.VERTICAL;
//
//    // 画笔
//    private Paint paint;
//
//    // 布局方向
//    private int orientation;
//    // 分割线颜色
//    private int color;
//    // 分割线尺寸
//    private int size;
//
//    public DividerItemDecoration() {
//        this(VERTICAL);
//    }
//
//    public DividerItemDecoration(int orientation) {
//        this.orientation = orientation;
//
//        paint = new Paint();
//    }
//
//    @Override
//    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        super.onDrawOver(c, parent, state);
//
//        if (orientation == VERTICAL) {
//            drawHorizontal(c, parent);
//        } else {
//            drawVertical(c, parent);
//        }
//    }
//
//    /**
//     * 设置分割线颜色
//     *
//     * @param color 颜色
//     */
//    public void setColor(int color) {
//        this.color = color;
//        paint.setColor(color);
//    }
//
//    /**
//     * 设置分割线尺寸
//     *
//     * @param size 尺寸
//     */
//    public void setSize(int size) {
//        this.size = size;
//    }
//
//    // 绘制垂直分割线
//    protected void drawVertical(Canvas c, RecyclerView parent) {
//        final int top = parent.getPaddingTop();
//        final int bottom = parent.getHeight() - parent.getPaddingBottom();
//
//        final int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            final View child = parent.getChildAt(i);
//            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
//            final int left = child.getRight() + params.rightMargin;
//            final int right = left + size;
//
//            c.drawRect(left, top, right, bottom, paint);
//        }
//    }
//
//    // 绘制水平分割线
//    protected void drawHorizontal(Canvas c, RecyclerView parent) {
//        final int left = parent.getPaddingLeft();
//        final int right = parent.getWidth() - parent.getPaddingRight();
//
//        final int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            final View child = parent.getChildAt(i);
//            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
//            final int top = child.getBottom() + params.bottomMargin;
//            final int bottom = top + size;
//
//            c.drawRect(left, top, right, bottom, paint);
//        }
//    }
}