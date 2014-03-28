package com.zmax.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmax.app.R;
import com.zmax.app.model.Act;
import com.zmax.app.utils.Constant;

public class HotelFacilityListAdapter extends BaseAdapter {
	
	List<Act> mList = new ArrayList<Act>();
	private Context mContext;
	private LayoutInflater mInflater;
	
	public HotelFacilityListAdapter(Context context) {
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
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		Act item = mList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.hotel_facility_list_item, null);
			
			holder.img_thu = (ImageView) convertView.findViewById(R.id.iv_fac_img);
			holder.header_ = (TextView) convertView.findViewById(R.id.tv_fac_name);
			
			convertView.setTag(holder);
			
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.header_.setText("酒店设施");
		holder.img_thu.setImageResource(R.drawable.ic_launcher);
		ImageLoader.getInstance().displayImage(Constant.TEST_ICON_URI, holder.img_thu);
		return convertView;
	}
	
	class ViewHolder {
		public TextView header_;
		public ImageView img_thu;
	}
	
}
