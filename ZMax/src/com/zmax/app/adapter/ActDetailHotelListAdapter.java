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
import android.widget.Button;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.model.Act;
import com.zmax.app.utils.PhoneUtil;

public class ActDetailHotelListAdapter extends BaseAdapter {

	List<Act> mList = new ArrayList<Act>();
	private Context mContext;
	private LayoutInflater mInflater;

	public ActDetailHotelListAdapter(Context context) {
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
			convertView = mInflater.inflate(R.layout.act_detail_third_listitem,
					null);
			convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,
				PhoneUtil.dip2px(mContext, 60)));
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_address);
			holder.btn_book = (Button) convertView.findViewById(R.id.btn_book);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// holder.tv_name.setText("酒店设施");
		// holder.tv_address.setText("酒店设施");

		return convertView;
	}

	class ViewHolder {
		public TextView tv_name;
		public TextView tv_address;
		public Button btn_book;
	}

}
