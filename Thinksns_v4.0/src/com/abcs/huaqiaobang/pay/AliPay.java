package com.abcs.huaqiaobang.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.huaqiaobang.MyApplication;
import com.alipay.sdk.app.PayTask;

/**
 * @author xbw
 * @version 创建时间：2015年12月2日 上午10:10:18
 */
public class AliPay {
	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	private static AliPay instance;
	private Activity context;
	private PaySuccessListener listener;

	public static AliPay getInstance() {
		if (instance == null)
			instance = new AliPay();
		return instance;
	}

	public void init(Activity context) {
		this.context = context;
	}

	/**
	 * call alipay sdk pay. 调用SDK支付 payInfo 完整的符合支付宝参数规范的订单信息
	 * 
	 */
	public void pay(final String payInfo) {
		try {
			Runnable payRunnable = new Runnable() {

				@Override
				public void run() {
					// 构造PayTask 对象
					PayTask alipay = new PayTask(context);
					// 调用支付接口，获取支付结果
					String result = alipay.pay(payInfo);

					Message msg = new Message();
					msg.what = SDK_PAY_FLAG;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			};
			// 必须异步调用
			Thread payThread = new Thread(payRunnable);
			payThread.start();

		}catch (NullPointerException e){
			e.printStackTrace();
			Toast.makeText(context, "请先安装支付宝！", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check() {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(context);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				Log.i("tga", msg.obj.toString());

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();

					//发送YYG购买成功
					MyUpdateUI.sendUpdateCollection(context,MyUpdateUI.YYGBUY);
					MyApplication.getInstance().self
							.setTotalAssets(MyApplication.getInstance().self
									.getTotalAssets() +

							MyApplication.getInstance().turnIn_money*100);
					if(listener!=null){
						listener.paySuccess(resultStatus);
					}

				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT)
								.show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT)
								.show();
					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(context, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT)
						.show();
				break;
			}
			default:
				break;
			}
		};
	};


	public void setPaySuccessListener(PaySuccessListener  listener){

		this.listener=listener;

	}

	public  interface PaySuccessListener{

		public void paySuccess(String resultStatus);

	}
}
