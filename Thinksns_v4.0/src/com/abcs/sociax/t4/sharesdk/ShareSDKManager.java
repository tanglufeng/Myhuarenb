package com.abcs.sociax.t4.sharesdk;

import android.text.TextUtils;
import android.util.Log;

import com.abcs.sociax.t4.android.Thinksns;
import com.abcs.sociax.t4.model.ModelUser;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * ShareSDK管理
 */
public class ShareSDKManager {
    private static final String TAG = ShareSDKManager.class.getSimpleName();
    private static String uid;
    private static boolean isOk;       //是否绑定别名成功

    private static TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            Log.i("zjz", "jpush_code=" + code);
            String logs = "";
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i("zjz", logs);
                    isOk = true;
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.e(TAG, logs);
                    Log.i("zjz", logs);
                    startRegister();
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.i("zjz", logs);
                    Log.e(TAG, logs);
            }

        }
    };

    //绑定极光推送
    public static void register() {
        ModelUser user = Thinksns.getMy();
        if (user != null) {
            ShareSDKManager.uid = String.valueOf(user.getUid());
            startRegister();
        }
    }

    private static void startRegister() {
        Log.i("zjz", " String.valueOf(uid)=" + String.valueOf(uid));
        if (TextUtils.isEmpty(uid))
            uid = "";
        Log.i("zjz", "jpush_isStop=" + JPushInterface.isPushStopped(Thinksns.getContext()));
        if (JPushInterface.isPushStopped(Thinksns.getContext())) {
            JPushInterface.resumePush(Thinksns.getContext());
        }
        JPushInterface.setAliasAndTags(Thinksns.getContext(), String.valueOf(uid), null,
                mAliasCallback);
    }

    //取消绑定极光推送
    public static void unregister() {
        if (isOk) {
            if (JPushInterface.isPushStopped(Thinksns.getContext()) == false) {
                JPushInterface.stopPush(Thinksns.getContext());
            }
        } else {
            ShareSDKManager.uid = null;
            startRegister();
        }
    }

}
