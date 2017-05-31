package com.abcs.huaqiaobang.util;

import android.util.Log;

public class LogUtil {
	private static final int Info = 3;
	private static final int Warn = 4;
	private static final int Error = 5;
	public static final int Nothing = 6;

	private static int Level = Info;
	public static void i(String tag, String msg) {
		if (Level <= Info)
			Log.i("info▍▍▍▍" + tag, msg);
	}
	public static void w(String tag, String msg) {
		if (Level <= Warn)
			Log.w("warn▋▋▋▋" + tag, msg);
	}
	public static void e(String tag, String msg) {
		if (Level <= Error)
			Log.e("error▊▊▊▊" + tag, msg);
	}

	public static int sMaxStackMsg = Integer.MAX_VALUE;// 显示所有的stack

	public static String getCaller(int stackIndex) {
		if (stackIndex == Integer.MAX_VALUE)
			stackIndex = 100000;// TODO 暂时写为100000,以后改
		stackIndex += 2;
		String result = "\n★Call Log★：\n";
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		stackIndex = (stackIndex < stack.length ? stackIndex : stack.length - 1);
		for (int i = 2; i <= stackIndex; i++) {
			StackTraceElement s = stack[i];
			result += "▉" + s.getClassName() + "\t\t▉";
			result += s.getMethodName() + "\t\t▉";
			result += s.getLineNumber() + "\n";
		}
		return result;
	}
}
