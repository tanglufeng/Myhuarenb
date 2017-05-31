package com.abcs.huaqiaobang.ytbt.voicemeeting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.sortlistview.CharacterParser;
import com.abcs.huaqiaobang.ytbt.sortlistview.ClearEditText;
import com.abcs.huaqiaobang.ytbt.sortlistview.FriendsSortAdapter;
import com.abcs.huaqiaobang.ytbt.sortlistview.PinyinFriendsComparator;
import com.abcs.huaqiaobang.ytbt.sortlistview.SideBar;
import com.abcs.huaqiaobang.ytbt.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FriendsListActivity extends BaseActivity implements
		OnClickListener {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog, tv_checked, tv_checked_cancel;
	private FriendsSortAdapter adapter;
	private ClearEditText mClearEditText;
	private CharacterParser characterParser;
	private List<User> SourceDateList = new ArrayList<User>();;
	private PinyinFriendsComparator pinyinComparator;
	private int checkNum = 0;
	private ImageView back;
	private ArrayList<User> nums = new ArrayList<>();
	private int type;
	private FriendBroadCastReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts_list);
		type = getIntent().getIntExtra("type", 0);
		if (MyApplication.friends == null) {
			Tool.showProgressDialog(this, "正在加载好友，请稍等...",false);
			getAllFriends();
		} else initView();
		// if (MyApplication.friends.size() == 0) {
		// Tool.showProgressDialog(this, "正在加载联系人，请稍等...");
		// receiver = new FriendBroadCastReceiver();
		// registerReceiver(receiver, new IntentFilter(
		// GlobalConstant.ACTION_READ_CONTACT));
		// } else {
		// initView();
		// }
	}

	private void initView() {
		tv_checked_cancel = (TextView) findViewById(R.id.tv_checked_cancel);
		tv_checked_cancel.setOnClickListener(this);
		tv_checked = (TextView) findViewById(R.id.tv_checked);
		tv_checked.setOnClickListener(this);
		findViewById(R.id.linerlayout).setVisibility(View.GONE);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		setCheckText();
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinFriendsComparator();
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}
			}
		});
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView
				.setEmptyView(findViewById(R.id.empty_tip_recommend_bind_tv));
		sortListView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							mClearEditText.getWindowToken(), 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});
		sortListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				FriendsSortAdapter.ViewHolder holder = (FriendsSortAdapter.ViewHolder) view
						.getTag();
				// 改变CheckBox的状态
				holder.cb.toggle();
				// 将CheckBox的选中状况记录下来
				FriendsSortAdapter.getIsSelected().put(position,
						holder.cb.isChecked());
				User s = (User) adapter.getItem(position);
				if (holder.cb.isChecked()) {
					nums.add(s);
					checkNum++;
					setCheckText();
					tv_checked_cancel.setVisibility(View.VISIBLE);
					back.setVisibility(View.GONE);
				} else {
					nums.remove(s);
					checkNum--;
					if (checkNum <= 0) {
						tv_checked_cancel.setVisibility(View.GONE);
						back.setVisibility(View.VISIBLE);
					}
					setCheckText();
				}
				// if (InviteContactActivity.isNums) {
				// // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				// SortAdapter.ViewHolder holder = (SortAdapter.ViewHolder) view
				// .getTag();
				// // 改变CheckBox的状态
				// holder.cb.toggle();
				// // 将CheckBox的选中状况记录下来
				// SortAdapter.getIsSelected().put(position,
				// holder.cb.isChecked());
				// User s = (User) adapter.getItem(position);
				// if (holder.cb.isChecked()) {
				// nums.add(s);
				// } else {
				// nums.remove(s);
				// }
				// }else{
				//
				// ContactEntity entity = new ContactEntity();
				// entity.setName(((SortModel) adapter.getItem(position))
				// .getName());
				// entity.setNum(((SortModel) adapter.getItem(position))
				// .getNum());
				// entity.setTime(new Date().getTime());
				// ((InviteContactActivity) getActivity()).exit(entity);
				// }
				// User user = SourceDateList.get(position);
				// Log.i("xbbdasda",user.getVoipAccout()+3);
				// Intent intent = new Intent(getActivity(),
				// FriendDetailsActivity.class);
				// intent.putExtra("friend", user.getVoipAccout());
				// startActivity(intent);
			}
		});
		SourceDateList = filledData(MyApplication.friends.keySet());
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new FriendsSortAdapter(this, SourceDateList);
		adapter.setHasCheckBox(true);
		sortListView.setAdapter(adapter);
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		mClearEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private void setCheckText() {
		tv_checked.setText(getString(R.string.str_check_num, checkNum));
	}

	private void cancelAllChecked() {
		// 遍历list的长度，将已选的按钮设为未选
		for (int i = 0; i < SourceDateList.size(); i++) {
			if (adapter.getIsSelected().get(i)) {
				adapter.getIsSelected().put(i, false);
				checkNum--;// 数量减1
			}
		}
		tv_checked_cancel.setVisibility(View.GONE);
		back.setVisibility(View.VISIBLE);
		adapter.notifyDataSetChanged();
		setCheckText();
	}

	// public void setAllContacts(HashMap<String, List<String>> result) {
	// contacts = result;
	// Tool.removeProgressDialog();
	// initView();
	// }

	private List<User> filledData(Set<String> set) {
		List<User> mSortList = new ArrayList<User>();
		for (String s : set) {
			Log.i("xbbcc", s);
			User sortModel = new User();
			sortModel.setNickname(MyApplication.friends.get(s).getNickname());
			sortModel.setRemark(MyApplication.friends.get(s).getRemark());
			sortModel.setUid(MyApplication.friends.get(s).getUid());
			sortModel.setVoipAccout(MyApplication.friends.get(s)
					.getVoipAccout());
			sortModel.setAvatar(MyApplication.friends.get(s).getAvatar());
			String pinyin;
			// pinyin =
			// characterParser.getSelling(GlobalConstant.friends.get(s).getNickname());
			if (MyApplication.friends.get(s).getRemark().trim().equals("")) {
				pinyin = characterParser.getSelling(MyApplication.friends
						.get(s).getNickname());
			} else {
				pinyin = characterParser.getSelling(MyApplication.friends
						.get(s).getRemark());
			}
			String sortString = pinyin.substring(0, 1).toUpperCase();
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	private void filterData(String filterStr) {
		List<User> filterDateList = new ArrayList<User>();
		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (User user : SourceDateList) {
				String name = user.getRemark().trim().equals("")?user.getNickname():user.getRemark();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(user);
				}
			}
		}
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.tv_checked:
			if (nums.size() == 0) {
				Tool.showInfo(FriendsListActivity.this, "还未选择任何联系人");
				return;
			}
			if (type == 1) {
				StringBuffer sb = new StringBuffer();
				for (User u : nums) {
					ContactEntity entity = new ContactEntity();
					entity.setName(u.getNickname());
					String num = u.getVoipAccout().trim();
					entity.setNum(num);
					entity.setTime(new Date().getTime());
					sb.append(num);
					sb.append(",");
				}
				sb.deleteCharAt(sb.length() - 1);
				Intent intent = new Intent();
				intent.putExtra("nums", sb.toString());
				setResult(1, intent);
			} else {
				StringBuffer sb2 = new StringBuffer();
				for (User u : nums) {
					ContactEntity entity = new ContactEntity();
					entity.setName(u.getNickname());
					String num = String.valueOf(u.getUid());
					entity.setNum(num);
					entity.setTime(new Date().getTime());
					sb2.append(num);
					sb2.append(",");
				}
				sb2.deleteCharAt(sb2.length() - 1);
				Intent intent2 = new Intent();
				intent2.putExtra("uids", sb2.toString());
				intent2.putExtra("users", nums);
				setResult(2, intent2);
			}
			finish();
			break;
		case R.id.tv_checked_cancel:
			cancelAllChecked();
			break;
		}

	}

	public void getAllFriends() {
		Log.i("info", MyApplication.getInstance().getUid() + "");
		HttpRequest.sendGet(TLUrl.URL_GET_VOIP + "User/findfriendUser", "uid="
				+ MyApplication.getInstance().getUid() + "&page=1"
				+ "&size=1000", new HttpRevMsg() {
			@Override
			public void revMsg(String msg) {
				if (msg.length() <= 0) {
					return;
				}
				try {
					JSONObject jsonObject = new JSONObject(msg);
					if (jsonObject.getInt("status") == 1) {
						JSONArray jsonArray = jsonObject.getJSONArray("msg");
						if (jsonArray.length() == 0) {
							Tool.showInfo(getApplicationContext(), "暂无好友");
							return;
						}
						List<User> users = new ArrayList<User>();
						MyApplication.friends = new ConcurrentHashMap<>();
						// List<String> nums = new ArrayList<String>();
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject object = jsonArray.getJSONObject(i);
							User user = new User();
							user.setVoipAccout(object.getString("voipAccount"));
							user.setNickname(object.getString("nickname"));
							user.setUid(object.getInt("frienduid"));
							user.setAvatar(object.getString("avatar"));
							user.setRemark(object.getString("remarks"));
							users.add(user);
							if (user.getRemark().trim().equals("")) {
								MyApplication.friends.put(user.getNickname(),
										user);
							} else {
								MyApplication.friends.put(user.getRemark(),
										user);
							}
						}
						try {
							MyApplication.dbUtils.saveOrUpdateAll(users);
						} catch (DbException e) {
							e.printStackTrace();
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
					Tool.showInfo(getApplicationContext(), "加载好友失败");
				} finally {
					Tool.removeProgressDialog();
					initView();
				}
			}
		});

	}

	class FriendBroadCastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

		}
	}
}
