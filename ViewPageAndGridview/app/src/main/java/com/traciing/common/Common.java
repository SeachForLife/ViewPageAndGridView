package com.traciing.common;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

/**
 * Created by Carl_yang on 2016/6/12.
 */
public class Common {

    private static Application CONTENT = null;

    /**
     * 将application的context传过来
     * @param context
     */
    public static void init(Application context) {
        if (context != null && CONTENT == null)
            CONTENT = context;
    }

    public static String getIMEI() {
        return getSimNo(CONTENT);
    }

    /**
     * 获取设备IMEI，移动入网标示(手机唯一串号)
     * @param context
     * @return
     */
    private static String getSimNo(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static String getVersionName() {

        PackageManager packageManager = CONTENT.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(CONTENT.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException swallow) {
            return "";
        }
    }

}
