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

public class HotelBookListAdapter extends PagerAdapter {
	
	private List<View> mListViews = new ArrayList<View>();
	private List<Act> mDetails = new ArrayList<Act>();
	private Context mContext;
	
	public HotelBookListAdapter(Context context, List<View> list) {
		this.mContext = context;
		this.mListViews = list;
	}
	
	public void addViews(List<View> list) {
		this.mListViews = list;
		notifyDataSetChanged();
	}
	
	public void addViews(View view) {
		this.mListViews.add(view);
		notifyDataSetChanged();
	}
	
	private void init() {
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		for (int i = 0; i < mDetails.size(); i++) {
			View view = inflater.inflate(R.layout.hotel_book_list_item, null);
			((ImageView) view.findViewById(R.id.iv_img)).setBackgroundResource(R.drawable.ic_launcher);
			mListViews.add(view);
		}
		notifyDataSetChanged();
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (mListViews != null && mListViews.size() > 0) container.removeView(mListViews.get(position));
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
		if (mListViews == null) return 0;
		return mListViews.size();
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
}
