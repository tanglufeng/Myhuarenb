package com.abcs.huaqiaobang.ytbt.im;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.ytbt.bean.AddFriendRequestBean;
import com.abcs.huaqiaobang.ytbt.bean.ConversationBean;
import com.abcs.huaqiaobang.ytbt.bean.TopConversationBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.chats.group.SearchActivity;
import com.abcs.huaqiaobang.ytbt.sortlistview.CharacterParser;
import com.abcs.huaqiaobang.ytbt.sortlistview.ClearEditText;
import com.abcs.huaqiaobang.ytbt.sortlistview.FriendsSortAdapter;
import com.abcs.huaqiaobang.ytbt.sortlistview.PinyinFriendsComparator;
import com.abcs.huaqiaobang.ytbt.sortlistview.SideBar;
import com.abcs.huaqiaobang.ytbt.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.abcs.huaqiaobang.ytbt.util.GlobalConstant;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FriendsActivity extends BaseActivity implements OnClickListener {
	ImageView search;
	public static final int CREATE_GROUP = 200;
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private FriendsSortAdapter adapter;
	private EditText etNum;
	private ImageView unread;
	private ClearEditText mClearEditText;
	private CharacterParser characterParser;
	private List<User> SourceDateList = new ArrayList<>();
	private boolean addFriendRequsrt = false;
	// private ArrayList<User> users = new ArrayList<User>();
	private PinyinFriendsComparator pinyinComparator;
	private AddFriendBroadcast broadcast = new AddFriendBroadcast();
	// private View v;
	// private ContactBroadCastReceiver receiver;
	private List<User> nums = new ArrayList<>();
	final HashMap<String, User> list = new HashMap<String, User>();
	private DbUtils dbUtils;
	Boolean isagete = true;
	@SuppressLint("HandlerLeak")
	public Handler myhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				Toast.makeText(FriendsActivity.this, "您目前还没有好友，请添加!",
						Toast.LENGTH_SHORT).show();
				initView();
			}
			if (msg.what == 1) {
				if (MyApplication.friends == null) {
					MyApplication.friends = new ConcurrentHashMap<>();
				}
				MyApplication.friends.clear();
				// SourceDateList.clear();
				MyApplication.friends.putAll(list);
				try {
					dbUtils.saveOrUpdateAll(MyApplication.users);
				} catch (DbException e) {
					e.printStackTrace();
				}
				initView();
			}
			if (msg.what == 4) {
				Toast.makeText(FriendsActivity.this, "好友添加成功！",
						Toast.LENGTH_SHORT).show();
				getAllFriends();
			}
			if (msg.what == 7) {
				Intent intent = new Intent();
				intent.setAction("updateconversation");
				sendBroadcast(intent);
				Toast.makeText(FriendsActivity.this, "删除好友成功!",
						Toast.LENGTH_SHORT).show();
				User u = SourceDateList.get(msg.arg1);
				SourceDateList.remove(msg.arg1);
				try {
					dbUtils.delete(u);
					dbUtils.deleteById(TopConversationBean.class, MyApplication
							.getInstance().getUserBean().getVoipAccount()
							+ u.getVoipAccout());
					Log.i("xbb", u.getVoipAccout() + "d");
					// dbUtils.delete(Selector.from(ConversationBean.class).where("conversationpeople","=",
					// MyApplication.getInstance().getUserBean().getVoipAccount()+u.getVoipAccout()));

					dbUtils.deleteById(ConversationBean.class, MyApplication
							.getInstance().getUserBean().getVoipAccount()
							+ u.getVoipAccout());
					dbUtils.deleteById(AddFriendRequestBean.class, u.getUid());
					if (u.getRemark().trim().equals("")) {
						MyApplication.friends.remove(u.getNickname());
					} else {
						MyApplication.friends.remove(u.getRemark());
					}
					MyApplication.users.remove(u);
				} catch (DbException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
			}
		}
	};
	private String pinyin;
	private BroadcastReceiver receiver;
	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_friends);
		addFriendRequsrt = getIntent().getBooleanExtra("addFriendRequsrt",
				false);
		search = (ImageView) findViewById(R.id.search);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		search.setOnClickListener(this);
		findViewById(R.id.newfriends).setOnClickListener(this);
		findViewById(R.id.mark).setOnClickListener(this);
//		refreshBc();
		dbUtils = MyApplication.dbUtils;
		if (MyApplication.friends == null || MyApplication.friends.size() == 0) {
			getAllFriends();
		} else {
			initView();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalConstant.ACTION_READ_ADDFRIEND);
		filter.addAction(GlobalConstant.ACTION_UPDATE_FRIENDS);
		filter.addAction(GlobalConstant.ACTION_ADDFRIEND_REQUEST);
		registerReceiver(broadcast, filter);
		// addFriendsRequest();
	}

