package com.abcs.huaqiaobang.ytbt.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.util.CircleImageView;
import com.abcs.huaqiaobang.ytbt.bean.GroupMemberBean;
import com.abcs.huaqiaobang.ytbt.bean.MsgBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.chats.ChattingActivity;
import com.abcs.huaqiaobang.ytbt.chats.ChattingFragment;
import com.abcs.huaqiaobang.ytbt.chats.PhotoActivity;
import com.abcs.huaqiaobang.ytbt.emotion.EmotionUtils;
import com.abcs.huaqiaobang.ytbt.util.JsonUtil;
import com.abcs.huaqiaobang.ytbt.util.MsgTimeUtil;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/26.
 */
public class ChatsDetailsAdapter extends BaseAdapter {

    private Context context;
    private List<MsgBean> list = new ArrayList<>();
    private User freind;
    private Boolean isgroup;
    GroupMemberBean memberBean;
    private int current_voice_type;
    private ImageView current_voice_view;
    List<GroupMemberBean> members;
    public MediaPlayer mPlayer = new MediaPlayer();
    AnimationDrawable voiceAnimation;

    // private List<MsgBean> list2;

    public ArrayList<MsgBean> getList() {
        return MyApplication.getInstance().getMsgBeans();
    }

    public void setList(List<MsgBean> findAll) {
        list.clear();
        for (int i = 0; i < findAll.size(); i++) {
            MsgBean msg = findAll.get(i);
            // msg.setFlag(1);
            if (msg.getType().equals(
                    MyApplication.getInstance().getUserBean().getVoipAccount()
                            + "sendto" + freind.getVoipAccout())) {
                this.list.add(msg);
            } else if (msg.getType().equals(
                    MyApplication.getInstance().getUserBean().getVoipAccount()
                            + "rev" +
                            // 群组 ID或 好友 账号
                            freind.getVoipAccout())) {
                this.list.add(msg);
            } else if (msg.getType().equals("notice")
                    && msg.getMsgfrom().equals(freind.getVoipAccout())) {
                this.list.add(msg);
            }
            // if (msg.getType().equals(
            // freind.getVoipAccout() + "rev"+ msg.getMsgfrom())) {
            // this.list.add(msg);
            // }
        }
        notifyDataSetChanged();
    }

