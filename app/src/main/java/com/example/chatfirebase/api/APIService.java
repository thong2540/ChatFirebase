package com.example.chatfirebase.api;

import com.example.chatfirebase.api.Notifications.MyResponse;
import com.example.chatfirebase.api.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAApTB38rA:APA91bFiDodiIo67kZbSpAZ_CQbnVp6C8Fr4F8lQ98wFU6XiSueRy4aGo_FL8GXemmfGcEEIMq4z7tB2DpDn-4D_ZDgXOOQdppXjZ0ahugvOPZuK_X3ek0KAvV5CYN0hOac2Y4oY-PmQ"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
