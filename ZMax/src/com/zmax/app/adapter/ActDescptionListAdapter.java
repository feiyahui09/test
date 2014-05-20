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
import com.zmax.app.model.ActDetailDescription;
import com.zmax.app.utils.Utility;

public class ActDescptionListAdapter extends BaseAdapter {
	
	List<ActDetailDescription> mList = new ArrayList<ActDetailDescription>();
	private Context mContext;
	private LayoutInflater mInflater;
	
	public ActDescptionListAdapter(Context context) {
		mContext = context;
		mInflater = ((Activity) mContext).getLayoutInflater();
	}
	
	public void appendToList(List<ActDetailDescription> lists) {
		
		if (lists == null || lists.isEmpty()) {
			return;
		}
		mList.clear();
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
		ActDetailDescription item = mList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.act_detail_second_listitem, null);
			
			holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
			holder.tv_des_content = (TextView) convertView.findViewById(R.id.tv_des_content);
			
			convertView.setTag(holder);
			
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_des_content.setText(""+item.content);
		ImageLoader.getInstance().displayImage(Utility.getImgUrlOnDensity(mContext,item.image_path), holder.iv_img);
		return convertView;
	}
	
	class ViewHolder {
		public TextView tv_des_content;
		public ImageView iv_img;
	}
	
}
