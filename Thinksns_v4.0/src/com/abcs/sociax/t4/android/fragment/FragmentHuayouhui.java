package com.abcs.sociax.t4.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.abcs.hqbtravel.RollPager.Util;
import com.abcs.sociax.android.R;
import com.abcs.sociax.constant.AppConstant;
import com.abcs.sociax.t4.android.ActivityHome;
import com.abcs.sociax.t4.android.activity.HomeMessageActivity;
import com.abcs.sociax.t4.android.activity.HomeMyActivity;
import com.abcs.sociax.t4.android.channel.ActivityChannel;
import com.abcs.sociax.t4.android.checkin.ActivityCheckIn;
import com.abcs.sociax.t4.android.data.StaticInApp;
import com.abcs.sociax.t4.android.findpeople.ActivityFindPeople;
import com.abcs.sociax.t4.android.findpeople.ActivityFindPeopleDetails;
import com.abcs.sociax.t4.android.findpeople.ActivitySearchUser;
import com.abcs.sociax.t4.android.img.Bimp;
import com.abcs.sociax.t4.android.temp.SelectImageListener;
import com.abcs.sociax.t4.android.topic.ActivityTopicList;
import com.abcs.sociax.t4.android.video.MediaRecorderActivity;
import com.abcs.sociax.t4.android.weiba.ActivityWeiba;
import com.abcs.sociax.t4.android.weibo.ActivityCreateBase;
import com.abcs.sociax.t4.component.MoreWindow;
import com.abcs.sociax.t4.model.ModelUser;
import com.thinksns.sociax.thinksnsbase.utils.Anim;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zjz on 2016/11/3.
 */

public class FragmentHuayouhui extends FragmentSociax{

    ActivityHome activity;
    protected ModelUser user;        // 当前界面所属用户
    protected int uid;                // 当前界面所属uid

    private RelativeLayout rl_title;
    private Fragment fragment;
    private ImageButton ib_new;
    private MoreWindow mMoreWindow;
    protected SelectImageListener listener_selectImage;   //拍照工具
    protected FragmentManager fragmentManager ;
    protected FragmentTransaction fragmentTransaction ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity= (ActivityHome) getActivity();
        initFragment();
    }
    private void initFragment() {
        fragment = new FragmentHome();
        fragmentManager= activity.getSupportFragmentManager();
        fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ll_content, fragment);
        fragmentTransaction.commit();

        ib_new=(ImageButton)findViewById(R.id.ib_new);
        listener_selectImage = new SelectImageListener(activity);
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_huayouhui;
    }

    @Override
    public void initView() {
        rl_title= (RelativeLayout) findViewById(R.id.rl_title);
    }

    @Override
    public void initIntentData() {

    }

    @Override
    public void initListener() {
        ib_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMoreWindow == null) {
                    mMoreWindow = new MoreWindow(activity);
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
                                Intent intent = new Intent(activity, ActivityCheckIn.class);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        Anim.in(activity);
                    }
                });

                //长按事件
                ib_new.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Intent intent = new Intent(activity, ActivityCreateBase.class);
                        intent.putExtra("type", AppConstant.CREATE_TEXT_WEIBO);
                        startActivityForResult(intent, 100);
                        Anim.in(activity);
                        return false;
                    }
                });
            }
        });
    }

    private void selectPhoto() {
        Intent getImage = new Intent(activity, MultiImageSelectorActivity.class);
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
        Intent intentVideo = new Intent(activity, MediaRecorderActivity.class);
        startActivityForResult(intentVideo,AppConstant.CREATE_VIDEO_WEIBO);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    intent = new Intent(activity, ActivityCreateBase.class);
                    intent.putExtra("type", AppConstant.CREATE_ALBUM_WEIBO);
                    intent.putExtra("is_original", original);
                    startActivity(intent);
                    Anim.in(activity);
                    break;
                case StaticInApp.CAMERA_IMAGE:
                    if (Bimp.address.size() < 9) {
                        Bimp.address.add(listener_selectImage.getImagePath());
                    }
                    //跳转至发布微博页
                    intent = new Intent(activity, ActivityCreateBase.class);
                    intent.putExtra("type", AppConstant.CREATE_ALBUM_WEIBO);
                    intent.putExtra("is_original", false);
                    startActivity(intent);
                    Anim.in(activity);
                    break;
                case AppConstant.CREATE_VIDEO_WEIBO:
                    intent = new Intent(activity,ActivityCreateBase.class);
                    intent.putExtra("type", AppConstant.CREATE_VIDEO_WEIBO);
                    startActivity(intent);
                    Anim.in(activity);
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
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.item_popview, null);
            view.findViewById(R.id.topic).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ActivityTopicList.class);
                    startActivity(intent);
                }
            });
            view.findViewById(R.id.near).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ActivitySearchUser.class);
                    intent.putExtra("type", StaticInApp.FINDPEOPLE_NEARBY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });



            //频道
            view.findViewById(R.id.channel).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(activity, ActivityChannel.class);
                    startActivity(intent);
                }
            });
            //找人
            view.findViewById(R.id.near).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity,
                            ActivityFindPeople.class);
                    startActivity(intent);
                }
            });
            //微吧
            view.findViewById(R.id.weiba).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ActivityWeiba.class);
                    //TODO test
                    intent.putExtra("unread", 0);
                    startActivity(intent);

                }
            });

            //风云榜、排行榜
            view.findViewById(R.id.fengyunbang).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ActivityFindPeopleDetails.class);
                    intent.putExtra("type", StaticInApp.FINDPEOPLE_TOPLIST);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

          /*  *//**
             * 附近的人
             *//*
            view.findViewById(R.id.nearpeople).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, HomePeopleActivity.class);
                    startActivity(intent);
                }
            });*/

            /**
             * 我的
             */
            view.findViewById(R.id.my).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, HomeMyActivity.class);
                    startActivity(intent);
                }
            });


            /**
             * 消息
             */
            view.findViewById(R.id.message).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, HomeMessageActivity.class);
                    startActivity(intent);
                }
            });


            popupWindow = new PopupWindow(view, Util.dip2px(activity.getBaseContext(),120), Util.dip2px(activity.getBaseContext(),8*35));
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        int xPos = -popupWindow.getWidth() / 2
                + rl_title.getWidth() / 2;

        popupWindow.showAsDropDown(parent, xPos, 4);


    }
}
