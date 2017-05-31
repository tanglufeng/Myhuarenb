package com.abcs.sociax.t4.android.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.design.widget.TabLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.Thinksns;
import com.abcs.sociax.t4.android.info.InformationPagerAdapter;
import com.abcs.sociax.t4.component.LazyViewPager;
import com.abcs.sociax.t4.component.SmallDialog;
import com.abcs.sociax.t4.model.ModelInformationCate;
import com.thinksns.sociax.thinksnsbase.activity.widget.EmptyLayout;
import com.thinksns.sociax.thinksnsbase.exception.ApiException;
import com.thinksns.sociax.thinksnsbase.network.ApiHttpClient;

import java.util.ArrayList;

/**
 * Created by Zoey on 2016-05-09.
 */
public class FragmentHomeInfo extends FragmentSociax {

    private TabLayout tb_information;
    private LazyViewPager vp_information;
    private SmallDialog dialog;
    private FrameLayout fl_tabs;
    private LinearLayout ll_top;
    private boolean isHideTitle = false;
    private RelativeLayout rl_title;
    AnimatorSet hideAnimatorSet;
    AnimatorSet backAnimatorSet;
    private EmptyLayout error_layout;

    private static FragmentHomeInfo instance;
    public static FragmentHomeInfo getInstance() {
        return instance;
    }

    @Override
    public int getLayoutId() {
      return  R.layout.activity_information;
    }

    @Override
    public void initView() {
        instance = this;

        tb_information = (TabLayout) this.findViewById(R.id.tb_information);
        tb_information.setTabMode(TabLayout.MODE_SCROLLABLE);
        vp_information = (LazyViewPager) this.findViewById(R.id.vp_information);
        vp_information.setOffscreenPageLimit(0);

        error_layout = (EmptyLayout)findViewById(R.id.error_layout);
        error_layout.setNoDataContent(getResources().getString(R.string.empty_content));

        dialog = new SmallDialog(getActivity(), getString(R.string.please_wait));

        fl_tabs=(FrameLayout)this.findViewById(R.id.fl_tabs);
        ll_top=(LinearLayout)this.findViewById(R.id.ll_top);

        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        rl_title.post(new Runnable() {
            @Override
            public void run() {
                moveViewPagerDown();
            }
        });

        initTab();//初始化tab
    }

    @Override
    public void initIntentData() {

    }

    //初始化TabLayout的设置
    public void initTab(){
        tb_information.setTabMode(TabLayout.MODE_SCROLLABLE);
        vp_information.setOffscreenPageLimit(0);
    }

    public void initListener(){
        vp_information.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tb_information));
        tb_information.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp_information.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void initData(){
        if (!dialog.isShowing()){
            dialog.setContent("请稍后...");
            dialog.show();
        }
        getCate();
    }

    /**
     * 获取分类
     */
    public void getCate() {
        try {
            Thinksns.getApplication().getInformationApi().getCate(new ApiHttpClient.HttpResponseListener() {
                @Override
                public void onSuccess(Object result) {
                    dialog.dismiss();
                    ArrayList<ModelInformationCate> cateList = (ArrayList<ModelInformationCate>) result;
                    for (int i = 0; i < cateList.size(); i++) {
                        tb_information.addTab(tb_information.newTab().setText(cateList.get(i).getName()));
                    }
                    setPager(cateList);
                    error_layout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                }

                @Override
                public void onError(Object result) {
                    dialog.dismiss();
                    error_layout.setErrorType(EmptyLayout.NETWORK_LOADING);
                    Toast.makeText(getActivity().getApplicationContext(),result.toString(),Toast.LENGTH_SHORT).show();
                }
            });
        } catch (ApiException e) {
            dialog.dismiss();
            error_layout.setErrorType(EmptyLayout.NETWORK_LOADING);
            e.printStackTrace();
        }
    }

    public void setPager(ArrayList<ModelInformationCate> tables) {
        InformationPagerAdapter adapter = new InformationPagerAdapter(getActivity().getSupportFragmentManager(), tables);
        vp_information.setAdapter(adapter);
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

                headerAnimator.addListener(new Animator.AnimatorListener() {

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
            ObjectAnimator viewPagerAnimator = ObjectAnimator.ofFloat(vp_information, "y", vp_information.getY(),
                    fl_tabs.getHeight());
            viewPagerAnimator.setDuration(100).start();
            hideAnimatorSet.setDuration(300);
            hideAnimatorSet.playTogether(animators);
            hideAnimatorSet.start();
        }
    }

    public void moveViewPagerDown() {
        ObjectAnimator.ofFloat(vp_information, "y", vp_information.getY(), fl_tabs.getY() + fl_tabs.getHeight())
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
            headerAnimator.addListener(new Animator.AnimatorListener() {

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
            ObjectAnimator viewPagerAnimator = ObjectAnimator.ofFloat(vp_information, "y", vp_information.getY(),
                    fl_tabs.getY() + fl_tabs.getHeight());
            animators.add(headerAnimator);
            viewPagerAnimator.setDuration(300).start();
            backAnimatorSet.setDuration(300);
            backAnimatorSet.playTogether(animators);
            backAnimatorSet.start();

        }
    }
}
