package com.abcs.sociax.t4.android.popupwindow;

import com.abcs.sociax.t4.android.Thinksns;
import com.abcs.sociax.t4.model.ModelTask;
import com.abcs.sociax.android.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 类说明：
 * 
 * @author wz
 * @date 2014-11-25
 * @version 1.0
 */
public class PopupWindowTaskDetail {
	private ImageView pic;
	private TextView tv_task_name, tv_task_price, tv_describ, tv_cancle;
	LayoutInflater inflater;
	View view;
	Dialog dialog = null;
	private Thinksns application;
	
	public PopupWindowTaskDetail(Context context, ModelTask mdtask, int style) {
		this.inflater=LayoutInflater.from(context);
		view=inflater.inflate(R.layout.dialog_task_detail, null);
		application = (Thinksns) context.getApplicationContext();
		
		pic = (ImageView) view.findViewById(R.id.img_taskimg);
		tv_task_name = (TextView) view.findViewById(R.id.tv_task_name);
		tv_task_price = (TextView) view.findViewById(R.id.tv_task_desc);
		tv_describ = (TextView) view.findViewById(R.id.tv_task_type);
		tv_cancle = (TextView) view.findViewById(R.id.tv_task_status);
		if (mdtask.getImg().equals("")) {
			pic.setImageResource(R.drawable.default_task);
		} else {
//			pic.setImageUrl(mdtask.getImg());
			
			application.displayImage(mdtask.getImg(),pic);
		}
		tv_task_name.setText(mdtask.getTask_name());
		tv_task_price.setText(mdtask.getReward());
		tv_describ.setText(mdtask.getStep_desc());
		tv_cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog = new Dialog(context, R.style.my_dialog);
        dialog.setContentView(view);
        dialog.show();
	}
}
