package com.zmax.app.chat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.zmax.app.R;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.EmotionUtils;
import com.zmax.app.utils.Log;
import com.zmax.app.utils.Utility;
import com.zmax.app.widget.WebImageViewer;

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
	private List<ChatMsg> myList = new ArrayList<ChatMsg>();
	
	public ChatListAdapter(Context context) {
		
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	public void addItems(List<ChatMsg> list) {
		myList.addAll(list);
		
		notifyDataSetChanged();
	}
	
	public void addItem(ChatMsg item) {
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
	
	public List<ChatMsg> getMsgList() {
		return myList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		
		final ChatMsg chatMsg = myList.get(position);
		int type = getItemViewType(position);
		ViewHolder holder = null;
		if (convertView == null) {
			
			holder = new ViewHolder();
			switch (type) {
			
				case VALUE_TIME_TIP:
					convertView = mInflater.inflate(R.layout.chat_list_item_time_tip, null);
					holder.tvTimeTip = (TextView) convertView.findViewById(R.id.tv_time_tip);
					break;
				case VALUE_LEFT_TEXT:
					
					convertView = mInflater.inflate(R.layout.chat_list_item_left_text, null);
					holder.ivLeftIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
					holder.btnLeftText = (Button) convertView.findViewById(R.id.btn_left_text);
					holder.tvLeftName = (TextView) convertView.findViewById(R.id.tv_left_name);
					break;
				
				case VALUE_LEFT_IMAGE:
					
					convertView = mInflater.inflate(R.layout.chat_list_item_left_iamge, null);
					holder.ivLeftIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
					holder.ivLeftImage = (ImageView) convertView.findViewById(R.id.iv_left_image);
					holder.tvLeftName = (TextView) convertView.findViewById(R.id.tv_left_name);
					break;
				case VALUE_RIGHT_TEXT:
					
					convertView = mInflater.inflate(R.layout.chat_list_item_right_text, null);
					holder.ivRightIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
					holder.btnRightText = (Button) convertView.findViewById(R.id.btn_right_text);
					holder.tvRightName = (TextView) convertView.findViewById(R.id.tv_right_name);
					break;
				
				case VALUE_RIGHT_IMAGE:
					
					convertView = mInflater.inflate(R.layout.chat_list_item_right_iamge, null);
					holder.ivRightIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
					holder.tvRightName = (TextView) convertView.findViewById(R.id.tv_right_name);
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
		
		switch (type) {
		
			case VALUE_TIME_TIP:
				holder.tvTimeTip.setText(chatMsg.tipTime);
				break;
			case VALUE_LEFT_TEXT:
				try {
					holder.btnLeftText.setText(EmotionUtils.getSinaEmotionsString(SpannableString.valueOf(chatMsg.msg.content), context));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				holder.tvLeftName.setText(chatMsg.from);
				holder.ivLeftIcon.setImageResource(chatMsg.gender == 0 ? R.drawable.chat_female_icon : R.drawable.chat_male_icon);
				
				break;
			case VALUE_LEFT_IMAGE:
				holder.tvLeftName.setText(chatMsg.from);
				holder.ivLeftIcon.setImageResource(chatMsg.gender == 0 ? R.drawable.chat_female_icon : R.drawable.chat_male_icon);
				ImageLoader.getInstance().displayImage(Utility.getImgUrlOnDensity(context,
						getShrinkImg(chatMsg.msg.content)), holder.ivLeftImage);
				holder.ivLeftImage.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// showImg(v, chatMsg.msg.content);
						showImg(chatMsg.msg.content);
						
					}
				});
				
				break;
			case VALUE_RIGHT_TEXT:
				try {
					holder.btnRightText.setText(EmotionUtils.getSinaEmotionsString(SpannableString.valueOf(chatMsg.msg.content), context));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				holder.tvRightName.setText(chatMsg.from);
				holder.ivRightIcon.setImageResource(chatMsg.gender == 0 ? R.drawable.chat_female_icon : R.drawable.chat_male_icon);
				
				break;
			case VALUE_RIGHT_IMAGE:
				holder.tvRightName.setText(chatMsg.from);
				holder.ivRightIcon.setImageResource(chatMsg.gender == 0 ? R.drawable.chat_female_icon : R.drawable.chat_male_icon);
				ImageLoader.getInstance().displayImage(getShrinkImg(chatMsg.msg.content), holder.ivRightImage);
				holder.ivRightImage.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// showImg(v, chatMsg.msg.content);
						showImg(chatMsg.msg.content);
					}
				});
				break;
			
			default:
				break;
		}
		return convertView;
	}
	
	/**
	 * 根据数据源的position返回需要显示的的layout的type
	 * 
	 * */
	@Override
	public int getItemViewType(int position) {
		
		ChatMsg msg = myList.get(position);
		int type = msg.item_type;
		return type;
	}
	
	private String getShrinkImg(String img) {
		Log.i("orgin image url:   " + img);
		String result = "";
		if (img.startsWith("http"))
			result = img + "_s";
		else
			result = "file:///" + img;
		Log.i("thumb image url:   " + result);
		
		return result;
	}
	
	private void showImg(String path) {
		Intent intent = new Intent();
		intent.setClass(context, WebImageViewer.class);
		intent.putExtra(Constant.Chat.CHAT_IMG_LARGE_IMG_KEY, path);
		context.startActivity(intent);
		
	}
	
	private void showImg(View view, final String path) {
		final PopupWindow popupWindow = new PopupWindow(context);
		popupWindow.setWindowLayoutMode(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		ColorDrawable dw = new ColorDrawable(R.color.black);
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		View v = ((Activity) context).getLayoutInflater().inflate(R.layout.chat_big_img, null);
		final ImageView imageView = (ImageView) v.findViewById(R.id.iv_img);
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		
		ImageLoader.getInstance().displayImage(path, imageView, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.update();
				}
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
		popupWindow.setContentView(v);
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
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
