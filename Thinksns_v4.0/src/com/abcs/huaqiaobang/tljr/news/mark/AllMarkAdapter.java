package com.abcs.huaqiaobang.tljr.news.mark;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.activity.StartActivity;
import com.abcs.huaqiaobang.tljr.news.bean.News;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView.PinnedHeaderInterface;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

public class AllMarkAdapter extends BaseAdapter {
	Activity activity;
	public ArrayList<News> newsList;
	ZrcListView listView;
	public AllMarkAdapter(Activity activity, ZrcListView listView ,ArrayList<News> newsList) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.newsList = newsList;
		this.listView = listView;
		addTopPinned();
	}

	
	private void addTopPinned(){
		
		
		
		View v = (LayoutInflater.from(activity).inflate(
				R.layout.tljr_item_news_top_mark, listView, false));
	 
		
		 
		listView.setPinnedHeaderView(v);
		listView.setPinnedHeaderInterface(new PinnedHeaderInterface() {

			@Override
			public int getPinnedHeaderState(int position) {
				// TODO Auto-generated method stub
 

//				if (  newsList.size()==0) {
//					return PINNED_HEADER_GONE;
//				}
				return PINNED_HEADER_VISIBLE;
			}

			@Override
			public void configurePinnedHeader(View header, int position,
					int alpha) {
		 
			}
		});
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return newsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder mHolder;
		if (view == null) {
			view = View.inflate(activity, R.layout.tljr_item_news_hqss_mark,
					null);
			mHolder = new ViewHolder();
			mHolder.author_mark = (TextView) view
					.findViewById(R.id.mark_author);
			mHolder.title_mark = (TextView) view
					.findViewById(R.id.mark_title);
			mHolder.date_mark = (TextView) view.findViewById(R.id.mart_date);
			mHolder.avatar_mark = (ImageView) view
					.findViewById(R.id.mark_avatar);
			view.setTag(mHolder);
			 
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		
		RelativeLayout tljr_item_news_top_mark =(RelativeLayout) view.findViewById(R.id.tljr_item_news_top_mark);
		tljr_item_news_top_mark.setVisibility(position==0?View.INVISIBLE:View.GONE);
		
		
		 News news = newsList.get(position);
		 mHolder.title_mark.setText(news.getTitle());
		 mHolder.date_mark.setText(news.getSytime());
		 mHolder.author_mark.setText(news.getAuthor());
		 mHolder.url = news.getUrl();
		 StartActivity.options =getListOptions();
		 StartActivity.imageLoader.displayImage(news.getAuthorAvatar(),
		 mHolder.avatar_mark, getListOptions());
		return view;
	}

	

	public static DisplayImageOptions getListOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				// // 设置图片在下载期间显示的图片
				.showImageOnLoading(R.drawable.img_morentupian)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageForEmptyUri(R.drawable.img_morentupian)
				// 设置图片加载/解码过程中错误时候显示的图片
				.showImageOnFail(R.drawable.img_morentupian)
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)
				// 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型
				// .decodingOptions(android.graphics.BitmapFactory.Options
				// decodingOptions)//设置图片的解码配置
				.considerExifParams(true)

				// 设置图片下载前的延迟
				// .delayBeforeLoading(int delayInMillis)//int
				// delayInMillis为你设置的延迟时间
				// 设置图片加入缓存前，对bitmap进行设置
				// 。preProcessor(BitmapProcessor preProcessor)
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				  .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(100))// 淡入

				.build();
		return options;
	}
	
	
	static class ViewHolder {
		TextView author_mark;
		TextView date_mark;
		TextView title_mark;
		ImageView avatar_mark;
		String url ;

	}

}
 
 
