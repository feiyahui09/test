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

import com.zmax.app.R;
import com.zmax.app.model.Act;

public class ActCalenderListAdapter  extends BaseAdapter {

		List<Act> mList = new ArrayList<Act>();
		private Context mContext;
		private LayoutInflater mInflater;
		public ActCalenderListAdapter(Context  context) {
			mContext=context;
			mInflater=((Activity)mContext).getLayoutInflater();
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
				convertView = mInflater.inflate(R.layout.act_calender_list_item,
						null);
				 
				holder.img_thu = (ImageView) convertView
						.findViewById(R.id.iv_img);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.img_thu.setImageResource(R.drawable.ic_launcher);
		return 	  convertView;
		}


	class ViewHolder {
		public TextView header_;
		public TextView title_;
		public TextView short_;
		public ImageView img_thu;
	}

}
