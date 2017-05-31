package com.abcs.huaqiaobang.ytbt.voicemeeting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.sortlistview.CharacterParser;
import com.abcs.huaqiaobang.ytbt.sortlistview.ClearEditText;
import com.abcs.huaqiaobang.ytbt.sortlistview.PinyinComparator;
import com.abcs.huaqiaobang.ytbt.sortlistview.SideBar;
import com.abcs.huaqiaobang.ytbt.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.abcs.huaqiaobang.ytbt.sortlistview.SortAdapter;
import com.abcs.huaqiaobang.ytbt.sortlistview.SortModel;
import com.abcs.huaqiaobang.ytbt.util.GlobalConstant;
import com.abcs.huaqiaobang.ytbt.util.Tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class List_Contact_Fragment extends Fragment implements OnClickListener {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private EditText etNum;
	private ClearEditText mClearEditText;
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	private PinyinComparator pinyinComparator;
	private View v;
	private ContactBroadCastReceiver receiver;
	private List<SortModel> nums = new ArrayList<>();
	private int checkNum = 0;
	public int getCheckNum() {
		return checkNum;
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.activity_contacts_list, null);
		if (MyApplication.contacts.size() == 0) {
			Tool.showProgressDialog(getActivity(), "正在加载联系人，请稍等...",false);
			receiver = new ContactBroadCastReceiver();
			getActivity().registerReceiver(receiver,
					new IntentFilter(GlobalConstant.ACTION_READ_CONTACT));
		} else {
			initView();
		}
		return v;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			getActivity().unregisterReceiver(receiver);
		}

	}

	public List<SortModel> getNums() {
		return nums;
	}

	private void initView() {
		v.findViewById(R.id.relativeLayout1).setVisibility(View.GONE);
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		setCheckNum();
		sideBar = (SideBar) v.findViewById(R.id.sidrbar);
		dialog = (TextView) v.findViewById(R.id.dialog);
		etNum = (EditText) v.findViewById(R.id.et_num);
		v.findViewById(R.id.bt_numOK).setOnClickListener(this);
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
		sortListView = (ListView) v.findViewById(R.id.country_lvcountry);
		sortListView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					InputMethodManager imm = (InputMethodManager) getActivity()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
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
				if (InviteContactActivity.isNums) {
					// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
					SortAdapter.ViewHolder holder = (SortAdapter.ViewHolder) view
							.getTag();
					// 改变CheckBox的状态
					holder.cb.toggle();
					// 将CheckBox的选中状况记录下来
					SortAdapter.getIsSelected().put(position,
							holder.cb.isChecked());
					SortModel s = (SortModel) adapter.getItem(position);
					if (holder.cb.isChecked()) {
						nums.add(s);
						checkNum++;  
						setCheckNum();
						((InviteContactActivity)getActivity()).getTv_checked_cancel().setVisibility(View.VISIBLE);
						((InviteContactActivity)getActivity()).getBack().setVisibility(View.GONE);
					} else {
						nums.remove(s);
						checkNum--;  
						if(checkNum<=0){
							((InviteContactActivity)getActivity()).getTv_checked_cancel().setVisibility(View.GONE);
							((InviteContactActivity)getActivity()).getBack().setVisibility(View.VISIBLE);
						}
						setCheckNum();
					}
				}else{
				// Toast.makeText(getApplication(),
				// ((SortModel) adapter.getItem(position)).getName(),
				// Toast.LENGTH_SHORT).show();+
				 ContactEntity entity = new ContactEntity();
				 entity.setName(((SortModel) adapter.getItem(position))
						 .getName());
				 entity.setNum(((SortModel) adapter.getItem(position))
						 .getNum());
				 entity.setTime(new Date().getTime());
				 ((InviteContactActivity) getActivity()).exit(entity);
				}
			}
		});

		SourceDateList = filledData(MyApplication.contacts.keySet());
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(getActivity(), SourceDateList);
		sortListView.setAdapter(adapter);
		mClearEditText = (ClearEditText) v.findViewById(R.id.filter_edit);
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

	private void setCheckNum() {
		((InviteContactActivity)getActivity()).getTv_check().setText(getString(R.string.str_check_num, checkNum));
	}
	void cancelAllChecked(){
		 // 遍历list的长度，将已选的按钮设为未选  
        for (int i = 0; i < SourceDateList.size(); i++) {  
            if (adapter.getIsSelected().get(i)) {  
            	adapter.getIsSelected().put(i, false);  
                checkNum--;// 数量减1  
            }  
        }  
        ((InviteContactActivity)getActivity()).getTv_checked_cancel().setVisibility(View.GONE);
		((InviteContactActivity)getActivity()).getBack().setVisibility(View.VISIBLE);
		adapter.notifyDataSetChanged();
		setCheckNum();
	}
	// public void setAllContacts(HashMap<String, List<String>> result) {
	// contacts = result;
	// Tool.removeProgressDialog();
	// initView();
	// }

	private List<SortModel> filledData(Set<String> set) {
		List<SortModel> mSortList = new ArrayList<SortModel>();
		for (String s : set) {
			SortModel sortModel = new SortModel();
			sortModel.setName(MyApplication.contacts.get(s));
			sortModel.setNum(s);
			String pinyin = characterParser.getSelling(MyApplication.contacts
					.get(s));
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
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_numOK:
			String num = etNum.getText().toString();
			if (TextUtils.isEmpty(num)) {
				Toast.makeText(getActivity(), "号码输入有误", Toast.LENGTH_SHORT)
						.show();
			} else {
				ContactEntity entity = new ContactEntity();
				entity.setName(num);
				entity.setNum(num);
				entity.setTime(new Date().getTime());
				((InviteContactActivity) getActivity()).exit(entity);
			}
			break;
		}
	}

	class ContactBroadCastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Tool.removeProgressDialog();
			initView();
		}
	}

}
