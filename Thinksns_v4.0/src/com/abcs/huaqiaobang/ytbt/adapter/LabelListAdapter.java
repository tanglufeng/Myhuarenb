package com.abcs.huaqiaobang.ytbt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.bean.LabelBean;

import java.util.List;

public class LabelListAdapter extends ArrayAdapter<LabelBean> {
	private Context context;
	private List<LabelBean> labels;
	private int resource;

	public LabelListAdapter(Context context, int resource,
			List<LabelBean> labels) {
		super(context, resource, labels);
		this.context = context;
		this.labels = labels;
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LabelBean bean = labels.get(position);
		convertView = View.inflate(context, resource, null);
		TextView textView = (TextView) convertView.findViewById(R.id.label_name);
		textView.setText(bean.getLabelName());
		return convertView;
	}
	
	

}
