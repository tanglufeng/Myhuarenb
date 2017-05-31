package com.abcs.sociax.t4.android.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.adapter.AdapterViewPager;
import com.abcs.sociax.t4.android.activity.HomePeopleActivity;
import com.abcs.sociax.t4.android.findpeople.ActivityFindPeople;
import com.abcs.sociax.t4.unit.TabUtils;

import java.util.ArrayList;

/**
 * Created by hedong on 16/5/19.
 * 附件的人首页
 */
public class FragmentHomePeople extends FragmentSociax {
    private ViewPager viewPager;
    private AdapterViewPager adapter;

    private TabUtils mTabUtils;
    private RelativeLayout rl_title;
    private RelativeLayout rl_tabs;
    private RadioGroup rg_title;
    private LinearLayout ll_top;
    private ImageView iv_search;

    private boolean isHideTitle = false;
    private AnimatorSet hideAnimatorSet;
    private AnimatorSet backAnimatorSet;

    //zjz
    private RelativeLayout relativeBack;
    HomePeopleActivity activity;

    private static FragmentHomePeople instance;

    public static FragmentHomePeople getInstance() {
        return instance;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_people_home;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        activity= (HomePeopleActivity) getActivity();
    }

    @Override
    public void initView() {
        rg_title = (RadioGroup)findViewById(R.id.rg_title);
        rl_title = (RelativeLayout)findViewById(R.id.rl_title);
        // 首页
        viewPager = (ViewPager) findViewById(R.id.vp_home);
        adapter = new AdapterViewPager(getChildFragmentManager());
        ll_top = (LinearLayout)findViewById(R.id.ll_top);
        rl_tabs = (RelativeLayout)findViewById(R.id.rl_tabs);
        iv_search = (ImageView)findViewById(R.id.iv_search);
        relativeBack=(RelativeLayout)findViewById(R.id.relative_back);
    }

    /**
     * 初始化Fragment集合
     */
    private void initFragments() {
        mTabUtils = new TabUtils();
        mTabUtils.addFragments(new FragmentFindPeopleNearBy(),		//我加入的微吧
                FragmentFindPeopleNearBy.newInstance(true));
        mTabUtils.addButtons(rg_title);
        mTabUtils.setButtonOnClickListener(tabOnClickListener);

        adapter.bindData(mTabUtils.getFragments());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {
                viewPager.setCurrentItem(index); // 默认加载第一个Fragment
                mTabUtils.setDefaultUI(getActivity(), index);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }
        });
    }

    private final View.OnClickListener tabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem((Integer) v.getTag());
        }
    };

    @Override
    public void initIntentData() {

    }

    @Override
    public void initListener() {
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),
                        ActivityFindPeople.class);
                startActivity(intent);
            }
        });
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    @Override
    public void initData() {
        initFragments();
        rl_title.post(new Runnable() {
            @Override
            public void run() {
                moveViewPagerDown(viewPager, rl_tabs);
            }
        });
    }

    /**
     * 将View移动到另一个View的下方
     * @param target 要移动的View
     * @param source 指定View的下方
     */
    private void moveViewPagerDown(ViewGroup target, ViewGroup source) {
        ObjectAnimator.ofFloat(target, "y", target.getY(), source.getY() + source.getHeight())
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

            ObjectAnimator viewPagerAnimator = ObjectAnimator.ofFloat(viewPager, "y", viewPager.getY(),
                    rl_tabs.getY() + rl_tabs.getHeight());
            animators.add(headerAnimator);
            viewPagerAnimator.setDuration(300).start();
            backAnimatorSet.setDuration(300);
            backAnimatorSet.playTogether(animators);
            backAnimatorSet.start();

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
            ObjectAnimator viewPagerAnimator = ObjectAnimator.ofFloat(viewPager, "y", viewPager.getY(),
                    rl_tabs.getHeight());
            viewPagerAnimator.setDuration(100).start();
            hideAnimatorSet.setDuration(300);
            hideAnimatorSet.playTogether(animators);
            hideAnimatorSet.start();
        }
    }

}
