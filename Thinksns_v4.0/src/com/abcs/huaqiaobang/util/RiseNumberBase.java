package com.abcs.huaqiaobang.util;

import com.abcs.huaqiaobang.view.RiseNumberTextView;

public interface RiseNumberBase {
	public void start();

	public RiseNumberTextView withNumber(double number);

	public RiseNumberTextView withNumber(int number);

	public RiseNumberTextView setDuration(long duration);

	public void setOnEnd(RiseNumberTextView.EndListener callback);
}