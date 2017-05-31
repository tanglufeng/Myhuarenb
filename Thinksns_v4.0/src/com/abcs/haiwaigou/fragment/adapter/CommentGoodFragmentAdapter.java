package com.abcs.haiwaigou.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.fragment.viewholder.CommentItemFragmentViewHolder;
import com.abcs.haiwaigou.model.Comment;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.tljr.news.ShowWebImageActivity;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/16.
 */
public class CommentGoodFragmentAdapter extends RecyclerView.Adapter<CommentItemFragmentViewHolder> {
    public Handler handler = new Handler();

    Context context;
    private SortedList<Comment> mSortedList;

    CommentItemFragmentViewHolder.ItemOnClick itemOnClick;

    Activity activity;

    public CommentGoodFragmentAdapter(Activity activity, CommentItemFragmentViewHolder.ItemOnClick itemOnClick) {
        this.itemOnClick = itemOnClick;
        this.activity = activity;
        mSortedList = new SortedList<>(Comment.class, new SortedList.Callback<Comment>() {

            /**
             * 返回一个负整数（第一个参数小于第二个）、零（相等）或正整数（第一个参数大于第二个）
             */
            @Override
            public int compare(Comment o1, Comment o2) {

                if (o1.getId() < o2.getId()) {
                    return -1;
                } else if (o1.getId() > o2.getId()) {
                    return 1;
                }

                return 0;
            }

            @Override
            public boolean areContentsTheSame(Comment oldItem, Comment newItem) {
                return oldItem.getGeval_goodsname().equals(newItem.getGeval_goodsname());
            }

            @Override
            public boolean areItemsTheSame(Comment item1, Comment item2) {
                return item1.getId() == item2.getId();
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }
        });
    }


    public SortedList<Comment> getList() {
        return mSortedList;
    }

    public void addItems(ArrayList<Comment> list) {
        mSortedList.beginBatchedUpdates();

        for (Comment itemModel : list) {
            mSortedList.add(itemModel);
        }

        mSortedList.endBatchedUpdates();
    }

