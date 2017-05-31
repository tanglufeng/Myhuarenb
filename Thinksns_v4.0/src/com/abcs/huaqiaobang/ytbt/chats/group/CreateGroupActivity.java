package com.abcs.huaqiaobang.ytbt.chats.group;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.ytbt.bean.GroupBean;
import com.abcs.huaqiaobang.ytbt.bean.GroupMemberBean;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CreateGroupActivity extends BaseActivity implements OnClickListener {

    private EditText group_name, group_declared, province, city;
    private Button create, cancel;
    private String[] grouptype = {"临时组(上限100人)", "普通组(上限300人)"};
    private String[] grouppermission = {"默认直接加入"};
    //    private String[] grouppermission = {"默认直接加入", "需要身份验证", "私有群组"};
    String name, declared, type, permission;
    private Spinner group_type, group_permission;
    private String groupid;
    List<String> voips;
    public Handler myhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Toast.makeText(CreateGroupActivity.this, "群组创建成功！",
                        Toast.LENGTH_SHORT).show();
                GroupBean entity = new GroupBean();
                entity.setGroupOwner(MyApplication.getInstance().getUid() + "");
                entity.setGroupId(groupid);
                entity.setGroupName(name);
                entity.setGroupAvatar(MyApplication.getInstance().getAvater());
                entity.setGroupType(group_type.getSelectedItemPosition() + "");
                entity.setGroupPermission(group_permission.getSelectedItemPosition() + "");
                entity.setGroupDeclared(declared);
                entity.setMemberInGroup("");
                GroupMemberBean bean = new GroupMemberBean();
                bean.setGroupid(groupid);
                bean.setMembers("");
                try {
                    MyApplication.dbUtils.saveOrUpdate(entity);
                    MyApplication.dbUtils.saveOrUpdate(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.putExtra("group", entity);
                setResult(999, intent);
                finish();
            }
            if (msg.what == 1) {
                Toast.makeText(CreateGroupActivity.this, "群组创建失败！",
                        Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 2) {
                Toast.makeText(CreateGroupActivity.this, "群组创建失败！",
                        Toast.LENGTH_SHORT).show();
            }

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        initView();
    }

    private void initView() {
        create = (Button) findViewById(R.id.bt_creat);
        cancel = (Button) findViewById(R.id.bt_cancel);
        create.setOnClickListener(this);
        cancel.setOnClickListener(this);
        group_name = (EditText) findViewById(R.id.group_name);
        group_declared = (EditText) findViewById(R.id.et_group_notice);
        group_type = (Spinner) findViewById(R.id.spinner_type);
        group_type.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item,
                grouptype));
        group_permission = (Spinner) findViewById(R.id.spinner_permission);
        group_permission.setAdapter(new ArrayAdapter<>(this,
                R.layout.spinner_item, grouppermission));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_creat:
                name = group_name.getText().toString().trim();
                declared = group_declared.getText().toString().trim();
                type = String.valueOf("1");
                permission = String.valueOf("0");
                createGroup();
                break;
            case R.id.bt_cancel:
                finish();
                break;
        }
    }

    private void createGroup() {
        Tool.showInfo(this, "正在创建...");
        HttpRequest.sendPost(TLUrl.URL_GET_VOIP + "group/creategroup", "uid="
                + MyApplication.getInstance().getUid() + "&avatar="
                + MyApplication.getInstance().getAvater() + "&name=" + name
                + "&type=" + type + "&permission=" + permission + "&declared="
                + declared, new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                Log.i("xbb创建群组", msg);
                try {
                    JSONObject jsonObject = new JSONObject(msg);
                    if (jsonObject.getInt("status") == 1) {
                        JSONObject object = jsonObject.getJSONObject("msg");
                        groupid = object.getString("groupId");
                        myhandler.sendEmptyMessage(0);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    Tool.removeProgressDialog();
                }
            }
        });
    }

    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, Intent
    // data) {
    // // TODO Auto-generated method stub
    // super.onActivityResult(requestCode, resultCode, data);
    // if (requestCode == 300 && resultCode == 1) {
    // String nums = data.getStringExtra("nums");
    // Log.i("xbb邀请加入群", nums);
    // if (nums != null) {
    // // doInviteMobileMember(num, mMeeting.getMeetingNo(), false);
    // String[] phones = nums.split(",");
    // for (String string : phones) {
    // voips.add(string);
    // }
    // doInvitegroupMembers(voips);
    // }
    // }
    // }
    //
    // private void doInvitegroupMembers(List<String> voips) {
    // // TODO Auto-generated method stub
    // HttpRequest.sendPost(TLUrl.getInstance().URL_GET_VOIP + "group/InviteJoinGroup",
    // "uid="
    // + MyApplication.getInstance().getUid()
    // + "&groupId="
    // + groupid
    // + "&member="
    // + voips
    // + "&confirm="
    // + 1
    // + "&declared="
    // + (MyApplication.getInstance().getOwnernickname()
    // + "邀请你加入" + name), new HttpRevMsg() {
    // @Override
    // public void revMsg(String msg) {
    // Log.i("xbb邀请加入群", msg);
    // try {
    // JSONObject jsonObject = new JSONObject(msg);
    // if (jsonObject.getInt("status") == 1) {
    // myhandler.sendEmptyMessage(2);
    // }
    // } catch (JSONException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } finally {
    // Tool.removeProgressDialog();
    // }
    // }
    // });
    // }
}
