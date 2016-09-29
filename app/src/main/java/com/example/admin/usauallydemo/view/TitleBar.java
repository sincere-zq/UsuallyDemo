package com.example.admin.usauallydemo.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.usauallydemo.R;


/**
 * Created by zengqiang on 2016/8/30.
 * Description:
 */
public class TitleBar extends LinearLayout implements View.OnClickListener {

    private TextView tv_title;

    private Context context;

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        View view = View.inflate(context, R.layout.title_bar, null);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        addView(view);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.titleBar);
            String title = typedArray.getString(R.styleable.titleBar_setTitle);
            tv_title.setText(title);
        }
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_back) {
            try {
                ((Activity) context).finish();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }
}
