package com.abcs.haiwaigou.local.fragment.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.local.beans.HireFind;
import com.abcs.haiwaigou.local.fragment.viewholder.PublishViewHolder;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zjz on 2016/9/6.
 */
public class PublishAdapter extends RecyclerView.Adapter<PublishViewHolder> {
    Activity activity;
    PublishViewHolder.RootOnClick rootOnClick;
    public ArrayList<HireFind> hireFinds;
    private SortedList<HireFind> mSortedList;
    String oLd_menuId;
    public Handler handler = new Handler();

    private HashMap<String, Boolean> inPublishMap = new HashMap<String, Boolean>();   // 用于存放选中的项
    public PublishAdapter(Activity activity, PublishViewHolder.RootOnClick rootOnClick, String oLd_menuId) {
        this.hireFinds=new ArrayList<HireFind>();
        this.oLd_menuId = oLd_menuId;
        this.activity = activity;
        this.rootOnClick = rootOnClick;
        mSortedList = new SortedList<>(HireFind.class, new SortedList.Callback<HireFind>() {

            /**
             * 返回一个负整数（第一个参数小于第二个）、零（相等）或正整数（第一个参数大于第二个）
             */
            @Override
            public int compare(HireFind o1, HireFind o2) {

                if (o1.getIds()<o2.getIds()) {
                    return -1;
                } else if (o1.getIds()> o2.getIds()) {
                    return 1;
                }

                return 0;
            }

            @Override
            public boolean areContentsTheSame(HireFind oldItem, HireFind newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areItemsTheSame(HireFind item1, HireFind item2) {
                return item1.getId() .equals( item2.getId());
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


    public SortedList<HireFind> getList() {
        return mSortedList;
    }

    public void addItems(ArrayList<HireFind> list) {
        mSortedList.beginBatchedUpdates();

        for (HireFind itemModel : list) {
            mSortedList.add(itemModel);
        }

        mSortedList.endBatchedUpdates();
    }

    public void deleteItems(ArrayList<HireFind> items) {
        mSortedList.beginBatchedUpdates();
        for (HireFind item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    @Override
    public PublishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_item_publish, parent, false);
        PublishViewHolder currentViewHolder = new PublishViewHolder(view,rootOnClick);
        return currentViewHolder;
    }

    @Override
    public void onBindViewHolder(final PublishViewHolder holder, int position) {
        final HireFind hireFind=mSortedList.get(position);
        holder.t_title.setText(hireFind.getTitle());
        if(hireFind.getPubTime()==0){
            if(hireFind.getCreateTime()==0){
                holder.t_time.setVisibility(View.GONE);
            }else {
                if(hireFind.getCreateTime()<2*1000000000){
                    holder.t_time.setText(Util.formatzjz3.format(hireFind.getCreateTime() * 1000));
                }else {
                    holder.t_time.setText(Util.formatzjz3.format(hireFind.getCreateTime() ));
                }
            }
        }else {
            holder.t_time.setVisibility(View.VISIBLE);
            if(hireFind.getPubTime()<2*1000000000){
                holder.t_time.setText(Util.formatzjz3.format(hireFind.getPubTime() * 1000));
            }else {
                holder.t_time.setText(Util.formatzjz3.format(hireFind.getPubTime() ));
            }
        }
        if(hireFind.getIcon().equals("")){

            if(!TextUtils.isEmpty(oLd_menuId)){

                if(oLd_menuId.equals(MyString.LOCAL_MENU1)){
                    holder.img_icon.setImageResource(R.drawable.img_local_gongzuo);
                }else if(oLd_menuId.equals(MyString.LOCAL_MENU1)){
                    holder.img_icon.setImageResource(R.drawable.img_local_gongzuo);
                }else if(oLd_menuId.equals(MyString.LOCAL_MENU3)){
                    holder.img_icon.setImageResource(R.drawable.img_local_fangwu);
                }else if(oLd_menuId.equals(MyString.LOCAL_MENU4)){
                    holder.img_icon.setImageResource(R.drawable.img_local_genduo);
                }else if(oLd_menuId.equals(MyString.LOCAL_MENU2)){
                    holder.img_icon.setImageResource(R.drawable.img_local_shengyi);
                }else {
                    holder.img_icon.setImageResource(R.drawable.img_local_huangye);
                }
            }

//            holder.img_icon.setImageResource(R.drawable.img_local_default);
            if(!TextUtils.isEmpty(hireFind.getTitle())){
                if(Util.containsString(hireFind.getTitle().substring(0, 1))){
                    holder.t_text.setText(hireFind.getTitle().substring(1, 2));
                }else {
                    holder.t_text.setText(hireFind.getTitle().substring(0, 1));
                }
                holder.t_text.setVisibility(View.VISIBLE);
            }
        }else {
            holder.t_text.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(hireFind.getIcon(), holder.img_icon, Options.getListOptions());
        }


        holder.cb_check.setChecked(hireFind.isCheeck());

        holder.cb_check.setOnCheckedChangeListener(null);
        Boolean isChecked = inPublishMap.get(hireFind.getId());

        if (isChecked != null && isChecked) {
            holder.cb_check.setChecked(true);
        } else {
            holder.cb_check.setChecked(false);
        }

        holder.cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    inPublishMap.put(hireFind.getId(), b);
                    holder.cb_check.setChecked(true);
                }else {
                    holder.cb_check.setChecked(false);
                    inPublishMap.remove(hireFind.getId());
                    notifyDataSetChanged();
                }
            }
        });

        holder.img_delete_myPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PromptDialog(activity, "确认删除这条发布？", new Complete() {
                    @Override
                    public void complete() {
                        ProgressDlgUtil.showProgressDlg("Loading...", activity);
                        Log.i("删除的发布的id:","hire_url:"+hireFind.getId());
                        HttpRequest.sendGet(TLUrl.getInstance().URL_local_delete_mypublish, "id=" + hireFind.getId() , new HttpRevMsg() {
                            @Override
                            public void revMsg(final String msg) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject object = new JSONObject(msg);
                                            if (object.getInt("status") == 1) {
                                                Log.i("xuke", "hire_url,msg=" + msg);
                                                ProgressDlgUtil.stopProgressDlg();
                                                MyUpdateUI.sendUpdateCollection(activity, MyUpdateUI.LOCALMYPUBLISH);
                                            } else {
                                                ProgressDlgUtil.stopProgressDlg();
                                                Log.i("xuke", "hire_url,goodsDetail:参数校验失败");
                                            }
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            Log.i("zjz", e.toString());
                                            e.printStackTrace();
                                            ProgressDlgUtil.stopProgressDlg();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }


    //-------------------------------全选删除未完成----------------------------------
//    /**
//     * 更新选中状态
//     * */
//    private void notifyCheckedChanged() {
//        PublishFragment.publishId.clear();
//        for (int i=0;i<mSortedList.size();i++){
//            Boolean isChecked = inPublishMap.get(mSortedList.get(i).getId());
//            if (isChecked!=null&&isChecked){
//                PublishFragment.publishId.add(mSortedList.get(i).getId());
//            }
//        }
//    }
//
//    /**
//     * 选中所有
//     * */
//    public void checkAll() {
//        PublishFragment.publishId.clear();
//        for (int i=0;i<mSortedList.size();i++){
//            inPublishMap.put(mSortedList.get(i).getId(),true);
//            PublishFragment.publishId.add(mSortedList.get(i).getId());
//        }
//        notifyDataSetChanged();
//
//    }
//
//    /**
//     * 取消选中所有
//     * */
//    public void cancelAll() {
//        inPublishMap.clear();
//        PublishFragment.publishId.clear();
//        notifyDataSetChanged();
//    }
}