    public void deleteItems(ArrayList<Comment> items) {
        mSortedList.beginBatchedUpdates();
        for (Comment item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    @Override
    public CommentItemFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_goods_comment, parent, false);
        CommentItemFragmentViewHolder hwgFragmentViewHolder = new CommentItemFragmentViewHolder(view, itemOnClick);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    //    String[] strings;
    @Override
    public void onBindViewHolder(CommentItemFragmentViewHolder holder, final int position) {
        final Comment item = mSortedList.get(position);

        LoadPicture loadPicture = new LoadPicture();
        Log.i("zjz", "url=" + TLUrl.getInstance().URL_hwg_comment_goods + item.getGeval_storeid() + "/" + item.getGeval_goodsimage());
        String userhead=TLUrl.getInstance().URL_hwg_base+"/data/upload/shop/avatar/avatar_";
//        loadPicture.initPicture(holder.img_user, userhead+ item.getGeval_frommemberid()+".jpg");
        ImageLoader.getInstance().displayImage( userhead+ item.getGeval_frommemberid()+".jpg",holder.img_user, Options.getUserHeadOptions());
        holder.t_time.setText(Util.format.format(item.getGeval_addtime() * 1000) + "");
        if(item.isGeval_isanonymous()){
            char[] chars=item.getGeval_frommembername().toCharArray();
            char first=chars[0];
            char last=chars[chars.length-1];
            holder.t_user_name.setText(first+"***"+last);
        }else {
            holder.t_user_name.setText(item.getGeval_frommembername());
        }
//        holder.t_user_name.setText(item.getGeval_frommembername());
        holder.ratingBar.setRating(item.getGeval_scores());
        holder.t_comment_info.setText(item.getGeval_content());
        if (item.getGeval_image().equals("null")) {
            holder.linear_share.setVisibility(View.GONE);
        } else {
            final ArrayList<String> uList = new ArrayList<String>();
            uList.clear();
            final String[] strings = item.getGeval_image().split(",");
            switch (strings.length) {
                case 1:
                    uList.add(TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[0]);
                    loadPicture.initPicture(holder.img_1, TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[0]);
                    holder.relative_pic1.setVisibility(View.VISIBLE);
                    holder.relative_pic1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ShowWebImageActivity.class);
                            intent.putExtra("image", TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[0]);
                            intent.putExtra("ulist", uList);
                            activity.startActivity(intent);
                        }
                    });
                    holder.relative_pic2.setVisibility(View.INVISIBLE);
                    holder.relative_pic3.setVisibility(View.INVISIBLE);
                    holder.relative_pic4.setVisibility(View.INVISIBLE);
                    holder.relative_pic5.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    uList.add(TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[0]);
                    uList.add(TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[1]);
                    loadPicture.initPicture(holder.img_1, TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[0]);
                    holder.relative_pic1.setVisibility(View.VISIBLE);
                    holder.relative_pic1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ShowWebImageActivity.class);
                            intent.putExtra("image", TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[0]);
                            intent.putExtra("ulist", uList);
                            activity.startActivity(intent);
                        }
                    });
                    loadPicture.initPicture(holder.img_2, TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[1]);
                    holder.relative_pic2.setVisibility(View.VISIBLE);
                    holder.relative_pic2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ShowWebImageActivity.class);
                            intent.putExtra("image", TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[1]);
                            intent.putExtra("ulist", uList);
                            activity.startActivity(intent);
                        }
                    });
                    holder.relative_pic3.setVisibility(View.INVISIBLE);
                    holder.relative_pic4.setVisibility(View.INVISIBLE);
                    holder.relative_pic5.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    uList.add(TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[0]);
                    uList.add(TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[1]);
                    uList.add(TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[2]);
                    loadPicture.initPicture(holder.img_1, TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[0]);
                    holder.relative_pic1.setVisibility(View.VISIBLE);
                    holder.relative_pic1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ShowWebImageActivity.class);
                            intent.putExtra("image", TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[0]);
                            intent.putExtra("ulist", uList);
                            activity.startActivity(intent);
                        }
                    });
                    loadPicture.initPicture(holder.img_2, TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[1]);
                    holder.relative_pic2.setVisibility(View.VISIBLE);
                    holder.relative_pic2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ShowWebImageActivity.class);
                            intent.putExtra("image", TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[1]);
                            intent.putExtra("ulist", uList);
                            activity.startActivity(intent);
                        }
                    });
                    loadPicture.initPicture(holder.img_3, TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[2]);
                    holder.relative_pic3.setVisibility(View.VISIBLE);
                    holder.relative_pic3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ShowWebImageActivity.class);
                            intent.putExtra("image", TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[2]);
                            intent.putExtra("ulist", uList);
                            activity.startActivity(intent);
                        }
                    });
                    holder.relative_pic4.setVisibility(View.INVISIBLE);
                    holder.relative_pic5.setVisibility(View.INVISIBLE);
                    break;
                case 4:
                    uList.add(TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[0]);
                    uList.add(TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[1]);
                    uList.add(TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[2]);
                    uList.add(TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[3]);
                    loadPicture.initPicture(holder.img_1, TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[0]);
                    holder.relative_pic1.setVisibility(View.VISIBLE);
                    holder.relative_pic1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ShowWebImageActivity.class);
                            intent.putExtra("image", TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[0]);
                            intent.putExtra("ulist", uList);
                            activity.startActivity(intent);
                        }
                    });
                    loadPicture.initPicture(holder.img_2, TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[1]);
                    holder.relative_pic2.setVisibility(View.VISIBLE);
                    holder.relative_pic2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ShowWebImageActivity.class);
                            intent.putExtra("image", TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[1]);
                            intent.putExtra("ulist", uList);
                            activity.startActivity(intent);
                        }
                    });
                    loadPicture.initPicture(holder.img_3, TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[2]);
                    holder.relative_pic3.setVisibility(View.VISIBLE);
                    holder.relative_pic3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ShowWebImageActivity.class);
                            intent.putExtra("image", TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[2]);
                            intent.putExtra("ulist", uList);
                            activity.startActivity(intent);
                        }
                    });
                    loadPicture.initPicture(holder.img_4, TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[3]);
                    holder.relative_pic4.setVisibility(View.VISIBLE);
                    holder.relative_pic4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ShowWebImageActivity.class);
                            intent.putExtra("image", TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[3]);
                            intent.putExtra("ulist", uList);
                            activity.startActivity(intent);
                        }
                    });
                    holder.relative_pic5.setVisibility(View.INVISIBLE);
                    break;
                case 5:
                    uList.add(TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[0]);
                    uList.add(TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[1]);
                    uList.add(TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[2]);
                    uList.add(TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[3]);
                    uList.add(TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[4]);
                    loadPicture.initPicture(holder.img_1, TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[0]);
                    holder.relative_pic1.setVisibility(View.VISIBLE);
                    holder.relative_pic1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ShowWebImageActivity.class);
                            intent.putExtra("image", TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[0]);
                            intent.putExtra("ulist", uList);
                            activity.startActivity(intent);
                        }
                    });
                    loadPicture.initPicture(holder.img_2, TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[1]);
                    holder.relative_pic2.setVisibility(View.VISIBLE);
                    holder.relative_pic2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ShowWebImageActivity.class);
                            intent.putExtra("image", TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[1]);
                            intent.putExtra("ulist", uList);
                            activity.startActivity(intent);
                        }
                    });
                    loadPicture.initPicture(holder.img_3, TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[2]);
                    holder.relative_pic3.setVisibility(View.VISIBLE);
                    holder.relative_pic3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ShowWebImageActivity.class);
                            intent.putExtra("image", TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[2]);
                            intent.putExtra("ulist", uList);
                            activity.startActivity(intent);
                        }
                    });
                    loadPicture.initPicture(holder.img_4, TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[3]);
                    holder.relative_pic4.setVisibility(View.VISIBLE);
                    holder.relative_pic4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ShowWebImageActivity.class);
                            intent.putExtra("image", TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[3]);
                            intent.putExtra("ulist", uList);
                            activity.startActivity(intent);
                        }
                    });
                    loadPicture.initPicture(holder.img_5, TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[4]);
                    holder.relative_pic5.setVisibility(View.VISIBLE);
                    holder.relative_pic5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ShowWebImageActivity.class);
                            intent.putExtra("image", TLUrl.getInstance().URL_hwg_comment_share + item.getGeval_frommemberid() + "/" + strings[4]);
                            intent.putExtra("ulist", uList);
                            activity.startActivity(intent);
                        }
                    });
                    break;
            }

            holder.linear_share.setVisibility(View.VISIBLE);

        }

        if(item.getGeval_explain().equals("null")){
            holder.linear_explain.setVisibility(View.GONE);
        }else {
            holder.linear_explain.setVisibility(View.VISIBLE);
            holder.t_explain.setText(item.getGeval_explain());
        }

    }


    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}
