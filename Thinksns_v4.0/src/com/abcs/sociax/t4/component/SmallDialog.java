package com.abcs.sociax.t4.component;

import com.abcs.sociax.android.R;

import android.content.Context;

public class SmallDialog extends CustomerDialogNoTitle {
	public SmallDialog(Context context, String title) {
		super(context, R.style.myDialog, R.layout.small_dailog, title);
	}

	public SmallDialog(Context context, String title, float margin) {
		super(context, R.style.myDialog, R.layout.small_dailog, margin, title);
	}
}
