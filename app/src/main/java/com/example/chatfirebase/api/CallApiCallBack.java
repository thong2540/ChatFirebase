package com.example.chatfirebase.api;

import okhttp3.Call;

public interface CallApiCallBack {

    void onLoading();

    void onResponse(Call call, String response);
}
