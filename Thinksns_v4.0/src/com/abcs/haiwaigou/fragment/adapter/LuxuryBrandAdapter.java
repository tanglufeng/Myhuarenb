package com.abcs.haiwaigou.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.HotActivity;
import com.abcs.haiwaigou.activity.HotSpecialActivity;
import com.abcs.haiwaigou.activity.LinkActivity;
import com.abcs.haiwaigou.fragment.HWGLuxuryFragment;
import com.abcs.haiwaigou.fragment.viewholder.LuxuryBrandViewHolder;
import com.abcs.haiwaigou.model.Luxury;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/9/13.
 */
public class LuxuryBrandAdapter extends RecyclerView.Adapter<LuxuryBrandViewHolder> {
    ArrayList<Luxury> luxuries;
    Context context;
    private Handler handler = new Handler();

    public ArrayList<Luxury> getDatas() {
        return luxuries;
    }

    Activity activity;

    public LuxuryBrandAdapter(Activity activity, ArrayList<Luxury> luxuries) {
        this.activity = activity;
        this.luxuries = luxuries;
    }

    @Override
    public LuxuryBrandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_luxury_brand, parent, false);
        LuxuryBrandViewHolder hwgFragmentViewHolder = new LuxuryBrandViewHolder(view);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(LuxuryBrandViewHolder holder, final int position) {
        final Luxury luxury = luxuries.get(position);
        if (!TextUtils.isEmpty(luxury.getImgBean().getImg()))
            ImageLoader.getInstance().displayImage(luxury.getImgBean().getImg(), holder.img_title, Options.getHDOptions());

        if (luxury.getDatasBean() != null && luxury.getDatasBean().size() != 0) {
            holder.linear_small.removeAllViews();
            holder.linear_small.invalidate();

            for (int i = 0; i < luxury.getDatasBean().size(); i++) {
                if (luxury.getDatasBean().get(i).getSort().equals("1")) {
                    ImageLoader.getInstance().displayImage(luxury.getDatasBean().get(i).getImg(), holder.img_big, Options.getHDOptions());
                    final int finalI1 = i;
                    holder.img_big.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = null;
                            if (luxury.getDatasBean().get(finalI1).getFlag().equals(HWGLuxuryFragment.TYPE_SPECIAL)) {
                                intent = new Intent(activity, HotSpecialActivity.class);
                                intent.putExtra("words", "");
                                intent.putExtra("text_position", 5);
                                intent.putExtra("picture", luxury.getDatasBean().get(finalI1).getImg());
                                intent.putExtra("title", luxury.getDatasBean().get(finalI1).getTitle());
                                intent.putExtra("isMain", false);
                                intent.putExtra("special_id",luxury.getDatasBean().get(finalI1).getDesc());
                            } else if (luxury.getDatasBean().get(finalI1).getFlag().equals(HWGLuxuryFragment.TYPE_KEYWORD)) {
                                intent = new Intent(activity, HotActivity.class);
                                intent.putExtra("words", "");
                                intent.putExtra("keyword", luxury.getDatasBean().get(finalI1).getDesc());
                                intent.putExtra("title", luxury.getDatasBean().get(finalI1).getTitle());
                                intent.putExtra("text_position", 5);
                                intent.putExtra("isLuxury", true);
                                intent.putExtra("picture", luxury.getDatasBean().get(finalI1).getImg());
                            } else if (luxury.getDatasBean().get(finalI1).getFlag().equals(HWGLuxuryFragment.TYPE_LINK)) {
                                intent = new Intent(activity, LinkActivity.class);
                                intent.putExtra("keyword",luxury.getDatasBean().get(finalI1).getDesc());
                                intent.putExtra("title", luxury.getDatasBean().get(finalI1).getTitle());
                                String[] strings = luxury.getDatasBean().get(finalI1).getAdvert().split(",");
                                intent.putExtra("goods_id", strings[1]);
                                intent.putExtra("goods_img", strings[0]);
                            } else if (luxury.getDatasBean().get(finalI1).getFlag().equals(HWGLuxuryFragment.TYPE_GOODS)) {
                                intent = new Intent(activity, GoodsDetailActivity.class);
                                intent.putExtra("sid", luxury.getDatasBean().get(finalI1).getDesc());
                                intent.putExtra("pic",luxury.getDatasBean().get(finalI1).getImg());
                            }
                            activity.startActivity(intent);
                        }
                    });
                } else if (luxury.getDatasBean().get(i).getSort().equals("2")) {
                    View secondView = activity.getLayoutInflater().inflate(R.layout.hwg_item_luxury_img, null);
                    secondView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Util.dip2px(activity, 120), 1.0f));
                    final ImageView imageView = (ImageView) secondView.findViewById(R.id.img_item);
                    LinearLayout.LayoutParams otherParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Util.dip2px(activity, 120));
                    otherParams.setMargins(0, 0, 0, 0);
                    imageView.setLayoutParams(otherParams);
                    // imageView.setPadding(0,15,0,10);
                    ImageLoader.getInstance().displayImage(luxury.getDatasBean().get(i).getImg(), imageView, Options.getHDOptions());
                    final int finalI = i;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(luxury.getDatasBean().get(finalI).getFlag().equals("0")||luxury.getDatasBean().get(finalI).getFlag().equals("null")||luxury.getDatasBean().get(finalI).getFlag().equals("")){
                                Toast.makeText(activity,"敬请期待", Toast.LENGTH_SHORT).show();
                            }else {
                                Intent intent = null;
                                if (luxury.getDatasBean().get(finalI).getFlag().equals(HWGLuxuryFragment.TYPE_SPECIAL)) {
                                    intent = new Intent(activity, HotSpecialActivity.class);
                                    intent.putExtra("words", "");
                                    intent.putExtra("text_position", 5);
                                    intent.putExtra("picture", luxury.getDatasBean().get(finalI).getImg());
                                    intent.putExtra("title", luxury.getDatasBean().get(finalI).getContext());
                                    intent.putExtra("isMain", false);
                                    intent.putExtra("special_id", luxury.getDatasBean().get(finalI).getDesc());
                                } else if (luxury.getDatasBean().get(finalI).getFlag().equals(HWGLuxuryFragment.TYPE_KEYWORD)) {
                                    intent = new Intent(activity, HotActivity.class);
                                    intent.putExtra("words", "");
                                    intent.putExtra("keyword", luxury.getDatasBean().get(finalI).getDesc());
                                    intent.putExtra("title", luxury.getDatasBean().get(finalI).getContext());
                                    intent.putExtra("text_position", 5);
                                    intent.putExtra("isMain", false);
                                    intent.putExtra("picture", luxury.getDatasBean().get(finalI).getImg());
                                } else if (luxury.getDatasBean().get(finalI).getFlag().equals(HWGLuxuryFragment.TYPE_GOODS)) {
                                    intent = new Intent(activity, GoodsDetailActivity.class);
                                    intent.putExtra("sid", luxury.getDatasBean().get(finalI).getDesc());
                                    intent.putExtra("pic", luxury.getDatasBean().get(finalI).getImg());
                                }
                                activity.startActivity(intent);
                            }


                        }
                    });
                    //添加到容器
                    holder.linear_small.addView(secondView);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return luxuries.size();
    }
}
