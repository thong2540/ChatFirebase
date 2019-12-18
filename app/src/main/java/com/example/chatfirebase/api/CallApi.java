package com.example.chatfirebase.api;

import com.example.chatfirebase.api.Notifications.Data;
import com.example.chatfirebase.constant.ConfigAuthorization;
import com.example.chatfirebase.constant.ConfigURL;
import com.example.chatfirebase.constant.MediaType;
import com.example.chatfirebase.constant.MethodRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Response;

public class CallApi {

    private static CallApi instance;
    private CallApiCallBack mCallApiCallBack;
    private int time = 10;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    public static CallApi getInstance() {
        if (instance == null) {
            instance = new CallApi();
        }
        return instance;
    }

    public CallApi setCallApiCallBack(CallApiCallBack callApiCallBack) {
        mCallApiCallBack = callApiCallBack;
        return this;
    }

    public void postNotifacation(Data data, String token) {

        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Authorization", "key=" + ConfigAuthorization.authorization);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("to", token);
            JSONObject jsonData = new JSONObject();
            jsonData.put("user", data.getUser());
            jsonData.put("icon", data.getIcon());
            jsonData.put("title", data.getTitle());
            jsonData.put("body", data.getBody());

            jsonBody.put("data", jsonData);

            System.out.println("TEST" + jsonBody.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiSetting.getInstance()
                .setUrl(ConfigURL.URL + "fcm/send")
                .setTimeout(time, timeUnit)
                .setBody(MediaType.JSON, jsonBody.toString())
                .setHeader(header)
                .setMethod(MethodRequest.post)
                .setOnApiSettingCallBack(new ApiSettingCallBack() {
                    @Override
                    public void onLoading() {
                        mCallApiCallBack.onLoading();
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        try {
                            if (response != null) {
                                String mResponse = response.body().string();
                                System.out.println(mResponse);
                                mCallApiCallBack.onResponse(call, mResponse);
                            } else {
                                mCallApiCallBack.onResponse(null, null);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            mCallApiCallBack.onResponse(null, null);
                        }
                    }
                }).clientCall();
    }

}