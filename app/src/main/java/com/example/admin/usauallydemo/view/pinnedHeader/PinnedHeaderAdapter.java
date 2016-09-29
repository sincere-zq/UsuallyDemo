package com.example.admin.usauallydemo.view.pinnedHeader;

import android.view.View;

public interface PinnedHeaderAdapter
{

	public static final int PINNED_HEADER_GONE = 0;//header 不显示
	
	public static final int PINNED_HEADER_VISIBLE = 1;//header 显示
	
	public static final int PINNED_HEADER_PUSHED_UP = 2;////header 悬浮
	
	int getPinnedHeaderState(int position);
	
	void configurePinnedHeader(View headerView, int position, int alpaha);
	
}