//	private void refreshBc() {
//		receiver = new BroadcastReceiver() {
//			// private MsgBean msgBean;
//			@Override
//			public void onReceive(Context context, Intent intent) {
//				getAllFriends();
//			}
//		};
//		IntentFilter filter = new IntentFilter();// 构造过滤器对象
//		filter.addAction("com.abcs.refreshfriendsbc");
//		FriendsActivity.this.registerReceiver(receiver, filter);
//	}

	public void getAllFriends() {
		Tool.showProgressDialog(this, "正在加载...", true);
		HttpRequest.sendGet(TLUrl.URL_GET_VOIP + "User/findfriendUser", "uid="
				+ MyApplication.getInstance().getUid() + "&page=1"
				+ "&size=1000", new HttpRevMsg() {
			@Override
			public void revMsg(String msg) {
				Tool.removeProgressDialog();
				Log.i("xbb好友", "好友：" + msg);
				if (msg.length() <= 0) {
					loadFriendsFailed();
				}
				try {
					JSONObject jsonObject = new JSONObject(msg);
					if (jsonObject.getInt("status") == 1) {
						if (MyApplication.users == null) {
							MyApplication.users = new ArrayList<>();
						}
						JSONArray jsonArray = jsonObject.getJSONArray("msg");
						if (jsonArray.length() == 0) {
							myhandler.sendEmptyMessage(0);
							return;
						}
						MyApplication.users.clear();
						list.clear();
						// List<String> nums = new ArrayList<String>();
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject object = jsonArray.getJSONObject(i);
							User user = new User();
							user.setVoipAccout(object.getString("voipAccount"));
							user.setNickname(object.getString("nickname"));
							user.setUid(object.getInt("frienduid"));
							user.setAvatar(object.getString("avatar"));
							user.setRemark(object.getString("remarks"));
							MyApplication.users.add(user);
							if (user.getRemark().trim().equals("")) {
								list.put(user.getNickname(), user);
							} else {
								list.put(user.getRemark(), user);
							}
						}
						myhandler.sendEmptyMessage(1);
					} else
						loadFriendsFailed();
				} catch (JSONException e) {
					e.printStackTrace();
					loadFriendsFailed();
				} finally {
					// sortListView.stopRefresh();
				}
			}

			private void loadFriendsFailed() {
				Tool.showInfo(FriendsActivity.this, "服务器异常");
			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
		unregisterReceiver(broadcast);
	}

	public List<User> getNums() {
		return nums;
	}

	@SuppressLint("ClickableViewAccessibility")
	private void initView() {
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinFriendsComparator();
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		etNum = (EditText) findViewById(R.id.et_num);
		unread = (ImageView) findViewById(R.id.unread);
		if (addFriendRequsrt) {
			unread.setVisibility(View.VISIBLE);
		}
		findViewById(R.id.bt_numOK).setOnClickListener(this);
		findViewById(R.id.newfriends).setOnClickListener(this);
		findViewById(R.id.mark).setOnClickListener(this);
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
		// sortListView.setPullRefreshEnable(true);
		// sortListView.setPullLoadEnable(false);
		// sortListView.setXListViewListener(new IXListViewListener() {
		//
		// @Override
		// public void onRefresh() {
		// getAllFriends();
		// }
		//
		// @Override
		// public void onLoadMore() {
		//
		// }
		// });
		// sortListView.setOnTouchListener(new OnTouchListener() {
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// try {
		// InputMethodManager imm = (InputMethodManager) FriendsActivity.this
		// .getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(
		// mClearEditText.getWindowToken(), 0);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return false;
		// }
		// });
		sortListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				showDeleDailog(position);
				return true;
			}
		});
		sortListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				User user = SourceDateList.get(position);
				Log.i("xbbdasda", user.getVoipAccout() + 3);
				Intent intent = new Intent(FriendsActivity.this,
						FriendDetailsActivity.class);
				// intent.putExtra("friend", user.getVoipAccout());
				intent.putExtra("friend", user);
				startActivity(intent);
			}
		});
		setData();
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

	private void setData() {
		SourceDateList.clear();
		SourceDateList.addAll(filledData(MyApplication.friends.keySet()));
		Collections.sort(SourceDateList, pinyinComparator);
		if (adapter == null) {
			adapter = new FriendsSortAdapter(FriendsActivity.this,
					SourceDateList);
			sortListView.setAdapter(adapter);
		} else
			adapter.notifyDataSetChanged();
	}

	// public void setAllContacts(HashMap<String, List<String>> result) {
	// contacts = result;
	// Tool.removeProgressDialog();
	// initView();
	// }

	@SuppressLint("DefaultLocale")
	private List<User> filledData(Set<String> set) {
		List<User> mSortList = new ArrayList<User>();
		for (String s : set) {
			Log.i("xbbcc", s);
			User sortModel = new User();
			sortModel.setVoipAccout(MyApplication.friends.get(s)
					.getVoipAccout());
			sortModel.setNickname(MyApplication.friends.get(s).getNickname());
			sortModel.setRemark(MyApplication.friends.get(s).getRemark());
			sortModel.setUid(MyApplication.friends.get(s).getUid());
			sortModel.setVoipAccout(MyApplication.friends.get(s)
					.getVoipAccout());
			sortModel.setAvatar(MyApplication.friends.get(s).getAvatar());
			// pinyin =
			// characterParser.getSelling(GlobalConstant.friends.get(s).getNickname());
			if (MyApplication.friends.get(s).getRemark().trim().equals("")) {
				pinyin = characterParser.getSelling(MyApplication.friends
						.get(s).getNickname());
			} else {
				pinyin = characterParser.getSelling(MyApplication.friends
						.get(s).getRemark());
			}

			String sortString = pinyin.substring(0, 1)
					.toUpperCase(Locale.CHINA);
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase(Locale.CHINA));
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
				String name = user.getNickname();
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
		case R.id.search:
			Intent intent = new Intent(this, SearchActivity.class);
			intent.putExtra("flag", "searchfriend");
			startActivityForResult(intent, 888);
			break;
		case R.id.back:
			finish();
			break;
		case R.id.newfriends:
			startActivity(new Intent(FriendsActivity.this,
					NewFriendsActivity.class));
			break;
		case R.id.mark:
			startActivity(new Intent(FriendsActivity.this,
					LabelListActivity.class));
			break;
		}

	}

	// protected void addFriends(final String friendId) {
	// myhandler.post(new Runnable() {
	// @Override
	// public void run() {
	// HttpRequest.sendGet(TLUrl.getInstance().URL_GET_VOIP
	// + "User/addMongoDBfrienduser", "uid="
	// + MyApplication.getInstance().getUid() + "&frienduid="
	// + friendId, new HttpRevMsg() {
	// @Override
	// public void revMsg(String msg) {
	// Log.i("xbb3", msg);
	// if (msg.length() <= 0) {
	// return;
	// }
	// try {
	// JSONObject jsonObject = new JSONObject(msg);
	// if (jsonObject.getInt("status") == 1) {
	// Message message = myhandler.obtainMessage(5,
	// jsonObject.getString("msg"));
	// myhandler.sendMessage(message);
	// } else {
	// Message message = myhandler.obtainMessage(2,
	// jsonObject.getString("msg"));
	// myhandler.sendMessage(message);
	// }
	//
	// } catch (JSONException e) {
	// Log.i("xbb2", e.toString());
	// e.printStackTrace();
	// }
	// }
	// });
	// }
	// });
	// }

	private void showDeleDailog(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				FriendsActivity.this); // 先得到构造器
		builder.setTitle("删除好友"); // 设置标题
		builder.setMessage("真的要删除" + SourceDateList.get(position).getNickname()
				+ "?"); // 设置内容
		// builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // 设置确定按钮
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteFriend(position);
						dialog.dismiss(); // 关闭dialog
					}
				});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // 设置取消按钮
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
		// 参数都设置完成了，创建并显示出来
		builder.create().show();
	}

	private void deleteFriend(final int position) {
		User user = (User) adapter.getItem(position);
		HttpRequest.sendGet(TLUrl.URL_GET_VOIP + "User/deletefrienduid",
				"uid=" + MyApplication.getInstance().getUid() + "&frienduid="
						+ user.getUid(), new HttpRevMsg() {
					@Override
					public void revMsg(String msg) {
						Log.i("xbb9", msg);
						if (msg.length() <= 0) {
							return;
						}
						try {
							JSONObject jsonObject = new JSONObject(msg);
							Message message = new Message();
							if (jsonObject.getInt("status") == 1) {
								message.arg1 = position;
								message.what = 7;
							} else {
								message.what=8;
							}
							myhandler.sendMessage(message);
						} catch (JSONException e) {
							Log.i("xbb2", e.toString());
							e.printStackTrace();
						}
					}
				});
	}

	class AddFriendBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			switch (intent.getAction()) {
			case GlobalConstant.ACTION_READ_ADDFRIEND:
				unread.setVisibility(View.GONE);
				addFriendRequsrt = false;
				break;
			case GlobalConstant.ACTION_UPDATE_FRIENDS:
				setData();
				break;
			case GlobalConstant.ACTION_ADDFRIEND_REQUEST:
				unread.setVisibility(View.VISIBLE);
				addFriendRequsrt = true;
				break;
			}
		}
	}
}
