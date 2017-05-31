package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.Util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author xbw
 * @version 创建时间：2015年10月23日 上午11:17:51
 */
public class TurnInSuccessActivity extends BaseActivity {
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.occft_activity_turnin_success);
		Intent intent = getIntent();
		Util.setImage(intent.getStringExtra("icon"),
				findViewById(R.id.activity_turnout_icon), handler);
		((TextView) findViewById(R.id.activity_turnout_cardname))
				.setText(intent.getStringExtra("cardname"));
		((TextView) findViewById(R.id.activity_turnout_money)).setText(intent
				.getStringExtra("money"));
		((TextView) findViewById(R.id.today)).setText(Util
				.getDateOnlyDay(System.currentTimeMillis())
				+ "  今天   转到"
				+ intent.getStringExtra("name")
				+ intent.getStringExtra("money") + "元");
		long arrive = intent.getLongExtra("arrive",
				System.currentTimeMillis() + 86400000l);
		long start = intent.getLongExtra("start",
				System.currentTimeMillis() + 86400000l);
		((TextView) findViewById(R.id.startgain)).setText(Util
				.getDateOnlyDay(start) + "  " + getWeek(start) + "  开始产生收益");
		((TextView) findViewById(R.id.getgain)).setText(Util
				.getDateOnlyDay(arrive) + "  " + getWeek(arrive) + "  收益到账");

		findViewById(R.id.turnout_btn_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		findViewById(R.id.activity_turnout_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	/**
	 * 判断当前日期是星期几
	 * 
	 */

	private String getWeek(long time) {
		String Week = "星期";
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(time));
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "天";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "六";
		}
		return Week;
	}
}
