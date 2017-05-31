package com.abcs.haiwaigou.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/4/18.
 */
public class AddressRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView t_name;
    public TextView t_phone;
    public TextView t_address;
    public ImageView img_delete;
    public ImageView img_edit;
    public ImageView img_default;
    public LinearLayout linear_root;
    public LinearLayout linear_edit;
    public LinearLayout linear_delete;
    public RelativeLayout relative_root;
    public CheckBox radio_check;
    public TextView t_default;
    public TextView t_id_card;
//    public TextView t_isdefault;
    public AddressRecyclerViewHolder(View itemView) {
        super(itemView);
        t_name = (TextView) itemView.findViewById(R.id.t_name);
        t_phone = (TextView) itemView.findViewById(R.id.t_phone);
        t_address = (TextView) itemView.findViewById(R.id.t_address);
        img_delete = (ImageView) itemView.findViewById(R.id.img_delete);
        img_edit = (ImageView) itemView.findViewById(R.id.img_edit);
        img_default = (ImageView) itemView.findViewById(R.id.img_default);
        linear_root = (LinearLayout) itemView.findViewById(R.id.linear_root);
        linear_edit = (LinearLayout) itemView.findViewById(R.id.linear_edit);
        linear_delete = (LinearLayout) itemView.findViewById(R.id.linear_delete);
        t_default = (TextView) itemView.findViewById(R.id.t_default);
        t_id_card = (TextView) itemView.findViewById(R.id.t_id_card);
//        t_isdefault = (TextView) itemView.findViewById(R.id.t_isdefault);
        radio_check = (CheckBox) itemView.findViewById(R.id.radio_check);

    }
}
