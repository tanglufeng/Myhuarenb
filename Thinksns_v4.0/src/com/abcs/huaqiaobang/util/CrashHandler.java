package com.abcs.huaqiaobang.util;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/16.
 */
public class CrashHandler implements UncaughtExceptionHandler {


    private static CrashHandler crashHandler = new CrashHandler();


    private UncaughtExceptionHandler mDefaultHandle;

    private Map<String, String> infos = new HashMap<String, String>();//保存设备信息
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
    //程序的Context对象
    private Context mContext;

    private CrashHandler() {

    }

    public static CrashHandler getInstance() {
        return crashHandler;
    }

    public void init(Context context) {
        this.mContext = context;
        mDefaultHandle = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);

    }


    //当uncaughtException 异常出现处理
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handlerException(ex) && mDefaultHandle != null) {


            mDefaultHandle.uncaughtException(thread, ex);
        } else {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

    }

    private boolean handlerException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                Looper.prepare();
                Toast.makeText(mContext, "程序异常，即将退出程序!", Toast.LENGTH_LONG).show();
                Looper.loop();

            }
        }).start();

        //收集资料
//        collectDeviceInfo(mContext);

        //保存日志文件
//        saveCrashInfo2File(ex);
        return true;
    }

    private void saveCrashInfo2File(Throwable ex) {


        String errorInfo = getErrorInfo(ex);
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "hqb/log.txt");
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
            bw.write(errorInfo, 0, 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void collectDeviceInfo(Context mContext) {


    }

    /**
     * 获取错误的信息
     *
     * @param arg1
     * @return
     */
    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        return error;
    }

}
