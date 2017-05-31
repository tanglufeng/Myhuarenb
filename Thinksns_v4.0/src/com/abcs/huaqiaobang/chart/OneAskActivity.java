package com.abcs.huaqiaobang.chart;
 
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.chart.FutureAskViewAdapter.AskBean;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.activity.StartActivity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.ArrayList;
import java.util.HashMap;


public class OneAskActivity extends BaseActivity{
   
   public static String id = "";
   private String msg,nickname,focus,answers;
   private boolean isfocus = false;
   private TextView zan_bt;
   private ListView list;
   private ArrayList<AskBean> arrlist;
   private int page = 1,position;
   public static HashMap<String,AskBean > map;//点击回答所用的数据
   public static int HaveNewAnswer = 9999;
   private String TAG = "OneAskActivity";
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       // TODO Auto-generated method stub
       super.onCreate(savedInstanceState);
       setContentView(R.layout.tljr_activity_oneask);
//       getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.toumin));
       findview();
   }
   
   private void findview(){
       map = new HashMap<String, AskBean>();
       findViewById(R.id.tljr_img_futures_join_back).setOnClickListener(onclick);
       findViewById(R.id.addanswer).setOnClickListener(onclick);
       zan_bt = (TextView)findViewById(R.id.zan_bt);
       zan_bt.setOnClickListener(onclick);
       id = getIntent().getStringExtra("id");
       msg = getIntent().getStringExtra("msg");
       nickname = getIntent().getStringExtra("nickname");
       focus = getIntent().getStringExtra("focus");
       answers = getIntent().getStringExtra("answers");
       isfocus = getIntent().getBooleanExtra("isfocus", false);
       position = getIntent().getIntExtra("position", 0);
       if(isfocus){
           zan_bt.setText("取消关注");
           ((GradientDrawable)zan_bt.getBackground()).setColor(Color.GRAY);
       }else{
           zan_bt.setText("关注");
           ((GradientDrawable)zan_bt.getBackground()).setColor(Util.c_green);
       }
       ((TextView)findViewById(R.id.name)).setText(nickname);
       ((TextView)findViewById(R.id.time)).setText(Util.getDateOnlyHour(getIntent().getLongExtra("date", 0)));
       ((TextView)findViewById(R.id.content)).setText(msg);
       ((TextView)findViewById(R.id.zannumber)).setText(focus);
       arrlist = new ArrayList<AskBean>();
       list = (ListView) findViewById(R.id.list);
       list.setAdapter(new BaseAdapter() {
           
           @Override
           public View getView(int position, View v, ViewGroup arg2) {
               // TODO Auto-generated method stub
               Holder holder ;
               if(v == null){
                   holder = new Holder();
                   v = View.inflate(OneAskActivity.this, R.layout.activity_chart_askitem, null);
//                    holder.touxiang
                   holder.name = ((TextView) v.findViewById(R.id.name));
                   holder.time = ((TextView) v.findViewById(R.id.time));
                   holder.content = ((TextView) v.findViewById(R.id.content));
                   holder.zan = ((TextView) v.findViewById(R.id.zan));
                   holder.image = (CircularImage) v.findViewById(R.id.touxiang);
                   v.setTag(holder);
               }else{
                   holder = (Holder) v.getTag();
               }
               AskBean ask = arrlist.get(position);
               map.put(ask.id, ask);
               holder.content.setText(ask.msg);
               holder.zan.setText(ask.focus);
               StartActivity.imageLoader.displayImage(ask.avatar, holder.image);
               if(Util.getDateOnlyDat(System.currentTimeMillis()).equals(Util.getDateOnlyDat(ask.date))){
                   holder.time.setText(Util.getDateOnlyHour(ask.date));
               }else{
                   holder.time.setText(Util.getDateOnlyDat(ask.date));
               }
               holder.name.setText(ask.nickname);
               holder.id = ask.id;
               v.setOnClickListener(new OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       // TODO Auto-generated method stub
                       Intent i = new Intent(OneAskActivity.this,OneAnswer.class);
                       i.putExtra("type", "OneAskActivity");
                       i.putExtra("id", ((Holder)v.getTag()).id);
                       i.putExtra("title", msg);
//                       startActivity(i);
                       startActivityForResult(i, 1);
                   }
               });
               return v;
           }
           @Override
           public long getItemId(int arg0) {
               // TODO Auto-generated method stub
               return arg0;
           }
           @Override
           public Object getItem(int arg0) {
               // TODO Auto-generated method stub
               return arrlist.get(arg0);
           }
           @Override
           public int getCount() {
               // TODO Auto-generated method stub
               return arrlist.size();
           }
       });
       GetanswersList(true);
   }
   
   private OnClickListener onclick = new OnClickListener() {
       
       @Override
       public void onClick(View arg0) {
           // TODO Auto-generated method stub
           switch (arg0.getId()) {
           case R.id.tljr_img_futures_join_back:
               finish();
               break;
           case R.id.addanswer:
               EditDialog dialog = new EditDialog(OneAskActivity.this,handler);
               dialog.show();
               break;
           case R.id.zan_bt:
               if(isfocus){
                   Ask(id,false);
               }else{
                   Ask(id,true);
               }
               break;
           default:
               break;
           }
       }
   };
   
   //获取回答列表
       public void GetanswersList(final boolean isupdata){
           try {
               //&mobile=   &avatar=
               HttpRequest.sendGet(TLUrl.getInstance().URL_goods_base+"/HQChat/rest/userqa/getanswers",
                       "page="+page+"&size=10"+"&id="+id+"&uid="+MyApplication.getInstance().self.getId()
                       ,new HttpRevMsg() {
                   @Override
                   public void revMsg(String msg) {
                       // TODO Auto-generated method stub
                       Log.d("ChartActivity", "match :"+msg);
                       if(!msg.equals("error")){
                           JSONObject js = JSONObject.parseObject(msg);
                           if(js.getInteger("status") == 1){
                               JSONArray jr = JSONArray.parseArray(js.getString("msg"));
                               ArrayList<AskBean> list = new ArrayList<AskBean>();
                               for(int i = 0 ;i<jr.size();i++){
                                   JSONObject ob = jr.getJSONObject(i);
                                   AskBean ask = new AskBean();
                                   ask.date = ob.getLongValue("date")*1000;
                                   ask.isfocus = ob.getBooleanValue("isfocus");
                                   ask.nickname = ob.getString("nickname");
                                   ask.id = ob.getString("id");
                                   ask.msg = ob.getString("msg");
                                   ask.focus = ob.getString("focus");
                                   ask.avatar = ob.getString("avatar");
                                   list.add(ask);
                               }
                               if(HaveNewAnswer == 9998 && arrlist.size() == 0){
                            	   if(list.size()>0){
                            		   Log.d(TAG, "jr :"+jr.toJSONString());
                            		   FutureAskView.array.get(position).answers = jr.toJSONString();
                            		   HaveNewAnswer = 9999;
                            		   Intent intent = new Intent();  //Itent就是我们要发送的内容
                                       intent.putExtra("data", "updateFuture");  
                                       intent.setAction("Tip_ChartActivity");   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                                       sendBroadcast(intent);   //发送广播
                            	   }
                               }else{
                            	   HaveNewAnswer = 9999;
                               }
                               if(isupdata){
                            	   arrlist.clear();
                               }
                               arrlist.addAll(list);
                               handler.sendEmptyMessage(0);
                           }
                       }
                   }
               });
           } catch (Exception e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }
       }
   
   
   //关注
   public void Ask(String id,final boolean zanorno){
        try {
            //&mobile=   &avatar=
            Log.d("ChartActivity", "点赞 id :"+id);
            String url = TLUrl.getInstance().URL_goods_base+"/HQChat/rest/userqa/";
            if(zanorno){
                url+="focus";
            }else{
                url+="unfocus";
            }
            Log.d("ChartActivity", "url :"+url);
            HttpRequest.sendGet(url, 
                    "uid="+MyApplication.getInstance().self.getId()+"&id="+id
                    ,new HttpRevMsg() {
                @Override
                public void revMsg(String msg) {
                    // TODO Auto-generated method stub
                    Log.d("ChartActivity", "点赞 :"+msg);
                    if(!msg.equals("error")){
                        JSONObject js = JSONObject.parseObject(msg);
                        if(js.getInteger("status") == 1){
                            isfocus = zanorno;
                            handler.sendEmptyMessage(1);
                            handler.sendEmptyMessage(9);
                        }else{
                        }
                    }
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
   
   
   private Handler handler = new Handler(){
       public void handleMessage(android.os.Message msg) {
           switch (msg.what) {
           case 0:
               ((BaseAdapter)list.getAdapter()).notifyDataSetChanged();
               break;
           case 1:
               int i;
               if(isfocus){
                   zan_bt.setText("取消关注");
                   i = Integer.parseInt(((TextView)findViewById(R.id.zannumber)).getText().toString())+1;
                   ((GradientDrawable)zan_bt.getBackground()).setColor(Color.GRAY);
               }else{
                   zan_bt.setText("关注");
                   i = Integer.parseInt(((TextView)findViewById(R.id.zannumber)).getText().toString())-1;
                   ((GradientDrawable)zan_bt.getBackground()).setColor(Util.c_green);
               }
               ((TextView)findViewById(R.id.zannumber)).setText(i +"");
               FutureAskView.array.get(position).focus = i+"";
               FutureAskView.array.get(position).isfocus = isfocus;
               break;
           case 9:
               //重新更新(将清除list)
               page=1;
               GetanswersList(true);
               break;
           case 10:
        	   //EditDialog的更新
               page=1;
               GetanswersList(true);
               if(arrlist.size() == 0){
            	   HaveNewAnswer = 9998;
               }
        	   break;
           default:
               break;
           }
       };
   };
   
   public final class Holder{
       ImageView touxiang;
       TextView zan;
       TextView name;
       TextView time;
       TextView content;
       String id;
       CircularImage image;
   }
   
   @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
	   if(requestCode == 1 && resultCode == 1){
		   ((BaseAdapter)list.getAdapter()).notifyDataSetChanged();
	   }
	}
   
}