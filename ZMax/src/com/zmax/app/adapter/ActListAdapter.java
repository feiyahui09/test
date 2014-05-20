package com.zmax.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmax.app.R;
import com.zmax.app.model.Act;
import com.zmax.app.utils.DateTimeUtils;
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
		
		if (lists == null || lists.isEmpty()) {
			return;
		}
		List<Act> temlist = new ArrayList<Act>();
		for (int i = 0; i < lists.size(); i++) {
			Act tmp = lists.get(i);
			boolean isExsit = false;
			for (int j = 0; j < mList.size(); j++) {
				if (mList.get(j).id == tmp.id) isExsit = true;
			}
			if (!isExsit) temlist.add(tmp);
		}
		mList.addAll(temlist);
		notifyDataSetChanged();
	}
	
	public List<Act> getList() {
		
		return mList;
	}
	
	public void Clear() {
		mList.clear();
	}
	
	@Override
	public int getItemViewType(int position) {
		if (position % 2 != 0) return ITEM_ODD;
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
		if (getItemViewType(position) != ITEM_EVEN) return getEvenView(position, convertView, parent);
		return getOddView(position, convertView, parent);
	}
	
	public View getEvenView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		Act act = mList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.act_list_item_even, null);
//			convertView
//					.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, PhoneUtil.dip2px(mContext, 261)));
			holder.img_thu = (ImageView) convertView.findViewById(R.id.iv_img);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_city = (TextView) convertView.findViewById(R.id.tv_city);
			convertView.setTag(R.id.item_even, holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag(R.id.item_even);
		}
		ImageLoader.getInstance().displayImage(act.poster, holder.img_thu);
		holder.tv_city.setText("" + act.cities);
		act.duration = fromDateStr(act.start_date, act.end_date);
		holder.tv_date.setText("" + fromDateStrSimple(act.start_date, act.end_date));
		holder.tv_name.setText("" + act.name);
		return convertView;
	}
	
	public View getOddView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		Act act = mList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.act_list_item_odd, null);
//			convertView
//					.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, PhoneUtil.dip2px(mContext, 261)));
			holder.img_thu = (ImageView) convertView.findViewById(R.id.iv_img);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_city = (TextView) convertView.findViewById(R.id.tv_city);
			
			convertView.setTag(R.id.item_odd, holder);
			
		}
		else {
			holder = (ViewHolder) convertView.getTag(R.id.item_odd);
		}
		ImageLoader.getInstance().displayImage(act.poster, holder.img_thu);
		holder.tv_city.setText("" + act.cities);
		act.duration = fromDateStr(act.start_date, act.end_date);
		holder.tv_date.setText("" + fromDateStrSimple(act.start_date, act.end_date));
		holder.tv_name.setText("" + act.name);
		
		return convertView;
	}
	
	private String fromDateStrSimple(String start_date, String end_dateString) {
		String dateStr = "";
		
		start_date = DateTimeUtils.friendly_time(start_date);
		end_dateString = DateTimeUtils.friendly_time(end_dateString);
		if (TextUtils.equals(end_dateString, start_date))
			dateStr = start_date;
		else
			dateStr = start_date + "起";
		return dateStr;
	}
	
	private String fromDateStr(String start_date, String end_dateString) {
		String dateStr = "";
		
		start_date = DateTimeUtils.friendly_time(start_date);
		end_dateString = DateTimeUtils.friendly_time(end_dateString);
		if (TextUtils.equals(end_dateString, start_date))
			dateStr = start_date;
		else
			dateStr = start_date +  " 至  " + end_dateString;
		return dateStr;
	}
	
	class ViewHolder {
		public TextView tv_city;
		public TextView tv_name;
		public TextView tv_date;
		public ImageView img_thu;
	}
	
}
