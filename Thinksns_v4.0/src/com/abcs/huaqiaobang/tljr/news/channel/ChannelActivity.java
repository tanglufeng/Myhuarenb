package com.abcs.huaqiaobang.tljr.news.channel;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.main.MainActivity;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.tljr.news.HuanQiuShiShi;
import com.abcs.huaqiaobang.tljr.news.channel.adapter.ChannelListAdapter;
import com.abcs.huaqiaobang.tljr.news.channel.adapter.DragAdapter;
import com.abcs.huaqiaobang.tljr.news.channel.bean.ChannelItem;
import com.abcs.huaqiaobang.tljr.news.channel.view.ChannelListView;
import com.abcs.huaqiaobang.tljr.news.channel.view.DragGrid;
import com.abcs.huaqiaobang.tljr.news.store.ServerUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 频道管理
 * 
 * @Author RA
 * @Blog http://blog.csdn.net/vipzjyno1
 */
public class ChannelActivity extends BaseActivity implements OnItemClickListener {
	/** 用户栏目的GRIDVIEW */
	private DragGrid userGridView;
	/** 其它栏目的GRIDVIEW */
	// private OtherGridView otherGridView;
	/** 用户栏目对应的适配器，可以拖动 */
	public static DragAdapter userAdapter;
	/** 其它栏目对应的适配器 */
	// public static OtherAdapter otherAdapter;

	/** 用户栏目列表 */
	public static ArrayList<ChannelItem> userChannelList;
	/** 其它栏目列表 */
	public static ArrayList<ChannelItem> otherChannelList;

	/** 重要频道列表 */
	ArrayList<ChannelItem> impNewsChannelList = new ArrayList<ChannelItem>();
	/** 推荐（普通）频道列表 */
	ArrayList<ChannelItem> comNewsChannelList = new ArrayList<ChannelItem>();
	/** 其他频道列表 */
	ArrayList<ChannelItem> otherNewsChannelList = new ArrayList<ChannelItem>();

	RadioGroup radioGroup;

