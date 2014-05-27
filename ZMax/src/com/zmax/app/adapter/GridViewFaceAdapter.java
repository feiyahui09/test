package com.zmax.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.zmax.app.R;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.EmotionUtils;
import com.zmax.app.utils.PhoneUtil;

/**
 * 用户表情Adapter类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-8-9
 */
public class GridViewFaceAdapter extends BaseAdapter {
	// 定义Context
	private Context mContext;
	// 定义整型数组 即图片源
	private static int[] mImageIds = new int[] { R.drawable.e1, R.drawable.e2, R.drawable.e3, R.drawable.e4, R.drawable.e5, R.drawable.e6,
			R.drawable.e7, R.drawable.e8, R.drawable.e9, R.drawable.e10, R.drawable.e11, R.drawable.e12, R.drawable.e13, R.drawable.e14,
			R.drawable.e15, R.drawable.e16, R.drawable.e17, R.drawable.e18, R.drawable.e19, R.drawable.e20, R.drawable.e21, R.drawable.e22,
			R.drawable.e23, R.drawable.e24, R.drawable.e25, R.drawable.e26, R.drawable.e27, R.drawable.e28, R.drawable.e29, };
	
	public static int[] getImageIds() {
		return mImageIds;
	}
	
	public GridViewFaceAdapter(Context c) {
		mContext = c;
	}
	
	// 获取图片的个数
	public int getCount() {
		return mImageIds.length;
	}
	
	// 获取图片在库中的位置
	public Object getItem(int position) {
		return position;
	}
	
	// 获取图片ID
	public long getItemId(int position) {
		return mImageIds[position];
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(mContext);

            int padding =   PhoneUtil.dip2px(mContext, 10);
			// 设置图片n×n显示
			imageView.setLayoutParams(new GridView.LayoutParams(PhoneUtil.dip2px(mContext, Constant.Chat.EMOTION_DIMEN)+padding+padding,
                    PhoneUtil.dip2px(
					mContext, Constant.Chat.EMOTION_DIMEN)+padding+padding));

            imageView.setPadding(padding,padding,padding,padding);
			// 设置显示比例类型
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		}
		else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageResource(mImageIds[position]);
		imageView.setTag(R.id.emotion_drawable_position, "[" + position + "]");
		imageView.setTag(R.id.emotion_string, EmotionUtils.EMOTIONS_STR[position]);
		
		return imageView;
	}
}