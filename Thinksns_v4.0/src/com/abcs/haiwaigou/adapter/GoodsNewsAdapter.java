package com.abcs.haiwaigou.adapter;

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
import android.widget.Toast;

import com.abcs.haiwaigou.activity.MyDeliverActivity;
import com.abcs.haiwaigou.activity.OrderDetailActivity;
import com.abcs.haiwaigou.activity.RedBagActivity;
import com.abcs.haiwaigou.adapter.viewholder.GoodsNewsViewHolder;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/12.
 */
public class GoodsNewsAdapter extends RecyclerView.Adapter<GoodsNewsViewHolder> {


    ArrayList<Goods> goodses;
    Context context;
    Activity activity;

    public ArrayList<Goods> getDatas() {
        return goodses;
    }

    public Handler handler = new Handler();
    private SortedList<Goods> mSortedList;

    public GoodsNewsAdapter(Activity activity) {
        this.activity = activity;
        this.goodses = new ArrayList<>();
        mSortedList = new SortedList<>(Goods.class, new SortedList.Callback<Goods>() {

            /**
             * 返回一个负整数（第一个参数小于第二个）、零（相等）或正整数（第一个参数大于第二个）
             */
            @Override
            public int compare(Goods o1, Goods o2) {

                if (o1.getId() < o2.getId()) {
                    return -1;
                } else if (o1.getId() > o2.getId()) {
                    return 1;
                }

                return 0;
            }

            @Override
            public boolean areContentsTheSame(Goods oldItem, Goods newItem) {
                return oldItem.getTitle().equals(newItem.getTitle());
            }

            @Override
            public boolean areItemsTheSame(Goods item1, Goods item2) {
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

    public SortedList<Goods> getList() {
        return mSortedList;
    }

    public void addItems(ArrayList<Goods> list) {
        mSortedList.beginBatchedUpdates();

        for (Goods itemModel : list) {
            mSortedList.add(itemModel);
        }

        mSortedList.endBatchedUpdates();
    }

    public void deleteItems(ArrayList<Goods> items) {
        mSortedList.beginBatchedUpdates();
        for (Goods item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    @Override
    public GoodsNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_goods_news, parent, false);
        GoodsNewsViewHolder hwgFragmentViewHolder = new GoodsNewsViewHolder(view);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(final GoodsNewsViewHolder mHolder, final int position) {
        final Goods item = mSortedList.get(position);
        mHolder.t_time.setText(Util.formatDisplayTime(item.getTime() * 1000));

        LoadPicture loadPicture = new LoadPicture();
        loadPicture.initPicture(mHolder.img_goods_icon, item.getPicarr());
        if (item.getHint().equals("0")) {
            mHolder.img_goods_icon.setVisibility(View.VISIBLE);
            mHolder.t_state_and_name.setText("订单" + "[" + item.getOrder_sn() + "]" + "已成功支付！");
            mHolder.t_detail.setText("您购买的[" + item.getTitle() + "]商品*" + item.getGoods_num() + "已经支付成功！");
            mHolder.relative_detail.setVisibility(View.VISIBLE);
        } else if (item.getHint().equals("1")) {
            mHolder.img_goods_icon.setVisibility(View.VISIBLE);
            mHolder.t_state_and_name.setText("订单" + "[" + item.getOrder_sn() + "]" + "已发货！");
            mHolder.t_detail.setText("您购买的[" + item.getTitle() + "]商品*" + item.getGoods_num() + "已经发货！" + "快递公司：" + item.getExpress_name() + "，物流单号:" + item.getDeliver_code());
            mHolder.relative_detail.setVisibility(View.VISIBLE);
        } else if (item.getHint().equals("2")) {
            mHolder.img_goods_icon.setVisibility(View.VISIBLE);
            mHolder.t_state_and_name.setText("订单" + "[" + item.getOrder_sn() + "]" + "已收货！");
            mHolder.t_detail.setText("您购买的[" + item.getTitle() + "]商品*" + item.getGoods_num() + "已经收货成功！");
            mHolder.relative_detail.setVisibility(View.VISIBLE);
        } else if (item.getHint().equals("11")) {
            mHolder.img_goods_icon.setVisibility(View.GONE);
            mHolder.t_state_and_name.setText("理财转入转出");
            mHolder.t_detail.setText(item.getTitle());
            mHolder.relative_detail.setVisibility(View.GONE);
        }else if(item.getHint().equals("3")){
            mHolder.img_goods_icon.setVisibility(View.GONE);
            mHolder.t_state_and_name.setText("好评返红包");
            mHolder.t_detail.setText("您获得了一个口令红包：" + item.getTitle());
        }
        mHolder.relative_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (item.getHint().equals("0") || item.getHint().equals("2")) {
                    intent = new Intent(activity, OrderDetailActivity.class);
                    intent.putExtra("order_id", item.getOrder_id());
                    activity.startActivity(intent);
                } else if (item.getHint().equals("1")) {
                    intent = new Intent(activity, MyDeliverActivity.class);
                    intent.putExtra("order_id", item.getOrder_id());
                    activity.startActivity(intent);
                }else if(item.getHint().equals("3")){
                    intent = new Intent(activity, RedBagActivity.class);
                    intent.putExtra("red_code", item.getTitle());
                    activity.startActivity(intent);
                }
            }
        });
        mHolder.linear_root.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new PromptDialog(activity, "您确定删除该条消息?(此操作无法恢复)", new Complete() {
                            @Override
                            public void complete() {
                                ProgressDlgUtil.showProgressDlg("Loading...", activity);
                                Log.i("zjz", "url=" + TLUrl.getInstance().URL_hwg_goods_msg_del_one + "?" + "&id=" + item.getCid());
                                HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_goods_msg_del_one, "&id=" + item.getCid(),
                                        new HttpRevMsg() {
                                            @Override
                                            public void revMsg(final String msg) {
                                                handler.post(
                                                        new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    Log.i("zjz", "del_msg=" + msg);
                                                                    JSONObject object = new JSONObject(msg);
                                                                    if (object.optInt("status") == 1) {
                                                                        if (object.optString("msg").contains("成功")) {
                                                                            Toast.makeText(activity, "删除成功！", Toast.LENGTH_SHORT).show();
                                                                            MyUpdateUI.sendUpdateCollection(activity, MyUpdateUI.NEWS);
                                                                        }
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                } finally {
                                                                    ProgressDlgUtil.stopProgressDlg();
                                                                }
                                                            }
                                                        }

                                                );
                                            }
                                        }

                                );
                            }
                        }).show();

                        return false;
                    }
                }

        );
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}
