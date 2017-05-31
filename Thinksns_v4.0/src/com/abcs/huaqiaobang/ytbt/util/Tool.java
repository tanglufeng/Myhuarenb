package com.abcs.huaqiaobang.ytbt.util;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.widget.Toast;

import com.abcs.huaqiaobang.ytbt.bean.ConversationBean;
import com.abcs.huaqiaobang.ytbt.bean.TopConversationBean;
import com.abcs.huaqiaobang.ytbt.common.utils.DemoUtils;
import com.lidroid.xutils.DbUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Tool {
	private static ProgressDialog dialog;

	public static byte[] getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	private static byte[] compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			if(options==10){
				return baos.toByteArray();
			}
			options -= 10;// 每次都减少10
		}
//		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
//		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return baos.toByteArray();
	}

	public static int dp2px(Context context, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}

	public static void showInfo(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		// Toast toast = new Toast(context);
		// toast.setView(view);
		// Dialog
	}

	public static void showProgressDialog(Context context, String msg,boolean cancelable) {
		try {
			if (dialog == null) {
				dialog = new ProgressDialog(context);
			}
			dialog.setCancelable(cancelable);
			dialog.setMessage(msg);
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void removeProgressDialog() {
		if (dialog != null) {
			dialog.cancel();
			dialog = null;
			System.gc();
		}
	}

	public static String getTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
				Locale.CHINA);
		return format.format(new Date(time));
	}

	public static String getCurrentVersion(Context context)
			throws NameNotFoundException {
		PackageInfo info = context.getPackageManager().getPackageInfo(
				context.getPackageName(), 0);
		return info.versionName;
	}

	public static TopConversationBean toTopconversation(
			ConversationBean conversationBean,TopConversationBean topConversationBean) {
		topConversationBean = new TopConversationBean();
		topConversationBean.setConversationpeople(conversationBean
				.getConversationpeople());
		topConversationBean.setIsgroup(conversationBean.isIsgroup());
		topConversationBean.setMsg(conversationBean.getMsg());
		 topConversationBean.setMsgfrom(conversationBean.getMsgfrom());
		topConversationBean.setMsgto(conversationBean.getMsgto());
		topConversationBean.setMsglasttime(conversationBean.getMsglasttime());
		topConversationBean.setUnread(conversationBean.getUnread());
		return topConversationBean;
	}

	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}
	
	public static String getPhotoFileName() {
		return DemoUtils.md5(String.valueOf(System.currentTimeMillis()))
				+ ".png";
	};

	public static String getVioceFileName() {
		return DemoUtils.md5(String.valueOf(System.currentTimeMillis()))
				+ ".amr";
	};

	public static long getAmrDuration(File file) throws IOException {
		long duration = -1;
		int[] packedSize = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0,
				0, 0 };
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(file, "rw");
			long length = file.length();// 文件的长度
			int pos = 6;// 设置初始位置
			int frameCount = 0;// 初始帧数
			int packedPos = -1;
			// ///////////////////////////////////////////////////
			byte[] datas = new byte[1];// 初始数据值
			while (pos <= length) {
				randomAccessFile.seek(pos);
				if (randomAccessFile.read(datas, 0, 1) != 1) {
					duration = length > 0 ? ((length - 6) / 650) : 0;
					break;
				}
				packedPos = (datas[0] >> 3) & 0x0F;
				pos += packedSize[packedPos] + 1;
				frameCount++;
			}
			// ///////////////////////////////////////////////////
			duration += frameCount * 20;// 帧数*20
		} finally {
			if (randomAccessFile != null) {
				randomAccessFile.close();
			}
		}
		return duration;
	}

	// 在进程中去寻找当前APP的信息，判断是否在前台运行
	public static boolean isAppOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getApplicationContext().getSystemService(
						Context.ACTIVITY_SERVICE);
		String packageName = context.getApplicationContext().getPackageName();
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}
	
	public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
	public static void updateDb(DbUtils db, String tableName) {
		try {
			Class<ConversationBean> c = (Class<ConversationBean>) Class
					.forName("com.abcs.ytbt.bean."+ tableName);
			if (db.tableIsExist(c)) {
				List<String> dbFildsList = new ArrayList<String>();
				String str = "select * from " + "com_abcs_ytbt_bean_"+tableName;
				Cursor cursor = db.execQuery(str);
				int count = cursor.getColumnCount();
				for (int i = 0; i < count; i++) {
					dbFildsList.add(cursor.getColumnName(i));
				}
				cursor.close();
				Field f[] = c.getDeclaredFields();// 把属性的信息提取出来，并且存放到field类的对象中，因为每个field的对象只能存放一个属性的信息所以要用数组去接收
				for (int i = 0; i < f.length; i++) {
					String fildName = f[i].getName();
					if (fildName.equals("serialVersionUID")) {
						continue;
					}
					String fildType = f[i].getType().toString();
					if (!isExist(dbFildsList, fildName)) {
						if (fildType.equals("class Java.lang.String")) {
							db.execNonQuery("alter table " + "com_abcs_ytbt_bean_"+tableName
									+ " add " + fildName + " TEXT ");
						} else if (fildType.equals("int")
								|| fildType.equals("long")
								|| fildType.equals("boolean")) {
							db.execNonQuery("alter table " + "com_abcs_ytbt_bean_"+tableName
									+ " add " + fildName + " INTEGER ");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static boolean isExist(List<String> dbFildsList, String fildName) {
		for (String string : dbFildsList) {
			if(string.equals(fildName)){
				return true;
			}
		}
		return false;
	}
}
