package com.abcs.haiwaigou.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.abcs.sociax.android.R;

/**
 * Created by Administrator on 2016/2/23.
 */
public class CircleIndicator extends View {

    private Paint paint;
    private ViewPager viewpager;
    private int datasize;

    public CircleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircleIndicator(Context context) {
        super(context);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        drawCycle(canvas);

    }

    private void drawCycle(Canvas canvas) {
        canvas.save();
        canvas.translate(getScrollX(), getScrollY());
        int count = 0;
        if (viewpager == null)
            return;
        if (viewpager.getAdapter() != null) {
            count = viewpager.getAdapter().getCount();
            if (count <=1) {
                return;
            }
        }
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                invalidate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        int select = viewpager.getCurrentItem();
        float density = getContext().getResources().getDisplayMetrics().density;
        int itemWidth = (int) (11*density);
        int itemHeight = itemWidth / 2;
//        圆点居中
        int x = (getWidth() - count * itemWidth)/2;
//        圆点靠右
//        int x = (getWidth() - count * itemWidth);
        int y = getHeight() - itemWidth;
        int minItemHeight = (int) ((float) itemHeight * 0.8F);
        paint.setAntiAlias(true);

        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < count; i++) {
            if (select == i) {
                paint.setColor(getResources().getColor(R.color.tljr_statusbarcolor));
                canvas.drawCircle(x + itemWidth * i + itemWidth / 2, y, minItemHeight, paint);
            } else {
                paint.setColor(Color.parseColor("#55dfdfdf"));
                canvas.drawCircle(x + itemWidth * i + itemWidth / 2, y, minItemHeight, paint);
            }
        }
        canvas.restore();
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewpager = viewPager;
        invalidate();
    }

    public void setCurrent() {
        invalidate();
    }

    public void setViewPager(ViewPager viewPager, int datasize) {
        this.viewpager = viewPager;
        this.datasize = datasize;
        invalidate();
    }
}
