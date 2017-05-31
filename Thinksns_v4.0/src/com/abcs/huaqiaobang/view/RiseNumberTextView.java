package com.abcs.huaqiaobang.view;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.abcs.huaqiaobang.util.RiseNumberBase;
import com.abcs.huaqiaobang.util.Util;

/**
 * 数字动画自定义
 * 
 * @author zengtao 2015年7月17日 上午11:48:03
 *
 */
public class RiseNumberTextView extends TextView implements RiseNumberBase {

	private static final int STOPPED = 0;

	private static final int RUNNING = 1;

	private int mPlayingState = STOPPED;

	private double number;

	private double fromNumber;

	private long duration = 1000;

	/**
	 * 1.int 2.float
	 */
	private int numberType = 2;

	private EndListener mEndListener = null;

	final static int[] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999,
			99999999, 999999999, Integer.MAX_VALUE };

	public RiseNumberTextView(Context context) {
		super(context);
	}

	public RiseNumberTextView(Context context, AttributeSet attr) {
		super(context, attr);
	}

	public RiseNumberTextView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
	}

	public interface EndListener {
		public void onEndFinish();
	}

	public boolean isRunning() {
		return (mPlayingState == RUNNING);
	}

	private void runFloat() {
		ValueAnimator valueAnimator = ValueAnimator.ofObject(
				new PointEvaluator(), new Point(fromNumber), new Point(number));
		valueAnimator.setDuration(duration);
		valueAnimator
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator valueAnimator) {
						double d = ((Point) valueAnimator.getAnimatedValue())
								.getValue();
						setText(Util.df.format(d));
						if ((d + "").equalsIgnoreCase(number + "")) {
							setText(Util.df.format(number));
						}
						if (valueAnimator.getAnimatedFraction() >= 1) {
							mPlayingState = STOPPED;
							if (mEndListener != null)
								mEndListener.onEndFinish();
						}
					}
				});
		valueAnimator.start();
	}

	private void runInt() {
		ValueAnimator valueAnimator = ValueAnimator.ofInt((int) fromNumber,
				(int) number);
		valueAnimator.setDuration(duration);

		valueAnimator
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator valueAnimator) {

						setText(valueAnimator.getAnimatedValue().toString());
						if (valueAnimator.getAnimatedFraction() >= 1) {
							mPlayingState = STOPPED;
							if (mEndListener != null)
								mEndListener.onEndFinish();
						}
					}
				});
		valueAnimator.start();
	}

	static int sizeOfInt(int x) {
		for (int i = 0;; i++)
			if (x <= sizeTable[i])
				return i + 1;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	@Override
	public void start() {

		if (!isRunning()) {
			mPlayingState = RUNNING;
			if (numberType == 1)
				runInt();
			else
				runFloat();
		}
	}

	@Override
	public RiseNumberTextView withNumber(double number) {
		this.number = number;
		numberType = 2;
		fromNumber = 0;

		return this;
	}

	@Override
	public RiseNumberTextView withNumber(int number) {
		this.number = number;
		numberType = 1;
		fromNumber = 0;

		return this;
	}

	@Override
	public RiseNumberTextView setDuration(long duration) {
		this.duration = duration;
		return this;
	}

	@Override
	public void setOnEnd(EndListener callback) {
		mEndListener = callback;
	}

}

class PointEvaluator implements TypeEvaluator {

	@Override
	public Object evaluate(float fraction, Object startValue, Object endValue) {
		Point startPoint = (Point) startValue;
		Point endPoint = (Point) endValue;
		double x = startPoint.getValue() + fraction
				* (endPoint.getValue() - startPoint.getValue());
		Point point = new Point(x);
		return point;
	}

}

class Point {

	private double value;

	public Point(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

}