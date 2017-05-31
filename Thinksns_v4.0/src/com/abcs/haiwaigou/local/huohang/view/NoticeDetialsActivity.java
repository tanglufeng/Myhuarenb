package com.abcs.haiwaigou.local.huohang.view;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NoticeDetialsActivity extends BaseActivity {


    @InjectView(R.id.tljr_title)
    TextView tljrTitle;
    @InjectView(R.id.re_detele)
    RelativeLayout reDetele;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.tljr_messge)
    TextView tljrMessge;
    @InjectView(R.id.tv_time)
    TextView tvTime;

    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detials);
        ButterKnife.inject(this);

        String  messageTitle=getIntent().getStringExtra("messageTitle");
        String  messageDetails=getIntent().getStringExtra("messageDetails");
        long  addTime=getIntent().getLongExtra("addTime",0);
        int id=getIntent().getIntExtra("id",0);

        if(!TextUtils.isEmpty(messageTitle)){
            tljrTitle.setText(messageTitle);
        }
        if(!TextUtils.isEmpty(messageDetails)){
            tljrMessge.setText(messageDetails);
        }
        if (addTime < 2 * 1000000000) {
            tvTime.setText(Util.format11.format(addTime * 1000));
        } else {
            tvTime.setText(Util.format11.format(addTime));
        }

        loading(id);
    }

    private void loading(int id){
//        http://www.huaqiaobang.com/mobile/index.php?act=native_message&op=read_message&key=939f6c2c1ad7199187be733cc714955a&id=2
        HttpRequest.sendGet(com.thinksns.sociax.thinksnsbase.utils.TLUrl.getInstance().URL_hwg_base + "/mobile/index.php", "act=native_message&op=read_message&key=" + MyApplication.getInstance().getMykey()+"&id="+id, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "run: " + msg);

                        try {
                            JSONObject object=new JSONObject(msg);
                            if(object.optInt("state")==1){
                                EventBus.getDefault().post("readmsg");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @OnClick({R.id.relative_back,R.id.re_detele})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_back:// 返回
                finish();
                break;
            case R.id.re_detele: // 删除
                break;
        }
    }
}
