package com.abcs.haiwaigou.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.fragment.viewholder.DuDaoViewHolder;
import com.abcs.haiwaigou.model.DuDao;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.ActivityHome;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by zjz on 2016/9/13.
 */
public class DuDaoAdapter extends RecyclerView.Adapter<DuDaoViewHolder> {
    List<DuDao.DatasBean> luxuries;
    Context context;
    private Handler handler = new Handler();

    public List<DuDao.DatasBean> getDatas() {
        return luxuries;
    }

    ActivityHome activity;

    public DuDaoAdapter(ActivityHome activity, List<DuDao.DatasBean> luxuries) {
        this.activity = activity;
        this.luxuries = luxuries;
    }

    @Override
    public DuDaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_dudao, parent, false);
        DuDaoViewHolder hwgFragmentViewHolder = new DuDaoViewHolder(view);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(DuDaoViewHolder holder, final int position) {
        final DuDao.DatasBean bean = luxuries.get(position);
        if (!TextUtils.isEmpty(bean.img)) {
            ImageLoader.getInstance().displayImage(bean.img, holder.imge_dudao, Options.getHDOptions());
        }

        if(!TextUtils.isEmpty(bean.description)){
            holder.tv_instrution.setText(bean.description);
        }


        if (!TextUtils.isEmpty(bean.flag)&&bean.flag.equals("1")) {

            holder.imge_dudao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(activity, GoodsDetailActivity.class);
                    intent.putExtra("sid", bean.descs);
                    intent.putExtra("pic",bean.img);
                    activity.startActivity(intent);

//                    if (!TextUtils.isEmpty(bean.flag)&&bean.flag.equals(HWGDuDaoFragment.TYPE_SPECIAL)) {
//                        intent = new Intent(activity, .class);
//                        intent.putExtra("words", "");
//                        intent.putExtra("text_position", 5);
//                        intent.putExtra("picture", DuDao.getDatasBean().get(finalI1).getImg());
//                        intent.putExtra("title", DuDao.getDatasBean().get(finalI1).getTitle());
//                        intent.putExtra("isMain", false);
//                        intent.putExtra("special_id",DuDao.getDatasBean().get(finalI1).getDesc());
//                    } else if (!TextUtils.isEmpty(bean.flag)&&bean.flag.equals(HWGDuDaoFragment.TYPE_KEYWORD)) {
//                        intent = new Intent(activity, HotActivity.class);
//                        intent.putExtra("words", "");
//                        intent.putExtra("keyword", DuDao.getDatasBean().get(finalI1).getDesc());
//                        intent.putExtra("title", DuDao.getDatasBean().get(finalI1).getTitle());
//                        intent.putExtra("text_position", 5);
//                        intent.putExtra("isDuDao", true);
//                        intent.putExtra("picture", DuDao.getDatasBean().get(finalI1).getImg());
//                    } else if (!TextUtils.isEmpty(bean.flag)&&bean.flag.equals(HWGDuDaoFragment.TYPE_LINK)) {
//                        intent = new Intent(activity, LinkActivity.class);
//                        intent.putExtra("keyword",bean.descs);
//                        intent.putExtra("title", bean.descs);
//                        String[] strings = DuDao.getDatasBean().get(finalI1).getAdvert().split(",");
//                        intent.putExtra("goods_id", strings[1]);
//                        intent.putExtra("goods_img", strings[0]);
//                    } else if (!TextUtils.isEmpty(bean.flag)&&bean.flag.equals(HWGDuDaoFragment.TYPE_GOODS)) {
//                        intent = new Intent(activity, GoodsDetailActivity.class);
//                        intent.putExtra("sid", bean.id+"");
//                        intent.putExtra("pic",bean.img);
////                    }
//                    activity.startActivity(intent);
                }
            });
        }
//        else if (!TextUtils.isEmpty(bean.sort)&&bean.sort.equals("2")) {

//            View secondView = activity.getLayoutInflater().inflate(R.layout.hwg_item_DuDao_img, null);
//            secondView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Util.dip2px(activity, 120), 1.0f));
//            final ImageView imageView = (ImageView) secondView.findViewById(R.id.img_item);
//            LinearLayout.LayoutParams otherParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Util.dip2px(activity, 120));
//            otherParams.setMargins(0, 0, 0, 0);
//            imageView.setLayoutParams(otherParams);
//            // imageView.setPadding(0,15,0,10);
//            ImageLoader.getInstance().displayImage(DuDao.getDatasBean().get(i).getImg(), imageView, Options.getHDOptions());
//            final int finalI = i;
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(DuDao.getDatasBean().get(finalI).getFlag().equals("0")||DuDao.getDatasBean().get(finalI).getFlag().equals("null")||DuDao.getDatasBean().get(finalI).getFlag().equals("")){
//                        Toast.makeText(activity,"敬请期待", Toast.LENGTH_SHORT).show();
//                    }else {
//                        Intent intent = null;
//                        if (DuDao.getDatasBean().get(finalI).getFlag().equals(HWGDuDaoFragment.TYPE_SPECIAL)) {
//                            intent = new Intent(activity, .class);
//                            intent.putExtra("words", "");
//                            intent.putExtra("text_position", 5);
//                            intent.putExtra("picture", DuDao.getDatasBean().get(finalI).getImg());
//                            intent.putExtra("title", DuDao.getDatasBean().get(finalI).getContext());
//                            intent.putExtra("isMain", false);
//                            intent.putExtra("special_id", DuDao.getDatasBean().get(finalI).getDesc());
//                        } else if (DuDao.getDatasBean().get(finalI).getFlag().equals(HWGDuDaoFragment.TYPE_KEYWORD)) {
//                            intent = new Intent(activity, HotActivity.class);
//                            intent.putExtra("words", "");
//                            intent.putExtra("keyword", DuDao.getDatasBean().get(finalI).getDesc());
//                            intent.putExtra("title", DuDao.getDatasBean().get(finalI).getContext());
//                            intent.putExtra("text_position", 5);
//                            intent.putExtra("isMain", false);
//                            intent.putExtra("picture", DuDao.getDatasBean().get(finalI).getImg());
//                        } else if (DuDao.getDatasBean().get(finalI).getFlag().equals(HWGDuDaoFragment.TYPE_GOODS)) {
//                            intent = new Intent(activity, GoodsDetailActivity.class);
//                            intent.putExtra("sid", DuDao.getDatasBean().get(finalI).getDesc());
//                            intent.putExtra("pic", DuDao.getDatasBean().get(finalI).getImg());
//                        }
//                        activity.startActivity(intent);
//                    }
//
//
//                }
//            });
//            //添加到容器
//            holder.linear_small.addView(secondView);
//        }

    }

    @Override
    public int getItemCount() {
        return luxuries.size();
    }
}
