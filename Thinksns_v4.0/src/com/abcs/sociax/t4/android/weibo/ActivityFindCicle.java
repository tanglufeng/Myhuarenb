package com.abcs.sociax.t4.android.weibo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.abcs.sociax.constant.AppConstant;
import com.abcs.sociax.t4.android.activity.HomeMessageActivity;
import com.abcs.sociax.t4.android.activity.HomeMyActivity;
import com.abcs.sociax.t4.android.channel.ActivityChannel;
import com.abcs.sociax.t4.android.checkin.ActivityCheckIn;
import com.abcs.sociax.t4.android.data.StaticInApp;
import com.abcs.sociax.t4.android.findpeople.ActivityFindPeople;
import com.abcs.sociax.t4.android.findpeople.ActivityFindPeopleDetails;
import com.abcs.sociax.t4.android.findpeople.ActivitySearchUser;
import com.abcs.sociax.t4.android.fragment.FragmentHome;
import com.abcs.sociax.t4.android.img.Bimp;
import com.abcs.sociax.t4.android.temp.SelectImageListener;
import com.abcs.sociax.t4.android.topic.ActivityTopicList;
import com.abcs.sociax.t4.android.video.MediaRecorderActivity;
import com.abcs.sociax.t4.android.weiba.ActivityWeiba;
import com.abcs.sociax.t4.component.MoreWindow;
import com.abcs.sociax.t4.model.ModelUser;
import com.thinksns.sociax.thinksnsbase.utils.Anim;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Zoey on 2016-05-09.
 */
//public class ActivityFindCicle extends ThinksnsAbscractActivity {
public class ActivityFindCicle extends BaseFragmentActivity {

    protected ModelUser user;        // 当前界面所属用户
    protected int uid;                // 当前界面所属uid

    private Fragment fragment;
    private ImageButton ib_new;
    RelativeLayout back;
    private MoreWindow mMoreWindow;
    protected SelectImageListener listener_selectImage;   //拍照工具

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cicle);

        back=(RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.img_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });

        initFragment();
        initListener();
    }

    private void initFragment() {
        fragment = new FragmentHome();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ll_content, fragment);
        fragmentTransaction.commit();

        ib_new=(ImageButton)findViewById(R.id.ib_new);


        Animation anim = new TranslateAnimation(100, 0, 0, 0);
        anim .setDuration(100); //这个值越大动画就越卡，越容易花屏
