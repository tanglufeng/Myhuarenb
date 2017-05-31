package com.abcs.huaqiaobang.ytbt.voicemeeting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.db.sqlite.Selector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LastContactsFragment extends Fragment {
	private ListView lv;
	private List<ContactEntity> list = new ArrayList<>();

	public LastContactsFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			list = MyApplication.dbUtils.findAll(Selector.from(
					ContactEntity.class).orderBy("time", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_last_contact, null);
		initView(v);
		return v;
	}

	private void initView(View v) {
		lv = (ListView) v.findViewById(R.id.lv);
		lv.setEmptyView(v.findViewById(R.id.empty_tip_recommend_bind_tv));
		lv.setAdapter(new MyAdapter());
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				if (!InviteContactActivity.isNums) {
					ContactEntity entity = list.get(position);
					entity.setTime(new Date().getTime());
					((InviteContactActivity) getActivity()).exit(entity);
//				}else{
//					
//				}
			}
		});
	}

	class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			try {
				ViewHolder holder = null;
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = View.inflate(getActivity(),
							R.layout.fragment_last_contact_item, null);
					holder.name = (TextView) convertView
							.findViewById(R.id.tv_name);
					holder.num = (TextView) convertView
							.findViewById(R.id.tv_num);
					holder.time = (TextView) convertView
							.findViewById(R.id.tv_time);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				ContactEntity entity = list.get(position);
				holder.name.setText(entity.getName());
				holder.num.setText(entity.getNum());
				holder.time.setText(Tool.getTime(entity.getTime()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}

		class ViewHolder {
			TextView name;
			TextView num;
			TextView time;
		}
	}

}
