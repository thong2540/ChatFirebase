package com.example.chatfirebase.api;

import okhttp3.Call;
import okhttp3.Response;

public interface ApiSettingCallBack {

    void onLoading();

    void onResponse(Call call, Response response);

}
