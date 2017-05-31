# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in e:\Users\Administrator\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify

#表示不进行优化，建议使用此选项，因为根据proguard-android-optimize.txt中的描述，优化可能会造成一些潜在风险，不能保证在所有版本的Dalvik上都正常运行。
-dontoptimize
-verbose
-ignorewarnings
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-allowaccessmodification
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-repackageclasses ''

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.app.Fragment
-dontnote com.android.vending.licensing.ILicensingService

## Explicitly preserve all serialization members. The Serializable interface
## is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#表示不混淆任何一个View中的setXxx()和getXxx()方法，因为属性动画需要有相应的setter和getter的方法实现，混淆了就无法工作了。
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
#表示不混淆Activity中参数是View的方法，因为有这样一种用法，在XML中配置android:onClick=”buttonClick”属性，当用户点击该按钮时就会调用Activity中的buttonClick(View view)方法，如果这个方法被混淆的话就找不到了。
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keep class org.litepal.** {
    *;
}

-keep class android.support.** {
    *;
}


## Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

## Preserve static fields of inner classes of R classes that might be accessed
## through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}

## Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class * {
    public protected *;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
##---------------End: proguard configuration common for all Android apps ----------



##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
#-keepattributes Signature

# For using GSON @Expose annotation
#-keepattributes *Annotation*

# Gson specific classes
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
#-keep class com.google.gson.examples.android.model.** { *; }
##---------------End: proguard configuration for Gson  ----------


##---------------Begin: proguard configuration for Apache Http  ----------
-dontwarn org.apache.http.**
-keep class org.apache.http.**{*;}
-keep interface org.apache.http.**{*;}
##---------------End: proguard configuration for Apache Http  ----------

-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}


##---------------Begin: proguard configuration for EventBus  ----------
-keepclassmembers class ** {
    public void onEvent*(**);
}
##---------------End: proguard configuration for UMENG ----------
-dontwarn com.umeng.**
-keep class com.umeng.**{*;}
-keep interface com.umeng.**{*;}
-keep public class * extends com.umeng.**
-keep public class com.huashengrun.android.rourou.R$*{
    public static final int *;
}
-keep public class com.umeng.fb.ui.ThreadView { }
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class com.squareup.wire.* {
	public <fields>;
    public <methods>;
}

#-keep class org.android.agoo.impl.*{
#	public <fields>;
#    public <methods>;
#}

#-keep class org.android.agoo.service.* {*;}

#-keep class org.android.spdy.**{*;}
##---------------End: proguard configuration for Umeng  ----------

##---------------Begin: proguard configuration for support-v4  ----------
-dontwarn android.support.v4.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
##---------------End: proguard configuration for support-v4  ----------

#-keep class * extends java.lang.annotation.Annotation{ *; }





-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**

-keep class com.sina.** { *; }
-keep class com.yuntongxun.** { *; }

#支付宝混淆
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}


#=========华人圈==========
-keep class com.google.zxing.**


-dontwarn android.support.v4.**
-keep public class * extends android.app.Fragment

-keep public class com.zhishisoft.sociax.android.R$*{
 public static final int *;
}

-keep public class com.tencent.weibo.sdk.android.component.R$*{
 public static final int *;
}



-dontwarn com.tencent.weibo.sdk.android.component.**
-dontwarn org.apache.commons.httpclient.**
-dontwarn android.net.http.**


-keep class com.baidu.android.pushservice.** {*;}
-keep class com.baidu.android.common.** {*;}
-keep class com.baidu.location.** {*;}
-keep class com.baidu.mapapi.** {*;}
-keep class com.tencent.weibo.sdk.android.** {*;}
-keep class com.tencent.weibo.sdk.android.component.** {*;}
-keep class com.tencent.weibo.sdk.android.api.** {*;}
-keep class com.tencent.tauth.** {*;}
-keep class org.apache.commons.httpclient.** {*;}
-keep class org.apache.commons.httpclient.methods.** {*;}
-keep class org.apache.http.entity.mime.** {*;}
-keep class android.support.v4.** {*;}
-keep class android.net.http.** {*;}
-keep class com.weibo.sdk.android.** {*;}
-keep class com.sina.sso.** {*;}

#EventBus混淆
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#极光推送
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

#高德地图
-dontwarn com.amap.api.**
-dontwarn com.a.a.**
-dontwarn com.autonavi.**
-keep class com.amap.api.**  {*;}
-keep class com.autonavi.**  {*;}
-keep class com.a.a.**  {*;}

#视频录制
-keep class com.yixia.** { *; }
#百度地图
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**