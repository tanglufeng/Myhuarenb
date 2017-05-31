package com.abcs.sociax.t4.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.baidu.mapapi.search.core.PoiInfo;

import java.util.List;

public class PoiAdapter extends BaseAdapter {

    private List<PoiInfo> data;
    private LayoutInflater inflater;

    public PoiAdapter(@NonNull Context context, List<PoiInfo> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public PoiInfo getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_poi_search, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PoiInfo poiItem = getItem(position);
        holder.tvLocationStreet.setText(poiItem.name);
        holder.tvLocationDetail.setText(poiItem.address);

        return convertView;
    }

    private class ViewHolder {
        private TextView tvLocationStreet, tvLocationDetail;

        public ViewHolder(View parent) {
            tvLocationStreet = (TextView) parent.findViewById(R.id.tv_location_street);
            tvLocationDetail = (TextView) parent.findViewById(R.id.tv_location_detail);
        }
    }
}
