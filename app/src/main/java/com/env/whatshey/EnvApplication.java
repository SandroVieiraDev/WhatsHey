package com.env.whatshey;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Arrays;
import java.util.List;

public class EnvApplication extends Application {
    private final String ID_S21_ULTRA = "F4E3FF50DD47761D2F64B1E59ABACC09";

    @Override
    public void onCreate() {
        super.onCreate();
        initSDK();
    }

    private void initSDK() {
        List<String> testIds = Arrays.asList(ID_S21_ULTRA);
        RequestConfiguration configuration = new RequestConfiguration.Builder()
                .setTestDeviceIds(testIds)
                .build();
        MobileAds.setRequestConfiguration(configuration);
        MobileAds.initialize(this, initializationStatus -> {
        });
    }
}
