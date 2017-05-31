package com.abcs.haiwaigou.utils;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.sociax.android.R;

/**
 * 布局模板：layout/content_recyclerview_temp.xml
 */
public class RecyclerViewAndSwipeRefreshLayout implements RecyclerView.OnTouchListener, SwipeRefreshLayout.OnRefreshListener {


    //region RecyclerView组件
    RecyclerView recyclerView;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
    //endregion

    //region SwipeRefreshLayout组件
    SwipeRefreshLayout swipeRefreshLayout;

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }
    //endregion

    //region 适配器
    RecyclerView.Adapter adapter;
    //endregion

    //region 上下文
    Context context;
    //endregion

    //region 当前加载RecyclerView组件的View
    View view;
    //endregion

    //region 构造

    int num;
    private ImageView mImgOverlay;
    private LinearLayout linear_sort;
    private LinearLayout linear_select;
    private RelativeLayout relative_tishi;
    boolean isSelect;

    float downY = 0;
    float scrollY = 0;
    public static final String GOODSNEWS="goodsnews";
    public static final String NOMAL="namal";
    private String customer_type="nomal";
    /**
     * 构造
     *
     * @param context                   上下文
     * @param view                      当前加载RecyclerView组件的View,在活动或者碎片上面可以使用getWindow().getDecorView()获取对象
     * @param adapter                   RecyclerView适配器
     * @param swipeRefreshLayoutRefresh 内部接口，处理SwipeRefreshLayout组件的刷新回调
     */
    public RecyclerViewAndSwipeRefreshLayout(final Context context, View view, final RecyclerView.Adapter adapter, SwipeRefreshLayoutRefresh swipeRefreshLayoutRefresh, final String type) {
        this.context = context;
        this.view = view;
        this.adapter = adapter;
        this.swipeRefreshLayoutRefresh = swipeRefreshLayoutRefresh;
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, context.getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeResources(R.color.tljr_statusbarcolor, android.R.color.holo_green_light, android.R.color.holo_blue_bright, android.R.color.holo_orange_light);
//        swipeRefreshLayout.setColorScheme(android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_blue_bright, android.R.color.holo_orange_light);
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        final FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(context);
        if (type.equals(MyString.TYPE1)) {
            relative_tishi = (RelativeLayout) view.findViewById(R.id.relative_tishi);

        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(fullyLinearLayoutManager);
        recyclerView.setAdapter(adapter);
//        switch (type){
//            case MyString.TYPE_ACOMMENT:
//                if(recyclerView.getScrollY()==0)
//                    GoodsDetailActivity.setIsVisible(GoodsDetailActivity.ALLCOMMENT);
//                else
//                    GoodsDetailActivity.setIsVisible(GoodsDetailActivity.COMMENT);
//                break;
//            case MyString.TYPE_GCOMMENT:
//                if(recyclerView.getScrollY()==0)
//                    GoodsDetailActivity.setIsVisible(GoodsDetailActivity.GOODCOMMENT);
//                else
//                    GoodsDetailActivity.setIsVisible(GoodsDetailActivity.COMMENT);
//                break;
//            case MyString.TYPE_MCOMMENT:
//                if(recyclerView.getScrollY()==0)
//                    GoodsDetailActivity.setIsVisible(GoodsDetailActivity.MIDDLECOMMENT);
//                else
//                    GoodsDetailActivity.setIsVisible(GoodsDetailActivity.COMMENT);
//                break;
//            case MyString.TYPE_BCOMMENT:
//                if(recyclerView.getScrollY()==0)
//                    GoodsDetailActivity.setIsVisible(GoodsDetailActivity.BADCOMMENT);
//                else
//                    GoodsDetailActivity.setIsVisible(GoodsDetailActivity.COMMENT);
//                break;
//            case MyString.TYPE_ICOMMENT:
//                if(recyclerView.getScrollY()==0)
//                    GoodsDetailActivity.setIsVisible(GoodsDetailActivity.IMGCOMMENT);
//                else
//                    GoodsDetailActivity.setIsVisible(GoodsDetailActivity.COMMENT);
//                break;
//        }
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = fullyLinearLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = fullyLinearLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                switch (type) {
                    case MyString.TYPE1:
                        Log.i("zjz","recycler_top="+recyclerView.getChildAt(0).getTop());
                        if (fullyLinearLayoutManager.findFirstVisibleItemPosition() == 0  ) {
                            relative_tishi.setVisibility(View.VISIBLE);
                            customer_type=NOMAL;
                        }
                        else {
//                            relative_tishi.setVisibility(View.GONE);
                            customer_type=GOODSNEWS;
                        }
                        break;
                    case MyString.TYPE_ACOMMENT:
//                        if (recyclerView.getChildAt(0).getTop() == 0 && dy < 0)
//                            GoodsDetailActivity.setIsVisible(GoodsDetailActivity.ALLCOMMENT);
//                        else
//                            GoodsDetailActivity.setIsVisible(GoodsDetailActivity.NORMAL);

//                        if (dy > 50)
//                            MyUpdateUI.sendUpdateCollection(context, MyUpdateUI.HIDECOMMENTTITLE);
//                        if (dy < -50)
//                            MyUpdateUI.sendUpdateCollection(context, MyUpdateUI.SHOWCOMMENTTITLE);
                        break;
                    case MyString.TYPE_GCOMMENT:
//                        if (recyclerView.getChildAt(0).getTop() == 0 && dy < 0)
//                            GoodsDetailActivity.setIsVisible(GoodsDetailActivity.GOODCOMMENT);
//                        else
//                            GoodsDetailActivity.setIsVisible(GoodsDetailActivity.NORMAL);

//                        if (dy > 50)
//                            MyUpdateUI.sendUpdateCollection(context, MyUpdateUI.HIDECOMMENTTITLE);
//                        if (dy < -50)
//                            MyUpdateUI.sendUpdateCollection(context, MyUpdateUI.SHOWCOMMENTTITLE);
                        break;
                    case MyString.TYPE_MCOMMENT:
//                        if (recyclerView.getChildAt(0).getTop() == 0 && dy < 0)
//                            GoodsDetailActivity.setIsVisible(GoodsDetailActivity.MIDDLECOMMENT);
//                        else
//                            GoodsDetailActivity.setIsVisible(GoodsDetailActivity.NORMAL);

//                        if (dy > 50)
//                            MyUpdateUI.sendUpdateCollection(context, MyUpdateUI.HIDECOMMENTTITLE);
//                        if (dy < 0)
//                            MyUpdateUI.sendUpdateCollection(context, MyUpdateUI.SHOWCOMMENTTITLE);
                        break;
                    case MyString.TYPE_BCOMMENT:
//                        if (recyclerView.getChildAt(0).getTop() == 0 && dy < 0)
//                            GoodsDetailActivity.setIsVisible(GoodsDetailActivity.BADCOMMENT);
//                        else
//                            GoodsDetailActivity.setIsVisible(GoodsDetailActivity.NORMAL);

//                        if (dy > 50)
//                            MyUpdateUI.sendUpdateCollection(context, MyUpdateUI.HIDECOMMENTTITLE);
//                        if (dy < -50)
//                            MyUpdateUI.sendUpdateCollection(context, MyUpdateUI.SHOWCOMMENTTITLE);
                        break;
                    case MyString.TYPE_ICOMMENT:
//                        if (recyclerView.getChildAt(0).getTop() == 0 && dy < 0)
//                            GoodsDetailActivity.setIsVisible(GoodsDetailActivity.IMGCOMMENT);
//                        else
//                            GoodsDetailActivity.setIsVisible(GoodsDetailActivity.NORMAL);

//                        if (dy > 50)
//                            MyUpdateUI.sendUpdateCollection(context, MyUpdateUI.HIDECOMMENTTITLE);
//                        if (dy < -50)
//                            MyUpdateUI.sendUpdateCollection(context, MyUpdateUI.SHOWCOMMENTTITLE);
                        break;
                }
//                if(type.equals(MyString.TYPE1)){
//                    if(fullyLinearLayoutManager.findFirstVisibleItemPosition()==0){
//                        relative_tishi.setVisibility(View.VISIBLE);
//                    }else {
//                        relative_tishi.setVisibility(View.GONE);
//                    }
//                }

            }
        });
        //添加分割线
