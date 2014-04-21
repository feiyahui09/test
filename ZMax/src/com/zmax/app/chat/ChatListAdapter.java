package com.zmax.app.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmax.app.R;

/**
 * 想比较原来的多了getItemViewType和getViewTypeCount这两个方法，原来循环使用layout布局，起到了优化的作用
 * 
 * 
 * */
public class ChatListAdapter extends BaseAdapter {
	
	public static final String KEY = "key";
	public static final String VALUE = "value";
	
	public static final int VALUE_TIME_TIP = 0;// 7种不同的布局
	public static final int VALUE_LEFT_TEXT = 1;
	public static final int VALUE_LEFT_IMAGE = 2;
	public static final int VALUE_RIGHT_TEXT = 3;
	public static final int VALUE_RIGHT_IMAGE = 4;
	public static final int[] MSG_TYPE = { VALUE_TIME_TIP, VALUE_LEFT_TEXT, VALUE_LEFT_IMAGE, VALUE_RIGHT_TEXT, VALUE_RIGHT_IMAGE };
	public static final int[] MSG_TYPE_ID = { R.id.value_time_tip, R.id.value_left_text, R.id.value_left_image, R.id.value_right_text,
			R.id.value_right_image };
	
	private LayoutInflater mInflater;
	
	private Context context;
	private List<Message> myList = new ArrayList<Message>();
	
	public ChatListAdapter(Context context) {
		
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	public void addItems(List<Message> list) {
		myList.addAll(list);
		
		notifyDataSetChanged();
	}
	
	public void addItem(Message item) {
		myList.add(item);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return myList.size();
	}
	
	@Override
	public Object getItem(int arg0) {
		return myList.get(arg0);
	}
	
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		
		Message msg = myList.get(position);
		int type = getItemViewType(position);
		ViewHolder holder = null;
		if (convertView == null) {
			
			holder = new ViewHolder();
			switch (type) {
			
				case VALUE_TIME_TIP:
					convertView = mInflater.inflate(R.layout.chat_list_item_time_tip, null);
					holder.tvTimeTip = (TextView) convertView.findViewById(R.id.tv_time_tip);
					holder.tvTimeTip.setText(msg.getValue());
					break;
				case VALUE_LEFT_TEXT:
					
					convertView = mInflater.inflate(R.layout.chat_list_item_left_text, null);
					holder.ivLeftIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
					holder.btnLeftText = (Button) convertView.findViewById(R.id.btn_left_text);
					holder.btnLeftText.setText(msg.getValue());
					holder.tvLeftName = (TextView) convertView.findViewById(R.id.tv_left_name);
					holder.tvLeftName.setText(msg.getName());
					break;
				
				case VALUE_LEFT_IMAGE:
					
					convertView = mInflater.inflate(R.layout.chat_list_item_left_iamge, null);
					holder.ivLeftIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
					holder.ivLeftImage = (ImageView) convertView.findViewById(R.id.iv_left_image);
					// holder.ivLeftImage.setImageResource(R.drawable.test);
					break;
				case VALUE_RIGHT_TEXT:
					
					convertView = mInflater.inflate(R.layout.chat_list_item_right_text, null);
					holder.ivRightIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
					holder.btnRightText = (Button) convertView.findViewById(R.id.btn_right_text);
					holder.btnRightText.setText(msg.getValue());
					holder.tvRightName = (TextView) convertView.findViewById(R.id.tv_right_name);
					holder.tvRightName.setText(msg.getName());
					break;
				
				case VALUE_RIGHT_IMAGE:
					
					convertView = mInflater.inflate(R.layout.chat_list_item_right_iamge, null);
					holder.ivRightIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
					holder.ivRightImage = (ImageView) convertView.findViewById(R.id.iv_right_image);
					// holder.ivRightImage.setImageResource(R.drawable.test);
					break;
				
				default:
					break;
			}
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}
	
	/**
	 * 根据数据源的position返回需要显示的的layout的type
	 * 
	 * */
	@Override
	public int getItemViewType(int position) {
		
		Message msg = myList.get(position);
		int type = msg.getType();
		return type;
	}
	
	/**
	 * 返回所有的layout的数量
	 * 
	 * */
	@Override
	public int getViewTypeCount() {
		return MSG_TYPE.length;
	}
	
	class ViewHolder {
		// to be down
		private TextView tvTimeTip;// 时间
		
		private ImageView ivLeftIcon;// 左边的头像
		private Button btnLeftText;// 左边的文本
		private ImageView ivLeftImage;// 左边的图像
		private TextView tvLeftName;//
		
		private ImageView ivRightIcon;// 右边的头像
		private Button btnRightText;// 右边的文本
		private ImageView ivRightImage;// 右边的图像
		private TextView tvRightName;//
		
	}
	
}
