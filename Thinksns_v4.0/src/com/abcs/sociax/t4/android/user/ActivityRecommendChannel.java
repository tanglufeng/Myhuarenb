package com.abcs.sociax.t4.android.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.abcs.sociax.android.R;
import com.abcs.sociax.component.CustomTitle;
import com.abcs.sociax.component.LeftAndRightTitle;
import com.abcs.sociax.t4.android.ActivityHome;
import com.abcs.sociax.t4.android.ThinksnsAbscractActivity;
import com.abcs.sociax.t4.android.fragment.FragmentRecommendChannel;
import com.abcs.sociax.t4.service.AddRcdFriendService;
import com.thinksns.sociax.thinksnsbase.bean.SociaxItem;

import java.io.Serializable;
import java.util.List;

public class ActivityRecommendChannel extends ThinksnsAbscractActivity {

    private Button btn_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentRecommendChannel fragment = new FragmentRecommendChannel();
        fragmentTransaction.add(R.id.fl_content, fragment);
        fragmentTransaction.commit();

        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRecommendChannel fragment = (FragmentRecommendChannel) fragmentManager.findFragmentById(R.id.fl_content);
                Intent followIntent = new Intent(ActivityRecommendChannel.this, AddRcdFriendService.class);
                List<SociaxItem> data = fragment.getData();
                followIntent.putExtra("channel", (Serializable) data);
                startService(followIntent);
                startActivity(new Intent(ActivityRecommendChannel.this, ActivityHome.class));
            }
        });

    }

    @Override
    public String getTitleCenter() {
        return "推荐频道";
    }

    @Override
    protected CustomTitle setCustomTitle() {
        return new LeftAndRightTitle(this, R.drawable.img_back, "跳过");
    }

    @Override
    public View.OnClickListener getRightListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityRecommendChannel.this, ActivityHome.class));
                finish();
            }
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recommend_channel;
    }
}