//        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        //设置recyclerView的OnTouchListener
        recyclerView.setOnTouchListener(this);
    }

    public RecyclerViewAndSwipeRefreshLayout(Context context, View view, SwipeRefreshLayoutRefresh swipeRefreshLayoutRefresh) {
        this.swipeRefreshLayoutRefresh = swipeRefreshLayoutRefresh;
        this.context = context;
        this.view = view;
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, context.getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeResources(R.color.tljr_statusbarcolor);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public RecyclerViewAndSwipeRefreshLayout(Context context, View view, RecyclerView.Adapter adapter, SwipeRefreshLayoutRefresh swipeRefreshLayoutRefresh, boolean isItemDecoration) {
        this.context = context;
        this.view = view;
        this.adapter = adapter;
        this.swipeRefreshLayoutRefresh = swipeRefreshLayoutRefresh;
        mImgOverlay = (ImageView) view.findViewById(R.id.img_overlay);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, context.getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeResources(R.color.tljr_statusbarcolor);
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        final FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(context);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(fullyLinearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = fullyLinearLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = fullyLinearLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动

                if (dy > 0) {
                    mImgOverlay.setVisibility(View.VISIBLE);
                } else {
                    mImgOverlay.setVisibility(View.INVISIBLE);
                }
            }
        });
        //添加分割线
        if (isItemDecoration) {
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        }
        //设置recyclerView的OnTouchListener
        recyclerView.setOnTouchListener(this);

    }


    public RecyclerViewAndSwipeRefreshLayout(Context context, final View view, RecyclerView.Adapter adapter, SwipeRefreshLayoutRefresh swipeRefreshLayoutRefresh, int num) {
        this.num = num;
        this.context = context;
        this.view = view;
        this.adapter = adapter;
        this.swipeRefreshLayoutRefresh = swipeRefreshLayoutRefresh;
        mImgOverlay = (ImageView) view.findViewById(R.id.img_overlay);
        linear_sort = (LinearLayout) view.findViewById(R.id.linear_sort);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, context.getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeResources(R.color.tljr_statusbarcolor);
        swipeRefreshLayout.setOnRefreshListener(this);
        final FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(context);
//        final FullyGridLayoutManager fullyGridLayoutManager=new FullyGridLayoutManager(context,num);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(fullyLinearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = fullyLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                int totalItemCount = fullyLinearLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动

                if (lastVisibleItem == 0 || lastVisibleItem == 1) {
                    linear_sort.setVisibility(View.GONE);
                } else {
                    linear_sort.setVisibility(View.VISIBLE);
                }
                if (dy > 0) {
                    mImgOverlay.setVisibility(View.VISIBLE);
                } else {
                    mImgOverlay.setVisibility(View.INVISIBLE);
                }
            }
        });
        //设置recyclerView的OnTouchListener
        recyclerView.setOnTouchListener(this);

    }

    //endregion
    GridLayoutManager gridLayoutManager;

    public RecyclerViewAndSwipeRefreshLayout(Context context, final View view, RecyclerView.Adapter adapter, SwipeRefreshLayoutRefresh swipeRefreshLayoutRefresh, final GridLayoutManager gridLayoutManager, final boolean isAllBuy) {
        this.gridLayoutManager = gridLayoutManager;
        this.context = context;
        this.view = view;
        this.adapter = adapter;
        this.swipeRefreshLayoutRefresh = swipeRefreshLayoutRefresh;
        if (isAllBuy) {
            mImgOverlay = (ImageView) view.findViewById(R.id.img_overlay);
            linear_sort = (LinearLayout) view.findViewById(R.id.linear_sort);
        }
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, context.getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeResources(R.color.tljr_statusbarcolor);
        swipeRefreshLayout.setOnRefreshListener(this);
//        final FullyLinearLayoutManager fullyLinearLayoutManager=new FullyLinearLayoutManager(context);
//        final FullyGridLayoutManager fullyGridLayoutManager=new FullyGridLayoutManager(context,num);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                int totalItemCount = gridLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (isAllBuy) {
                    if (lastVisibleItem == 0 || lastVisibleItem == 1) {
                        linear_sort.setVisibility(View.INVISIBLE);
                    } else {
                        linear_sort.setVisibility(View.GONE);
                    }

                    if (dy > 0) {
                        mImgOverlay.setVisibility(View.VISIBLE);
                    } else {
                        mImgOverlay.setVisibility(View.INVISIBLE);
                    }
                }


            }
        });
        //设置recyclerView的OnTouchListener
        recyclerView.setOnTouchListener(this);
    }


    //region SwipeRefreshLayout的刷新回调接口
    //开出SwipeRefreshLayout的刷新接口
    SwipeRefreshLayoutRefresh swipeRefreshLayoutRefresh;

    public interface SwipeRefreshLayoutRefresh {
        void swipeRefreshLayoutOnRefresh();
    }
    //endregion

    //region SwipeRefreshLayout的onRefresh接口实现
    @Override
    public void onRefresh() {
        //由外界具体执行swipeRefreshLayout的刷新方案
        swipeRefreshLayoutRefresh.swipeRefreshLayoutOnRefresh();
    }
    //endregion

    //region RecyclerView的onTouch接口实现
    float startX = 0, startY = 0, offsetX, offsetY;
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        //当更新的时候，recyclerView不可滚动，可以有效的阻止bug
        if(customer_type.equals(GOODSNEWS)){

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    offsetX = event.getX() - startX;
                    offsetY = event.getY() - startY;
                    if (offsetX < -5) {
                        System.out.println("left");
                    } else if (offsetX > 5) {
                        System.out.println("right");
                    }
                    if (offsetY < -1) {
                        //up
                        relative_tishi.setVisibility(View.GONE);
                    }
                    else if (offsetY > 1) {
//                        relative_tishi.setVisibility(View.VISIBLE);
                        //down
                        System.out.println("down");
                    }
                    break;
            }
        }
        return swipeRefreshLayout.isRefreshing();
    }
    //endregion
}
