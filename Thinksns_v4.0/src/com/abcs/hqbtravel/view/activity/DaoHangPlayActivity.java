package com.abcs.hqbtravel.view.activity;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.hqbtravel.entity.DaohangDetials2;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.sociax.android.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class DaoHangPlayActivity extends Activity implements View.OnClickListener,MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{

    private  static final String TAG = "DaoHangPlayActivity";
    private ImageView img_back,img_logo,play;
    private TextView tv_title,tv_name_zh,tv_name_en,tv_mune_right,tv_play_time;
    private String type,cnName,enName,audio_url;
    private DaohangDetials2.BodyEntity.GuideEntity guideEntity;
    private DaohangDetials2.BodyEntity.GuideEntity.PicturesEntity picturesEntity;

    private Timer timer=null;
    private TimerTask mTimerTask=null;

    private boolean isPlay=true;
    private boolean isRun=true;
    private SimpleDateFormat format= new SimpleDateFormat("HH:mm:ss");

    private static final int OK = 898;


    private Handler mhandle=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case OK:
                    if(isRun){
                        //更新进度
                        int position = player.getCurrentPosition();
                        tv_play_time.setText(format.format(new Date(position)).substring(3));

                    }
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_dao_hang_play);
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        timer=new Timer();

        play=(ImageView)findViewById(R.id.play);
        img_back=(ImageView)findViewById(R.id.img_back);
        img_logo=(ImageView)findViewById(R.id.img_logo);
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_play_time=(TextView)findViewById(R.id.tv_play_time);
        tv_name_zh=(TextView)findViewById(R.id.tv_name_zh);
        tv_name_en=(TextView)findViewById(R.id.tv_name_en);
        tv_mune_right=(TextView)findViewById(R.id.tv_mune_right);

        img_back.setOnClickListener(this);
        play.setOnClickListener(this);
        tv_mune_right.setOnClickListener(this);


        type=getIntent().getStringExtra("type");

        if(type.equals("one")){
            guideEntity=(DaohangDetials2.BodyEntity.GuideEntity)getIntent().getBundleExtra("audio_url").getSerializable("guide");

//            Log.i("嘎嘎嘎  guideEntity",new Gson().toJson(guideEntity));

            if(!TextUtils.isEmpty(guideEntity.audioUrl)){
                audio_url=guideEntity.audioUrl;
//                String void_url="http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb_abc_other%2F%E6%97%A0%E6%A0%87%E9%A2%98%20(91479354284901.wma";
//                String void_url="http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb_abc_other%2F%E8%8A%AD%E6%8F%90%E9%9B%85%E6%B5%B7%E6%BB%A91479352432839.3gpp";
//                initMediaplayer("1",void_url);
                initMediaplayer(guideEntity.id,guideEntity.audioUrl);
//                initMediaplayer(guideEntity.audioUrl);
            }

            if(!TextUtils.isEmpty(guideEntity.picUrl)){
                MyApplication.imageLoader.displayImage(guideEntity.picUrl, img_logo, MyApplication.getListOptions());
            }

            if(!TextUtils.isEmpty(guideEntity.name)){
                tv_title.setText(guideEntity.name);
            }

            if(!TextUtils.isEmpty(guideEntity.name)){
                tv_name_zh.setText(guideEntity.name);
            }

            if(!TextUtils.isEmpty(guideEntity.enName)){
                tv_name_en.setText(guideEntity.enName);
            }

            Log.i("嘎嘎嘎  guide audio_url",audio_url+"");

        }else {
            picturesEntity=(DaohangDetials2.BodyEntity.GuideEntity.PicturesEntity)getIntent().getBundleExtra("audio_url_item").getSerializable("guide_item");

//            cnName=getIntent().getStringExtra("cnName");
//            enName=getIntent().getStringExtra("enName");

            audio_url=picturesEntity.audioUrl;
            initMediaplayer(picturesEntity.id,picturesEntity.audioUrl);
//            initMediaplayer(picturesEntity.audioUrl);

//            Log.i("嘎嘎嘎  picturesEntity",new Gson().toJson(picturesEntity));
//            Log.i("嘎嘎嘎  cnName",cnName+"");
//            Log.i("嘎嘎嘎  enName",enName+"");
//            Log.i("嘎嘎嘎  audio_url",audio_url+"");

            if(!TextUtils.isEmpty(picturesEntity.imgUrl)){

                MyApplication.imageLoader.displayImage(picturesEntity.imgUrl, img_logo, MyApplication.getListOptions());
            }

            if(!TextUtils.isEmpty(picturesEntity.voiceName)){

                tv_title.setText(picturesEntity.voiceName);
            }

            if(!TextUtils.isEmpty(picturesEntity.voiceName)){

                tv_name_zh.setText(picturesEntity.voiceName);
            }

//            tv_name_en.setText(enName);

        }



