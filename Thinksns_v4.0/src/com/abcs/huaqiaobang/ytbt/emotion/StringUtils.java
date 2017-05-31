package com.abcs.huaqiaobang.ytbt.emotion;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static SpannableString getEmotionContent(final Context context, final TextView tv, String source) {
		SpannableString spannableString = new SpannableString(source);
		Resources res = context.getResources();

		String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
		Pattern patternEmotion = Pattern.compile(regexEmotion);
		Matcher matcherEmotion = patternEmotion.matcher(spannableString);

		while (matcherEmotion.find()) {
			// ��ȡƥ�䵽�ľ����ַ�
			String key = matcherEmotion.group();
			// ƥ���ַ�Ŀ�ʼλ��
			int start = matcherEmotion.start();
			// ���ñ������ֻ�ȡ����Ӧ��ͼƬ
			Integer imgRes = EmotionUtils.getImgByName(key);
			if (imgRes != null) {
				// ѹ������ͼƬ
				int size = (int) tv.getTextSize();
				Bitmap bitmap = BitmapFactory.decodeResource(res, imgRes);
				Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);

				ImageSpan span = new ImageSpan(context, scaleBitmap);
				spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannableString;
	}

}
