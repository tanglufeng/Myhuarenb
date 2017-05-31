package com.abcs.haiwaigou.utils;

import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.sociax.android.R;

import org.json.JSONException;
import org.json.JSONObject;


public class TraCommeDialog implements View.OnClickListener {

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private BaseActivity activity;
    private String id;

    private EditText et_comment;
    private TextView tv_check,tv_cancel;


    public TraCommeDialog(BaseActivity activity,String Id) {
        this.activity = activity;
        this.id = Id;
        builder = new AlertDialog.Builder(activity, R.style.photodialog);

        View view = LayoutInflater.from(activity).inflate(R.layout.travel_addcom_dialog, null);
        et_comment = (EditText) view.findViewById(R.id.et_comment);
        tv_check = (TextView) view.findViewById(R.id.tv_check);
        tv_check.setOnClickListener(this);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
        //设置布局
        builder.setView(view);
        //生成dialog
        alertDialog = builder.create();

    }


    //设置dialog的方位
    private void setDialogGravity() {
        //获取dialog的window对象
        Window window = alertDialog.getWindow();
        //设置dialog的位置
        window.setGravity(Gravity.BOTTOM);
        //添加动画
        window.setWindowAnimations(R.style.photostyle);
        //设置宽度
        WindowManager windowManager = activity.getWindowManager();
        //获取手机屏幕display对象
        Display display = windowManager.getDefaultDisplay();
        //获取window的属性对象
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置属性的宽度
        lp.width = (int) (display.getWidth());
        //重新设置dialog的window的属性
        window.setAttributes(lp);
    }



    /**
     * 显示dialog
     */
    public void show() {
        //设置dialog的方位
        setDialogGravity();
        //然后显示
        alertDialog.show();
    }

    /**
     * 取消dialog
     */
    public void cancel() {
        alertDialog.cancel();
    }


    public interface getComCount{
        void setCount(int count);
    }

     getComCount mGetComCount;

    public void setGetComCount(getComCount getComCount) {
        mGetComCount = getComCount;
    }

    Handler mHandler=new Handler();

    private void initpopshow(String comment){
        //  http://120.24.19.29:7075/note/addComment?id=110008&uid=10666&comment=%E5%A5%BD%E5%A5%BD
        HttpRequest.sendGet("http://120.24.19.29:7075/note/addComment", "id="+id+"&uid="+ MyApplication.getInstance().getUid()+"&comment="+comment, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(!TextUtils.isEmpty(msg)){
                            Log.e("zds","youji_addcom"+msg+"");

                            try {
                                JSONObject object=new JSONObject(msg);

                                if(object.optInt("result")==1){
                                    JSONObject bodg=object.optJSONObject("body");
                                    if(bodg!=null&&bodg.optInt("count")>0){
                                        if(mGetComCount!=null){
                                            mGetComCount.setCount(bodg.optInt("count"));
                                        }
                                    }

                                    Toast.makeText(activity,object.optString("info"),Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                    et_comment.setText("");
                                }else {
                                    Toast.makeText(activity,"发表失败！",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_check) {
            String com=et_comment.getText().toString().trim();
            if(TextUtils.isEmpty(com)){
                Toast.makeText(activity,"评论内容不能为空！",Toast.LENGTH_SHORT).show();
                return;
            }else {
                initpopshow(com);
            }
        }else  if (v.getId() == R.id.tv_cancel) {
            cancel();
        }
    }
}
