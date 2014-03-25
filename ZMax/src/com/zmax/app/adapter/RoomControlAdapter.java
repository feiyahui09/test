package com.zmax.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zmax.app.R;
import com.zmax.app.model.Act;
import com.zmax.app.widget.PagerAdapter;

/*
 * 房控上下滑动切换控制面板和状态面板的
 */
public class RoomControlAdapter extends PagerAdapter {

	private List<View> mListViews = new ArrayList<View>();
	private List<Act> mDetails = new ArrayList<Act>();
	private Context mContext;

	public RoomControlAdapter(Context context, List<View> list) {
		this.mContext = context;
		this.mListViews = list;
	}

	public void addViews(List<View> list) {
		this.mListViews = list;
		notifyDataSetChanged();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (mListViews != null && mListViews.size() > 0)
			container.removeView(mListViews.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if (mListViews != null && mListViews.size() > 0) {
			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}
		return null;
	}

	@Override
	public int getCount() {
		return mListViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
