package com.abcs.huaqiaobang.tljr.news.download;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.abcs.huaqiaobang.tljr.news.channel.bean.ChannelItem;
import com.abcs.huaqiaobang.tljr.news.channel.view.ChannelListView;
import com.abcs.huaqiaobang.tljr.news.widget.NumberProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownLoadAdapter extends BaseAdapter {

	private Context context;
	public List<ChannelItem> channelList;
	LayoutInflater inflater = null;
	public static ArrayList<String> timeDownCacheChannel = new ArrayList<String>();
	private String Tag = "DownLoadActivity";
	public static ArrayList<String> temp_taskList = new ArrayList<String>();

	private ChannelListView listView;
	
	private ArrayList<String> state_temp = new ArrayList<String>();
	
	private Handler mHandler = new Handler() {};
	
	public DownLoadAdapter(Context context, List<ChannelItem> channelList, ChannelListView listView) {
		this.context = context;
		this.channelList = channelList;
		this.listView = listView;
		inflater = LayoutInflater.from(context);
			
//		String allspeical = Constent.preference.getString("downLoadChannelType", "nothing");
//		if (!allspeical.equals("nothing"))
//		{
//			String[] channel = allspeical.split(",");
//			for(String s:channel){
//				state_temp.add(s);
//			}
//			
//			
//			for(ChannelItem channelItem:channelList){
//				if(state_temp.contains(channelItem.getSpecies()+"")){
//					channelItem.setHasCheck(true);
//				}
//			}
//			 
//		}
	}

	public void setChannelList(List<ChannelItem> channelList)
	{
		this.channelList = channelList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public ChannelItem getItem(int position)
	{
		if (channelList != null && channelList.size() != 0)
		{
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
	HashMap<Integer,String> mflag = new HashMap<Integer,String>(); 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.tljr_item_news_download, null);
			 
		}

		ChannelItem channel = getItem(position);
	
		
		
		final TextView channelName = (TextView) ViewHolder.get(convertView, R.id.channel_name_download);
		final CheckBox btn_tick = (CheckBox) ViewHolder.get(convertView, R.id.btn_tick);
		final NumberProgressBar processbar = (NumberProgressBar) ViewHolder.get(convertView, R.id.processbar_download);
		final RelativeLayout ly_tick= (RelativeLayout) ViewHolder.get(convertView, R.id.ly_tick);
		
		String flag ;
		if(mflag.containsKey(position)){
			  flag =mflag.get(position);
		}else{
			 flag =Constent.preference.getString("downP-"+position, "0");
		}
	
		 mflag.put(position, flag); 
		if(flag.equals("0")){  //未下载
			processbar.setVisibility(View.INVISIBLE);
			btn_tick.setChecked(false);
		}else if(flag.equals("1")){  //正在下载
			processbar.setVisibility(View.VISIBLE);
			processbar.setProgress(0);
			btn_tick.setChecked(true);
		}else if(flag.equals("2")){  //下载完成
			processbar.setVisibility(View.VISIBLE);
			processbar.setProgress(100);
			btn_tick.setChecked(true);
		}
		
		
		
		// btn_tick.setClickable(false)
		
		
		// btn_tick.setChecked(channel.hasCheck);
		
		
		
		ly_tick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				
				// TODO Auto-generated method stub
				btn_tick.setChecked(btn_tick.isChecked()?false:true);
				processbar.setVisibility(View.INVISIBLE);
				if(processbar.getVisibility()==View.INVISIBLE){
					Constent.preference.edit().putString("downP-"+position, "0").commit();
				}
			}
		});
		
		btn_tick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
			 
				processbar.setVisibility(View.INVISIBLE);
				if(processbar.getVisibility()==View.INVISIBLE){
					Constent.preference.edit().putString("downP-"+position, "0").commit();
				}
			}
		});
		
//		btn_tick.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//			{
//				// TODO Auto-generated method stub
//
//	
//				
//			}
//		});

		channelName.setText(channel.getName());

		return convertView;
	}

	
	
	
	
	
	
	
	public boolean addListToDownLoadTask()
	{
		temp_taskList.clear();
		timeDownCacheChannel.clear();
		if (listView.getChildCount() <= 0)
		{
			return false;
		}
		for (int i = 0; i < listView.getChildCount(); i++)
		{
		
			
			
			View viewT = listView.getChildAt(i);
			final CheckBox btn_tick = (CheckBox) ViewHolder.get(viewT, R.id.btn_tick);
			if (btn_tick != null && btn_tick.isChecked())
			{
				temp_taskList.add(i + "");
				if (channelList.get(i).getSpecies() != null)
				{
					timeDownCacheChannel.add(channelList.get(i).getSpecies() + "");
				}

			}
		}
		if (timeDownCacheChannel.size() <= 0)
		{
			return false;
		}

		return temp_taskList.size() > 0 ? true : false;

	}

	public void startDownLoad()
	{
		  Log.i(Tag, "XXXXXXXXXXXXXXXXXXXXXX");
		 
				// TODO Auto-generated method stub
		  
		
			 
		   
		
				if (temp_taskList.size() > 0)
				{
					Editor editor = Constent.preference.edit();
					for (String str : temp_taskList)
					{
						int position = Integer.valueOf(str);
						View view = listView.getChildAt(position);
					
						if (view != null)
						{
							final NumberProgressBar processbar = (NumberProgressBar) ViewHolder.get(view,
									R.id.processbar_download);
							processbar.setProgress(0);
							String url = TLUrl.getInstance().URL_new + "news/oc" + "?" + "need=15&sp="
									+ channelList.get(position).getSpecies();
							
							DownLoadActivity.myService.addTask(DownLoadActivity.isDownLoadImage, str, url);
						}
						editor.putString("downP-"+position, "1");
					}
					editor.commit();
					
					DownLoadActivity.myService.startDownLoad();
				
				}
	 
		  Log.i(Tag, "YYYYYYYYYYYYYYYYYYY");
	}

	public void chooseAll(boolean select)
	{
		if (listView.getChildCount() <= 0)
		{
			return;
		}
		for (int i = 0; i < listView.getChildCount(); i++)
		{
			View viewT = listView.getChildAt(i);
			final CheckBox btn_tick = (CheckBox) ViewHolder.get(viewT, R.id.btn_tick);
			final NumberProgressBar processbar = (NumberProgressBar) ViewHolder.get(viewT,
					R.id.processbar_download);
			if (btn_tick != null)
			{
				btn_tick.setChecked(select);
				if(!select){
					processbar.setVisibility(View.INVISIBLE);
				}

			}
		}

	}

}

class ViewHolder {
	public static <T extends View> T get(View view, int id)
	{
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (viewHolder == null)
		{
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null)
		{
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}
}