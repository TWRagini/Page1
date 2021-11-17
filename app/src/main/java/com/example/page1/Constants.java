package com.example.page1;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

public class Constants {
    public static String BASE_URL = "https://twtech.in/";
    public static String androidVersionName;
    public static int versionCode;
    public static String appVersion;



    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static String getPackageInfo(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            androidVersionName = Build.MODEL;
            Log.e("Android Version", String.valueOf(Build.VERSION.SDK_INT));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
         return androidVersionName;
    }

    public static int getVersionCode(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = BuildConfig.VERSION_CODE;
            Log.e("versionCode", String.valueOf(versionCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getAppVersion(Context context) {
        PackageInfo pInfo = null;
        try {
            appVersion = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
            Log.e("appVersion", String.valueOf(appVersion));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }


}
