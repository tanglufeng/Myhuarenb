package com.abcs.huaqiaobang.ytbt.sortlistview;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.ytbt.util.GlobalConstant;

import java.util.HashMap;

public class QueryContactTask extends
		AsyncTask<Void, Void, HashMap<String, String>> {
	private MyApplication c;

	public QueryContactTask(MyApplication c) {
		super();
		this.c = c;
	}

	@Override
	protected HashMap<String, String> doInBackground(Void... params) {
		HashMap<String, String> list = new HashMap<String, String>();
		// 获得所有的联系人
		Cursor cur = c.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		// 循环遍历
		if(cur!=null)
		if (cur.moveToFirst()) {
			int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
			int displayNameColumn = cur
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			do {
				// 获得联系人的ID号
				String contactId = cur.getString(idColumn);
				// 获得联系人姓名
				String disPlayName = cur.getString(displayNameColumn);
				// 查看该联系人有多少个电话号码。如果没有这返回值为0
				int phoneCount = cur
						.getInt(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				if (phoneCount > 0) {
					// 获得联系人的电话号码
					Cursor phones = c.getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);
					if (phones.moveToFirst()) {
						do {
							// 遍历所有的电话号码
							String phoneNumber = phones
									.getString(phones
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							System.out.println(phoneNumber);
							list.put(phoneNumber, disPlayName);
						} while (phones.moveToNext());
					}
					phones.close();
				}
			} while (cur.moveToNext());
			cur.close();
		}
		return list;
	}

	@Override
	protected void onPostExecute(HashMap<String, String> result) {
		super.onPostExecute(result);
		// ((List_Contact_Fragment)a).setAllContacts(result);
		MyApplication.contacts.clear();
		MyApplication.contacts.putAll(result);
		c.sendBroadcast(new Intent(GlobalConstant.ACTION_READ_CONTACT));
	}
}