//        mhandle.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },10);

        mhandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressDlgUtil.stopProgressDlg();
            }
        },2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_mune_right:  /****分享****/
                Toast.makeText(this,"该功能暂未开启", Toast.LENGTH_SHORT).show();
                break;
            case R.id.play:  /****播放或者暂停****/
                if(isPlay){
                    if(player!=null) {
                        player.start();
                        isRun=true;
                        tv_play_time.setVisibility(View.VISIBLE);
                        play.setImageResource(R.drawable.stop);
                        isPlay=false;
                    }
                }else {
                    if(player != null &&player.isPlaying()) {
                        player.pause();
                        tv_play_time.setVisibility(View.GONE);
                        play.setImageResource(R.drawable.play3_yuyin);
                        isPlay=true;
                        isRun=false;
                    }
                }

                if(isRun){
                    timer.schedule(mTimerTask=new TimerTask() {
                        @Override
                        public void run() {
                            if(player==null)
                                return;
                            if (player.isPlaying()) {
                                mhandle.sendEmptyMessage(OK);
                            }
                        }
                    },100,1000);
                }

                break;
        }
    }

    private MediaPlayer player;

    public  void initMediaplayer(String fileName, String audioUrl){
        try {

//            String SDCard= Environment.getExternalStorageDirectory()+"";
//            String pathName=SDCard+"/"+path+"/"+fileName;//文件存储路径

//            if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
//
//                File file= new File(Environment.getExternalStorageDirectory()+"/"+"file"+"/",fileName+".mp3");
//                if(file.getPath()!=null){
//                    if(player==null){
//                        player = new MediaPlayer();
//                    }
//                    FileInputStream fis = new FileInputStream(file);
//
//                    player.setDataSource(fis.getFD());
//                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    player.setOnCompletionListener(this);
//                    player.setOnPreparedListener(this);
//                    player.prepare();
//                    Log.i("mp3",file.getPath().toString());
//                }else {
//                    player = MediaPlayer.create(this,Uri.parse(audioUrl));
//                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    player.setOnCompletionListener(this);
//                    player.setOnPreparedListener(this);
//                }
//            }else {
                player = MediaPlayer.create(this, Uri.parse(audioUrl));
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setOnCompletionListener(this);
                player.setOnPreparedListener(this);
//            }

//            if(file.getPath()!=null){
//                player.setDataSource(file.getPath());
//            }else {
//                player.setDataSource(audio_url);
////                player.setDataSource(this,Uri.parse(url));
//            }

//                player =  new MediaPlayer();

//                player.reset();
//                /**************播放网络文件**************/
//                player.setDataSource(url);
//                player.prepare();

        }catch (Exception e){
            Log.e(TAG, "initMediaplayer: "+e.getMessage(),e );
        }
    }
//    public  void initMediaplayer(String url){
//
//        try {
//            if(TextUtils.isEmpty(url)){
//                return;
//            }else {
//                if(player==null){
//                    player =   MediaPlayer.create(this, Uri.parse(url));
//                }
//
////                player =  new MediaPlayer();
//                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                player.setOnCompletionListener(this);
//                player.setOnPreparedListener(this);
////                player.reset();
////                /**************播放网络文件**************/
////                player.setDataSource(url);
////                player.prepare();
//
//            }
//        }catch (Exception e){
//            Log.e(TAG, "initMediaplayer: "+e.getMessage(),e );
//        }
//    }

    @Override
    protected void onDestroy() {
        // 在activity结束的时候回收资源
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        isRun=false;

        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        isRun=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRun=false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRun=false;
        isPlay=false;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {

        Log.e("mediaPlayer", "onCompletion");
        play.setImageResource(R.drawable.play3_yuyin);
        isPlay=true;
        isRun=false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
//        tv_yuyin_totaltime.setText(format.format(new Date(player.getDuration())).substring(3));
        tv_play_time.setText("00:00");
    }

}
