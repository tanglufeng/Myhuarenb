package com.abcs.sociax.t4.android.fragment;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.abcs.sociax.constant.AppConstant;
import com.abcs.sociax.t4.adapter.AdapterTabsPage;
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
import com.abcs.sociax.t4.android.interfaces.OnTabListener;
import com.abcs.sociax.t4.android.temp.SelectImageListener;
import com.abcs.sociax.t4.android.topic.ActivityTopicList;
import com.abcs.sociax.t4.android.video.MediaRecorderActivity;
import com.abcs.sociax.t4.android.weiba.ActivityWeiba;
import com.abcs.sociax.t4.android.weibo.ActivityCreateBase;
import com.abcs.sociax.t4.component.MoreWindow;
import com.abcs.sociax.t4.model.ModelUser;
import com.astuetz.PagerSlidingTabStrip;
import com.thinksns.sociax.thinksnsbase.utils.Anim;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static android.app.Activity.RESULT_OK;

/**
 * 主页,用户显示用户相关的微博
 */
public class FragmentHome extends FragmentSociax {
    private ViewPager viewPager_Home;            // 首页viewpager
    private PagerSlidingTabStrip tabs;
    private LinearLayout tabsContainer;
    private RelativeLayout ll_tabs;

    private AdapterTabsPage tabsAdapter;

    private RelativeLayout rl_title;
    private LinearLayout ll_top;

    private ImageView iv_create;

    private boolean isHideTitle = false;

    AnimatorSet hideAnimatorSet;
    AnimatorSet backAnimatorSet;
    private static FragmentHome instance;

    //from circle
    protected ModelUser user;        // 当前界面所属用户
    protected int uid;                // 当前界面所属uid

    private ImageButton ib_new;
    private MoreWindow mMoreWindow;
    protected SelectImageListener listener_selectImage;   //拍照工具
    //from circle

    ActivityHome activity;
    View view;

    public static FragmentHome getInstance() {
        if (instance == null) {
            instance = new FragmentHome();
        }
        return instance;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home,container,false);
        activity = (ActivityHome) getActivity();
        initMyViews(view);
        initMyListenr();
        initMyDatas();
       /* ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);*/

