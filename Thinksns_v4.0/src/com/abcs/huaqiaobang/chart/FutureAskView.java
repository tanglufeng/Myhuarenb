package com.abcs.huaqiaobang.chart;
 
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.chart.FutureAskViewAdapter.AskBean;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.tljr.zrclistview.SimpleFooter;
import com.abcs.huaqiaobang.tljr.zrclistview.SimpleHeader;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView.OnStartListener;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.ArrayList;


public class FutureAskView {
   Activity activity;
   View RealTimeView; 
   ZrcListView listview;
   public static ArrayList<AskBean> array;
   public FutureAskViewAdapter adapter;
   private EditText edit;
   private TextView send;
   private int page = 1;
   private boolean nomore = false;
   
   public FutureAskView(Activity activity){
       this.activity = activity;
       RealTimeView = activity.getLayoutInflater().inflate(R.layout.tljr_chart_edit, null);
       findView();
   }
   
   public View getView(){
       return RealTimeView;
   }
   
   private void findView(){
	   RealTimeView.findViewById(R.id.bedown).setVisibility(View.GONE);
       edit = (EditText) RealTimeView.findViewById(R.id.im_send_text);
//       RealTimeView.findViewById(R.id.im_audio).setVisibility(View.GONE);
//       RealTimeView.findViewById(R.id.im_send_image).setVisibility(View.GONE);
//       RealTimeView.findViewById(R.id.im_more).setVisibility(View.GONE);
       edit.setHint("我要提问");
       edit.addTextChangedListener(warcher);
       send = (TextView) RealTimeView.findViewById(R.id.im_send_btn);
       send.setOnClickListener(onclick);
       listview = (ZrcListView) RealTimeView.findViewById(R.id.list);
       listview.startLoadMore();
       
       SimpleHeader header = new SimpleHeader(activity);
          header.setTextColor(0xffeb5041);
          header.setCircleColor(0xffeb5041);
          listview.setHeadable(header);
    
          // 设置加载更多的样式（可选）
          SimpleFooter footer = new SimpleFooter(activity);
          footer.setCircleColor(0xffeb5041);
          listview.setFootable(footer);
    
          listview.setOnRefreshStartListener(new OnStartListener() {
    
              @Override
              public void onStart()
              {
                  // TODO Auto-generated method stub
                  page = 1;
                  GetAskList(false);
              }
          });
    
          listview.setOnLoadMoreStartListener(new OnStartListener() {
    
              @Override
              public void onStart()
              {
                  if(!nomore){
                      page++;
                      GetAskList(true);
                  }else{
                      listview.setLoadMoreSuccess();
                  }
//                   listview.setLoadMoreSuccess();
              }
          });
       array = new ArrayList<AskBean>();
//        AskBean r = new AskBean();
//        array.add(r);
//        array.add(r);
//        array.add(r);
       
       GetAskList(false);
   }
   
   
private OnClickListener onclick = new OnClickListener() {
       
       @Override
       public void onClick(View arg0) {
           // TODO Auto-generated method stub
           switch (arg0.getId()) {
           case R.id.im_send_btn:
               Ask(edit.getText().toString());
               edit.setText("");
               break;
           default:
               break;
           }
       }
   };
   
   //提问
   public void Ask(String msg){
        try {
            //&mobile=   &avatar=
            HttpRequest.sendPost(TLUrl.getInstance().URL_goods_base+"/HQChat/rest/userqa/question",
                    "uid="+MyApplication.getInstance().self.getId()+"&msg="+msg
                    ,new HttpRevMsg() {
                @Override
                public void revMsg(String msg) {
                    // TODO Auto-generated method stub
                    Log.d("ChartActivity", "match :"+msg);
                    if(msg.equals("error")){
                        Log.d("ChartActivity", "2");
                        Toast.makeText(activity, "提问失败，请保持网络畅通或咨询客服", Toast.LENGTH_LONG).show();
                    }else{
                        JSONObject js = JSONObject.parseObject(msg);
                        Log.d("ChartActivity", "1");
                        if(js.getInteger("status") == 1){
                        	Toast.makeText(activity, "提问成功", Toast.LENGTH_LONG).show();
                            Log.d("ChartActivity", "1");
                            handler.sendEmptyMessage(1);
//                            GetAskList();
                        }
                        
                    }
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
       
   //获取提问列表
   public void GetAskList(final boolean isadd){
        try {
            //&mobile=   &avatar=
            HttpRequest.sendGet(TLUrl.getInstance().URL_goods_base+"http://120.24.235.202:8080/HQChat/rest/userqa/getquestions",
                    "page="+page+"&size=10"+"&uid="+MyApplication.getInstance().self.getId()
                    ,new HttpRevMsg() {
                @Override
                public void revMsg(String msg) {
                    // TODO Auto-generated method stub
                    Log.d("ChartActivity", "match :"+msg);
                    if(!msg.equals("error")){
                        JSONObject js = JSONObject.parseObject(msg);
                        if(js.getInteger("status") == 1){
                            JSONArray arr = JSONArray.parseArray(js.getString("msg"));
                            ArrayList<AskBean> list = new ArrayList<AskBean>();
                            for(int i =0 ;i<arr.size();i++){
                                JSONObject ob = arr.getJSONObject(i);
                                AskBean ask = new AskBean();
                                ask.answers = ob.getString("answers");
                                ask.date = ob.getLongValue("date")*1000 ;
                                ask.focus = ob.getString("focus");
                                ask.id = ob.getString("id");
                                ask.msg = ob.getString("msg");
                                ask.nickname = ob.getString("nickname");
                                ask.isfocus = ob.getBooleanValue("isfocus");
//                                    ask.type = ob.getString("type");
                                ask.uid = ob.getString("uid");
                                list.add(ask);
                            }
                            if(isadd){
                                if(list.size()==0){
                                    nomore = true;
                                }else{
                                    array.addAll(list);
                                }
                                handler.sendEmptyMessage(2);
                            }else{
                                nomore = false;
                                array.clear();
                                array.addAll(list);
                                handler.sendEmptyMessage(0);
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
   
       
   public Handler handler = new Handler(){
       @Override
       public void handleMessage(Message msg) {
           // TODO Auto-generated method stub
           switch (msg.what) {
           case 0:
               Log.d("ChartActivity", "arr.size :"+array.size());
               adapter = new FutureAskViewAdapter(activity, array);
               listview.setAdapter(adapter);
               RealTimeView.findViewById(R.id.jiazai).setVisibility(View.GONE);
               adapter.notifyDataSetChanged();
               listview.setRefreshSuccess();
               break;
           case 1:
               GetAskList(false);
               break;
           case 2:
               if(adapter!=null) {
                   adapter.notifyDataSetChanged();
                   Log.d("TAG", "in loadSuccess");
                   listview.setLoadMoreSuccess();
               }
               break;
           default:
               break;
           }
       }
   };
   
   private TextWatcher warcher = new TextWatcher() {
       @Override
       public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
           // TODO Auto-generated method stub
           if(edit.getText().toString().equals("")){
               send.setVisibility(View.GONE);
           }else{
               send.setVisibility(View.VISIBLE);
           }
       }
       @Override
       public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
               int arg3) {
           // TODO Auto-generated method stub
       }
       
       @Override
       public void afterTextChanged(Editable arg0) {
           // TODO Auto-generated method stub
       }
   };
   }