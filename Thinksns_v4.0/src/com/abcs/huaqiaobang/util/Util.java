package com.abcs.huaqiaobang.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.abcs.sociax.t4.android.Thinksns;
import com.baidu.location.LocationClientOption;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Util {
    public static String netType = "";
    public static String token = "";
    public static String STARTKEY = "FirstStart";
    public static Pattern pattern = Pattern
            .compile("[+-]?[0-9]+[0-9]*(\\.[0-9]+)?");
    public static Pattern pattern1 = Pattern.compile("[a-zA-Z]+");
    public static String filePath;
    public static String appName;
    public static Animation rotateAni;
    public static int WIDTH;
    public static int HEIGHT;
    public static int IMAGEWIDTH = 720;
    public static int IMAGEHEIGTH = 1280;
    public static final SharedPreferences preference = PreferenceManager
            .getDefaultSharedPreferences(Thinksns.getContext());
    public static boolean isThirdLogin = false;
    public static boolean isYYGLogin = false;
    public static String lastSynTime = "";
    public static JSONArray bankList;
    //
    public static int news_grey = Color.parseColor("#969696");
    public static int news_b = Color.parseColor("#323232");
    public static int c_blue = Color.rgb(54, 102, 180);
    public static int c_red = Color.rgb(222, 20, 32);
    public static int c_green = Color.parseColor("#149628");


    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符
    private Bitmap bitmap;
    private static Bitmap bt;

    public static void init() {
        if (preference.getBoolean(STARTKEY, true)) {
            preference.edit().clear().commit();
            preference.edit().putBoolean(STARTKEY, false).commit();
        }
        filePath = MyApplication.getInstance().getFilesDir().getAbsolutePath();
        appName = getMeteDate("APPNAME", MyApplication.getInstance());
        WIDTH = getScreenWidth(MyApplication.getInstance());
        HEIGHT = getScreenHeight(MyApplication.getInstance());
        System.err.println(WIDTH);
        System.err.println(HEIGHT);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        format1.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        format2.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        format3.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        format4.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        format5.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        format6.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    public static void setParams(View view, int w, int h, int x, int y) {
        RelativeLayout.LayoutParams Params = new RelativeLayout.LayoutParams(
                (int) (Util.WIDTH * w / Util.IMAGEWIDTH), (int) (Util.HEIGHT
                * h / Util.IMAGEHEIGTH));
        Params.setMargins(
                (int) (Util.WIDTH * x / Util.IMAGEWIDTH),
                (int) (Util.HEIGHT * (Util.IMAGEHEIGTH - h - y) / Util.IMAGEHEIGTH),
                0, 0);
        view.setLayoutParams(Params);
    }

    public static boolean checkPassword(String mobiles) {
        Pattern p = Pattern
                .compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }

    public static void setParams(View view1, View view2, int w, int h, int x,
                                 int y) {
        RelativeLayout.LayoutParams Params = new RelativeLayout.LayoutParams(
                (int) (Util.WIDTH * w / Util.IMAGEWIDTH), (int) (Util.HEIGHT
                * h / Util.IMAGEHEIGTH));
        Params.addRule(RelativeLayout.ALIGN_LEFT, view2.getId());
        Params.addRule(RelativeLayout.ALIGN_BOTTOM, view2.getId());
        Params.leftMargin = (int) (Util.WIDTH * x / Util.IMAGEWIDTH);
        Params.bottomMargin = (int) (Util.HEIGHT * y / Util.IMAGEHEIGTH);
        view1.setLayoutParams(Params);
    }

    public static void setSize(View view, int w, int h) {
        try {
            if (view.getLayoutParams().getClass()
                    .equals(RelativeLayout.LayoutParams.class)) {
                RelativeLayout.LayoutParams Params = new RelativeLayout.LayoutParams(
                        (int) (Util.WIDTH * w / Util.IMAGEWIDTH),
                        (int) (Util.HEIGHT * h / Util.IMAGEHEIGTH));
                view.setLayoutParams(Params);
            } else if (view.getLayoutParams().getClass()
                    .equals(LinearLayout.LayoutParams.class)) {
                LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                        (int) (Util.WIDTH * w / Util.IMAGEWIDTH),
                        (int) (Util.HEIGHT * h / Util.IMAGEHEIGTH));
                view.setLayoutParams(Params1);
            }
        } catch (Exception e) {
            // TODO: handle exception
            RelativeLayout.LayoutParams Params = new RelativeLayout.LayoutParams(
                    (int) (Util.WIDTH * w / Util.IMAGEWIDTH),
                    (int) (Util.HEIGHT * h / Util.IMAGEHEIGTH));
            view.setLayoutParams(Params);
        }

    }

    public static String shortName(String name) {
        if (name.length() > 10) {
            name = name.substring(0, 3) + "***"
                    + name.substring(name.length() - 4, name.length());
        }
        return name;
    }

    public static String shortGuName(String name) {
        if (name.length() > 5) {
            name = name.substring(0, 4) + "...";
        }
        return name;
    }


    /*
     * 根据屏幕分辨率得到字体大小
     */
    public static int getFontSize(int textSize) {
        int rate = (int) (textSize * (float) WIDTH / IMAGEWIDTH);
        return rate;
    }

    /**
     * 得到屏幕宽度
     *
     * @return 单位:px
     */
    public static int getScreenWidth(Context context) {
        int screenWidth;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        return screenWidth;
    }

    /**
     * 得到屏幕高度
     *
     * @return 单位:px
     */
    public static int getScreenHeight(Context context) {
        int screenHeight;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        return screenHeight;
    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void downLoadFile(String url, String dirName,
                                    String fileName, Complete complete) throws IOException {
        int numBytes = 0;
        HttpURLConnection conn = null;
        conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(5000);
        conn.setDoInput(true);
        conn.setDoOutput(false);
        conn.setUseCaches(true);
        conn.connect();
        if (conn.getContentLength() < 0) {
            return;
        }
        byte[] bytes = new byte[conn.getContentLength()];
        numBytes = download(conn, bytes, url);
        if (numBytes != 0) {
            writeFile(dirName, fileName, bytes, numBytes);
            complete.complete();
        }
    }

    public static int download(HttpURLConnection conn, byte[] out, String url) {
        InputStream in = null;
        try {
            in = conn.getInputStream();
            int readBytes = 0;
            while (true) {
                int length = in.read(out, readBytes, out.length - readBytes);
                if (length == -1)
                    break;
                readBytes += length;
            }
            conn.disconnect();
            return readBytes;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
    }

    public static void writeFile(final String dirName, final String fileName,
                                 final byte[] b, final int byteCount) throws IOException {
        File path = new File(dirName);
        File file = new File(dirName + "/" + fileName);
        if (!path.exists()) {
            path.mkdir();
        }
        if (file.exists()) {
            file.delete();// 如果目标文件已经存在，则删除，产生覆盖旧文件的效果
        }
        file.createNewFile();
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(b, 0, byteCount);
        stream.close();
    }

    public static String getProperties(String name, String key) {
        File file = new File(filePath + "/" + name + ".properties");
        Properties dbProps = new Properties();
        String value = null;
        try {
            InputStream is = new FileInputStream(file);
            dbProps.load(is);
            for (Entry<Object, Object> string : dbProps.entrySet()) {
                if (string.getKey().toString().equals(key)) {
                    value = (String) string.getValue();
                }
            }
        } catch (Exception e) {
        }
        return value;
    }

    public static String getFromAssets(final String fileName) {
        String Result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(MyApplication
                    .getInstance().getResources().getAssets().open(fileName),
                    "utf-8");
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            bufReader.close();
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result;
    }

    public static Point getSize(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return new Point(view.getMeasuredWidth(), view.getMeasuredHeight());

    }

    /**
     * 得到当前时间
     *
     * @return 时间的字符串
     */
    public static String getCurrentTime() {
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = format.format(curDate);
        return str;
    }

    public static String getCurrentTime2() {
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = format7.format(curDate);
        return str;
    }

    public static long getLongTime(String time) {
        try {
            Date date = format.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return 0;
        }
    }

    public static String getMeteDate(String key, Context context) {
        String msg = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            msg = appInfo.metaData.getString(key);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return msg;
    }

    public static DecimalFormat df = new DecimalFormat("0.00");
    public static DecimalFormat df1 = new DecimalFormat("0.0");
    static TimeZone tz = TimeZone.getTimeZone("ETC/GMT+8");
    public static SimpleDateFormat formatzjz = new SimpleDateFormat(
            "yyyy年MM月dd日 HH:mm:ss");
    public static SimpleDateFormat formatzjz2 = new SimpleDateFormat(
            "dd天 HH小时 mm分ss秒");
    public static SimpleDateFormat formatzjz3 = new SimpleDateFormat("yyyy年MM月dd日");
    public static SimpleDateFormat format = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

    static SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM");
    // static SimpleDateFormat format2 = new SimpleDateFormat("yy-MM");
    public static SimpleDateFormat format3 = new SimpleDateFormat("HH:mm");
    static SimpleDateFormat format4 = new SimpleDateFormat("MM-dd");
    static SimpleDateFormat format5 = new SimpleDateFormat("dd/HH:mm");
    static SimpleDateFormat format6 = new SimpleDateFormat("HHmm");
    public static SimpleDateFormat format7 = new SimpleDateFormat("yyyy/MM/dd");
    static SimpleDateFormat format8 = new SimpleDateFormat("MM月dd日");
    public static SimpleDateFormat format10 = new SimpleDateFormat(
            "MM-dd HH:mm");

    public static SimpleDateFormat format9 = new SimpleDateFormat("dd");

    public static SimpleDateFormat format11 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");
    public static SimpleDateFormat format12 = new SimpleDateFormat("HH:mm:ss");


    // 一.日期转换为时间戳
    public static long getTime() {
        Date date = null;
        try {
            date = format.parse("2009/12/11 00:00:00");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    // 二.时间戳转换为date 型
    public static String getDate(long time) {
        Date date = new Date(time);
        return format.format(date);
    }

    public static String getDateOnlyDat(long time) {
        Date date = new Date(time);
        return format1.format(date);
    }

    public static String getDateOnlyMonth(long time) {
        Date date = new Date(time);
        return format2.format(date);
    }

    public static String getDateOnlyHour(long time) {
        Date date = new Date(time);
        return format3.format(date);
    }

    public static String getDateOnlyDay(long time) {
        Date date = new Date(time);
        return format4.format(date);
    }

    public static String getDateDayhhmm(long time) {
        Date date = new Date(time);
        return format5.format(date);
    }

    public static String getDatehhmm(long time) {
        Date date = new Date(time);
        return format6.format(date);
    }

    public static String getDateMY(long time) {
        Date date = new Date(time);
        return format8.format(date);
    }

    public static void setSizeNews(View view) {
        try {
            if (view.getLayoutParams().getClass()
                    .equals(RelativeLayout.LayoutParams.class)) {
                RelativeLayout.LayoutParams Params = new RelativeLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                view.setLayoutParams(Params);
            } else if (view.getLayoutParams().getClass()
                    .equals(LinearLayout.LayoutParams.class)) {
                LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                view.setLayoutParams(Params1);
            }
        } catch (Exception e) {
            RelativeLayout.LayoutParams Params = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            view.setLayoutParams(Params);
        }
    }

    /**
     * 获取字符串的长度，中文占2个字符,英文数字占1个字符
     *
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    public static String shortFenZuName(String name) {
        if (length(name) > 20) {
            name = substring(name, 20);
        }
        return name;
    }

    public static String substring(String orignal, int count) {
        if (orignal != null && !"".equals(orignal)) {
            String chinese = "[\u4e00-\u9fa5]";
            if (count > 0) {
                StringBuffer buff = new StringBuffer();
                String c;
                for (int i = 0; i < count; i++) {
                    c = orignal.substring(i, i + 1);
                    buff.append(c);
                    if (c.matches(chinese)) {
                        // 遇到中文汉字，截取字节总数减1
                        --count;
                    }
                }
                return buff.toString();
            }
        }
        return orignal;
    }

    @SuppressLint("SdCardPath")
    public static String sdPath = "/sdcard/huaqiaobang/";

    public static void getLocalAvatar(final String avatarUrl,
                                      final AvatarRev avatarRev) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                String pathName = sdPath;
                File file = new File(pathName
                        + avatarUrl.substring(avatarUrl.lastIndexOf("/") + 1));
                // byte[] out = new byte[50 * 1024];
                if (file.exists()) {
                    FileInputStream stream = null;
                    try {
                        stream = new FileInputStream(file);
                        int size = stream.available();
                        byte[] out = new byte[size];
                        stream.read(out);
                        stream.close();
                        avatarRev.revBtype(out, out.length);
                    } catch (Exception e) {
                        avatarRev.revBtype(null, 0);
                        e.printStackTrace();
                    } finally {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } else {
                    getAvatar(avatarUrl, avatarRev);
                }
            }
        }).start();

    }

    public static void getAvatar(final String avatarUrl,
                                 final AvatarRev avatarRev) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                String fileName = avatarUrl.substring(avatarUrl
                        .lastIndexOf("/") + 1);
                byte[] bytes = download(avatarUrl);
                if (bytes != null && bytes.length != 0) {
                    avatarRev.revBtype(bytes, bytes.length);
                    writeFileToSD(fileName, bytes, bytes.length);
                } else {
                    avatarRev.revBtype(null, 0);
                }
            }
        }).start();

    }

    /**
     * 根据URL下载头像
     * out 字节流 url 头像的URL
     *
     * @param
     * @param
     * @return 字节数组的长度
     */
    public static byte[] download(String url) {
        InputStream in = null;
        try {
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) new URL(url).openConnection();
            //conn.getContentLength()=-1解决
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.setRequestMethod("POST");

            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setUseCaches(true);
            conn.connect();
            in = conn.getInputStream();
            int size = conn.getContentLength();
            byte[] out = new byte[size];
            int readBytes = 0;
            while (true) {
                int length = in.read(out, readBytes, out.length - readBytes);
                if (length == -1)
                    break;
                readBytes += length;
            }
            conn.disconnect();
            return out;
        } catch (Exception ex) {
            return null;
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception ignored) {
            }
        }
    }

    public static byte[] download2(String url) {
        InputStream in = null;
        try {
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) new URL(url).openConnection();
            //conn.getContentLength()=-1解决
//            conn.setRequestProperty("Accept-Encoding", "identity");
//            conn.setRequestMethod("POST");

            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setUseCaches(true);
            conn.connect();
            in = conn.getInputStream();
            int size = conn.getContentLength();
            byte[] out = new byte[size];
            int readBytes = 0;
            while (true) {
                int length = in.read(out, readBytes, out.length - readBytes);
                if (length == -1)
                    break;
                readBytes += length;
            }
            conn.disconnect();
            return out;
        } catch (Exception ex) {
            return null;
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception ignored) {
            }
        }
    }


    public static void writeFileToSD(final String fileName, final byte[] b,
                                     final int byteCount) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String pathName = sdPath;
                    File path = new File(pathName);
                    File file = new File(pathName + fileName);
                    if (!path.exists()) {
                        path.mkdir();
                    }
                    if (!file.exists()) {
                        file.createNewFile();
                        FileOutputStream stream = new FileOutputStream(file);
                        stream.write(b, 0, byteCount);
                        stream.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static void getLocalImage(final String avatarUrl,
                                     final AvatarRev avatarRev) {
        if (avatarUrl.length() == 0||avatarUrl.equals("null")) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String pathName = sdPath;
                File file = new File(pathName
                        + avatarUrl.substring(avatarUrl.lastIndexOf("/"),
                        avatarUrl.length()));
                if (file.exists()) {
                    FileInputStream stream = null;
                    try {
                        stream = new FileInputStream(file);
                        int size = stream.available();
                        byte[] out = new byte[size];
                        stream.read(out);
                        stream.close();
                        avatarRev.revBtype(out, out.length);
                        stream = null;
                    } catch (Exception e) {
                        avatarRev.revBtype(null, 0);
                        e.printStackTrace();
                    } finally {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } else {
                    getImage(avatarUrl, avatarRev);
                }
            }
        }).start();
    }

    public static void getImage(final String avatarUrl,
                                final AvatarRev avatarRev) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                String fileName = avatarUrl.substring(
                        avatarUrl.lastIndexOf("/"), avatarUrl.length());
                byte[] bytes = download(avatarUrl);
                if (bytes != null && bytes.length != 0) {
                    avatarRev.revBtype(bytes, bytes.length);
                    writeFileToSD(fileName, bytes, bytes.length);
                } else {
                    avatarRev.revBtype(null, 0);
                }
            }
        }).start();
    }

    public static void setImage(String url, final View img,
                                final Handler handler) {
        getLocalImage(url, new AvatarRev() {

            @Override
            public void revBtype(byte[] b, int bytelength) {
                // TODO Auto-generated method stub
                if (b != null) {
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0,
                            b.length);
                    handler.post(new Runnable() {

                        @SuppressLint("NewApi")
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            img.setBackground(new BitmapDrawable(bitmap));
                            // img.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        });
    }


    public static void setNetImage(String url, final ImageView img,
                                   final Handler handler) {
        getImage(url, new AvatarRev() {

            @Override
            public void revBtype(byte[] b, int bytelength) {
                // TODO Auto-generated method stub
                if (b != null) {
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0,
                            b.length);
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            img.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        });
    }

    public static void setRoundImage(String url, final ImageView img,
                                     final Handler handler) {
        getLocalImage(url, new AvatarRev() {

            @Override
            public void revBtype(byte[] b, int bytelength) {
                // TODO Auto-generated method stub
                if (b != null) {
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0,
                            b.length);
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            img.setImageBitmap(getRoundedCornerBitmap(bitmap));
                        }
                    });
                }
            }
        });
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = bitmap.getWidth() / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static void setImageOnlyDown(final String url, final ImageView img,
                                        final Handler handler) {
        if (url.equals("") || url.length() < 3) {
            return;
        }
        String pathName = sdPath;
        File file = new File(pathName
                + url.substring(url.lastIndexOf("/"), url.length()));
        if (file.exists()) {
            FileInputStream stream = null;
            try {
                stream = new FileInputStream(file);
                final Bitmap bitmap = BitmapFactory.decodeStream(stream);
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        img.setImageBitmap(bitmap);
                    }
                });
                stream = null;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    String fileName = url.substring(url.lastIndexOf("/"),
                            url.length());
                    byte[] bytes = download(url);
                    if (bytes != null && bytes.length != 0) {
                        writeFileToSD(fileName, bytes, bytes.length);
                    }
                }
            }).start();
        }
    }

    public static void setImageOnlyDown(final String url, final ImageView img) {
        if (url.equals("") || url.length() < 3) {
            return;
        }
        String pathName = sdPath;
        File file = new File(pathName
                + url.substring(url.lastIndexOf("/"), url.length()));
        if (file.exists()) {
            FileInputStream stream = null;
            try {
                stream = new FileInputStream(file);
                final Bitmap bitmap = BitmapFactory.decodeStream(stream);
                img.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        img.setImageBitmap(bitmap);
                    }
                });
                stream = null;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    String fileName = url.substring(url.lastIndexOf("/"),
                            url.length());
                    byte[] bytes = download2(url);
                    if (bytes != null && bytes.length != 0) {
                        writeFileToSD(fileName, bytes, bytes.length);
                    }
                }
            }).start();
        }
    }


    public static void onlyDownImage(final String url) {
        if (url.length() == 0) {
            return;
        }
        String pathName = sdPath;
        File file = new File(pathName
                + url.substring(url.lastIndexOf("/"), url.length()));
        if (!file.exists()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    String fileName = url.substring(url.lastIndexOf("/"),
                            url.length());
                    byte[] bytes = download2(url);
                    if (bytes != null && bytes.length != 0) {
                        writeFileToSD(fileName, bytes, bytes.length);
                    }
                }
            }).start();
        }
    }

    /**
     * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络,4:3g
     *
     * @param context
     * @return
     */
    public static String getAPNType(Context context) {
        String netType = "未知";
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = "CMNET";
            } else if (networkInfo.getExtraInfo().toLowerCase().equals("3gnet")) {
                netType = "3GNET";
            } else {
                netType = "CMWAP";
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = "WIFI";
        }
        return netType;
    }

    public static String getMarketAddCode(String s) {
        String a = "";
        String b = "";
        Matcher m = pattern.matcher(s);
        Matcher n = pattern1.matcher(s);
        while (n.find()) {
            a = n.group();
        }
        while (m.find()) {
            b = n.group();
        }
        return a + "|" + b;
    }

    public static void readAssets(Context context, String fileName,
                                  Complete... completes) {
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            getFile(buffer, filePath, fileName, completes);
        } catch (IOException e) {
            // Should never happen!
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void getFile(byte[] bfile, String filePath, String fileName,
                               Complete... completes) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "/" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (completes != null) {
                for (Complete complete : completes) {
                    complete.complete();
                }
            }
        }
    }

    public static boolean isEmptyAndSpace(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        if (str.equals(" ")) {
            return true;
        }
        return false;
    }

    /**
     * 从上往下显示动画
     * position第几个显示
     * v显示的控件
     *
     * @param
     * @param
     */
    public static void startAni(final int position, final View v) {
        // if (v.getLayoutParams() == null) {
        // v.setLayoutParams(new
        // ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        // }
        // // 保证控件v的渲染高度getMeasuredHeight
        // v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
        // MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        v.getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        // 判断在屏幕显示范围内个数才播放动画
                        if (position > Util.HEIGHT / v.getMeasuredHeight() - 1) {
                            return;
                        }
                        ViewHelper.setAlpha(v, 0);
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(
                                ObjectAnimator.ofFloat(v, "translationY",
                                        -v.getMeasuredHeight(), 0),
                                ObjectAnimator.ofFloat(v, "alpha", 0f, 1f));
                        set.setStartDelay(100 * position);
                        set.setDuration(200).start();
                        v.getViewTreeObserver().removeGlobalOnLayoutListener(
                                this);
                    }
                });

    }

    public static void setLEDFont(TextView textView) {
        textView.setTypeface(Typeface.createFromAsset(MyApplication
                .getInstance().getAssets(), "digital-7.ttf"));
    }

    public static void setFZLTHFont(TextView textView) {
        textView.setTypeface(Typeface.createFromAsset(MyApplication
                .getInstance().getAssets(), "font/fangzhenglantinghei.TTF"));
    }
    public static void sethyqiheiFont(TextView textView) {
        textView.setTypeface(Typeface.createFromAsset(MyApplication
                .getInstance().getAssets(), "font/hyqihei.otf"));
    }
    public static void setlantingheijingFont(TextView textView) {
        textView.setTypeface(Typeface.createFromAsset(MyApplication
                .getInstance().getAssets(), "font/lantingheijian.TTF"));
    }
    public static void setlantingxiheiFont(TextView textView) {
        textView.setTypeface(Typeface.createFromAsset(MyApplication
                .getInstance().getAssets(), "font/lantingxiheigbk.TTF"));
    }

    public static String getStringByObject(Object object) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        String s = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        objectOutputStream.close();
        return s;
    }

    public static interface readComplete {
        public void read(Object object);
    }

    public static Object dataRead(final String mobilesString) {

        try {
            Object obj = null;
            if (mobilesString == null) {
                return obj;
            }
            byte[] mobileBytes = Base64.decode(mobilesString.getBytes(),
                    Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    mobileBytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            obj = objectInputStream.readObject();
            objectInputStream.close();
            return obj;
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    public static String getStringFromFile(final String fileName) {
        String Result = "";
        try {
            File file = new File(filePath + "/" + fileName);
            FileInputStream fin = new FileInputStream(file);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            Result = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result;
    }

    public static void writeFileToFile(final String fileName, final byte[] b,
                                       final int byteCount) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String pathName = filePath;
                    File path = new File(pathName);
                    File file = new File(pathName + "/" + fileName);
                    if (!path.exists()) {
                        path.mkdir();
                    }
                    if (file.exists()) {
                        file.delete();// 如果目标文件已经存在，则删除，产生覆盖旧文件的效果
                    }
                    file.createNewFile();
                    FileOutputStream stream = new FileOutputStream(file);
                    stream.write(b, 0, byteCount);
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

	/*
     * 检查是否有网络
	 */

    public static boolean checkNetUsable() {

        return !Constent.netType.equals("未知");
    }

    public static String getStandardDate(String timeStr) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",
                Locale.getDefault());
        Date date = sdf.parse(timeStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        System.out.println(cal.getTimeInMillis());

        StringBuffer sb = new StringBuffer();

        long t = cal.getTimeInMillis();
        long time = System.currentTimeMillis() - t;
        long mill = (long) Math.ceil(time / 1000);// 秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

    public static void startAni(final int position, final View v, final ListView lv) {
        v.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                // 判断在屏幕显示范围内个数才播放动画
                int h = Util.HEIGHT;
                if (position >= (h / v.getMeasuredHeight())) {
                    return;
                }
                ViewHelper.setAlpha(v, 0);
                AnimatorSet set = new AnimatorSet();
                set.playTogether(ObjectAnimator.ofFloat(v, "translationY", -v.getMeasuredHeight(), 0),
                        ObjectAnimator.ofFloat(v, "alpha", 0f, 1f));
                set.setStartDelay(100 * position);
                set.setDuration(200).start();
                v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

    }

    public static String ToDBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);

            }
        }
        return new String(c);
    }

    public static String delHTMLTag(String htmlStr) {
        Pattern p_script = Pattern.compile(regEx_script,
                Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern
                .compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern
                .compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        return htmlStr.trim(); // 返回文本字符串
    }

    public static String delHTMLTag2(String htmlStr) {
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签
        return htmlStr.trim(); // 返回文本字符串
    }

    public static String myTrim(String rs) { // 去掉空格 以及奇葩字符

        String str = rs.replaceAll("\\?", "")
                .replaceAll("target=\"_blank\">", "")
                .replaceAll("target=\"blank\">", "")
                .replaceAll("target='_blank'>", "")
                .replaceAll("target='blank'>", "").replaceAll("&nbsp;", "")
                .replaceAll("&sbquo;", ",")

                .replaceAll("   ", "").replaceAll("@@@", "\n");

//		for (int i = 0; i < SYXinWen.removeStr.length; i++) {
//			str = str.replaceAll(SYXinWen.removeStr[i], "");
//		}

        if (str != null) {
            int len = str.length();
            if (len > 0) {
                char[] dest = new char[len];
                int destPos = 0;
                for (int i = 0; i < len; ++i) {
                    char c = str.charAt(i);
                    if (!Character.isWhitespace(c)) {
                        dest[destPos++] = c;
                    }
                }
                return new String(dest, 0, destPos);
            }
        }
        return str;
    }

    public static String getTextFromHtml(String htmlStr) {
        htmlStr = delHTMLTag(htmlStr);
        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        htmlStr = htmlStr.substring(0, htmlStr.indexOf("。") + 1);
        return htmlStr;
    }

    public static String getNowTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
                .format(new Date());
    }

    public static String getDateNoss(long time) {
        Date date = new Date(time);
        return format11.format(date);
    }

    public static String getDateHHmmss(long time) {
        Date date = new Date(time);
        return format12.format(date);
    }

    /**
     * 针对TextView显示中文中出现的排版错乱问题，通过调用此方法得以解决
     *
     * @param str
     * @return 返回全部为全角字符的字符串
     */
    public static String toDBC(String str) {
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }

        }
        return new String(c);
    }

    // 替换、过滤特殊字符
    public static String StringFilter(String str) throws PatternSyntaxException {

        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }

        }
        str = new String(c);
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("（","(").replaceAll("）",")");//替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
    //钱够3位数加，

    public static String formatMoney(int money) {


        String str_money = String.valueOf(money);

        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer remoney = new StringBuffer();
        int flag = 0;
        for (int i = str_money.length() - 1; i >= 0; i--) {
            char c = str_money.charAt(i);

            stringBuffer.append(c);
            flag++;
            if (flag == 3 && 3 < str_money.length()) {
                stringBuffer.append(",");
                flag = 0;
            }

        }
        for (int i = stringBuffer.length() - 1; i >= 0; i--) {
            remoney.append(stringBuffer.charAt(i));

        }
        return remoney.toString();
    }

    //
    public static String hintNumber(String num, int frist, int last) {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(num.substring(0, frist));
        stringBuffer.append("****");
        stringBuffer.append(num.substring(num.length() - last, num.length()));
        return stringBuffer.toString();

    }

    public static boolean isValidMobile(String mobiles) {
//        Pattern p = Pattern
//                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//        Matcher m = p.matcher(mobiles);
//        return m.matches();

        Pattern p = Pattern
                .compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }

    //百度定位配置
    public static LocationClientOption initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(false);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//        mLocationClient.setLocOption(option);
        return option;
    }




    //zjz 判断是否为身份证
    public static boolean IDCardValidate(String IDStr) throws ParseException {
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2" };
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2" };
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            Log.i("zjz","idsard_errorInfo="+errorInfo);
            return false;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (!isNumeric(Ai)) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            Log.i("zjz","idsard_errorInfo="+errorInfo);
            return false;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
//        if (!isDataFormat(strYear + "-" + strMonth + "-" + strDay)) {
//            errorInfo = "身份证生日无效。";
//            Log.i("zjz","idsard_errorInfo="+errorInfo);
//            return false;
//        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                Log.i("zjz","idsard_errorInfo="+errorInfo);
                return false;
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            Log.i("zjz","idsard_errorInfo="+errorInfo);
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            Log.i("zjz","idsard_errorInfo="+errorInfo);
            return false;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            Log.i("zjz","idsard_errorInfo="+errorInfo);
            return false;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

//        if (IDStr.length() == 18) {
//            if (!Ai.equals(IDStr)) {
//                errorInfo = "身份证无效，不是合法的身份证号码";
//                Log.i("zjz","idsard_errorInfo="+errorInfo);
//                return false;
//            }
//        } else {
//            return true;
//        }
        // =====================(end)=====================
        return true;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     *
     * @param str
     * @return
     */
    private static boolean isDataFormat(String str) {
        boolean flag = false;
        // String
        // regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?" +
                "((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))" +
                "|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])" +
                "|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])" +
                "|(1[02]))[\\-\\/\\s]?((0?[1-9])| ([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])" +
                "|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])| (2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))" +
                "\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }


    public static String formatDisplayTime(long time) {
        String display = "";
        int tMin = 60 * 1000;
        int tHour = 60 * tMin;
        int tDay = 24 * tHour;

        if (time != 0) {
            try {
                Date tDate = new Date(time);
                Date today = new Date();
                SimpleDateFormat thisYearDf = new SimpleDateFormat("yyyy");
                SimpleDateFormat todayDf = new SimpleDateFormat("yyyy-MM-dd");
                Date thisYear = new Date(thisYearDf.parse(thisYearDf.format(today)).getTime());
                Date yesterday = new Date(todayDf.parse(todayDf.format(today)).getTime());
                Date beforeYes = new Date(yesterday.getTime() - tDay);
                if (tDate != null) {
                    SimpleDateFormat halfDf = new SimpleDateFormat("MM月dd日");
                    long dTime = today.getTime() - tDate.getTime();
                    if (tDate.before(thisYear)) {
                        display = new SimpleDateFormat("yyyy年MM月dd日").format(tDate);
                    } else {

                        if (dTime < tMin) {
                            display = "刚刚";
                        } else if (dTime < tHour) {
                            display = (int) Math.ceil(dTime / tMin) + "分钟前";
                        } else if (dTime < tDay && tDate.after(yesterday)) {
                            display = (int) Math.ceil(dTime / tHour) + "小时前";
                        } else if (tDate.after(beforeYes) && tDate.before(yesterday)) {
                            display = "昨天" + new SimpleDateFormat("HH:mm").format(tDate);
                        } else {
                            display = halfDf.format(tDate);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return display;
    }

    public static String  ReplaceSpecialSymbols( String s)
    {
        s = s.replace("&amp;", "&");
        s = s.replace("&quot;", " ");
        s = s.replace("&lt;", "<");
        s = s.replace("&gt;", ">");
        s = s.replace("&nbsp;", " ");
        s = s.replace("&laquo;", "«");
        s = s.replace("&raquo;", "»");
        s = s.replace("&lsquo;", "‘");
        s = s.replace("&rsquo;", "’");
        s = s.replace("&ldquo;", "“");
        s = s.replace("&rdquo;", "”");
        s = s.replace("&sect;", "§");
        s = s.replace("&copy;", "©");
        s = s.replace("&hellip;", "…");
        s = s.replace("&oplus;", "⊕");
        s = s.replace("&nabla;", "∇");
        s = s.replace("&times;", "×");
        s = s.replace("&divide;", "÷");
        s = s.replace("&plusmn;", "±");
        s = s.replace("&radic;", "√");
        s = s.replace("&infin;", "∞");
        s = s.replace("&ang;", "∠");
        s = s.replace("&int;", "∫");
        s = s.replace("&deg;", "°");
        s = s.replace("&ne;", "≠");
        s = s.replace("&equiv;", "≡");
        s = s.replace("&le;", "≤");
        s = s.replace("&ge;", "≥");
        s = s.replace("&perp;", "⊥");
        s = s.replace("&there4;", "∴");
        s = s.replace("&pi;", "π");
        s = s.replace("&sup1;", "¹");
        s = s.replace("&sup2;", "²");
        s = s.replace("&sup3;", "³");
        s = s.replace("&crarr;", "↵");
        s = s.replace("&larr;", "←");
        s = s.replace("&uarr;", "↑");
        s = s.replace("&rarr;", "→");
        s = s.replace("&darr;", "↓");
        s = s.replace("&lArr;", "⇐");
        s = s.replace("&uArr;", "⇑");
        s = s.replace("&rArr;", "⇒");
        s = s.replace("&dArr;", "⇓");
        s = s.replace("&alpha;", "α");
        s = s.replace("&beta;", "β");
        s = s.replace("&gamma;", "γ");
        s = s.replace("&Delta;", "Δ");
        s = s.replace("&theta;", "θ");
        s = s.replace("&lambda;", "λ");
        s = s.replace("&Sigma;", "Σ");
        s = s.replace("&tau;", "τ");
        s = s.replace("&omega;", "ω");
        s = s.replace("&Omega;", "Ω");
        s = s.replace("&bull;", "•");
        return s;
    }

    public static boolean isAppAlive(Context context, String packageName){
        ActivityManager activityManager =
                (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for(int i = 0; i < processInfos.size(); i++){
            if(processInfos.get(i).processName.equals(packageName)){
                Log.i("ziz",
                        String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("ziz",
                String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }

    /**
     * 定位
     * 提取出城市或者县
     * @param city
     * @param district
     * @return
     */
    public static String extractLocation(final String city, final String district){
        return district.contains("县") ? district.substring(0, district.length() - 1) : city.substring(0, city.length() - 1);
    }
    /**
     * 获取拼音的首字母（大写）
     * @param pinyin
     * @return
     */
    public static String getFirstLetter(final String pinyin){
        if (TextUtils.isEmpty(pinyin)) return "定位";
        String c = pinyin.substring(0, 1);
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c).matches()){
            return c.toUpperCase();
        } else if ("0".equals(c)){
            return "定位";
        } else if ("1".equals(c)){
            return "热门";
        }
        return "定位";
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels){
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8 ){
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        }else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels){
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 150 : (int) Math.min(Math.floor(w / minSideLength),Math.floor(h / minSideLength));
        if (upperBound < lowerBound){
            return lowerBound;
        }
        if((maxNumOfPixels == -1) && (minSideLength == -1)){
            return 1;
        }else if (minSideLength == -1){
            return lowerBound;
        }else {
            return upperBound;
        }
    }

    /**
     * 获取图片绝对路径
     *
     * */
    public static String getAbsoluteImagePath(Uri uri, Activity activity){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static Bitmap getBitmapFromUrl(String urlpath) {
        Bitmap map = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            options.inSampleSize = calculateInSampleSize(options, 480, 800);
            map = BitmapFactory.decodeStream(in,null,options);
            // TODO Auto-generated catch block
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static boolean containsString( String dest) {
        String src= "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        boolean flag = false;
        if (src.contains(dest)) {
            flag = true;
        }
        return flag;
    }
}