        return view;
    }

    private void initMyDatas(){

        initFragments();
        //默认选中第一个选项
        switchTabColor(0);
    }
    private void initMyListenr(){
        iv_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityCreateBase.class);
                intent.putExtra("type", AppConstant.CREATE_TEXT_WEIBO);
                startActivityForResult(intent, 100);
            }
        });

        ib_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mMoreWindow == null) {
                    mMoreWindow = new MoreWindow(getActivity());
                }
                mMoreWindow.showMoreWindow(view);
                mMoreWindow.setOnItemClick(new MoreWindow.IMoreWindowListener() {

                    @Override
                    public void OnItemClick(View v) {
                        switch (v.getId()) {
                            case R.id.tv_create_weibo_camera:
                                //拍摄图片
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
                                Intent intent = new Intent(getActivity(), ActivityCheckIn.class);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        Anim.in(getActivity());
                    }
                });

                //长按事件
                ib_new.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Intent intent = new Intent(getActivity(), ActivityCreateBase.class);
                        intent.putExtra("type", AppConstant.CREATE_TEXT_WEIBO);
                        startActivityForResult(intent, 100);
                        Anim.in(getActivity());
                        return false;
                    }
                });
            }
        });
    }

    private static final String TAG = "FragmentHome";

    private void initMyViews(View view){
        Log.i(TAG, "initMyViews: ");
        instance = this;
        ll_top = (LinearLayout) view.findViewById(R.id.ll_top);
        viewPager_Home = (ViewPager) view.findViewById(R.id.vp_home);
        tabs = (PagerSlidingTabStrip)view.findViewById(R.id.tabs);
        tabs.setTypeface(null, Typeface.NORMAL);
        tabs.setTabBackground(0);

        ll_tabs = (RelativeLayout)view.findViewById(R.id.ll_tabs);
        rl_title = (RelativeLayout) view.findViewById(R.id.rl_title);
        rl_title.post(new Runnable() {
            @Override
            public void run() {
//                moveViewPagerDown();
            }
        });

        iv_create = (ImageView)view.findViewById(R.id.iv_create);

        //from circle
        ib_new=(ImageButton)view.findViewById(R.id.ib_new);

        toggleCreateBtn(true); // 显示发布按钮

        Animation anim = new TranslateAnimation(100, 0, 0, 0);
        anim .setDuration(70); //这个值越大动画就越卡，越容易花屏
        anim .setFillAfter(true);
        ib_new.setAnimation(anim);

        listener_selectImage = new SelectImageListener(getActivity());

        view.findViewById(R.id.img_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initIntentData() {
    }

    @Override
    public void initListener() {
    }

    private void selectPhoto() {
        Intent getImage = new Intent(getActivity(), MultiImageSelectorActivity.class);
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
        Intent intentVideo = new Intent(getActivity(), MediaRecorderActivity.class);
        startActivityForResult(intentVideo,AppConstant.CREATE_VIDEO_WEIBO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 刷新首页数据
        if (resultCode == RESULT_OK) {
            Intent intent=null;
            switch (requestCode) {
                case StaticInApp.LOCAL_IMAGE:  // 来自本地相册
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
                    intent = new Intent(getActivity(), ActivityCreateBase.class);
                    intent.putExtra("type", AppConstant.CREATE_ALBUM_WEIBO);
                    intent.putExtra("is_original", original);
                    startActivity(intent);
                    Anim.in(getActivity());
                    break;
                case StaticInApp.CAMERA_IMAGE:  // 拍照
                    String path = listener_selectImage.getImagePath();
                    if (path != null&& Bimp.address.size() < 9) {
                        Bimp.address.add(path);
                    }

                  /*  if (Bimp.address.size() < 9) {
                        Bimp.address.add(listener_selectImage.getImagePath());
                    }*/
                    //跳转至发布微博页
                    intent = new Intent(getActivity(), ActivityCreateBase.class);
                    intent.putExtra("type", AppConstant.CREATE_ALBUM_WEIBO);
                    intent.putExtra("is_original", false);
                    startActivity(intent);
                    Anim.in(getActivity());
                    break;
                case AppConstant.CREATE_VIDEO_WEIBO:
                    intent = new Intent(getActivity(),ActivityCreateBase.class);
                    intent.putExtra("type", AppConstant.CREATE_VIDEO_WEIBO);
                    startActivity(intent);
                    Anim.in(getActivity());
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
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.item_popview, null);
            view.findViewById(R.id.topic).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ActivityTopicList.class);
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });
            view.findViewById(R.id.near).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ActivitySearchUser.class);
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

                    Intent intent = new Intent(getActivity(), ActivityChannel.class);
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });
            //找人
            view.findViewById(R.id.near).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),
                            ActivityFindPeople.class);
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });
            //微吧
            view.findViewById(R.id.weiba).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ActivityWeiba.class);
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
                    Intent intent = new Intent(getActivity(), ActivityFindPeopleDetails.class);
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
                    Intent intent = new Intent(getActivity(), HomePeopleActivity.class);
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
                    Intent intent = new Intent(getActivity(), HomeMyActivity.class);
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
                    Intent intent = new Intent(getActivity(), HomeMessageActivity.class);
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });


            popupWindow = new PopupWindow(view, Util.dip2px(getActivity(),170), Util.dip2px(getActivity(),8*44));
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //背景变暗
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.5f;
        getActivity().getWindow().setAttributes(params);

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
                WindowManager.LayoutParams params = getActivity().getWindow()
                        .getAttributes();
                params.alpha = 1f;
                getActivity().getWindow().setAttributes(params);
            }
        });
        popupWindow.showAsDropDown(parent);
