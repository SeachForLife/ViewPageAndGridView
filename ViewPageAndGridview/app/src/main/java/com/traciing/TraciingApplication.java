package com.traciing;


import android.app.Application;

import com.traciing.common.Common;

/**
 * Created by Carl_yang on 2016/6/12.
 */
public class TraciingApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Common.init(this);
    }
}
