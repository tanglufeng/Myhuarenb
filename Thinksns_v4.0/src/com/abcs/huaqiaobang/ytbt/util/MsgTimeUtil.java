package com.abcs.huaqiaobang.ytbt.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MsgTimeUtil {
	public static String getShowMsgTime(long msgTime, long lastMsgTime) {
		Calendar now = Calendar.getInstance();
		Calendar time = Calendar.getInstance();
		Date date = new Date(msgTime);
		time.setTime(date);
		if (now.get(Calendar.YEAR) != time.get(Calendar.YEAR)) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
					.format(date);
		} else if (now.get(Calendar.MONTH) != time.get(Calendar.MONTH)) {
			return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
					.format(date);
		} else if (now.get(Calendar.DAY_OF_MONTH) != time
				.get(Calendar.DAY_OF_MONTH)) {
			if ((now.get(Calendar.DAY_OF_MONTH) - time
					.get(Calendar.DAY_OF_MONTH)) == 1) {
				return "昨天 "
						+ new SimpleDateFormat("HH:mm", Locale.CHINA)
								.format(date);
			} else {
				return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
						.format(date);
			}
		}
		return new SimpleDateFormat("HH:mm", Locale.CHINA)
				.format(date);
	}
	
	public static String getShowconversationTime(long msgTime) {
		Calendar now = Calendar.getInstance();
		Calendar time = Calendar.getInstance();
		Date date = new Date(msgTime);
		time.setTime(date);
		if (now.get(Calendar.YEAR) != time.get(Calendar.YEAR)) {
			return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
					.format(date);
		} else if (now.get(Calendar.MONTH) != time.get(Calendar.MONTH)) {
			return new SimpleDateFormat("MM-dd", Locale.CHINA)
					.format(date);
		} else if (now.get(Calendar.DAY_OF_MONTH) != time
				.get(Calendar.DAY_OF_MONTH)) {
			if ((now.get(Calendar.DAY_OF_MONTH) - time
					.get(Calendar.DAY_OF_MONTH)) == 1) {
				return "昨天 ";
						
			} else {
				return new SimpleDateFormat("MM-dd", Locale.CHINA)
						.format(date);
			}
		}
		return new SimpleDateFormat("HH:mm", Locale.CHINA)
				.format(date);
	}
}
