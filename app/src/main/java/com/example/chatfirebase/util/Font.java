package com.example.chatfirebase.util;
import android.content.Context;
import android.graphics.Typeface;


public class Font {

    private static Font instance;
    private final Context ctx;

    private Font(Context context) {
        this.ctx = context;
    }

    public static Font getInstance(Context mContext) {
        if (instance == null) {
            instance = new Font(mContext);
        }
        return instance;
    }

    public static Font getInstance() {
        return instance;
    }

    public Typeface getFontTopic() {
        return Typeface.createFromAsset(this.ctx.getAssets(), "fonts/rockwell.ttf");
    }

    public Typeface getFontContent() {
        return Typeface.createFromAsset(this.ctx.getAssets(), "fonts/kanit_regular.ttf");
    }

    public Typeface getFontContentBold() {
        return Typeface.createFromAsset(this.ctx.getAssets(), "fonts/baijam_bold.ttf");
    }

    public Typeface getFontTohoma() {
        return Typeface.createFromAsset(this.ctx.getAssets(), "fonts/tahoma.ttf");
    }

    public Typeface getFontTohomaBold() {
        return Typeface.createFromAsset(this.ctx.getAssets(), "fonts/tahomabd.ttf");
    }
}
