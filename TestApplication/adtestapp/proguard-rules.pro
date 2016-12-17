# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Java\studio-sdk\sdk/tools/proguard/proguard-android.txt
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

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
-keep class com.wandoujia.ads.sdk.** { *; }
-dontwarn com.wandoujia.ads.sdk.**
-keepattributes Signature
-dontwarn android.support.**
-dontwarn com.squareup.**
-dontwarn okio.**
-keep class com.wandoujia.ads.sdk.** { *; }


#The below is used for AdsMOGO_Android_SDK_1.6.5 settings
-dontwarn
#-libraryjars /libs/AdsMOGO_Android_SDK_1.6.5.jar//Eclipse中才需要这一行，Android Studio中不需要
-keep public class com.adsmogo.** {*;}

#inMobi
-keepattributes SourceFile,LineNumberTable,InnerClasses
-keep class com.inmobi.** { *; }
-dontwarn com.inmobi.**
-dontwarn com.google.android.gms**
-keepclasseswithmembernames class * {                                           # 保持 native 方法不被混淆
    native <methods>;
}

#广点通
-keep class com.qq.e.** {
    public protected *;
}
-keep class android.support.v4.app.NotificationCompat**{
    public *;
}

#百度
-keep class com.baidu.** {
public protected *;
}
## for smartmad ad sdk
#-keeppackagenames  cn.smartmad.ads.android
#-keep class  cn.smartmad.ads.android.* {*;}
#
## for wqmobile ad sdk
#-keeppackagenames com.wqmobile
#-keep class   com.wqmobile.** {*;}


-optimizationpasses 5                                                           # 指定代码的压缩级别
-dontusemixedcaseclassnames                                                     # 是否使用大小写混合
-dontskipnonpubliclibraryclasses                                                # 是否混淆第三方jar
-dontpreverify                                                                  # 混淆时是否做预校验
-verbose                                                                        # 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*        # 混淆时所采用的算法

-keep public class * extends android.app.Activity                               # 保持哪些类不被混淆
-keep public class * extends android.app.Application                            # 保持哪些类不被混淆
-keep public class * extends android.app.Service                                # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver                  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider                    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper               # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference                      # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService              # 保持哪些类不被混淆

-keep class com.google.android.gms.common.api.GoogleApiClient { public *; }
-keep class com.google.android.gms.common.api.GoogleApiClient$* {public *;}
-keep class com.google.android.gms.location.LocationServices {public *;}
-keep class com.google.android.gms.location.FusedLocationProviderApi {public *;}
-keep class com.google.android.gms.location.ActivityRecognition {public *;}
-keep class com.google.android.gms.location.ActivityRecognitionApi {public *;}
-keep class com.google.android.gms.location.ActivityRecognitionResult {public *;}
-keep class com.google.android.gms.location.DetectedActivity {public *;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{public *;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{public *;}

#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);     # 保持自定义控件类不被混淆
#}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {                        # 保持自定义控件类不被混淆
   public void *(android.view.View);
}

-keepclassmembers enum * {                                                      # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {                                # 保持 Parcelable 不被混淆
  public static final android.os.Parcelable$Creator *;
}

                                                              # 保持自己定义的类不被混淆