//        popupWindow.showAsDropDown(parent, xPos, 4);

    }
    @Override
    public void initData() {
    }

    /**
     * 初始化Fragments
     */
    private void initFragments() throws NullPointerException{
        // 初始化适配器
        tabsAdapter = new AdapterTabsPage(getChildFragmentManager());
        tabsAdapter.addTab("全部", new FragmentWeiboListViewAll())
                .addTab("关注", new FragmentWeiboListViewFriends())
                .addTab("频道", new FragmentWeiboListViewChannel())
                .addTab("推荐", new FragmentWeiboListViewRecommend());
        viewPager_Home.setAdapter(tabsAdapter);
        tabs.setViewPager(viewPager_Home);
        tabsContainer = (LinearLayout)tabs.getChildAt(0);
        tabs.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((OnTabListener)tabsAdapter.getItem(position)).onTabClickListener();
                //设置选中样式
                switchTabColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void switchTabColor(int position) {
        int count = tabsContainer.getChildCount();
        for(int i=0; i<count; i++) {
            TextView selectView = (TextView)tabsContainer.getChildAt(i);
            if(position == i) {
                //设置选中颜色，背景
                selectView.setTextColor(getResources().getColor(R.color.blue));
            }
            else {
                selectView.setTextColor(getResources().getColor(R.color.black));
            }
        }

    }


    //隐藏标题栏
    public void animatorHide() {
        if (!isHideTitle && rl_title.getHeight() > 0) {
            isHideTitle = true;
            if (backAnimatorSet != null)
                backAnimatorSet.cancel();
            hideAnimatorSet = new AnimatorSet();
            ArrayList<Animator> animators = new ArrayList<Animator>();
            if (rl_title != null) {
                ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(ll_top, "translationY",
                        ll_top.getTranslationY(), -rl_title.getHeight());

                headerAnimator.addListener(new AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator arg0) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator arg0) {

                    }

                    @Override
                    public void onAnimationEnd(Animator arg0) {
                        isHideTitle = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator arg0) {

                    }
                });

                animators.add(headerAnimator);
            }
            ObjectAnimator viewPagerAnimator = ObjectAnimator.ofFloat(viewPager_Home, "y", viewPager_Home.getY(),
                    ll_tabs.getHeight());
            viewPagerAnimator.setDuration(100).start();
            hideAnimatorSet.setDuration(300);
            hideAnimatorSet.playTogether(animators);
            hideAnimatorSet.start();
        }
    }

    public void moveViewPagerDown() {
        ObjectAnimator.ofFloat(viewPager_Home, "y", viewPager_Home.getY(), ll_tabs.getY() + ll_tabs.getHeight())
                .setDuration(300).start();
    }


    /**
     * @param forced 强制显示标题栏
     */
    public void animatorShow(boolean forced) {
        if (!isHideTitle || forced) {
            isHideTitle = true;
            if (hideAnimatorSet != null) {
                hideAnimatorSet.cancel();
            }
            backAnimatorSet = new AnimatorSet();
            ArrayList<Animator> animators = new ArrayList<Animator>();
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(ll_top, "translationY",
                    ll_top.getTranslationY(), 0);
            headerAnimator.addListener(new AnimatorListener() {

                @Override
                public void onAnimationStart(Animator arg0) {
                }

                @Override
                public void onAnimationRepeat(Animator arg0) {

                }

                @Override
                public void onAnimationEnd(Animator arg0) {
                    isHideTitle = false;
                }

                @Override
                public void onAnimationCancel(Animator arg0) {
                }
            });
            ObjectAnimator viewPagerAnimator = ObjectAnimator.ofFloat(viewPager_Home, "y", viewPager_Home.getY(),
                    ll_tabs.getY() + ll_tabs.getHeight());
            animators.add(headerAnimator);
            viewPagerAnimator.setDuration(300).start();
            backAnimatorSet.setDuration(300);
            backAnimatorSet.playTogether(animators);
            backAnimatorSet.start();

        }
    }
}
