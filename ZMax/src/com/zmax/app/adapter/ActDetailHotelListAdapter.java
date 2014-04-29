package com.zmax.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.model.ActDetailHotel;
import com.zmax.app.ui.WebViewActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.PhoneUtil;

public class ActDetailHotelListAdapter extends BaseAdapter {
	
	List<ActDetailHotel> mList = new ArrayList<ActDetailHotel>();
	private Context mContext;
	private LayoutInflater mInflater;
	
	public ActDetailHotelListAdapter(Context context) {
		mContext = context;
		mInflater = ((Activity) mContext).getLayoutInflater();
	}
	
	public void appendToList(List<ActDetailHotel> lists) {
		
		if (lists == null || lists.isEmpty()) {
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
	
	public void clear() {
		// TODO Auto-generated method stub
		mList.clear();
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
		final ActDetailHotel item = mList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.act_detail_third_listitem, null);
			convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, PhoneUtil.dip2px(mContext, 60)));
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
			holder.btn_book = (Button) convertView.findViewById(R.id.btn_book);
			convertView.setTag(holder);
			
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv_name.setText(TextUtils.isEmpty(item.name) ? convertView.getResources().getString(R.string.default_unkown) : item.name);
		holder.tv_address.setText(TextUtils.isEmpty(item.address) ? convertView.getResources().getString(R.string.default_unkown)
				: item.address);
		holder.btn_book.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, WebViewActivity.class);
				intent.setAction(Constant.WAP.ACTION_HOTEL);
				intent.putExtra(Constant.WAP.HOTEL_ID_KEY, item.pms_hotel_id);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}
	
	class ViewHolder {
		public TextView tv_name;
		public TextView tv_address;
		public Button btn_book;
	}
	
}
