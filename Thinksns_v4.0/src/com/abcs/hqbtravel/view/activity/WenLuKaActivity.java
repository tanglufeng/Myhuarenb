package com.abcs.hqbtravel.view.activity;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;

public class WenLuKaActivity extends Activity implements View.OnClickListener,MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{

    private RelativeLayout re_back;
    private String cn_na,en_na,location_na,photos_url,voice_url;
    private TextView tvname,en_name,location_name;
    private ImageView logo,imge_paly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wen_lu_ka);

        voice_url=getIntent().getStringExtra("voice_url");
        photos_url=getIntent().getStringExtra("photos_url");
        cn_na=getIntent().getStringExtra("cn_name");
        en_na=getIntent().getStringExtra("en_name");
        location_na=getIntent().getStringExtra("location_name");

        logo=(ImageView) findViewById(R.id.logo);
        imge_paly=(ImageView) findViewById(R.id.imge_paly);
        imge_paly.setOnClickListener(this);
        tvname=(TextView) findViewById(R.id.tvname);
        en_name=(TextView) findViewById(R.id.en_name);
        location_name=(TextView) findViewById(R.id.location_name);

        re_back=(RelativeLayout) findViewById(R.id.re_back);

        re_back.setOnClickListener(this);

        if(!TextUtils.isEmpty(photos_url)){
            MyApplication.imageLoader.displayImage(photos_url, logo, MyApplication.getListOptions());
        }
        if(!TextUtils.isEmpty(voice_url)){
            imge_paly.setVisibility(View.VISIBLE);
            initMediaplyer(voice_url);
        }
        if(!TextUtils.isEmpty(cn_na)){
            tvname.setText(cn_na);
        }
        if(!TextUtils.isEmpty(en_na)){
            en_name.setText(en_na);
        }
        if(!TextUtils.isEmpty(location_na)){
            location_name.setText(location_na);
        }
    }
    private MediaPlayer player;

    void initMediaplyer(String voice_url){
        try {
            player = MediaPlayer.create(this, Uri.parse(voice_url));
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnCompletionListener(this);
            player.setOnPreparedListener(this);

        }catch (Exception e){
            Log.e("zds", "initMediaplayer: "+e.getMessage(),e );
        }

    }


    private boolean isPlay=true;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.re_back:
                finish();
                break;
            case  R.id.imge_paly:
                if(isPlay){
                    if(player!=null) {
                        player.start();
                        imge_paly.setImageResource(R.drawable.stop);
                        isPlay=false;
                    }
                }else {
                    if(player != null &&player.isPlaying()) {
                        player.pause();
                        imge_paly.setImageResource(R.drawable.play3_yuyin);
                        isPlay=true;
                    }
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        // 在activity结束的时候回收资源
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }

        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isPlay=false;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {

        Log.e("mediaPlayer", "onCompletion");
        imge_paly.setImageResource(R.drawable.play3_yuyin);
        isPlay=true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
////        tv_yuyin_totaltime.setText(format.format(new Date(player.getDuration())).substring(3));
//        tv_play_time.setText("00:00");
    }

}
