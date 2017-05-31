package com.abcs.haiwaigou.yyg.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.huaqiaobang.util.Util;


public class MyTextView extends TextView {

    OnCountDownFinishListener countDownFinishListener;

    public interface OnCountDownFinishListener {
        void onFinish(MyTextView tv);
    }


    public OnCountDownFinishListener getCountDownFinishListener() {
        return countDownFinishListener;
    }


    public void setCountDownFinishListener(OnCountDownFinishListener countDownFinishListener) {
        this.countDownFinishListener = countDownFinishListener;
    }


    public boolean isRun = false;
    MyTextView myTextView;

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.myTextView = this;
    }


    public boolean isRun() {
        return isRun;
    }


    public void setRun(boolean isRun) {
        this.isRun = isRun;
    }


    CountDown countTime;

    public void setTime(long time) {

//        if (time - System.currentTimeMillis() < 0) {
//            if (countTime != null) {
//                countTime.cancel();
//            }
//
//            RelativeLayout.LayoutParams layoutParams =
//                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//            this.setLayoutParams(layoutParams);
//
//
//            this.setText("正在计算...");
//        } else {
//            if (countTime != null) {
//                countTime.cancel();
//            }
//
//            countTime = new CountDown(time - System.currentTimeMillis(), 10, myTextView, countDownFinishListener);
//            countTime.start();
//        }
        if(countTime!=null)
            countTime.cancel();
        countTime=new CountDown(time,10,myTextView,countDownFinishListener);
        countTime.start();


    }

    Handler handler=new Handler();

    class CountDown extends CountDownTimer {



        OnCountDownFinishListener countDownFinishListener;

        MyTextView tv;

        public CountDown(long millisInFuture, long countDownInterval, MyTextView tv, OnCountDownFinishListener countDownFinishListener) {
            super(millisInFuture, countDownInterval);
            this.tv = tv;
            this.countDownFinishListener = countDownFinishListener;
        }



        @Override
        public void onFinish() {
            tv.setText("计算中...");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MyUpdateUI.sendUpdateCollection(getContext(),MyUpdateUI.YYGLOTTERY);
                }
            },3000);
//            MyUpdateUI.sendJieXiao(zuixinjiexiaoFragment.activity);
        }

        @Override
        public void onTick(long millisUntilFinished) {
//            Log.i("zjz", millisUntilFinished + "");

            timeC((int) millisUntilFinished);

        }

        public String getDoubelString(int i) {
            String time = "" + i;
            if (i < 10) {
                time = "0" + i;
            }
            return time;
        }

        public void timeC(int mss) {
            int hours = (mss / (1000 * 60 * 60));
            int minutes = (mss - hours * (1000 * 60 * 60)) / (1000 * 60);
            int seconds = (mss - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
            int mSeconds = ((mss - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 10) % 100;

            tv.setText(getDoubelString(minutes) + ":"
                    + getDoubelString(seconds) + ":" + getDoubelString(mSeconds));

        }

    }

}
