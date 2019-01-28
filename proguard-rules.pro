# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\AndroidSDK-NDK\sdk/tools/proguard/proguard-android.txt
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

##############  Genel Tan?mlamalar


-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet);
 }

 -keepclasseswithmembers class * {
  public <init>(android.content.Context, android.util.AttributeSet, int);
 }
 -keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
 }

 -dontwarn org.kobjects.**
 -dontwarn org.ksoap2.**
 -dontwarn org.kxml2.**
 -dontwarn org.xmlpull.v1.**
 -dontwarn org.mockito.**
 -dontwarn sun.reflect.**
 -dontwarn android.test.**

 -keep class org.kobjects.** { *; }
 -keep class org.ksoap2.** { *; }
 -keep class org.kxml2.** { *; }
 -keep class org.xmlpull.** { *; }

 -dontusemixedcaseclassnames
 -dontskipnonpubliclibraryclasses
 -dontoptimize
 -dontpreverify
##################################



#####################  Tanimlanmis classs lar icin

-keepclassmembers class tr.net.rota.aeyacin.rotamobil.model.sbt.** { *; }


######################################3
################### hazir Kutuphanelericin tanimlanmis

# firebase icin
# Add this global rule
-keepattributes Signature




# splash icin
-keepclassmembers class * extends com.stephentuso.welcome.WelcomeActivity {
    public static java.lang.String welcomeKey();
}


# otto
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}


## picasso
-dontwarn com.squareup.okhttp.**

##gson
-keep class com.google.code.ksoap2-android.**
-dontwarn okio.Okio.**
-dontwarn java.nio.file.**
-dontwarn org.codehaus.**

## Loading Indicator
-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }


##fakeSearchView

-keep class com.github.leonardoxh.** { *; }
-dontwarn com.github.leonardoxh.**

#Crashlytics'i
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
###################################################################################################
