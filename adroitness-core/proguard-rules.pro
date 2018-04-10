# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/oscarr/Development/adt-bundle-mac-x86_64/sdk/tools/proguard/proguard-android.txt
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

# Proguard obfuscates method names. However, the onEvent methods (MessageBroker) must not
# be renamed becasuse they accessed using reflection.
-keepclassmembers class ** {
    public void onEvent*(**);
}

-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepclassmembers

# Suppress warnings from gRPC dependencies
-dontwarn com.google.common.**
-dontwarn com.google.api.client.**
-dontwarn com.google.protobuf.**
-dontwarn io.grpc.**
-dontwarn okio.**
-dontwarn com.google.errorprone.annotations.**
-keep class io.grpc.internal.DnsNameResolveProvider
-keep class io.grpc.okhttp.OkHttpChannelProvider
