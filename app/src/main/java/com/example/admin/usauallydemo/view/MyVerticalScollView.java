package com.example.admin.usauallydemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by zengqiang on 2016/8/5.
 * Description:
 * 消除与recyclerView的滑动冲突 且重写OnScrollChanged回调,消除与子View水平滑动的冲突
 */
public class MyVerticalScollView extends ScrollView {
    private int downX,x;
    private int downY,y;
    private int mTouchSlop;
    private OnScrollChangedCallBack onScrollChangedCallBack;

    public void setOnScrollChangedCallBack(OnScrollChangedCallBack onScrollChangedCallBack) {
        this.onScrollChangedCallBack = onScrollChangedCallBack;
    }

    public MyVerticalScollView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyVerticalScollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyVerticalScollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                x = (int) e.getX();
                y = (int) e.getY();
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
                int new_x = (int) e.getX();
                int new_y = (int) e.getY();
                int move_x = Math.abs(new_x - x);
                int move_y = Math.abs(new_y - y);
                if (move_x > move_y + 10)
                    return false;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        if (onScrollChangedCallBack != null) {
            onScrollChangedCallBack.onScrollChanged(x, y, oldx, oldy);
        }
        super.onScrollChanged(x, y, oldx, oldy);
    }

    public interface OnScrollChangedCallBack{
        void onScrollChanged(int x, int y, int oldx, int oldy);
    }
}
