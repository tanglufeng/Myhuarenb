package com.abcs.huaqiaobang.tljr.news.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.abcs.huaqiaobang.tljr.news.HuanQiuShiShi;
import com.abcs.huaqiaobang.tljr.news.store.DBHandler;
import com.abcs.huaqiaobang.tljr.news.store.DBManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DownLoadService extends Service {
	private NotificationManager manager;

	private DBManager dbManager;

	private MyBind mb = new MyBind();

	// Notification notification;
	// Intent notificationIntent;

	private boolean isWIFI = false;

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("service", "onCreate()");
		dbManager = new DBManager(this);
		dbManager.clearInvalidData();

		isWIFI = Util.getAPNType(this).equals("WIFI") ? true : false;
		Log.i("DownLoadActivity", "当前网络状态WIFI:" + isWIFI);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// TODO Auto-generated method stub

		Log.i("service", "onStartCommand()");
		if (intent != null && intent.getExtras().containsKey("isTiming")
				&& intent.getExtras().getString("isTiming") != null)
		{
			if (isWIFI)
			{
				String Flag = intent.getExtras().getString("isTiming");
				if (Flag.equals("true"))
				{
					String allspeical = Constent.preference.getString("downLoadChannelType", "nothing");
					boolean downloadImage = Constent.preference.getBoolean("isDownLoadImage", false);

					if (!allspeical.equals("nothing"))
					{
						String[] channel = allspeical.split(",");
						for (int i = 0; i < channel.length; i++)
						{
							Log.i("DownLoadActivity", "开始离线下载频道/l" + channel[i]);
							System.out.println("---------------------------开始离线下载频道:" + channel[i]);
							String url = TLUrl.getInstance().URL_new + "news/oc" + "?" + "need=15&sp=" + channel[i];

							mb.addTask(downloadImage, i + "", url);
						}
						mb.startDownLoad();

					}
				}
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("service", "onDestroy()");
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg)
		{

			if (msg.what == 2)
			{
				
				manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
				Notification notification = new NotificationCompat.Builder(getApplicationContext())
						.setSmallIcon(R.drawable.tljr_launcher).setTicker("").setContentInfo("图灵金融").setTicker("离线新闻下载完成")
						.setContentTitle("提示").setContentText("离线新闻下载完成").setNumber(1).setAutoCancel(true)
						.setDefaults(Notification.DEFAULT_LIGHTS).build();
				notification.flags = Notification.FLAG_AUTO_CANCEL;
				Log.i("DownLoadActivity", "离线下载完成，发送广播"  );
				Intent it = new Intent();
				it.setAction(HuanQiuShiShi.FINISH_ACTION);
				sendBroadcast(it);
				 
				// Intent intent = new Intent(getApplicationContext(),
				// MainActivity.class);
				// intent.putExtra("type", "brrow");
				// PendingIntent pendingIntent =
				// PendingIntent.getActivity(getApplicationContext(), 0, intent,
				// 0);
				// notification.setLatestEventInfo(getApplicationContext(),
				// "提示", "离线新闻下载完成", pendingIntent);
				manager.notify(0, notification);
			 
			}

		};
	};

	/**
	 * 获取当前Service的实例
	 * 
	 * @return
	 */
	public class MyBind extends Binder {
		private Handler mHandler = new Handler() {};
		// public void SetOnProgressListener(OnProgressListener pro) { //
		// 注册回调接口的方法，供外部调用
		// this.onProgressListener = pro;
		// }
		// private ArrayList<DownThread> taskList = new ArrayList<DownThread>();

		private HashMap<String, DownThread> taskList = new HashMap<String, DownThread>();

		private ExecutorService executorService;

		public MyBind() {
			// TODO Auto-generated constructor stub
			executorService = Executors.newFixedThreadPool(3);
		}

		public void addTask(boolean isDownLoadImage, String id, String url)
		{
			DownThread downThread = new DownThread(isDownLoadImage, url,id);
			taskList.put(id, downThread);
		}

	 

		public void startDownLoad()
		{
		
			for (DownThread thread : taskList.values())
			{
				executorService.submit(thread);
			}
		
		}

		class DownThread extends Thread {
			final int MAX_PROGRESS = 100; // 进度条最大值
			int progress = 0; // 进度条进度值
			String isoString;
			String url;
			private boolean isWorking = false;
			boolean isDownLoadImage = false;
			byte[] bytes;
			String id ;
			public DownThread(boolean isDownLoadImage, String url,String id) {
				// TODO Auto-generated constructor stub
				this.isWorking = true;
				this.url = url;
				this.id= id ;
				this.isDownLoadImage = isDownLoadImage;
			
			}

			public void stopTask()
			{
				this.isWorking = false;
			}

			public void update(int progress)
			{

				int threadCount = ((ThreadPoolExecutor) executorService).getActiveCount();

				if (threadCount == 1 && progress == MAX_PROGRESS)
				{
					Message msg = new Message();
					msg.what = 2;
					msg.arg1 = (int) Thread.currentThread().getId();
					handler.sendMessage(msg);
					taskList.clear();
				}
				
				if (progress == MAX_PROGRESS)
				{

					taskList.remove(this);
					isWorking = false;
					
				}
				
				Intent idd = new Intent();
				idd.setAction(DownLoadActivity.DYNAMICACTION) ;
				idd.putExtra("position", id);
				idd.putExtra("progress", progress+"");
				sendBroadcast(idd);
				
				
				if(progress == MAX_PROGRESS){
					
					Constent.preference.edit().putString("downP-"+id, "2").commit();
				}
				
 

			}

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				super.run();
		
				// TODO Auto-generated method stub
				System.out.println(Thread.currentThread().getName() + "下载开始:" + url);
				while (isWorking)
				{
					try
					{
						HttpURLConnection conn = null;
						conn = (HttpURLConnection) new URL(url).openConnection();
						conn.setConnectTimeout(5000);
						conn.setDoInput(true);
						conn.setDoOutput(false);
						conn.setUseCaches(true);

						conn.connect();
						if (conn.getContentLength() < 0)
						{
							System.out.println("无");
							break;
						}
						bytes = new byte[conn.getContentLength()];
						InputStream in = null;
						try
						{
							in = conn.getInputStream();
							int readBytes = 0;
							while (true)
							{
								int length = in.read(bytes, readBytes, bytes.length - readBytes);
								if (length == -1)
									break;
								readBytes += length;

								update((int) (readBytes * 100 / bytes.length));

							}
							conn.disconnect();

							isoString = new String(bytes, "UTF-8");

						} catch (Exception ex)
						{
							ex.printStackTrace();
						} finally
						{
							try
							{
								if (in != null)
									in.close();
							} catch (Exception ignored)
							{
								ignored.printStackTrace();
							}
						}
					} catch (Exception e)
					{
					}

				}

				// "开始解析json拿链接下载图片"

			
				try
				{
					JSONObject object = new JSONObject(isoString);
					JSONArray array = object.getJSONArray("news");
					for (int i = 0; i < array.length(); i++)
					{
						com.alibaba.fastjson.JSONObject info = com.alibaba.fastjson.JSONObject.parseObject(array
								.getString(i));
						LogUtil.e("HqssFragment", "开始插入----------------" + i);
						dbManager.insertOnceRequestNews(info, DBHandler.TABLE_NAME);
						if (isDownLoadImage)
						{
							System.out.println("开始解析json拿链接下载图片");
							DownPictureThread cstd = new DownPictureThread(info);
							cstd.start();

						}

					}
				} catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub

		return new MyBind();
	}

	class DownPictureThread extends Thread {
		com.alibaba.fastjson.JSONObject info;
		int num = 0;

		public DownPictureThread(com.alibaba.fastjson.JSONObject info) {
			this.info = info;
		}

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			super.run();

			com.alibaba.fastjson.JSONArray array2 = com.alibaba.fastjson.JSONArray.parseArray(info.getString("news"));
			for (int k = 0; k < array2.size(); k++)
			{

				com.alibaba.fastjson.JSONObject rs = array2.getJSONObject(k);
				if (rs.containsKey("purl"))
				{
					// StartActivity.imageLoader.displayImage(rs.getString("purl"),
					// iv, StartActivity.options);
					System.out.println("start picturedownload " + Thread.currentThread().getName() + num);

					String url = rs.getString("purl");
					String pathUrl = url.substring(0, url.lastIndexOf("/") + 1);
					String change = url.substring(url.lastIndexOf("/") + 1, url.length());
					try
					{
						change = URLEncoder.encode(change, "utf-8");
					} catch (UnsupportedEncodingException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					onlyDownImage(pathUrl + change, rs.getString("id") + ".png");

				}

			}

		}

		public void onlyDownImage(final String url, final String newsId)
		{
			if (url.length() == 0 || newsId == null)
			{
				return;
			}
			String pathName = Util.sdPath;
			File file = new File(pathName + newsId);
			if (!file.exists())
			{

				byte[] bytes = download(url);
				if (bytes != null && bytes.length != 0)
				{
					writeFileToSD(newsId, bytes, bytes.length);
					System.out.println("best:" + url);
				} else
				{
					System.out.println("error:" + url);
				}

			}
		}

		public byte[] download(String url)
		{
			InputStream in = null;
			try
			{
				HttpURLConnection conn = null;
				conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(false);
				conn.setUseCaches(true);
				conn.setConnectTimeout(5000);
				conn.connect();
				in = conn.getInputStream();
				int size = conn.getContentLength();
				byte[] out = new byte[size];
				int readBytes = 0;
				while (true)
				{
					int length = in.read(out, readBytes, out.length - readBytes);
					if (length == -1)
						break;
					readBytes += length;
				}
				conn.disconnect();
				return out;
			} catch (Exception ex)
			{
				return null;
			} finally
			{
				try
				{
					if (in != null)
						in.close();
				} catch (Exception ignored)
				{
				}
			}
		}

		public void writeFileToSD(final String fileName, final byte[] b, final int byteCount)
		{
			System.out.println("startdownload picture----writeFileToSD----!!!");

			try
			{
				String pathName = Util.sdPath;
				File path = new File(pathName);
				File file = new File(pathName + fileName);
				if (!path.exists())
				{
					path.mkdir();
				}
				if (!file.exists())
				{
					file.createNewFile();
					FileOutputStream stream = new FileOutputStream(file);
					stream.write(b, 0, byteCount);
					stream.close();
				}

			} catch (Exception e)
			{
				e.printStackTrace();
			}

			System.out.println("end picturedownload " + Thread.currentThread().getName() + num++);
		}

	}

}
