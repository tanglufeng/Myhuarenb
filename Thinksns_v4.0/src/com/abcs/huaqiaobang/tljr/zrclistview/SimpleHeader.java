package com.abcs.huaqiaobang.tljr.zrclistview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.TypedValue;
import android.widget.Toast;

import com.abcs.huaqiaobang.util.Util;

import java.util.Date;

public class SimpleHeader implements Headable {
	private float PI = (float) Math.PI;
	private int mState = STATE_REST;
	private int mPice = 6;
	private Paint mPaint;
	private int mHeight = 0;
	private int mTime = 0;
	private int mTextColor;
	private int mPointColor;
	private float mPointRadius = 0;
	private float mCircleRadius = 0;
	private float mFontOffset;
	private String mMsg;
	private boolean isClipCanvas = true;

	private String lastRefreshTime;

	private Context context;

	public SimpleHeader(Context context) {
		this.context = context;
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.FILL);
		int fontSize = Util.dp2px(context,16);

		mPaint.setTextSize(fontSize);
		mPaint.setTextAlign(Align.CENTER);
		mTextColor = 0xffffffff;
		mPointColor = 0xffffffff;
		mFontOffset = -(mPaint.getFontMetrics().top + mPaint.getFontMetrics().bottom) / 2;
		mHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources()
				.getDisplayMetrics());
		mPointRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.5f, context.getResources()
				.getDisplayMetrics());
		mCircleRadius = mPointRadius * 3.5f;
		lastRefreshTime = Util.format10.format(new Date(System.currentTimeMillis()));

	}

	public int getDp(int num)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, num, context.getResources()
				.getDisplayMetrics());
	}

	public void setTextColor(int color)
	{
		mTextColor = color;
	}

	public void setCircleColor(int color)
	{
		mPointColor = color;
	}

	public void setIsClipCanvas(boolean bool)
	{
		isClipCanvas = bool;
	}

	@Override
	public void stateChange(int state, String msg)
	{
		if (mState != state)
		{
			mTime = 0;
		}
		mState = state;
		this.mMsg = msg;
	}

	@Override
	public int getState()
	{
		return mState;
	}

	@Override
	public boolean draw(Canvas canvas, int left, int top, int right, int bottom)
	{
		boolean more = false;
		final int width = right - left;
		final int Mwidth = (right / 2) - getDp(20);
		final int height = mHeight;
		final int offset = bottom - top;
		canvas.save();

		if (isClipCanvas)
		{
			canvas.clipRect(left + 5, 1, right + 5, bottom - 1);
		}
		switch (mState)
		{
		case STATE_REST:
			break;
		case STATE_PULL:
		case STATE_RELEASE:
			if (offset < 10)
			{
				break;
			}
			mPaint.setColor(mPointColor);
			for (int i = 0; i < mPice; i++)
			{
				int angleParam;
				if (offset < height * 3 / 4)
				{
					angleParam = offset * 16 / height - 3;// 每1%转0.16度;
				} else
				{
					angleParam = offset * 300 / height - 217;// 每1%转3度;
				}
				float angle = -(i * (360 / mPice) - angleParam) * PI / 180;
				float radiusParam=1;
//				if (offset <= height)
//				{
//					radiusParam = offset / (float) height;
//					radiusParam = 1 - radiusParam;
//					radiusParam *= radiusParam;
//					radiusParam = 1 - radiusParam;
//				} else
//				{
//					radiusParam = 1;
//				}
				float radius = Mwidth / 2 - radiusParam * (Mwidth / 2 - mCircleRadius);
				float x = (float) (Mwidth / 2 + radius * Math.cos(angle));
				float y = (float) (offset / 2 + radius * Math.sin(angle));
				canvas.drawCircle(x, y + top, mPointRadius, mPaint);
			}

			mPaint.setTextSize(Util.getFontSize(30));
			mPaint.setColor(0xff969696);
			if (offset > 120)
			{
				canvas.drawText("松开刷新", right / 2, (offset / 2) - getDp(5), mPaint);
			} else
			{
				canvas.drawText("下拉刷新", right / 2, (offset / 2) - getDp(5), mPaint);
			}
			mPaint.setTextSize(Util.getFontSize(25));
			canvas.drawText("上次刷新时间 : " + lastRefreshTime, right / 2, (offset / 2) + getDp(15), mPaint);

			break;
		case STATE_LOADING:
			more = true;
			mPaint.setColor(mPointColor);
			mPaint.setTextSize(Util.getFontSize(30));
			for (int i = 0; i < mPice; i++)
			{
				int angleParam = mTime * 5;
				float angle = -(i * (360 / mPice) - angleParam) * PI / 180;
				float radius = mCircleRadius;
				float x = (float) (Mwidth / 2 + radius * Math.cos(angle));
				float y;
				if (offset < height)
				{
					y = (float) (offset - height / 2 + radius * Math.sin(angle));
				} else
				{
					y = (float) (offset / 2 + radius * Math.sin(angle));
				}
				canvas.drawCircle(x, y + top, mPointRadius, mPaint);
			}

			mPaint.setTextSize(Util.getFontSize(30));
			mPaint.setColor(0xff969696);

			canvas.drawText(" 刷新中", right / 2, (offset / 2) - getDp(5), mPaint);

			mPaint.setTextSize(Util.getFontSize(25));
			canvas.drawText("上次刷新时间 : " + lastRefreshTime, right / 2, (offset / 2) + getDp(15), mPaint);

			mTime++;
			break;
		case STATE_SUCCESS:
		case STATE_FAIL:
			more = true;
			final int time = mTime;
			mPaint.setTextSize(Util.getFontSize(30));
			if (time < 30)
			{
				mPaint.setColor(mPointColor);
				for (int i = 0; i < mPice; i++)
				{
					int angleParam = mTime * 10;
					float angle = -(i * (360 / mPice) - angleParam) * PI / 180;
					float radius = mCircleRadius + time * mCircleRadius;
					float x = (float) (width / 2 + radius * Math.cos(angle));
					float y;
//					if (offset < height)
//					{
//						y = (float) (offset - height / 2 + radius * Math.sin(angle));
//					} else
//					{
//						y = (float) (offset / 2 + radius * Math.sin(angle));
//					}
					y = (float) (offset / 2 + radius * Math.sin(angle));
					canvas.drawCircle(x, y + top, mPointRadius, mPaint);
				}
				mPaint.setColor(mTextColor);
				mPaint.setAlpha(time * 255 / 30);
				String text = mMsg != null ? mMsg : mState == STATE_SUCCESS ? "加载成功" : "加载失败";
				float y;
				if (offset < height)
				{
					y = offset - height / 2;
				} else
				{
					y = offset / 2;
				}
				canvas.drawText(text, width / 2, y + top + mFontOffset, mPaint);

				lastRefreshTime = Util.format10.format(new Date(System.currentTimeMillis()));

			} else
			{
				mPaint.setColor(mTextColor);
				String text = mMsg != null ? mMsg : mState == STATE_SUCCESS ? "加载成功" : "加载失败";
				float y;
				if (offset < height)
				{
					y = offset - height / 2;
					mPaint.setAlpha(offset * 255 / height);
				} else
				{
					y = offset / 2;
				}
				canvas.drawText(text, width / 2, y + top + mFontOffset, mPaint);
			}
			mTime++;
			break;
		}
		canvas.restore();
		return more;
	}

	@Override
	public void toastResultInOtherWay(Context context, int state)
	{
		if (state == Headable.STATE_SUCCESS)
		{
		} else if (state == Headable.STATE_FAIL)
		{
			Toast.makeText(context.getApplicationContext(), mMsg != null ? mMsg : "加载失败", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public int getHeight()
	{
		return mHeight;
	}
}
