package com.example.chatfirebase.util;

import android.app.Activity;

import com.example.chatfirebase.R;

/**
 * Created by ChoCoFire on 2/26/2018.
 */

public class ActivityTransition {

    public static void GoRight(Activity activity){
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public static void GoLeft(Activity activity){
        activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
