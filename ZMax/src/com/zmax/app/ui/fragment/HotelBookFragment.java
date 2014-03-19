package com.zmax.app.ui.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zmax.app.R;
import com.zmax.app.adapter.HotelBookListAdapter;
import com.zmax.app.ui.MainActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.widget.VerticalViewPager;
import com.zmax.app.widget.VerticalViewPager.OnPageChangeListener;

public class HotelBookFragment extends Fragment implements OnPageChangeListener,OnItemClickListener {

	private int mPosition = 0;

	protected View view;
	private RadioGroup rg_top_title;
	private Button btn_more;

	private HotelBookListAdapter adapter;

	private VerticalViewPager pager;
	private LinearLayout indicator;

	public HotelBookFragment() {
		this(R.color.white);
	}

	public HotelBookFragment(int mosition) {
		mPosition = 0;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			mPosition = savedInstanceState.getInt("mPosition");
		 

		view = inflater.inflate(R.layout.hotel_book_list, null);

		rg_top_title = (RadioGroup) view.findViewById(R.id.rg_top_title);

		rg_top_title.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.btn_hotel_book)
					switchFragment(new HotelBookFragment(R.color.red));
				else if (checkedId == R.id.btn_activities)
					switchFragment(new ActListFragment(R.color.red));
			}
		});

		btn_more = (Button) view.findViewById(R.id.btn_more);
		btn_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleMenu();
			}
		});

		pager = (VerticalViewPager) view.findViewById(R.id.vvp_hotel);
		indicator = (LinearLayout) view.findViewById(R.id.indicator);
		adapter = new HotelBookListAdapter(getActivity(),Constant.getHotelFalseDataView(getActivity(),inflater));
		pager.setAdapter(adapter);
		pager.setCurrentItem(mPosition);
		pager.setOnPageChangeListener(this);
		
		initPagerIndicator(Constant.getHotelFalseDataView(getActivity(),inflater ),
				indicator);
		return view;
	}

	private void initPagerIndicator(List<View> falseDataView,
			LinearLayout indicator) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		for (int i = 0; i < 10; i++) {
			View view = inflater.inflate(R.layout.vpager_indicator_item, null);
			((ImageView) view.findViewById(R.id.iv_img))
					.setBackgroundResource(R.drawable.ic_launcher);

			indicator.addView(view);

		}
		switchInidcator(0);
	}

	private void switchInidcator(int position) {
		mPosition=position;
		ImageView imageView;
		for (int i = 0; i < indicator.getChildCount(); i++) {
			imageView = (ImageView) indicator.getChildAt(i).findViewById(
					R.id.iv_img);
			imageView.setImageResource(R.drawable.ic_launcher);
		}
		imageView = (ImageView) indicator.getChildAt(position )
				.findViewById(R.id.iv_img);
		imageView.setImageResource(R.drawable.progress_drawable_);

	}

	private void switchFragment(Fragment fragment) {

		if (getActivity() == null)
			return;
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
		}
	}

	private void toggleMenu() {

		if (getActivity() == null)
			return;
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.toggle();
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mPosition", mPosition);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		
		switchInidcator(position);
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

}
