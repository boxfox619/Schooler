package com.schooler.schoolerapplication;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class DetectScrollingScrollView extends ScrollView {
    public static final int DOWN = 1;
    public static final int UP = 2;

    private boolean hide;
    private Handler handler;


    public DetectScrollingScrollView(Context context) {
        super(context);
    }

    public DetectScrollingScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
        this.hide = false;
    }

    public void setHide(boolean check) {
        this.hide = check;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (handler != null) {
            Message message = null;
            if (!hide && t > oldt) {
                message = handler.obtainMessage();
                message.what = DOWN;
                message.arg1 = t;
            } else if (hide && t < oldt) {
                message = handler.obtainMessage();
                message.what = UP;
                message.arg1 = t;
            }
            if (message != null)
                handler.sendMessage(message);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
