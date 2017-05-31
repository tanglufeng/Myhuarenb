package com.abcs.haiwaigou.view.autosrollup;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import static com.abcs.sociax.android.R.id.img_icon;
import static com.abcs.sociax.android.R.id.t_text;
import static com.abcs.sociax.android.R.id.t_time;
import static com.abcs.sociax.android.R.id.t_tips;
import static com.abcs.sociax.android.R.id.t_title;


/**
 * <pre>
 * 京东快报 自动向上滚动的广告基类
 * 内部包含TextView的自动向上滚动
 * 
 * <pre>
 *
 * @param <T>
 */
public abstract class BaseAutoScrollUpTextView<T> extends ListView implements
		AutoScrollData<T> {

	/**
	 * 数据源
	 */
	private ArrayList<T> mDataList = new ArrayList<T>();
	
	/**
	 * 字体大小
	 */
	private float mSize=14;

	/**
	 * 数据总数
	 */
	private int mMax;

	private int position = -1;

	/**
	 * 向上滚动距离
	 */
	private int scroll_Y;

	private int mScrollY;

	/**
	 * 适配器
	 */
	private AutoScrollAdapter mAutoScrollAdapter = new AutoScrollAdapter();

	/**
	 * 监听器
	 */
	private OnItemClickListener mOnItemClickListener;

	private long mTimer = 4000;

	private Context mContext;

	/**
	 * 获取高度
	 * 
	 * @return
	 */
	protected abstract int getAdertisementHeight();

	private Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// 开启轮播
			switchItem();
			handler.postDelayed(this, mTimer);
		}
	};

	public interface OnItemClickListener {
		public void onItemClick(int position);
	}

	public BaseAutoScrollUpTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		mScrollY = dip2px(getAdertisementHeight());
		init();

	}

	public BaseAutoScrollUpTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BaseAutoScrollUpTextView(Context context) {
		this(context, null);
	}

	private void init() {
		this.setDivider(null);
		this.setFastScrollEnabled(false);
		this.setDividerHeight(0);
		this.setEnabled(false);
	}

	/**
	 * dp-->px
	 * 
	 * @param dipValue
	 * @return
	 */
	private int dip2px(float dipValue) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 开始轮播
	 */
	private void switchItem() {
		if (position == -1) {
			scroll_Y = 0;
		} else {
			scroll_Y = mScrollY;
		}
		smoothScrollBy(scroll_Y, 3000);
		setSelection(position);
		position++;
	}

	/**
	 * 广告条适配器
	 * 
	 *
	 */
	private class AutoScrollAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			final int count = mDataList == null ? 0 : mDataList.size();
			return count > 1 ? Integer.MAX_VALUE : count;
		}

		@Override
		public Object getItem(int position) {
			return mDataList.get(position % mMax);
		}

		@Override
		public long getItemId(int position) {
			return position % mMax;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder viewHolder;
			if (null == convertView) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.view_scroll_banner2, null);

				viewHolder.relative_scroll = (RelativeLayout) convertView.findViewById(R.id.relative_scroll);
				viewHolder.img_icon = (com.abcs.huaqiaobang.view.CircleImageView) convertView.findViewById(img_icon);
				viewHolder.t_title = (TextView) convertView.findViewById(t_title);
				viewHolder.t_time = (TextView) convertView.findViewById(t_time);
				viewHolder.t_tips = (TextView) convertView.findViewById(t_tips);
				viewHolder.t_text = (TextView) convertView.findViewById(t_text);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			T data = mDataList.get(position % mMax);

			viewHolder.relative_scroll
					.setLayoutParams(new AbsListView.LayoutParams(
							AbsListView.LayoutParams.WRAP_CONTENT,
							dip2px(getAdertisementHeight())));

			viewHolder.t_title.setTextSize(mSize);
			viewHolder.t_time.setTextSize(mSize);
//			viewHolder.t_tips.setTextSize(mSize);
			viewHolder.t_text.setTextSize(mSize);

			if (!TextUtils.isEmpty(getIcon(data))) {
				ImageLoader.getInstance().displayImage(getIcon(data), viewHolder.img_icon, Options.getCornerListOptions());
				viewHolder.t_text.setVisibility(View.GONE);
			} else {
				viewHolder.img_icon.setImageResource(R.drawable.img_local_default);
				if (!TextUtils.isEmpty(getTextTitle(data))) {
					viewHolder.t_text.setText(getTextTitle(data).substring(0, 1));
					viewHolder.t_text.setVisibility(View.VISIBLE);
				}
			}

//        img_icon.setImageBitmap(CreateBitmap.create(msg.getTypeName().substring(0, 1),activity));
			viewHolder.t_title.setText(getTextTitle(data));

			if (getTime(data) < 2 * 1000000000) {
				viewHolder.t_time.setText(Util.format1.format(getTime(data) * 1000));
			} else {
				viewHolder.t_time.setText(Util.format1.format(getTime(data)));
			}
//        t_time.setText(Util.format1.format(msg.getCreateTime()));
			viewHolder.t_tips.setVisibility(TextUtils.isEmpty(getTips(data)) ? View.GONE : View.VISIBLE);
			viewHolder.t_tips.setText(getTips(data));

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mOnItemClickListener.onItemClick(position % mMax);
				}
			});

			return convertView;
		}
	}

	static class ViewHolder {
		RelativeLayout relative_scroll ;
		com.abcs.huaqiaobang.view.CircleImageView img_icon ;
		TextView t_title;
		TextView t_time ;
		TextView t_tips ;
		TextView t_text;

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}

	/**
	 * 添加数据
	 * 
	 * @param _datas
	 */
	public void setData(ArrayList<T> _datas) {
		mDataList.clear();
		mDataList.addAll(_datas);
		mMax = mDataList == null ? 0 : mDataList.size();
		this.setAdapter(mAutoScrollAdapter);
		mAutoScrollAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 设置文字大小
	 */
	public void setTextSize(float _size){
		this.mSize=_size;
	}

	/**
	 * 设置监听事件
	 */
	public void setOnItemClickListener(OnItemClickListener _listener) {
		this.mOnItemClickListener = _listener;
	}

	/**
	 * 设置轮播间隔时间
	 * 
	 * @param _time
	 *            毫秒单位
	 */
	public void setTimer(long _time) {
		this.mTimer = _time;
	}

	/**
	 * 开启轮播
	 */
	public void start() {
		handler.postDelayed(runnable, 4000);
	}

	/**
	 * 关闭轮播
	 */
	public void stop() {
		handler.removeCallbacks(runnable);
	}

}