//        anim .setInterpolator(this, android.R.anim.accelerate_interpolator);
        anim .setFillAfter(true);
        ib_new.setAnimation(anim);

        listener_selectImage = new SelectImageListener(this);
    }

    public void initListener(){
        ib_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mMoreWindow == null) {
                    mMoreWindow = new MoreWindow(ActivityFindCicle.this);
                }
                mMoreWindow.showMoreWindow(view);
                mMoreWindow.setOnItemClick(new MoreWindow.IMoreWindowListener() {

                    @Override
                    public void OnItemClick(View v) {
                        switch (v.getId()) {
                            case R.id.tv_create_weibo_camera:
                                listener_selectImage.cameraImage();
                                break;
                            case R.id.tv_create_weibo_pic:
                                selectPhoto();
                                break;
                            case R.id.tv_create_weibo_video:
                                //拍摄视频
                                recordVideo();
                                break;
                            case R.id.tv_create_weibo_sign:
                                //签到
                                Intent intent = new Intent(ActivityFindCicle.this, ActivityCheckIn.class);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        Anim.in(ActivityFindCicle.this);
                    }
                });

                //长按事件
                ib_new.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Intent intent = new Intent(ActivityFindCicle.this, ActivityCreateBase.class);
                        intent.putExtra("type", AppConstant.CREATE_TEXT_WEIBO);
                        startActivityForResult(intent, 100);
                        Anim.in(ActivityFindCicle.this);
                        return false;
                    }
                });
            }
        });
    }


    private void selectPhoto() {
        Intent getImage = new Intent(this, MultiImageSelectorActivity.class);
        getImage.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        getImage.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
        getImage.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        getImage.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
                new ArrayList<String>());
        startActivityForResult(getImage, StaticInApp.LOCAL_IMAGE);
    }

    //录制视频
    private void recordVideo() {
        //跳转视频录制
        Intent intentVideo = new Intent(this, MediaRecorderActivity.class);
        startActivityForResult(intentVideo,AppConstant.CREATE_VIDEO_WEIBO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 刷新首页数据
        if (resultCode == RESULT_OK) {
            Intent intent=null;
            switch (requestCode) {
                case StaticInApp.LOCAL_IMAGE:
                    List<String> photoList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    boolean original = data.getBooleanExtra(MultiImageSelectorActivity.EXTRA_SELECT_ORIGIANL, false);
                    if (Bimp.address.size() < 9) {
                        for (String addr : photoList) {
                            if (!Bimp.address.contains(addr)) {
                                Bimp.address.add(addr);
                            }
                        }
                    }
                    //跳转至发布微博页
                    intent = new Intent(ActivityFindCicle.this, ActivityCreateBase.class);
                    intent.putExtra("type", AppConstant.CREATE_ALBUM_WEIBO);
                    intent.putExtra("is_original", original);
                    startActivity(intent);
                    Anim.in(ActivityFindCicle.this);
                    break;
                case StaticInApp.CAMERA_IMAGE:
                    if (Bimp.address.size() < 9) {
                        Bimp.address.add(listener_selectImage.getImagePath());
                    }
                    //跳转至发布微博页
                    intent = new Intent(ActivityFindCicle.this, ActivityCreateBase.class);
                    intent.putExtra("type", AppConstant.CREATE_ALBUM_WEIBO);
                    intent.putExtra("is_original", false);
                    startActivity(intent);
                    Anim.in(ActivityFindCicle.this);
                    break;
                case AppConstant.CREATE_VIDEO_WEIBO:
                    intent = new Intent(ActivityFindCicle.this,ActivityCreateBase.class);
                    intent.putExtra("type", AppConstant.CREATE_VIDEO_WEIBO);
                    startActivity(intent);
                    Anim.in(ActivityFindCicle.this);
                    break;
            }
        }
    }

    /**
     * 隐藏或显示发布微博按钮
     * @param isShow
     */
    public void toggleCreateBtn(boolean isShow) {
        if(isShow && ib_new != null) {
            ib_new.setVisibility(View.VISIBLE);
        }else {
            ib_new.setVisibility(View.GONE);
        }
    }


    PopupWindow popupWindow ;
    private void showPopupWindow(View parent) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.item_popview, null);
            view.findViewById(R.id.topic).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ActivityFindCicle.this, ActivityTopicList.class);
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });
            view.findViewById(R.id.near).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ActivityFindCicle.this, ActivitySearchUser.class);
                    intent.putExtra("type", StaticInApp.FINDPEOPLE_NEARBY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });



            //频道
            view.findViewById(R.id.channel).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ActivityFindCicle.this, ActivityChannel.class);
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });
            //找人
            view.findViewById(R.id.near).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityFindCicle.this,
                            ActivityFindPeople.class);
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });
            //微吧
            view.findViewById(R.id.weiba).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityFindCicle.this, ActivityWeiba.class);
                    //TODO test
                    intent.putExtra("unread", 0);
                    startActivity(intent);
                    popupWindow.dismiss();

                }
            });

            //风云榜、排行榜
            view.findViewById(R.id.fengyunbang).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityFindCicle.this, ActivityFindPeopleDetails.class);
                    intent.putExtra("type", StaticInApp.FINDPEOPLE_TOPLIST);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });

      /*      *//**
             * 附近的人
             *//*
            view.findViewById(R.id.nearpeople).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityFindCicle.this, HomePeopleActivity.class);
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });*/

            /**
             * 我的
             */
            view.findViewById(R.id.my).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityFindCicle.this, HomeMyActivity.class);
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });


            /**
             * 消息
             */
            view.findViewById(R.id.message).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityFindCicle.this, HomeMessageActivity.class);
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });


            popupWindow = new PopupWindow(view, Util.dip2px(getBaseContext(),170), Util.dip2px(getBaseContext(),8*44));
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //背景变暗
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        //监听如果popupWindown消失之后背景变亮
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow()
                        .getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        popupWindow.showAsDropDown(parent);
//        popupWindow.showAsDropDown(parent, xPos, 4);

    }

}
