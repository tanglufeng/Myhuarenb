package com.abcs.huaqiaobang.ytbt.voicemeeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.sortlistview.SortModel;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InviteContactActivity extends FragmentActivity implements
		OnClickListener {
	private List<Fragment> list = new ArrayList<>();
	private ViewPager vp;
	private RadioButton last_contact;
	private RadioButton list_contact;
	private List_Contact_Fragment list_Contact_Fragment;
	public static boolean isNums;
	private TextView tv_check, tv_checked_cancel;
	private ImageView back;

	public ImageView getBack() {
		return back;
	}

	public TextView getTv_check() {
		return tv_check;
	}

	public TextView getTv_checked_cancel() {
		return tv_checked_cancel;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.list.add(this);
		setContentView(R.layout.activity_invite_contact);
		isNums = getIntent().getBooleanExtra("isNums", false);
		try {
			MyApplication.dbUtils.createTableIfNotExist(ContactEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LastContactsFragment lastContactsFragment = new LastContactsFragment();
		list_Contact_Fragment = new List_Contact_Fragment();
		list.add(lastContactsFragment);
		list.add(list_Contact_Fragment);
		initView();
	}

	private void initView() {
		
		vp = (ViewPager) findViewById(R.id.vp);
		vp.setAdapter(new MyVPAdapter(getSupportFragmentManager()));
		tv_check = (TextView) findViewById(R.id.tv_checked);
		tv_checked_cancel = (TextView) findViewById(R.id.tv_checked_cancel);
		tv_checked_cancel.setOnClickListener(this);
		tv_check.setVisibility(View.GONE);
		tv_checked_cancel.setVisibility(View.GONE);
		last_contact = (RadioButton) findViewById(R.id.last_contact);
		list_contact = (RadioButton) findViewById(R.id.list_contact);
		last_contact.setOnClickListener(this);
		list_contact.setOnClickListener(this);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_check.setOnClickListener(this);
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					last_contact.setChecked(true);
					back.setVisibility(View.VISIBLE);
					tv_check.setVisibility(View.GONE);
					tv_checked_cancel.setVisibility(View.GONE);
					break;
				case 1:
					list_contact.setChecked(true);
					if (isNums) {
						tv_check.setVisibility(View.VISIBLE);
					}
					if(list_Contact_Fragment.getCheckNum()>0){
						tv_checked_cancel.setVisibility(View.VISIBLE);
					}
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.list.remove(this);
	}

	void exit(List<ContactEntity> list, String nums) {
		try {
			MyApplication.dbUtils.saveOrUpdateAll(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Intent intent = new Intent();
		intent.putExtra("nums", nums);
		setResult(1, intent);
		finish();
	}

	void exit(ContactEntity entity) {
		String num = entity.getNum().trim();
		num = numFormat(num);
		try {
			entity.setNum(num);
			MyApplication.dbUtils.saveOrUpdate(entity);
		} catch (DbException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent();
		intent.putExtra("num", num);
		intent.putExtra("name", entity.getName());
		setResult(1, intent);
		finish();
	}

	private String numFormat(String num) {
		StringBuffer buffer = new StringBuffer(num);
		Log.i("info", num);
		if (num.contains(" ")) {
			String[] s = num.split(" ");
			buffer.delete(0, buffer.length());
			for (String string : s) {
				buffer.append(string);
			}
			num = buffer.toString();
		}
		Log.i("info", num);

		if (num.contains("-")) {
			buffer.delete(0, buffer.length());
			String[] s = num.split("-");
			for (String string : s) {
				buffer.append(string);
			}
			num = buffer.toString();
		}
		Log.i("info", num);
		if (num.startsWith("+86")) {
			buffer.delete(0, buffer.length());
			buffer.append(num.substring(3));
		}
		Log.i("info", buffer.toString());
		return buffer.toString();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.last_contact:
			vp.setCurrentItem(0);
			break;
		case R.id.list_contact:
			vp.setCurrentItem(1);
			break;
		case R.id.tv_checked:
			List<SortModel> list = list_Contact_Fragment.getNums();
			if (list.size() == 0) {
				Tool.showInfo(InviteContactActivity.this, "还未选择任何联系人");
				return;
			}
			List<ContactEntity> contactEntities = new ArrayList<>();
			StringBuffer sb = new StringBuffer();
			for (SortModel sortModel : list) {
				ContactEntity entity = new ContactEntity();
				entity.setName(sortModel.getName());
				String num = numFormat(sortModel.getNum().trim());
				entity.setNum(num);
				entity.setTime(new Date().getTime());
				sb.append(num);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			exit(contactEntities, sb.toString());
			break;
		case R.id.tv_checked_cancel:
			list_Contact_Fragment.cancelAllChecked();
			break;
		}
	}

	class MyVPAdapter extends FragmentPagerAdapter {

		public MyVPAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			return list.size();
		}

	}

}
