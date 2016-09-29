package com.example.admin.usauallydemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.usauallydemo.R;

import java.util.List;


/**
 * Created by zengqiang on 2016/9/28.
 * Description:MyViewPagerIndicator
 */
public class MyViewPagerIndicator extends LinearLayout {

    private Paint mPaint;
    private Path mPath; //线
    private int mWidth; //三角形的宽
    private int mHeight;//三角形的高
    private static final float SCALE = 1 / 6F;//三角形宽与TextView宽的比例
    private int minitTranslationX;//三角形的偏移距离
    private int mTranslationX;//三角形移动时的位置
    private int mTabVisibleCount;
    private static final int COUNT_DEFAULT_TAB = 4;
    private List<String> mTitles;
    private static final int COLOR_TEXT_NORMAL = 0x77FFFFFF;
    public MyViewPagerIndicator(Context context) {
        this(context, null);
    }
    public MyViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public MyViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 获取可见Tab的数量
         */
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.MyViewPagerIndicator);
        mTabVisibleCount = t.getInt(R.styleable.MyViewPagerIndicator_visible_tab_count, COUNT_DEFAULT_TAB);
        if (mTabVisibleCount < 0)
            mTabVisibleCount = COUNT_DEFAULT_TAB;
        t.recycle();
        mPaint = new Paint();//初始化画笔
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(2));//防止三角形尖锐
    }
    /**
     * 绘制三角形
     *
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(minitTranslationX + mTranslationX, getHeight());
        canvas.drawPath(mPath, mPaint);//画三角形
        canvas.restore();
        super.dispatchDraw(canvas);
    }
    /**
     * 设置三角形的大小
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = (int) (w / mTabVisibleCount * SCALE);//底边的宽度（三角形）
        minitTranslationX = w / mTabVisibleCount / 2 - mWidth / 2;//偏移量
        initTriangle();
    }

    /**
     * 初始化三角形
     */
    private void initTriangle() {
        mHeight = mWidth / 2;//三角形的高
        mPath = new Path();
        mPath.moveTo(0, 0);//初始化线
        mPath.lineTo(mWidth, 0);
        mPath.lineTo(mWidth / 2, -mHeight);
        mPath.close();
    }

    /**
     * 指示器跟随手指进行滚动
     *
     * @param position
     * @param positionOffset
     */
    public void scroll(int position, float positionOffset) {
        int tabWidth = getWidth() / mTabVisibleCount;
        mTranslationX = (int) (tabWidth * (positionOffset + position));//三角形的偏移量
        //容器移动，当tab处于移动至最后一个时
        if (position >= mTabVisibleCount - 1 && positionOffset > 0 && getChildCount() > mTabVisibleCount) {
            if (mTabVisibleCount != 0)
                this.scrollTo((position - (mTabVisibleCount - 1)) * tabWidth + (int) (tabWidth * positionOffset), 0);
            else
                this.scrollTo(position * tabWidth + (int) (tabWidth * positionOffset), 0);
        }
        invalidate();
    }

    /**
     * 设置子view的Inflate
     */
    @Override
    protected void onFinishInflate() {
        int mCount = getChildCount();
        if (mCount == 0)
            return;
        for (int i = 0; i < mCount; i++) {
            View view = getChildAt(i);
            LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.weight = 0;

            lp.width = getScreenWidth() / mTabVisibleCount;
            view.setLayoutParams(lp);
        }
        super.onFinishInflate();
    }

    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics d = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(d);
        return d.widthPixels;
    }

    public void setTabItemTitles(List<String> titles) {
        if (titles != null && titles.size() > 0) {
            this.removeAllViews();
            mTitles = titles;
            for (String title : mTitles) {
                addView(setTabTextView(title));
            }
        }
    }


    /**
     * 根据title创建Tab
     *
     * @param title
     */
    public View setTabTextView(String title) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth() / mTabVisibleCount;
        tv.setText(title);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setTextColor(COLOR_TEXT_NORMAL);
        tv.setLayoutParams(lp);
        return tv;
    }

    /**
     * 设置可见Tab的数量(在setTabItemTitles方法前调用)
     *
     * @param count
     */
    public void setVisibleTabCount(int count) {
        mTabVisibleCount = count;
    }
}
