package com.zmax.app.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.zmax.app.R;
import com.zmax.app.model.Hotel;
import com.zmax.app.ui.WebViewActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.Log;
import com.zmax.app.utils.Utility;
import com.zmax.app.widget.PagerAdapter;
import com.zmax.app.widget.PreferenceManager;

public class HotelBookItemFragment extends Fragment implements PagerAdapter.IPagerDisplay {
	Hotel hotel;
	ImageView iv_img;
	TextView tv_book;

	public static HotelBookItemFragment newInstance(Hotel hotel) {

		HotelBookItemFragment fragment = new HotelBookItemFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("hotel", hotel);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		hotel = (Hotel) getArguments().getSerializable("hotel");
		View view = inflater.inflate(R.layout.hotel_book_list_item, null);
		((TextView) view.findViewById(R.id.tv_title)).setText("" + hotel.name);
		iv_img = (ImageView) view.findViewById(R.id.iv_img);
		tv_book = (TextView) view.findViewById(R.id.tv_book);
		tv_book.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), WebViewActivity.class);
				intent.setAction(Constant.WAP.ACTION_HOTEL);
				intent.putExtra(Constant.WAP.HOTEL_ID_KEY, hotel.pms_hotel_id);
				intent.putExtra(Constant.WAP.FROM_DATE_KEY, PreferenceManager.getStartDateStr());
				intent.putExtra(Constant.WAP.TO_DATE_KEY, PreferenceManager.getEndDateStr());
				startActivity(intent);
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ImageLoader.getInstance().displayImage(Utility.getImgUrlOnDensity(getActivity(), hotel.poster), iv_img);

	}

	@Override
	public void onDisplay() {
		try {
			ImageLoader.getInstance().displayImage(hotel.poster, iv_img, new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					// TODO Auto-generated method stub
					if (failReason.getType().equals(FailReason.FailType.OUT_OF_MEMORY)){
						Log.i("[maxMemory]:  " + Runtime.getRuntime().maxMemory() / 1000 + " k");
						Log.i("[totalMemory]:  " + Runtime.getRuntime().totalMemory() / 1000 + " k");
						Log.i("[freeMemory]:   " + Runtime.getRuntime().freeMemory() / 1000 + " k");
						ImageLoader.getInstance().clearMemoryCache();
						System.gc();

						Log.i("after  [maxMemory]:  " + Runtime.getRuntime().maxMemory() / 1000 + " k");
						Log.i("after  [totalMemory]:  " + Runtime.getRuntime().totalMemory() / 1000 + " k");
						Log.i("after  [freeMemory]:   " + Runtime.getRuntime().freeMemory() / 1000 + " k");
					}

				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
