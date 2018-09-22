# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# 消除Picasso相关错误
-dontwarn com.squareup.picasso.**
-keep class com.squareup.picasso.**{*;}

# EZOpenSDK.jar包
#-dontwarn  com.ezviz.push.sdk.**
-keep class com.ezviz.push.sdk.**{*;}
-keep class com.githang.android.apnbb.**{*;}
-keep class com.hik.CASClient.**{*;}
-keep class com.hik.ppvclient.**{*;}
-keep class com.hik.RtspClient.**{*;}
-keep class com.hik.streamclient.**{*;}
-keep class com.hik.streamconvert.**{*;}
-keep class com.hik.stunclient.**{*;}
-keep class com.hik.TTSClient.**{*;}
-dontwarn com.hikvision.wifi.**
-keep class com.hikvision.wifi.**{*;}
-keep class com.hikvision.audio.**{*;}
-keep class com.hikvision.keyprotect.**{*;}
-keep class com.hikvision.netsdk.**{*;}
-keep class com.hikvision.sadp.**{*;}
-dontwarn com.videogo.**
-keep class com.videogo.**{*;}
-dontwarn com.ezviz.**
-keep class com.ezviz.**{*;}
-keep class com.tencent.mars.**{*;}
-keep class javax.validation.**{*;}

# org目录也不混淆
-dontwarn org.androidpn.client.**
-keep class org.androidpn.client.**{*;}
-keep class org.eclipse.paho.client.mqttv3.**{*;}
-keep class org.MediaPlayer.PlayM4.**{*;}

# httpmime/httpcore
-dontwarn org.apache.http.**
-keep class org.apache.http.** {*;}
-dontwarn org.apache.http.entity.mime.**
-keep class org.apache.http.entity.mime.**{*;}

# 萤石jar包中wifi配置相关的JmDNS包*
-dontwarn javax.jmdns.**
-keep class javax.jmdns.** { *; }

# gcm.jar包
-dontwarn com.google.android.gcm.**
-keep class com.google.android.gcm.**{*;}

# 即log4j相关，找不到javax相关的错误,及d57a43b9-13ee-3ba5-a473-1148b0d468.jar
-dontwarn javax.management.**
-dontwarn javax.naming.**
-dontwarn java.lang.management.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-keep class javax.** { *; }
-keep class org.** { *; }
#-keep class org.apache.log4j.** { *; }

# bcprov-jdk15on-1.47.jar包错误
-dontwarn org.bouncycastle.**
-keep class org.bouncycastle.**{*;}

# jpush jar包
-dontwarn org.jpush.**
-keep class org.jpush.**{*;}

# broadcastreceiver报警接收的相关类
-dontwarn com.hikvision.mobile.bean.**
-keep class com.hikvision.mobile.bean.**{*;}

# 继承android四大组件相关类不进行混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.app.ActivityManager
-keep public class * extends android.app.Dialog
-keep public class * extends android.app.Notification

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {                                               # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);     # 保持自定义控件类不被混淆
}
-keepclassmembers class * extends android.app.Activity {                        # 保持自定义控件类不被混淆
   public void *(android.view.View);
}

# gson-2.2.4.jar包  Signature避免混淆泛型
-dontwarn com.google.gson.**
-keep class com.google.gson.**{*;}
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }

# json-20151123.jar包
-dontwarn org.json.**
-keep class org.json.**{*;}


# v4包中相关类不进行混淆     在sdk目录中的proguard-android.txt已有语句   -dontwarn android.support.**
# ，所以不需要再填下面的语句
-dontwarn android.support.v4.**
-dontwarn android.support.v4.view.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment



# 消除Logcat的输出日志
-assumenosideeffects class android.util.Log{
	public static boolean isLoggable(java.lang.String, int);
    public static *** e(...);
    public static *** w(...);
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}