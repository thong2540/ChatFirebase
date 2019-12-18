package com.example.chatfirebase;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ChatApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/SukhumvitSet-Text.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
