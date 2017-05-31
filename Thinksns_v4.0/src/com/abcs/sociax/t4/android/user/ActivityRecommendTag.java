package com.abcs.sociax.t4.android.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.abcs.sociax.android.R;
import com.abcs.sociax.component.CustomTitle;
import com.abcs.sociax.component.LeftAndRightTitle;
import com.abcs.sociax.t4.android.Thinksns;
import com.abcs.sociax.t4.android.ThinksnsAbscractActivity;
import com.abcs.sociax.t4.android.data.StaticInApp;
import com.abcs.sociax.t4.android.fragment.FragmentTag;
import com.abcs.sociax.t4.android.interfaces.OnPopupWindowClickListener;
import com.abcs.sociax.t4.android.popupwindow.PopupWindowCommon;
import com.thinksns.sociax.thinksnsbase.exception.ApiException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类说明：
 *
 * @author Zoey
 * @version 1.0
 * @date 2015年10月25日
 */
public class ActivityRecommendTag extends ThinksnsAbscractActivity {

    private Button btn_next;
    private ImageView iv_back;
    private PopupWindowCommon backTip;
    private ExecutorService single;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentTag fragment = new FragmentTag();
        fragmentTransaction.add(R.id.fl_content, fragment);
        fragmentTransaction.commit();

        single = Executors.newSingleThreadExecutor();

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //把tag存起来
                SharedPreferences preferences= Thinksns.getContext().getSharedPreferences(StaticInApp.TAG_CLOUD, Context.MODE_PRIVATE);
                final String ids=preferences.getString("title","");

                if (ids!=null&&!ids.equals("null")&&!ids.equals("")){
                    single.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Message msg = new Message();
                                msg.what = StaticInApp.ADD_MY_TAG;
                                try {
                                    Thinksns app = (Thinksns)getApplicationContext();
                                    msg.obj = app.getTagsApi().addTag(ids);
                                } catch (ApiException e) {
                                    e.printStackTrace();
                                }
                                handler.sendMessage(msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),"请至少选择一个标签",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    OnPopupWindowClickListener listener = new OnPopupWindowClickListener() {
        @Override
        public void firstButtonClick() {
            backTip.dismiss();
        }

        @Override
        public void secondButtonClick() {
            ActivityRecommendTag.this.finish();
        }
    };

    @Override
    public String getTitleCenter() {
        return "选择标签";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recommend_tag;
    }

    @Override
    protected CustomTitle setCustomTitle() {
        return new LeftAndRightTitle(this, null, "跳过");
    }

    @Override
    public View.OnClickListener getRightListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityRecommendTag.this, ActivityRecommendFriend.class));
            }
        };
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticInApp.ADD_MY_TAG:

                    startActivity(new Intent(ActivityRecommendTag.this, ActivityRecommendFriend.class));

                    break;
            }
        }
    };
}