	/** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
	boolean isMove = false;

	ChannelListAdapter channelListAdapter;
	ChannelListView channelListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tljr_activity_news_channel);
		initView();
		initData();
	}

	/** 初始化数据 */
	private void initData() {
		userChannelList = new ArrayList<ChannelItem>(MainActivity.userChannelList);
		otherChannelList = new ArrayList<ChannelItem>(MainActivity.otherChannelList);
	 

		for (ChannelItem cItem : otherChannelList) {
			if (cItem.getOtherChannel() == 1 || cItem.getOtherChannel() == 2) {
				otherNewsChannelList.add(cItem);
			} else {
				if (cItem.getChannelType() == 0) {
					impNewsChannelList.add(cItem);
				}
				if (cItem.getChannelType() == 1) {
					comNewsChannelList.add(cItem);
				}
			}
		}

		channelListAdapter = new ChannelListAdapter(this, comNewsChannelList); // 一开始首选为推荐频道，所以加载推荐频道的数据
		channelListView.setAdapter(channelListAdapter);

		userAdapter = new DragAdapter(this);
		userGridView.setAdapter(userAdapter);
		// otherAdapter = new OtherAdapter(this, otherChannelList);
		// otherGridView.setAdapter(this.otherAdapter);
		// 设置GRIDVIEW的ITEM的点击监听
		// otherGridView.setOnItemClickListener(this);
		userGridView.setOnItemClickListener(this);

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkId) {
				switch (checkId) {
				case R.id.tljr_hqss_channel_rg_0:
					channelListAdapter.setChannelList(comNewsChannelList);
					break;
				case R.id.tljr_hqss_channel_rg_1:
					channelListAdapter.setChannelList(impNewsChannelList);
					break;
				case R.id.tljr_hqss_channel_rg_2:
					channelListAdapter.setChannelList(otherNewsChannelList);
					break;

				default:
					break;
				}

			}
		});

	}

	/** 初始化布局 */
	private void initView() {
		userGridView = (DragGrid) findViewById(R.id.userGridView);
		// otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
		radioGroup = (RadioGroup) findViewById(R.id.tljr_hqss_channel_rg);

		channelListView = (ChannelListView) findViewById(R.id.channelListView);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void btnConfirm(View view) {
		this.setResult(HuanQiuShiShi.RESULT_CODE_TRUE);
		
		if(userChannelList.size()<=0){
			Toast.makeText(this, "至少保留一个频道", Toast.LENGTH_SHORT).show();
			return ;
		}
		
		saveChannel();
		finish();
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

		if (!ServerUtils.isConnect(this)) {
			return;
		}

		
		/*
		 * 打包用户频道排序习惯上传到服务端
		 */
		JSONArray array = new JSONArray();
		for (int i = 0; i < userChannelList.size(); i++) {
			ChannelItem item = userChannelList.get(i);
			JSONObject obj = JSONObject.parseObject(JSON.toJSONString(item));
			array.add(obj);
		}
		JSONArray array2 = new JSONArray();
		for (int i = 0; i < otherChannelList.size(); i++) {
			ChannelItem item = otherChannelList.get(i);
			JSONObject obj = JSONObject.parseObject(JSON.toJSONString(item));
			array2.add(obj);
		}
		JSONObject js = new JSONObject();
		js.put("selected", array.toString());
		js.put("inSelected", array2.toString());
		if (MyApplication.getInstance().self != null) {
			String url = TLUrl.getInstance().URL_newsInitChannel;
			String params = "type=2&pId=" + MyApplication.getInstance().self.getId();
			System.out.println(url + "?" + params);
			System.out.println(js.toJSONString());
			Log.i("uphao", js.toJSONString());
			HttpRequest.sendPost(TLUrl.getInstance().URL_newsInitChannel, "type=2&pId=" + MyApplication.getInstance().self.getId()
					+ "&data=" + js.toJSONString(), new HttpRevMsg() {
				@Override
				public void revMsg(String msg) {
					// TODO Auto-generated method stub
					System.out.println("--上传频道msg:" + msg);

				}
			});
		}

	}

	public void btnCancel(View view) {
		this.setResult(HuanQiuShiShi.RESULT_CODE_FALSE);
//		finish();
//		MainActivity.huanQiuShiShi.adapter.notifyDataSetChanged();
//		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_right);
	}

	/** GRIDVIEW对应的ITEM点击监听接口 */
	@Override
	public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
		// 如果点击的时候，之前动画还没结束，那么就让点击事件无效
		if (isMove) {
			return;
		}
		switch (parent.getId()) {	
		case R.id.userGridView: {
			// position为 0，1 的不可以进行任何操作
			// if (position != 0 && position != 1) {
			final ImageView moveImageView = getView(view);
			if (moveImageView != null) {
				TextView newTextView = (TextView) view.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final ChannelItem channel = ((DragAdapter) parent.getAdapter()).getItem(position);// 获取点击的频道内容
				// otherAdapter.setVisible(false);
				// 添加到最后一个

				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							// 获取终点的坐标
							// otherAdapter.addItem(channel);
							// otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
							userAdapter.setRemove(position);
							MoveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
							if (channel.getOtherChannel() == 1 || channel.getOtherChannel() == 2) {
								otherNewsChannelList.add(channel);
							} else {
								if (channel.getChannelType() == 0) {
									impNewsChannelList.add(channel);
								}

								if (channel.getChannelType() == 1) {
									comNewsChannelList.add(channel);
								}

							}

							channelListAdapter.notifyDataSetChanged();
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
		}
			break;

		default:
			break;
		}
	}

	/**
	 * 点击ITEM移动动画
	 * 
	 * @param moveView
	 * @param startLocation
	 * @param endLocation
	 * @param moveChannel
	 * @param clickGridView
	 */
	private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final ChannelItem moveChannel,
			final GridView clickGridView) {
		int[] initLocation = new int[2];
		// 获取传递过来的VIEW的坐标
		moveView.getLocationInWindow(initLocation);
		// 得到要移动的VIEW,并放入对应的容器中
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
		// 创建移动动画

		moveViewGroup.removeView(mMoveView);
		// instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
		if (clickGridView instanceof DragGrid) {
			// otherAdapter.setVisible(true);
			// otherAdapter.notifyDataSetChanged();
			userAdapter.remove();
		} else {
			userAdapter.setVisible(true);
			userAdapter.notifyDataSetChanged();
			// otherAdapter.remove();
		}

 
	}

	/**
	 * 获取移动的VIEW，放入对应ViewGroup布局容器
	 * 
	 * @param viewGroup
	 * @param view
	 * @param initLocation
	 * @return
	 */
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}

	/**
	 * 创建移动的ITEM对应的ViewGroup布局容器
	 */
	private ViewGroup getMoveViewGroup() {
		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(this);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}

	/**
	 * 获取点击的Item的对应View，
	 * 
	 * @param view
	 * @return
	 */
	private ImageView getView(View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(cache);
		return iv;
	}

	/** 退出时候保存选择后数据库的设置 */
	private void saveChannel() {
		 
		saveUserChannel(userAdapter.getChannnelLst());
		
		
		ArrayList<ChannelItem> temp_list = new ArrayList<ChannelItem>(userAdapter.getChannnelLst());
		
		MainActivity.userChannelList = (ArrayList<ChannelItem>) temp_list;

		 
		
		
		otherChannelList = new ArrayList<ChannelItem>();
		for (ChannelItem item : impNewsChannelList) {
			otherChannelList.add(item);
		}
		for (ChannelItem item : comNewsChannelList) {
			otherChannelList.add(item);
		}
		for (ChannelItem item : otherNewsChannelList) {
			otherChannelList.add(item);
		}
		MainActivity.otherChannelList = otherChannelList;
		saveOtherChannel(otherChannelList);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
//		this.setResult(HuanQiuShiShi.RESULT_CODE_FALSE);
//		MainActivity.huanQiuShiShi.adapter.notifyDataSetChanged();
//		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		
	}

	/**
	 * 保存用户频道到数据库
	 * 
	 * @param userList
	 */
	public void saveUserChannel(List<ChannelItem> userList) {
		for (int i = 0; i < userList.size(); i++) {
			ChannelItem channelItem = (ChannelItem) userList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(Integer.valueOf(1));
		}
	}

	/**
	 * 保存其他频道到数据库
	 * 
	 * @param otherList
	 */
	public void saveOtherChannel(List<ChannelItem> otherList) {
		for (int i = 0; i < otherList.size(); i++) {
			ChannelItem channelItem = (ChannelItem) otherList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(Integer.valueOf(0));
		}
	}

}