    public ChatsDetailsAdapter(Context context, ArrayList<MsgBean> list,
                               User freind, Boolean isgroup) {

        // try {
        // members=MyApplication.dbUtils.findAll(GroupMemberBean.class);
        // Log.i("xbbada",members.size()+"a");
        // } catch (DbException e) {
        // e.printStackTrace();
        // }
        this.context = context;
        this.freind = freind;
        this.isgroup = isgroup;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private View getViewByType(String msgtype) {
        return LayoutInflater.from(context).inflate(
                msgtype.contains("sendto") ? R.layout.chats_listview_item_send
                        : R.layout.chats_listview_item_rev, null);
    }

    @Override
    public int getItemViewType(int position) {
        String msgtype = list.get(position).getType();
        if (msgtype.contains("rev")) {
            return 0;
        } else if (msgtype.contains("sendto")) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String msgtype = list.get(position).getType();
        final MsgBean msgBean = list.get(position);
        // String msgTo = freind.getVoipAccout();
        long LastTime = position == 0 ? list.get(position).getMsgtime() : list
                .get(position - 1).getMsgtime();
        long MsgTime = list.get(position).getMsgtime();
        final int type = getItemViewType(position);
        if (type == 2) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.chats_listview_item_notice, null);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            setTimeVisibility(position, LastTime, MsgTime, time);
            TextView notice = (TextView) convertView.findViewById(R.id.notice);
            notice.setText(msgBean.getMsg());
            return convertView;
        }
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getViewByType(msgtype);
            holder.avadar = (CircleImageView) convertView
                    .findViewById(R.id.avatar);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.msg = (TextView) convertView.findViewById(R.id.msg);
            holder.length = (TextView) convertView
                    .findViewById(R.id.voice_length);
            holder.voice = (ImageView) convertView.findViewById(R.id.voice);
            holder.image = (ImageView) convertView.findViewById(R.id.img);
            if (type == 0) {
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.start = (RelativeLayout) convertView
                        .findViewById(R.id.relativelayoutrev);
            } else {
                holder.start = (RelativeLayout) convertView
                        .findViewById(R.id.relativelayoutsend);
                holder.pb = (ProgressBar) convertView
                        .findViewById(R.id.pb_sending);
                holder.state = (ImageView) convertView
                        .findViewById(R.id.iv_state);
            }
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        if (type == 1) {
            MyApplication.bitmapUtils.display(holder.avadar, MyApplication
                    .getInstance().getAvater());
            holder.state.setOnClickListener(new OnClickListener() {
                ChattingFragment fragment = ((ChattingActivity) context)
                        .getChattingFragment();

                @Override
                public void onClick(View v) {
                    msgBean.setMsgtime(System.currentTimeMillis());
                    if (!msgBean.getVoicepath().equals("")) {
                        fragment.sendVoice(msgBean);
                    } else if (!msgBean.getImg().equals("")) {
                        fragment.sendimg(msgBean);
                    } else {
                        fragment.sendtxt(msgBean);
                    }
                }
            });
            if (msgBean.getFlag() == 2) {
                holder.pb.setVisibility(View.GONE);
                holder.state.setVisibility(View.VISIBLE);
            } else if (msgBean.getFlag() == 1) {
                holder.pb.setVisibility(View.GONE);
                holder.state.setVisibility(View.GONE);
            } else {
                holder.pb.setVisibility(View.VISIBLE);
                holder.state.setVisibility(View.GONE);
            }
        } else if (isgroup) {
            queryMember(position, holder.name, holder.avadar);
        } else {
            MyApplication.bitmapUtils.display(holder.avadar, freind.getAvatar());
        }
        holder.image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startPhotoActivity(msgBean);
            }
        });
        if (!msgBean.getVoicepath().equals("")) {
            holder.voice.setVisibility(View.VISIBLE);
            holder.length.setVisibility(View.VISIBLE);
            holder.msg.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            final String[] infos = msgBean.getVoicepath().split("~");
            if (infos.length > 1)
                holder.length.setText(infos[1] + "''");
            holder.start.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        resetAnim();
                        current_voice_type = 0;
                        current_voice_view = holder.voice;
//                        holder.voice.setImageResource(type==0?R.anim.voice_from_icon:R.anim.voice_to_icon);
                        voiceAnimation = (AnimationDrawable) holder.voice.getDrawable();
                        if (mPlayer == null)
                            mPlayer = new MediaPlayer();
                        mPlayer.reset();
                        mPlayer.setDataSource(infos[0]);
                        mPlayer.prepare();
                        mPlayer.start();
                        voiceAnimation.start();
                        mPlayer.setOnCompletionListener(new OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mPlayer.release();
                                mPlayer = null;
                                voiceAnimation.stop();
                                holder.voice.setImageResource(type==1?R.drawable.chatfrom_voice_playing:R.drawable.chatto_voice_playing);
                            }
                        });
                        Log.i("xbb录音", mPlayer.getDuration() + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        voiceAnimation.stop();
                        holder.voice.setImageResource(type==1?R.drawable.chatfrom_voice_playing:R.drawable.chatto_voice_playing);
                    }
                }

            });
        }
        else if (msgBean.getImg().equals("")) {
            holder.msg.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
            holder.voice.setVisibility(View.GONE);
            holder.length.setVisibility(View.GONE);
            holder.msg.setText(EmotionUtils.getEmotionContent(context,
                    holder.msg, list.get(position).getMsg()));
        } else {
            holder.msg.setVisibility(View.GONE);
            holder.voice.setVisibility(View.GONE);
            holder.length.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            MyApplication.bitmapUtils
                    .configDefaultLoadFailedImage(R.drawable.image_download_fail_icon);
            MyApplication.bitmapUtils.display(holder.image,
                    Uri.parse(msgBean.getImg()).toString());
            holder.msg.setText(EmotionUtils.getEmotionContent(context,
                    holder.msg, list.get(position).getMsg()));
        }
        setTimeVisibility(position, LastTime, MsgTime, holder.time);
        return convertView;
    }

    private void setTimeVisibility(final int position, long LastTime,
                                   long MsgTime, TextView time) {
        time.setText(MsgTimeUtil.getShowMsgTime(MsgTime, LastTime));
        if (MsgTime - LastTime <= 300000 && position != 0) {
            time.setVisibility(View.GONE);
        }
    }

    public void resetAnim() {
        if (voiceAnimation != null) {
            voiceAnimation.stop();
            current_voice_view
                    .setImageResource(current_voice_type == 0 ? R.drawable.chatto_voice_playing
                            : R.drawable.chatfrom_voice_playing);
        }
    }

    private void queryMember(int position, TextView name,
                             CircleImageView avatar2) {
        name.setVisibility(View.VISIBLE);
        User user;
        try {
            user = MyApplication.dbUtils.findById(User.class, list
                    .get(position).getMsgfrom());
            if (user != null) {
                name.setText(user.getRemark().trim().equals("") ? user
                        .getNickname() : user.getRemark());
                MyApplication.bitmapUtils.display(avatar2, user.getAvatar());
                return;
            }
        } catch (DbException e1) {
            e1.printStackTrace();
        }
        try {
            GroupMemberBean member = MyApplication.dbUtils.findById(
                    GroupMemberBean.class, freind.getVoipAccout());
            // ArrayList<User> userlist2=(ArrayList<User>)
            // MyApplication.dbUtils.findAll(User.class);
            if (member != null) {
                ArrayList<User> userlist = JsonUtil.parseString(member
                        .getMembers());
                for (int i = 0; i < userlist.size(); i++) {
                    if (userlist.get(i).getVoipAccout()
                            .equals(list.get(position).getMsgfrom())) {
                        // if(userlist.get(i).getRemark().trim().equals("")){
                        // name.setText(userlist.get(i).getNickname());
                        // }else{
                        // name.setText(userlist.get(i).getRemark());
                        // }
                        name.setText(userlist.get(i).getNickname());
                        MyApplication.bitmapUtils.display(avatar2, userlist
                                .get(i).getAvatar());
                        // initCircleAvater(userlist.get(i).getAvatar(),
                        // avatar2);
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // private GroupMemberBean SpeakeMember(int position) {
    // for(int i=0;i<members.size();i++){
    // if(msgBean.getMsgfrom().equals(members.get(i).getMemberVoip())){
    // return members.get(i);
    // }
    // }
    // return null;
    // }
    private void startPhotoActivity(final MsgBean msgBean) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra("path", msgBean.getImg());
        context.startActivity(intent);
    }

    public void notichange() {
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView time, name, msg, length;
        ImageView image, state, voice;
        CircleImageView avadar;
        ProgressBar pb;
        RelativeLayout start;
    }
}
