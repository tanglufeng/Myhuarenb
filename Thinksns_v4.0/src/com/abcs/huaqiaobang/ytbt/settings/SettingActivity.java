package com.abcs.huaqiaobang.ytbt.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.ytbt.capturePhoto.CapturePhoto;
import com.abcs.huaqiaobang.ytbt.emotion.DisplayUtils;
import com.abcs.huaqiaobang.ytbt.util.CircleImageView;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.abcs.sociax.android.R;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.http.client.entity.InputStreamUploadEntity;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private CircleImageView photo;
	private TextView name, uid;
	private ImageView changenickname;
	private ToggleButton btn_sound, btn_shake;
	private RelativeLayout version, exit, logout;
	private SharedPreferences preferences;


	
	private CapturePhoto capture;
  //  private Handler handler = new Handler();
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 1) {
				uploadavatar(msg.obj.toString());
			}if (msg.what == 2) {
				MyApplication.bitmapUtils.display(photo, MyApplication.getInstance()
						.getAvater());
				Toast.makeText(SettingActivity.this, "上传头像成功！", Toast.LENGTH_SHORT).show();
			}
		}
	};
	private byte[] bs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
	    capture = new CapturePhoto(this);
		preferences = getSharedPreferences("user", MODE_PRIVATE);
		initView();
		name.setText(MyApplication.getInstance().getOwnernickname());
		uid.setText(MyApplication.getInstance().getUid() + "");
		MyApplication.bitmapUtils.display(photo, MyApplication.getInstance()
				.getAvater());
	}

	

	private void initView() {
		changenickname=(ImageView) findViewById(R.id.changenickname);
		changenickname.setOnClickListener(this);
		photo = (CircleImageView) findViewById(R.id.avatar);
		name = (TextView) findViewById(R.id.name);
		uid = (TextView) findViewById(R.id.uid);
		photo.setOnClickListener(this);
		btn_sound = (ToggleButton) findViewById(R.id.btn_sound);
		btn_sound.setChecked(MyApplication.isSound);
		btn_shake = (ToggleButton) findViewById(R.id.btn_shake);
		btn_shake.setChecked(MyApplication.isShake);
		version = (RelativeLayout) findViewById(R.id.version);
		exit = (RelativeLayout) findViewById(R.id.exit);
		logout = (RelativeLayout) findViewById(R.id.logout);
		btn_shake.setOnClickListener(this);
		btn_sound.setOnClickListener(this);
		version.setOnClickListener(this);
		exit.setOnClickListener(this);
		logout.setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.button1).setOnClickListener(this);
		try {
			((TextView) findViewById(R.id.current_version))
					.setText(getString(R.string.str_current_version,
							Tool.getCurrentVersion(this)));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.avatar:
			selectImage();
			break;
		case R.id.btn_shake:
			btn_shake.setChecked(!MyApplication.isShake);
			MyApplication.isShake = !MyApplication.isShake;
			preferences.edit().putBoolean("isShake", MyApplication.isShake).commit();
			break;
		case R.id.btn_sound:
			btn_sound.setChecked(!MyApplication.isSound);
			MyApplication.isSound = !MyApplication.isSound;
			preferences.edit().putBoolean("isSound",MyApplication.isSound).commit();
			break;
		case R.id.exit:
			exit();
			break;
		case R.id.version:
//			checkUpdate();
			break;
		case R.id.logout:
			break;
		case R.id.back:
			finish();
			break;
		case R.id.button1:
			//startActivity(new Intent(SettingActivity.this,QR_Activity.class));
			showQRCode();
			break;
		case R.id.changenickname:
			modifyNickName(name);
			break;
		}
	}

	private void showQRCode() {
		Tool.showProgressDialog(this, "正在加载", true);
		final ImageView iv = new ImageView(this);
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(iv);
		final String filePath = getFileRoot(this) + File.separator + "qr_"
				+ MyApplication.getInstance().getUid() + ".jpg";
		String path = getFileRoot(this) + File.separator + "avator_"
				+ MyApplication.getInstance().getUid() + ".jpg";
		new HttpUtils().download(MyApplication.getInstance().getAvater(), path, new RequestCallBack<File>() {
			private Bitmap bitmap;
			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				bitmap = BitmapFactory
						.decodeFile(arg0.result.getAbsolutePath());
				creatQR(iv, builder, filePath);
			}
			private void creatQR(final ImageView iv,
					final AlertDialog.Builder builder, final String filePath) {
				new Thread(new Runnable() {
					int width =  DisplayUtils.getScreenWidthPixels(SettingActivity.this);
					@Override
					public void run() {
						boolean success = QRCodeUtil.createQRImage("{\"name\":\""+MyApplication.getInstance().getOwnernickname()+"\",\"uid\":\""
					+MyApplication.getInstance().getUid()+"\",\"avator\":\""+MyApplication.getInstance().getAvater()+"\"}",
					width/3*2, width/3*2, bitmap, filePath);
						if (success) {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									iv.setImageBitmap(BitmapFactory
											.decodeFile(filePath));
									Tool.removeProgressDialog();
									builder.show();
								}
							});
						}
					}
				}).start();
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				creatQR(iv, builder, filePath);
			}
		});
	}



	/*private void checkUpdate() {
		Tool.showProgressDialog(this, "正在检查更新...",false);
		PgyUpdateManager.register(SettingActivity.this,
				new UpdateManagerListener() {
					@Override
					public void onUpdateAvailable(final String result) {
					//	Log.i("info", "onUpdateAvailable");
						Tool.removeProgressDialog();
						final AppBean appBean = getAppBeanFromString(result);
						AlertDialog a = new AlertDialog.Builder(
								SettingActivity.this)
								.setTitle("更新")
								.setMessage("检测到新版本,是否更新?")
								.setPositiveButton("立即更新",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												startDownloadTask(
														SettingActivity.this,
														appBean.getDownloadURL());
											}
										}).setNegativeButton("暂不更新", null)
								.setCancelable(false).create();
						a.setCanceledOnTouchOutside(false);
						a.show();
					}

					@Override
					public void onNoUpdateAvailable() {
						Log.i("info", "onNoUpdateAvailable");
						Tool.removeProgressDialog();
						Toast.makeText(SettingActivity.this, "已经是最新版本",
								Toast.LENGTH_SHORT).show();
					}
				});
	}*/

	private void exit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this); // 先得到构造器
		builder.setTitle("提示"); // 设置标题
		builder.setMessage("是否退出程序"); // 设置内容
		// builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
		builder.setPositiveButton("退出", new DialogInterface.OnClickListener() { // 设置确定按钮
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss(); // 关闭dialog
						MyApplication.exit();
					}
				});
		builder.setNegativeButton("取消", null);
		// 参数都设置完成了，创建并显示出来
		builder.create().show();
	}
	 public void selectImage() {
	        final CharSequence[] items = {"拍照", "从手机相册选择", "取消"};
	        AlertDialog.Builder builder = new AlertDialog.Builder(
	                SettingActivity.this);
	        builder.setTitle("修改头像");
	        builder.setItems(items, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int item) {
	                if (items[item].equals("拍照")) {
	                    // take photo from camera
	                    capture.dispatchTakePictureIntent(CapturePhoto.SHOT_IMAGE,
	                            0);
	                } else if (items[item].equals("从手机相册选择")) {
	                    // take photo from gallery
	                    capture.dispatchTakePictureIntent(CapturePhoto.PICK_IMAGE,
	                            1);
	                } else if (items[item].equals("取消")) {
	                    // close the dialog
	                    dialog.dismiss();
	                }
	            }
	        });
	        builder.show();
	    }
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        byte[] bs = null;
	        super.onActivityResult(requestCode, resultCode, data);
	        if (resultCode == RESULT_OK) {
	            if (capture.getActionCode() == CapturePhoto.PICK_IMAGE) {
	                Uri targetUri = data.getData();
	                if (targetUri != null)
	                    bs = capture.handleMediaPhoto(targetUri);
	            } else {
	                bs = capture.handleCameraPhoto();
	            }
	            if (bs != null && bs.length > 0) {
	                uploadAvatar(bs);
	            } else {
	         
	            	Toast.makeText(SettingActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
	            }
	        }

	    }
	 private void uploadAvatar(final byte[] bs) {
	   
			this.bs = bs;
			Tool.showProgressDialog(SettingActivity.this, "正在上传...",false);
	        HttpRequest.sendGet(TLUrl.URL_getAvatar, "", new HttpRevMsg() {

				@Override
				public void revMsg(String msg) {
					Log.i("xbb上传头像", msg);
					if (msg.length() == 0) {
//	                 
						Tool.removeProgressDialog();
						Toast.makeText(SettingActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
						return;
					}
					if (msg.equals("error")) {
//	                  
						Tool.removeProgressDialog();

						Toast.makeText(SettingActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
					} else {
						Message message = handler.obtainMessage(1, msg);
						handler.sendMessage(message);

					}
				}
			});
	    }
	 
	 
	 protected void uploadavatar(String msg) {
			// TODO Auto-generated method stub
		 JSONObject obj = JSONObject.parseObject(msg);
         final String putURL = obj.getString("puturl");
         final String avatar = obj.getString("objname");
        
         final HttpUtils hu = new HttpUtils(10000);
         final RequestParams params = new RequestParams();
         InputStream fStream = new ByteArrayInputStream(bs);
         hu.configTimeout(20000);
         params.addQueryStringParameter("Content-Length", bs.length
                 + "");
         params.setBodyEntity(new InputStreamUploadEntity(fStream,
                 bs.length));
         params.setContentType("image/jpeg");
         hu.configResponseTextCharset("utf-8");
         hu.configRequestRetryCount(5);
         handler.post(new Runnable() {
             @Override
             public void run() {
                 hu.send(HttpMethod.PUT, putURL, params,
                         new RequestCallBack<String>() {

                             @Override
                             public void onFailure(
                                     HttpException arg0, String arg1) {
//                            
                            	 
                             	Tool.removeProgressDialog();
         	                	handler.sendEmptyMessage(2);
                             }

                             @Override
                             public void onSuccess(
                                     ResponseInfo<String> arg0) {
                                 String postUrl = TLUrl.URL_user
                                         + "changeavatar?iou=1";
                                 String params = "token="
                                         + MyApplication.getInstance().getToken() + "&avatar="
                                         + TLUrl.URL_avatar + avatar;
                                 HttpRequest.sendPost(postUrl,
                                         params, new HttpRevMsg() {

                                             @Override
                                             public void revMsg(String msg) {
//                                              
                                             	Tool.removeProgressDialog();
                                                 if (msg.length() == 0) {
                                                		handler.sendEmptyMessage(2);
                                                 } else {
                                                     JSONObject obj = JSONObject
                                                             .parseObject(msg);
                                                     int code = obj
                                                             .getIntValue("code");
                                                     if (code == 1) {
                                                        MyApplication.getInstance().setAvater(TLUrl.URL_avatar
                                                                                    + avatar);
                                                    		handler.sendEmptyMessage(2);
//                                                      
                                                     } else {
                                                    		handler.sendEmptyMessage(3);
                                                     }
                                                 }
											 }
                                         });

                             }
                         });
             }
         });
		}
	 private void modifyNickName(final TextView name) {
		 	LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.nickname_edittext, null);
	        final EditText inputNickName = (EditText) layout.findViewById(R.id.editText1);
	        inputNickName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("修改昵称").setIcon(android.R.drawable.ic_dialog_info)
	                .setView(layout).setPositiveButton("取消", null);
	        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int which) {
	                final String newNickName = inputNickName.getText().toString()
	                        .trim();
	                if (newNickName.length() == 0) {
	               
	                	Toast.makeText(SettingActivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
	                    return;
	                }
	           //     ProgressDlgUtil.showProgressDlg("修改中", activity);
	                String geturl = TLUrl.URL_user + "changenickname?iou=1";
	                String param = "token=" + MyApplication.getInstance().getToken() + "&nickname="
	                        + newNickName;
	                HttpRequest.sendPost(geturl, param, new HttpRevMsg() {
	                    @Override
	                    public void revMsg(final String msg) {
	                      //  ProgressDlgUtil.stopProgressDlg();
	                        handler.post(new Runnable() {

	                            @Override
	                            public void run() {
	                                // TODO Auto-generated method stub
	                                try {
	                                    org.json.JSONObject object = new org.json.JSONObject(
	                                            msg);
	                                    if (object.getInt("code") == 1) {
	                                    	Toast.makeText(SettingActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
	                                        name.setText(newNickName);
	                                        MyApplication.getInstance().setOwnernickname(newNickName);
//	                                        MyApplication.getInstance().self
//	                                                .setNickName(newNickName);
//	                                        if (MyApplication.getInstance()
//	                                                .getMainActivity().main != null) {
//	                                            MyApplication.getInstance()
//	                                                    .getMainActivity().main
//	                                                    .modifyNickName();
	                                      //  }
	                                    } else {
	                                    	Toast.makeText(SettingActivity.this, "修改失败！！", Toast.LENGTH_SHORT).show();
	                                    }
	                                } catch (JSONException e) {
	                                    // TODO Auto-generated catch block
	                                    e.printStackTrace();
	                              //      activity.showToast("修改失败");
	                                }

	                            }
	                        });
						}
	                });

	            }
	        });
	        builder.show();
	    }
}
