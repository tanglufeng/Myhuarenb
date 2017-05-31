package com.abcs.haiwaigou.utils;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.abcs.huaqiaobang.util.Util;

import java.math.BigInteger;

public class mCountDownTimer extends CountDownTimer {

    OnFinishListener finishListener;

    public interface OnFinishListener {
        void finish(TextView[] tv);
    }


    TextView[] tv;

    /**
     * @param millisInFuture    表示以毫秒为单位 倒计时的总数
     *                          <p/>
     *                          例如 millisInFuture=1000 表示1秒
     * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
     *                          <p/>
     *                          例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
     */
    public mCountDownTimer(long millisInFuture, long countDownInterval, TextView... tv) {
        super(millisInFuture, countDownInterval);
        this.tv = tv;
    }

    public void setOnFinishListener(OnFinishListener finishListener) {
        this.finishListener = finishListener;
    }


    @Override
    public void onFinish() {
        //    tv.setText("done");
        if (finishListener != null) {
            finishListener.finish(tv);
        }

    }

    @Override
    public void onTick(long millisUntilFinished) {
//        Log.i("zjz", millisUntilFinished + "");

        timeC(millisUntilFinished);


    }

    public String getDoubelString(long i) {
        String time = "" + i;
        if (i < 10) {
            time = "0" + i;
        }
        return time;
    }

    public String timeC(long mss) {

//        String substring= String.valueOf(mss);
//        String substring = s.substring(0, s.length() - 4);
//        int tempmss = Integer.parseInt(substring);
        long days=0;
        long hours=0;
        long minutes = 0;
        long seconds=0;
        long mSeconds=0;
//        int days = mss / (24 * 60 * 60 * 1000) * (-10);
//        int hours = (mss - days * (24 * 60 * 60 * 1000)) / (1000 * 60 * 60);
//        int minutes = (mss - days * (24 * 60 * 60 * 1000) - hours * (1000 * 60 * 60)) / (1000 * 60);
//        int seconds = (mss - days * (24 * 60 * 60 * 1000) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
//        int mSeconds = ((mss - days * (24 * 60 * 60 * 1000) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 10) % 100;
//        Log.i("zjz", "days=" + days);
//        Log.i("zjz", "hours=" + hours);
//        Log.i("zjz", "minutes=" + minutes);
//        Log.i("zjz", "seconds=" + seconds);
//        int hours = (mss / (1000 * 60 * 60));
//        int minutes = (mss - hours * (1000 * 60 * 60)) / (1000 * 60);
//        int seconds = (mss - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
//        int mSeconds =((mss - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 10 ) %100 ;
        if (tv.length == 1) {
//            tv[0].setText(getDoubelString(minutes) + " : "
//                    + getDoubelString(seconds) + " : " + getDoubelString(mSeconds));
            tv[0].setText(Util.formatzjz2.format(mss));
        } else if (tv.length == 3) {
            hours = (mss / (1000 * 60 * 60));
            minutes = (mss - hours * (1000 * 60 * 60)) / (1000 * 60);
            seconds = (mss - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
            mSeconds = ((mss - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 10) % 100;
            tv[0].setText(getDoubelString(hours));
            tv[1].setText(getDoubelString(minutes));
            tv[2].setText(getDoubelString(seconds));

        } else if (tv.length == 4) {
            days = mss / (24 * 60 * 60 * 1000);
            hours = (mss - days * (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
            minutes = (mss - days * (24 * 60 * 60 * 1000) - hours * (60 * 60 * 1000)) / (60 * 1000);
            seconds = (mss - days * (24 * 60 * 60 * 1000) - hours * (60 * 60 * 1000) - minutes * (60 * 1000)) / 1000;
            mSeconds = ((mss - days * (24 * 60 * 60 * 1000) - hours * (60 * 60 * 1000) - minutes * (60 * 1000)) / 10) % 100;
            tv[0].setText(getDoubelString(days));
            tv[1].setText(getDoubelString(hours));
            tv[2].setText(getDoubelString(minutes));
            tv[3].setText(getDoubelString(seconds));
        }


        return getDoubelString(minutes) + " : "
                + getDoubelString(seconds) + " : " + getDoubelString(mSeconds);
    }

}