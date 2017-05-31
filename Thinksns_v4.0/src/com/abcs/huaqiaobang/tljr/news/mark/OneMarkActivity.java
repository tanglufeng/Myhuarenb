package com.abcs.huaqiaobang.tljr.news.mark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.activity.WebActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.activity.StartActivity;
import com.abcs.huaqiaobang.tljr.news.bean.News;
import com.abcs.huaqiaobang.tljr.zrclistview.SimpleFooter;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView.OnStartListener;

/**
 * @author xbw
 * @version 创建时间：2015年9月8日 下午3:29:48
 */
public class OneMarkActivity extends BaseActivity {
	public static OneMark mark;
	private Handler handler = new Handler();
	private ZrcListView lv;
	private BaseAdapter adapter;
	private Map<String, News> map = new HashMap<String, News>();
	private ArrayList<News> list = new ArrayList<News>();
	private ArrayList<News> mList = new ArrayList<News>();
	private int page = 0;
	private boolean isFlush = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tljr_activity_news_onemark);
		findViewById(R.id.tljr_mark_btn_lfanhui).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		Drawable dw = new Drawable() {

			@Override
			public void setColorFilter(ColorFilter arg0) {
			}

			@Override
			public void setAlpha(int arg0) {
			}

			@Override
			public int getOpacity() {
				return 0;
			}

			@Override
			public void draw(Canvas canvas) {
				// TODO Auto-generated method stub
				Paint p = new Paint();
				String start = "#36648B";
				String middle = "#828282";
				String end = "#36648B";

				int[] color = { Color.parseColor(start),
						Color.parseColor(middle), Color.parseColor(end) };
				float[] position = { 0, 0.3f, 0.9f };
				LinearGradient lg = new LinearGradient(0, 0, Util.WIDTH, 1000, color,
						position, TileMode.MIRROR);
				p.setShader(lg);
				canvas.drawRect(0, 0, Util.WIDTH, 1000, p);
			}
		};
		findViewById(R.id.layout_title_background).setBackground(dw);
		((TextView) findViewById(R.id.tljr_tv_news_name)).setText(mark
				.getName());
		((TextView) findViewById(R.id.tljr_tv_news_info)).setText(mark
				.getInfo());
		StartActivity.imageLoader.displayImage(mark.getAvatar(),
				((ImageView) findViewById(R.id.tljr_tv_news_avatar)),
				StartActivity.options);
		lv = (ZrcListView) findViewById(R.id.tljr_mark_grp_lv);
		// 设置加载更多的样式（可选）
		SimpleFooter footer = new SimpleFooter(this);
		footer.setCircleColor(0xff33bbee);
		lv.setFootable(footer);

		// 设置列表项出现动画（可选）
		lv.setItemAnimForTopIn(R.anim.alpha_in);
		lv.setItemAnimForBottomIn(R.anim.alpha_in);
		// 加载更多事件回调（可选）
		lv.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				loadMore();
			}
		});
		adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				// TODO Auto-generated method stub]
				ViewHolder mHolder;
				if (view == null) {
					view = View.inflate(OneMarkActivity.this,
							R.layout.tljr_item_news_onemark, null);
					mHolder = new ViewHolder();
					mHolder.imp_news_title = (TextView) view
							.findViewById(R.id.imp_hqss_news_title);
					mHolder.imp_news_date = (TextView) view
							.findViewById(R.id.imp_news_time);
					mHolder.imp_news_picture = (ImageView) view
							.findViewById(R.id.imp_news_picture);
					view.setTag(mHolder);
					view.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							ViewHolder mHolder = (ViewHolder) v.getTag();
							Intent intent = new Intent(OneMarkActivity.this,
									WebActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("url", mHolder.url);
							bundle.putString("name", mark.getName());
							intent.putExtras(bundle);
							startActivity(intent);
						}
					});
				} else {
					mHolder = (ViewHolder) view.getTag();
				}
				News news = list.get(position);
				mHolder.imp_news_title.setText(news.getTitle());
				mHolder.imp_news_date.setText(news.getDate());
				mHolder.url = news.getUrl();
				StartActivity.imageLoader.displayImage(news.getpUrl(),
						mHolder.imp_news_picture, StartActivity.options);
				return view;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return list.get(position);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}
		};
		lv.setAdapter(adapter);
		lv.startLoadMore();
		loadMore();
	}

	static class ViewHolder {
		TextView imp_news_title;
		TextView imp_news_date;
		ImageView imp_news_picture;
		String url;
	}

	private void getData() {
		page++;
		ProgressDlgUtil.showProgressDlg("", this);
		HttpRequest.sendPost(TLUrl.getInstance().URL_wxxx, "weixinhao=" + mark.getNumber()
				+ "&page=" + page, new HttpRevMsg() {

			@Override
			public void revMsg(String msg) {
				// TODO Auto-generated method stub
				try {
					JSONObject object = new JSONObject(msg);
					final org.json.JSONArray array = object
							.getJSONArray("data");
					if (object.getInt("state") == 2) {
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							News news = new News();
							news.setId(obj.getString("id"));
							news.setTitle(obj.optString("title"));
							news.setContent(obj.optString("content"));
							news.setDate(obj.optString("time"));
							news.setUrl(obj.optString("url"));
							news.setpUrl(obj.optString("imglink"));
							map.put(news.getId(), news);
							mList.add(news);
						}
					} else {
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							if (!map.containsKey(obj.getString("id"))) {
								News news = new News();
								news.setId(obj.getString("id"));
								news.setTitle(obj.optString("title"));
								news.setContent(obj.optString("content"));
								news.setDate(obj.optString("time"));
								news.setUrl(obj.optString("url"));
								news.setpUrl(obj.optString("imglink"));
								map.put(news.getId(), news);
								mList.add(news);
							}
						}
					}
					ProgressDlgUtil.stopProgressDlg();
					handler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (array.length() > 0) {
								list.addAll(mList);
								mList.clear();
								lv.setLoadMoreSuccess();
								adapter.notifyDataSetChanged();
							} else {
								lv.stopLoadMore();
								showToast("没有更多数据");
							}
							isFlush = false;
						}
					});
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ProgressDlgUtil.stopProgressDlg();
				}
			}
		});
	}

	private void loadMore() {
		if (isFlush || list.size() >= 100) {
			return;
		}
		isFlush = true;
		getData();
	}

}
