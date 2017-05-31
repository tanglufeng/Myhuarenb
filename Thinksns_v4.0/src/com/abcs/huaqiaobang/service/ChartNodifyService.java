package com.abcs.huaqiaobang.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.abcs.huaqiaobang.chart.RealTimeView;

public class ChartNodifyService extends Service {
    public ChartNodifyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while (true) {

                        Log.i("tga", "service run===");
                        Thread.sleep(10000);

                        RealTimeView.CheckNewIMReal();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        return null;
    }


}
