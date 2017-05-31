package com.abcs.huaqiaobang.wxapi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.EnterBindSecurityDialog;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnePersonal {
    private PersonalActivity activity;
    private View v;
    private View v1;
    private Handler handler = new Handler();

    public View getView() {
        return v;
    }

    public OnePersonal(PersonalActivity activity, String name, String info,
                       int type) {
        this.activity = activity;
        if (type != 0) {
            this.v = View.inflate(activity, R.layout.occft_item_personal, null);
            if (type == 6 || type == 7) {
                v.setVisibility(View.GONE);
            }
            v1 = (TextView) v.findViewById(R.id.tljr_item_personal_info);
            ((TextView) v1).setText(info);
            if (MyApplication.getInstance().self == null) {
                activity.finish();
            }
//            Log.i("tga", "Onpersonal==" + MyApplication.getInstance().self.isInvail());
            if (type == 6 && MyApplication.getInstance().self != null
                    && MyApplication.getInstance().self.getEmail() != null) {
                if (!MyApplication.getInstance().self.isInvail()) {
                    ((TextView) v1).setText(info + "(未验证)");
                } else {
                    ((TextView) v1).setText(info + "(已验证)");
                }
            }
            if (type == 7 && MyApplication.getInstance().self != null
                    && MyApplication.getInstance().self.getPhone() != null) {
                if (!MyApplication.getInstance().self.isInvailPhone()) {
                    ((TextView) v1).setText(info + "(未验证)");
                } else {
                    ((TextView) v1).setText(info + "(已验证)");
                }
            }
        } else {
            this.v = View
                    .inflate(activity, R.layout.occft_item_personal1, null);
            v1 = (ImageView) v.findViewById(R.id.tljr_item_personal_info);
            v1.getLayoutParams().width = 120 * Util.HEIGHT / Util.IMAGEHEIGTH;
            v1.getLayoutParams().height = 120 * Util.HEIGHT / Util.IMAGEHEIGTH;
            Util.setRoundImage(info, (ImageView) v1, handler);
            MyApplication.imageLoader.displayImage(info, (ImageView) v1,
                    Options.getCircleListOptions());
        }
        ((TextView) v.findViewById(R.id.tljr_item_personal_name)).setText(name);
        OnClickListener listener = getOnClickListener(type);
        if (Util.isThirdLogin) {

            if (type != 6 && type != 7) {
                listener = null;
            }

        }

        if (listener != null) {
            v.setOnClickListener(listener);
        } else {
            v.findViewById(R.id.tljr_item_personal_arrow).setVisibility(
                    View.INVISIBLE);
        }
    }

    private OnClickListener getOnClickListener(int i) {
        switch (i) {
            case 0:
                return new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        activity.selectImage();
                    }
                };
            case 1:
                return null;
            case 2:
                return new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        modifyNickName((TextView) v1);
                        MobclickAgent.onEvent(activity, "modifyNickName");
                    }
                };
            case 3:

                return null;
            case 4:
                return null;
            case 5:
                return null;

            case 6:
                return new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        bindEmail((TextView) v1);
                    }
                };
            case 7:
                return new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        bindPhone((TextView) v1);
                    }
                };
            case 8:
                return new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        modifyPwd();
                    }
                };
            default:
                return null;
        }
    }

    private void bindEmail(final TextView tag) {
        MobclickAgent.onEvent(activity, "bindEmail");
        final EditText inputEmail = new EditText(activity);
        inputEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//        inputEmail.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});

        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("输入您的邮箱").setIcon(android.R.drawable.ic_dialog_info)
                .setView(inputEmail)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        setDialogDis(dialog, true);
                    }
                });
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                setDialogDis(dialog, false);
                final String newMail = inputEmail.getText().toString().trim();
                if (newMail.length() == 0) {
                    activity.showToast("邮箱不能为空");
                    return;
                }
                if (!isValidEmail(newMail)) {
                    activity.showToast("请输入正确的邮箱地址");
                    return;
                }

                String geturl = TLUrl.getInstance().URL_user + "bind?iou=1";
                String param = "token=" + Util.token + "&type=email"
                        + "&value=" + newMail;
                setDialogDis(dialog, true);

                ProgressDlgUtil.showProgressDlg("修改中", activity);
                HttpRequest.sendPost(geturl, param, new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        ProgressDlgUtil.stopProgressDlg();

                        handler.post(new Runnable() {

                            @Override
                            public void run() {

                                if (msg.length() > 0) {
                                    JSONObject obj = JSONObject.parseObject(msg);
                                    int code = obj.getIntValue("code");

                                    if (code == 1) {


                                        activity.showToast("绑定成功,已发验证邮件到您的邮箱,请到您的邮箱完成验证");
                                        tag.setText(newMail + "(未验证)");

                                    } else {

                                        if (code == -1015) {

                                            activity.showToast("此邮箱已绑定其它帐号");


                                        } else {

                                            activity.showToast("绑定失败");
                                        }

                                    }
                                } else

                                {
                                    activity.showToast("修改失败");
                                }

                            }
                        });
                    }
                });

            }
        });
        builder.show();
    }

    private void bindPhone(final TextView tag) {
        MobclickAgent.onEvent(activity, "bindPhone");
        final EditText inputPhone = new EditText(activity);
        inputPhone.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);
        inputPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("输入手机号,绑定重新登录生效").setView(inputPhone)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        setDialogDis(dialog, true);
                    }
                });
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                setDialogDis(dialog, false);
                final String newPhone = inputPhone.getText().toString().trim();
                if (newPhone.length() == 0) {
                    activity.showToast("邮箱不能为空");
                    return;
                }
                if (!isValidMobile(newPhone)) {
                    activity.showToast("请输入正确的手机号码");
                    return;
                }
                String geturl = TLUrl.getInstance().URL_user + "bind?iou=1";
                String param = "token=" + Util.token + "&type=phone"
                        + "&value=" + newPhone;
                setDialogDis(dialog, true);
                ProgressDlgUtil.showProgressDlg("修改中", activity);
                System.err.println("param:" + param);
                HttpRequest.sendPost(geturl, param, new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        ProgressDlgUtil.stopProgressDlg();
                        System.err.println("msg:" + msg);
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (msg.length() > 0) {
                                    JSONObject obj = JSONObject
                                            .parseObject(msg);
                                    try {
                                        int code = obj.getIntValue("code");
                                        if (code == 1) {
                                            new EnterBindSecurityDialog(
                                                    activity, newPhone,
                                                    obj.getString("result"))
                                                    .show();
                                        } else {
                                            if (code == -1016) {
                                                activity.showToast("此手机已绑定其它帐号");
                                            } else if (code == -1024) {
                                                activity.showToast("此类绑定暂不支持，请重试!");
                                            } else {
                                                activity.showToast("绑定失败");
                                            }
                                        }
                                    } catch (Exception e) {
                                        activity.showToast(obj
                                                .getString("code"));
                                    }
                                } else {
                                    activity.showToast("绑定失败");
                                }
                            }
                        });
                    }
                });

            }
        });
        builder.show();
    }

    private boolean isValidEmail(String mail) {
        Pattern pattern = Pattern
                .compile("^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})");
        Matcher mc = pattern.matcher(mail);
        return mc.matches();
    }

    private boolean isValidMobile(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }

    private void setDialogDis(DialogInterface dialog, boolean isDimiss) {
        try {// 下面三句控制弹框的关闭

            Field field = dialog.getClass().getSuperclass()
                    .getDeclaredField("mShowing");

            field.setAccessible(true);

            field.set(dialog, isDimiss);// true表示要关闭

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private void modifyNickName(final TextView name) {
        final EditText inputNickName = new EditText(activity);
        inputNickName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("输入您的新昵称").setIcon(android.R.drawable.ic_dialog_info)
                .setView(inputNickName).setPositiveButton("取消", null);
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                final String newNickName = inputNickName.getText().toString()
                        .trim();
                if (newNickName.length() == 0) {
                    activity.showToast("请输入昵称");
                    return;
                }
                ProgressDlgUtil.showProgressDlg("修改中", activity);
                String geturl = TLUrl.getInstance().URL_user + "changenickname?iou=1";
                String param = "token=" + Util.token + "&nickname="
                        + newNickName;
                HttpRequest.sendPost(geturl, param, new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        ProgressDlgUtil.stopProgressDlg();
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                try {
                                    org.json.JSONObject object = new org.json.JSONObject(
                                            msg);
                                    if (object.getInt("code") == 1) {
                                        activity.showToast("修改成功");
                                        name.setText(newNickName);
                                        MyApplication.getInstance().self
                                                .setNickName(newNickName);
                                        if (MyApplication.getInstance()
                                                .getMainActivity().main != null) {
//                                            MyApplication.getInstance()
//                                                    .getMainActivity().main
//                                                    .modifyNickName();
                                        }
                                    } else {
                                        activity.showToast("修改失败");
                                    }
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                    activity.showToast("修改失败");
                                }

                            }
                        });
                    }
                });

            }
        });
        builder.show();
    }

    private void modifyPwd() {
        final View view = activity.getLayoutInflater().inflate(
                R.layout.occft_dialog_modiypwd, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("修改密码").setIcon(android.R.drawable.ic_dialog_info)
                .setView(view)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        setDialogDis(dialog, true);
                    }
                });
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(final DialogInterface dialog, int which) {

                setDialogDis(dialog, false);
                EditText et_pwd = (EditText) view
                        .findViewById(R.id.tljr_mdy_pwd);
                EditText et_n_pwd = (EditText) view
                        .findViewById(R.id.tljr_mdy_n_pwd);
                EditText et_n_rpwd = (EditText) view
                        .findViewById(R.id.tljr_mdy_n_rpwd);
                String str_pwd = et_pwd.getText().toString().trim();
                String str_n_pwd = et_n_pwd.getText().toString().trim();
                String str_n_rpwd = et_n_rpwd.getText().toString().trim();

                if (str_pwd.length() == 0) {
                    activity.showToast("请输入密码");
                    return;
                }

                if (str_n_pwd.length() == 0) {
                    activity.showToast("请输入新密码");
                    return;
                }
                if (str_n_rpwd.length() == 0) {
                    activity.showToast("请输入确认新密码");
                    return;
                }
                if (!str_n_pwd.equals(str_n_rpwd)) {
                    activity.showToast("两次密码不一致");
                    return;
                }
                if (str_n_pwd.length() < 6) {
                    activity.showToast("密码长度不能少于6");
                    return;
                }
                MobclickAgent.onEvent(activity, "modifyPwd");
                String postUrl = TLUrl.getInstance().URL_user + "changepwd?iou=1";
                String param = "uname="
                        + MyApplication.getInstance().self.getUserName()
                        + "&pwd=" + str_pwd + "&changepwd=" + str_n_pwd;
                setDialogDis(dialog, true);
                ProgressDlgUtil.showProgressDlg("修改中", activity);
                HttpRequest.sendPost(postUrl, param, new HttpRevMsg() {
                    @Override
                    public void revMsg(String msg) {
                        ProgressDlgUtil.stopProgressDlg();
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        });
                        if (msg.length() == 0) {
                            activity.showToast("修改密码失败");
                            return;
                        }
                        JSONObject obj = JSONObject.parseObject(msg);
                        int code = obj.getIntValue("code");
                        if (code == 1) {
                            activity.showToast("修改密码成功");
                        } else {
                            activity.showToast("修改密码失败");
                        }

                    }
                });

            }
        });
        builder.show();
    }
}
