package com.example.chatfirebase.api;

import android.support.annotation.NonNull;


import com.bumptech.glide.BuildConfig;
import com.example.chatfirebase.constant.MethodRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.chatfirebase.constant.MediaType.MEDIA_TYPE_PNG;


public class ApiSetting {

    private static ApiSetting instance;
    private static OkHttpClient.Builder client;
    private static Request.Builder request;
    private String mUrl;
    private String mSBody;
    private ApiSettingCallBack mApiSettingCallBack;
    private RequestBody mBody;
    private MultipartBody requestBody;

    public ApiSetting setOnApiSettingCallBack(ApiSettingCallBack apiSettingCallBack) {
        mApiSettingCallBack = apiSettingCallBack;
        return this;
    }

    public static ApiSetting getInstance() {
        if (instance == null) {
            instance = new ApiSetting();
            client = new OkHttpClient.Builder();
//            client.addInterceptor(new LoggingInterceptor());
        }
        request = new Request.Builder();
        return instance;
    }

    public ApiSetting setTimeout(long time, @NonNull TimeUnit timeUnit) {
        client.readTimeout(time, timeUnit);
        client.callTimeout(time, timeUnit);
        client.connectTimeout(time, timeUnit);
        client.writeTimeout(time, timeUnit);
        return this;
    }

    public ApiSetting setUrl(String url) {
        mUrl = url;
        request.url(url);
        return this;
    }

    public ApiSetting setBody(MediaType mediaType, String content) {
        mSBody = content;
        mBody = RequestBody.create(mediaType, content);
        return this;
    }

    public ApiSetting setMultiPartBody(ArrayList<String> stringArrayList) {
        MultipartBody.Builder buildernew = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (int i = 0; i < stringArrayList.size(); i++) {
            File f = new File(stringArrayList.get(i));
            if (f.exists()) {
                buildernew.addFormDataPart("files", getFilename(stringArrayList.get(i)), RequestBody.create(MEDIA_TYPE_PNG, f));
            }
        }
        requestBody = buildernew.build();
        return this;
    }

    public ApiSetting setHeader(Map<String, String> map) {
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    public ApiSetting setMethodMultiPart(int caseRequest) {
        switch (caseRequest) {
            case MethodRequest.post://post
                request.post(requestBody);
                break;
            case MethodRequest.get://get
                request.get();
                break;
            case MethodRequest.put://put
                request.put(requestBody);
                break;
            case MethodRequest.delete://delete
                request.delete(requestBody);
                break;
            case MethodRequest.patch://patch
                request.patch(requestBody);
                break;
        }
        return this;
    }

    public ApiSetting setMethod(int caseRequest) {
        switch (caseRequest) {
            case MethodRequest.post://post
                request.post(mBody);
                break;
            case MethodRequest.get://get
                request.get();
                break;
            case MethodRequest.put://put
                request.put(mBody);
                break;
            case MethodRequest.delete://delete
                request.delete(mBody);
                break;
            case MethodRequest.patch://patch
                request.patch(mBody);
                break;
        }
        return this;
    }

    public void clientCall() {
        try {
            System.out.println("url : " + mUrl);
            System.out.println("body : " + mSBody);
            System.out.println("bodyLength : " + mBody.contentLength());
            System.out.println("bodyType : " + mBody.contentType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mApiSettingCallBack.onLoading();
        client.build().newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                e.printStackTrace();
                mApiSettingCallBack.onResponse(call, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (BuildConfig.DEBUG) {
                    System.out.println("code : " + response.code());
                    System.out.println("message : " + response.message());
                }
                mApiSettingCallBack.onResponse(call, response);
            }
        });
    }

    private static String getFilename(String uri) {
        String Filename = "files";
        int length = uri.split("\\/").length;
        return uri.split("\\/")[length - 1];
    }

}
