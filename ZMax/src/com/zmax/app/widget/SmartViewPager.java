package com.zmax.app.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/*
 * 可以禁止滑动的viewpager
 */
public class SmartViewPager extends ViewPager {
	
	private boolean canScroll = true;
	
	public SmartViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public SmartViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (!canScroll) return false;
		return super.onInterceptTouchEvent(arg0);
		
	}
	
	public boolean isCanScroll() {
		return canScroll;
	}
	
	public void setCanScroll(boolean canScroll) {
		this.canScroll = canScroll;
	}
	
}
