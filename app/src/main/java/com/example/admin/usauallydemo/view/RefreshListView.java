package com.example.admin.usauallydemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.usauallydemo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zengqiang on 2016/9/29.
 * ListView的上下拉刷新
 */

public class RefreshListView extends ListView implements AbsListView.OnScrollListener {
    private int headerHeight;//顶部布局文件的高度
    private View header;//顶部布局文件
    private View footer;//底部布局文件
    private int firstVisibleItem;//当前页面第一个可见Item的位置
    private boolean isRemark;//标记，当前是在ListView最顶端按下的
    private int startY;//按下时的Y值
    private static final int NONE = 0;//正常状态
    private static final int PULL = 1;//提示下拉状态
    private static final int RELESE = 2;//提示释放状态
    private static final int REFRESHING = 3;//正在刷新状态
    private static final int LOADING = 4;//正在刷新状态
    private int state;//当前的状态
    private int scrollState;//ListView当前滚动状态
    private MRefreshListener listener;//刷新回调
    private int totalItemCount;//总数量
    private int lastVisibleItem;//最后一个可见的Item
    private boolean isLoading;//正在加载

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 刷新回调接口传入
     *
     * @param listener
     */
    public void setRefreshListener(MRefreshListener listener) {
        this.listener = listener;
    }

    /**
     * 初始化界面，添加顶部布局文件到ListView
     *
     * @param context
     */
    private void initView(Context context) {
        header = View.inflate(context, R.layout.header, null);
        measureView(header);
        footer = View.inflate(context, R.layout.footer, null);
        footer.findViewById(R.id.load_layout).setVisibility(GONE);
        headerHeight = header.getMeasuredHeight();
        topPaddind(-headerHeight);
        this.addHeaderView(header);
        this.addFooterView(footer);
        this.setOnScrollListener(this);
    }

    /**
     * 设置header布局的上边距
     */
    private void topPaddind(int topPadding) {
        header.setPadding(header.getPaddingLeft(), topPadding, header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }

    /**
     * 通知父布局，占用的宽高
     *
     * @param view
     */
    private void measureView(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int height;
        int tempHeight = lp.height;
        if (tempHeight > 0)
            height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
        else
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
        if (state == LOADING || state == REFRESHING || scrollState != SCROLL_STATE_IDLE)
            return;
        if (totalItemCount == lastVisibleItem) {
            if (!isLoading) {
                footer.findViewById(R.id.load_layout).setVisibility(VISIBLE);
                state = LOADING;
                isLoading = true;
            }
            //加载数据
            listener.onLoadMore();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    /**
     * onTouch事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    isRemark = true;
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELESE) {
                    state = REFRESHING;
                    //加载最新数据
                    reflashViewByState();
                    listener.onRefresh();
                } else if (state == PULL) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 判断移动过程中的操作
     *
     * @param ev
     */
    private void onMove(MotionEvent ev) {
        if (!isRemark) {
            return;
        }
        int tempY = (int) ev.getY();//当前手指位置
        int space = tempY - startY;//移动的距离
        int topPadding = space - headerHeight;
        switch (state) {
            case NONE:
                if (space > 0) {
                    state = PULL;
                    reflashViewByState();
                }
                break;
            case PULL:
                topPaddind(topPadding);
                if (space >= headerHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = RELESE;
                    reflashViewByState();
                }
                break;
            case RELESE:
                topPaddind(topPadding);
                if (space <= headerHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = PULL;
                    reflashViewByState();
                } else if (space <= 0) {
                    state = NONE;
                    reflashViewByState();
                    isRemark = false;
                }
                break;
            case REFRESHING:
                break;
        }
    }

    /**
     * 根据当前状态，改变界面显示
     */
    private void reflashViewByState() {
        TextView down = (TextView) header.findViewById(R.id.tv_down);
        ImageView arrow = (ImageView) header.findViewById(R.id.img_arrow);
        ProgressBar progressBar = (ProgressBar) header.findViewById(R.id.progress);
        RotateAnimation animDown = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(250);
        animDown.setFillAfter(true);
        RotateAnimation animUp = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(250);
        animUp.setFillAfter(true);
        switch (state) {
            case NONE:
                topPaddind(-headerHeight);
                break;
            case PULL:
                arrow.setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
                down.setText("下拉刷新");
                arrow.clearAnimation();
                arrow.setAnimation(animUp);
                break;
            case RELESE:
                arrow.setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
                down.setText("松开刷新");
                arrow.clearAnimation();
                arrow.setAnimation(animDown);
                break;
            case REFRESHING:
                topPaddind(50);
                arrow.setVisibility(GONE);
                progressBar.setVisibility(VISIBLE);
                down.setText("正在刷新");
                arrow.clearAnimation();
                break;
        }
    }

    /**
     * 获取完数据
     */
    public void refreshComplete() {
        isLoading = false;
        footer.findViewById(R.id.load_layout).setVisibility(GONE);
        state = NONE;
        reflashViewByState();
        isRemark = false;
        TextView lastTime = (TextView) header.findViewById(R.id.tv_lasttime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = format.format(date);
        lastTime.setText(time);
    }

    /**
     * 刷新数据接口
     */
    public interface MRefreshListener {
        void onRefresh();

        void onLoadMore();
    }
}
