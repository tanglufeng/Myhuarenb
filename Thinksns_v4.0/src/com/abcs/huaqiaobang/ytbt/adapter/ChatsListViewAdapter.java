package com.abcs.huaqiaobang.ytbt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.bean.MsgBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.bean.UserBean;
import com.abcs.huaqiaobang.ytbt.util.CircleImageView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/26.
 */
public class ChatsListViewAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<UserBean> userBeans;
	private ArrayList<User> userlist;
	private ArrayList<MsgBean> list=new ArrayList<>();
	
	
	public void setList(ArrayList<MsgBean> msgBeans) {
		list.clear();
		  for(int i=0;i<msgBeans.size();i++){		  
        	if(msgBeans.get(i).getFlag()==0){
        		list.add(msgBeans.get(i)); 
        		MsgBean bean=msgBeans.get(i);
        		bean.setFlag(1);
        		msgBeans.set(i, bean);
        	};
        }
		  MyApplication.getInstance().setMsgBeans(msgBeans);
		  notifyDataSetChanged();
	}
    public ArrayList<MsgBean> getList() {
		return list;
	}

	public ChatsListViewAdapter(Context context, ArrayList<User> userlist) {

        this.context = context;
		this.userlist = userlist;
      
    }

    @Override
    public int getCount() {
        return userlist.size();
    }


    @Override
    public Object getItem(int position) {
        return userlist.get(position).getVoipAccout();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.chat_inbox_listitem,null);
        TextView name= (TextView) convertView.findViewById(R.id.name);
        CircleImageView avatar=(CircleImageView) convertView.findViewById(R.id.avatar);   
        MyApplication.bitmapUtils.display(avatar, userlist.get(position).getAvatar());
        ImageView flag=(ImageView) convertView.findViewById(R.id.flag);
        unRead(position,flag);
        name.setText(userlist.get(position).getNickname()+"");
        return convertView;
    }
    
    private void unRead(int position, ImageView flag) {
		// TODO Auto-generated method stub
		for(int i=0;i<getList().size();i++){
			if(getList().get(i).getMsgfrom().equals(userlist.get(position).getVoipAccout())){
				flag.setVisibility(View.VISIBLE);	
			}
		}
	}
}
