package bong.android.fcmtest;


import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by Polarium on 2017-10-20.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
