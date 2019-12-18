package com.example.chatfirebase.ui.listener;

import android.view.View;
import android.widget.TextView;

public interface LastMessageCallback {
    void onLastMessageCallback(String userid, TextView last_msg);
}
