package com.zmax.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.model.Act;
import com.zmax.app.utils.PhoneUtil;

public class ActListAdapter extends BaseAdapter {

	List<Act> mList = new ArrayList<Act>();
	private Context mContext;
	private LayoutInflater mInflater;

	private int curType = 0;
	private final int ITEM_ODD = 1;
	private final int ITEM_EVEN = 0;

	public ActListAdapter(Context context) {
		mContext = context;
		mInflater = ((Activity) mContext).getLayoutInflater();
	}
	
	public void appendToList(List<Act> lists) {

		if (lists == null) {
			return;
		}
		mList.addAll(lists);
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		if (position % 2 != 0)
			return ITEM_ODD;
		return ITEM_EVEN;
	}

	/**
	 * getViewTypeCount返回的值必须比视图类型常量值大 2>ITEM_ODD=>ITEM_EVEN=0
	 */
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 刚好相反，fuck ui
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getItemViewType(position) != ITEM_EVEN)
			return getEvenView(position, convertView, parent);
		return getOddView(position, convertView, parent);
	}

	public View getEvenView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.act_list_item_even, null);

			convertView.setLayoutParams(new AbsListView.LayoutParams(
					AbsListView.LayoutParams.FILL_PARENT, PhoneUtil.dip2px(
							mContext, 261)));
			holder.img_thu = (ImageView) convertView.findViewById(R.id.iv_img);
			convertView.setTag(R.id.item_even, holder);

		} else {
			holder = (ViewHolder) convertView.getTag(R.id.item_even);
		}

		// holder.img_thu.setImageResource(R.drawable.ic_launcher);
		return convertView;
	}

	public View getOddView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.act_list_item_odd, null);
			convertView.setLayoutParams(new AbsListView.LayoutParams(
					AbsListView.LayoutParams.FILL_PARENT, PhoneUtil.dip2px(
							mContext, 261)));
			holder.img_thu = (ImageView) convertView.findViewById(R.id.iv_img);
			convertView.setTag(R.id.item_odd, holder);

		} else {
			holder = (ViewHolder) convertView.getTag(R.id.item_odd);
		}

		// holder.img_thu.setImageResource(R.drawable.ic_launcher);
		return convertView;
	}

	class ViewHolder {
		public TextView header_;
		public TextView title_;
		public TextView short_;
		public ImageView img_thu;
	}

}
