package com.zmax.app.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.zmax.app.R;
import com.zmax.app.adapter.CopyOfHotelBookListAdapter;
import com.zmax.app.manage.DataManage;
import com.zmax.app.model.Hotel;
import com.zmax.app.model.HotelList;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetHotelListTask;
import com.zmax.app.ui.WebViewActivity;
import com.zmax.app.ui.base.BaseSlidingFragmentActivity.HotelBookVisivleCallback;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.Constant.LOAD_STATE;
import com.zmax.app.utils.DateTimeUtils;
import com.zmax.app.utils.Log;
import com.zmax.app.utils.Utility;
import com.zmax.app.widget.DateChoiceDialog;
import com.zmax.app.widget.PreferenceManager;
import com.zmax.app.widget.VerticalViewPager;
import com.zmax.app.widget.VerticalViewPager.OnPageChangeListener;
import eu.inmite.android.lib.dialogs.ProgressDialogFragment;

import java.text.SimpleDateFormat;
import java.util.*;

public class HotelBookFragment extends Fragment implements OnPageChangeListener, OnItemClickListener,
		HotelBookVisivleCallback {

	public static LOAD_STATE state = LOAD_STATE.INITIAL;
	protected View view;
	private CopyOfHotelBookListAdapter adapter;
	private VerticalViewPager pager;
	private LinearLayout indicator;
	private GetHotelListTask getHotelListTask;
	private DialogFragment progressDialog;
	private TextView tv_begin_day;
	private TextView tv_begin_week_day;
	private TextView tv_begin_month;
	private TextView tv_end_day;
	private TextView tv_end_week_day;
	private TextView tv_end_month;
	private TextView tv_sel_duration;
	private RelativeLayout rl_date_sel_banner;

	public HotelBookFragment() {
		state = LOAD_STATE.INITIAL;
		Log.i("@@");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.hotel_book_list, null);
		tv_begin_day = (TextView) view.findViewById(R.id.tv_begin_day);
		tv_begin_week_day = (TextView) view.findViewById(R.id.tv_begin_week_day);
		tv_begin_month = (TextView) view.findViewById(R.id.tv_begin_month);
		tv_end_day = (TextView) view.findViewById(R.id.tv_end_day);
		tv_end_week_day = (TextView) view.findViewById(R.id.tv_end_week_day);
		tv_end_month = (TextView) view.findViewById(R.id.tv_end_month);
		tv_sel_duration = (TextView) view.findViewById(R.id.tv_sel_duration);
		rl_date_sel_banner = (RelativeLayout) view.findViewById(R.id.rl_date_sel_banner);
		rl_date_sel_banner.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				DateChoiceDialog dateChoiceDialog = new DateChoiceDialog(getActivity());
				dateChoiceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						intiDate(PreferenceManager.getStartCal(), PreferenceManager.getEndCal(),
								PreferenceManager.getSelDay());
					}
				});
				dateChoiceDialog.show();

			}
		});
		pager = (VerticalViewPager) view.findViewById(R.id.vvp_hotel);
		indicator = (LinearLayout) view.findViewById(R.id.indicator);
		adapter = new CopyOfHotelBookListAdapter(getActivity());
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(1);
		pager.setOnPageChangeListener(this);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("");

	}

	private void intiDate(Calendar startCal, Calendar endCal, int selDay) {
		tv_begin_day.setText("" + startCal.get(Calendar.DATE));
		tv_begin_week_day.setText("" + getDayOfWeek(startCal.get(Calendar.DAY_OF_WEEK)));
		tv_begin_month.setText(startCal.get(Calendar.MONTH) + 1 + "月");

		tv_end_day.setText("" + endCal.get(Calendar.DATE));
		tv_end_week_day.setText("" + getDayOfWeek(endCal.get(Calendar.DAY_OF_WEEK)));
		tv_end_month.setText(endCal.get(Calendar.MONTH) + 1 + "月");
		tv_sel_duration.setText(String.format("住%s晚", selDay));
	}

	private void initPreferDate() {
		PreferenceManager.setSelDay(1);
		Calendar startcal = Calendar.getInstance(Locale.CHINA);
		PreferenceManager.setStartCal(startcal);
		PreferenceManager.setStartDateStr(new SimpleDateFormat("yyyy-MM-dd").format(startcal.getTime()));

		Calendar endcal = Calendar.getInstance(Locale.CHINA);
		endcal.add(Calendar.DAY_OF_YEAR, 1);
		PreferenceManager.setEndCal(endcal);
		PreferenceManager.setEndDateStr(new SimpleDateFormat("yyyy-MM-dd").format(endcal.getTime()));
	}

	private String getDayOfWeek(int day_pos) {
		Log.i("@# day_pos              " + day_pos);
		String string = null;
		switch (day_pos) {
			case 2:
				string = "周一";
				break;
			case 3:
				string = "周二";
				break;
			case 4:
				string = "周三";
				break;
			case 5:
				string = "周四";
				break;
			case 6:
				string = "周五";
				break;
			case 7:
				string = "周六";
				break;
			case 1:
				string = "周日";
				break;

		}
		return string;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (progressDialog != null && progressDialog.getActivity() != null) progressDialog.dismissAllowingStateLoss();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i("");
		initPreferDate();
		intiDate(PreferenceManager.getStartCal(), PreferenceManager.getEndCal(), PreferenceManager.getSelDay());

		progressDialog = ProgressDialogFragment.createBuilder(getActivity(), getFragmentManager()).setMessage("正在加载中" +
				"...").setTitle("提示")
				.setCancelable(true).show();
		state = LOAD_STATE.LOADING;
		getHotelListTask = new GetHotelListTask(getActivity(), new GetHotelListTask.TaskCallBack() {
			@Override
			public void onCallBack(HotelList hotelList, HotelList upcomingHotelList) {
				if (getActivity() == null) return;
				if (progressDialog != null && progressDialog.getActivity() != null) progressDialog.dismissAllowingStateLoss();
				state = LOAD_STATE.SUCCESS;
				if (hotelList != null && hotelList.status == 200){
					List<Hotel> _hotelList = hotelList.hotels;
					List<Hotel> _upcomingHotelList = upcomingHotelList == null ? null : upcomingHotelList.hotels;
					initData(_hotelList, _upcomingHotelList);
					saveHotel(_hotelList, false);
					saveHotel(_upcomingHotelList, true);
					if (_hotelList == null || _hotelList.isEmpty()) Utility.toastNoMoreResult(getActivity());
				} else {
					if (!NetWorkHelper.checkNetState(getActivity())){
						Utility.toastNetworkFailed(getActivity());
					} else if (hotelList != null)
						Utility.toastResult(getActivity(), hotelList.message);
					else
						Utility.toastFailedResult(getActivity());

					List<Hotel> old = DataManage.getIndexHotellist4DB(false);
					if (old == null || old.size() == 0) state = LOAD_STATE.INITIAL;
					initData(old, DataManage.getIndexHotellist4DB(true));
				}
			}
		});
		getHotelListTask.execute(Constant.CUR_CITY, "1", "" + Constant.PER_NUM_GET_HOTELLIST);
	}


	private void initPagerIndicator(int size, LinearLayout indicator) {
		if (size <= 0) return;
		LayoutInflater inflater = getActivity().getLayoutInflater();
		for (int i = 0; i < size; i++) {
			View view = inflater.inflate(R.layout.vpager_indicator_item, null);
			((ImageView) view.findViewById(R.id.iv_img)).setImageResource(R.drawable.hotel_list_indicator_normal);
			indicator.addView(view);
		}
		switchInidcator(0);
	}

	private void switchInidcator(int position) {
		ImageView imageView;
		for (int i = 0; i < indicator.getChildCount(); i++) {
			imageView = (ImageView) indicator.getChildAt(i).findViewById(R.id.iv_img);
			imageView.setImageResource(R.drawable.hotel_list_indicator_normal);
		}
		imageView = (ImageView) indicator.getChildAt(position).findViewById(R.id.iv_img);
		imageView.setImageResource(R.drawable.hotel_list_indicator_activited);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		switchInidcator(position);
		// if (adapter.getItem(position) instanceof PagerAdapter.IPagerDisplay)
		// {
		// ((PagerAdapter.IPagerDisplay) adapter.getItem(position)).onDisplay();
		// }
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	public void onCallBack() {

		// getHotelListTask = new GetHotelListTask(getActivity(), new
		// GetHotelListTask.TaskCallBack() {
		//
		// @Override
		// public void onCallBack(HotelList hotelList, HotelList
		// upcomingHotelList) {
		// if (getActivity() == null) return;
		// if (hotelList != null && hotelList.status == 200 && hotelList.hotels
		// != null && !hotelList.hotels.isEmpty()) {
		// initData(hotelList.hotels, upcomingHotelList);
		// }
		// }
		// });
		// getHotelListTask.execute("武汉", "1", "10");

	}

	// private void initData(final List<Hotel> hotelList, List<Hotel>
	// upcomingHotelList) {
	// if (hotelList == null || hotelList.isEmpty()) return;
	// List<View> views = fromViews(hotelList, upcomingHotelList);
	// adapter.addViews(views);
	// initPagerIndicator(views, indicator);
	// pager.setCurrentItem(0);
	//
	// }
	private void initData(final List<Hotel> hotelList, List<Hotel> upcomingHotelList) {
		if (hotelList == null || hotelList.isEmpty()) return;
		// List<View> views = fromViews(hotelList, upcomingHotelList);
		// adapter.addViews(views);

		adapter.addAll(hotelList);
		if (upcomingHotelList == null || upcomingHotelList.isEmpty()){
			initPagerIndicator(hotelList.size(), indicator);
		} else {
			Hotel falseHotel = new Hotel();
			adapter.addItem(falseHotel, upcomingHotelList);
			initPagerIndicator(hotelList.size() + 1, indicator);
		}
		pager.setCurrentItem(0);

	}

	private void saveHotel(final List<Hotel> hotelList, final boolean isUpcoming) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				List<Hotel> tmpLists = hotelList;
				for (Hotel hotel : tmpLists) {
					hotel.isUpcoming = isUpcoming;
				}
				DataManage.saveIndexHotellist2DB(tmpLists, isUpcoming);
			}
		}).start();
	}

	public List<View> fromViews(List<Hotel> hotelList, List<Hotel> upcomingHotelList) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		List<View> mList = new ArrayList<View>();

		for (int i = 0; i < hotelList.size(); i++) {
			final Hotel hotel = hotelList.get(i);
			View view = inflater.inflate(R.layout.hotel_book_list_item, null);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), WebViewActivity.class);
					intent.setAction(Constant.WAP.ACTION_HOTEL);
					intent.putExtra(Constant.WAP.HOTEL_ID_KEY, hotel.pms_hotel_id);
					startActivity(intent);

				}
			});
			((TextView) view.findViewById(R.id.tv_title)).setText("" + hotel.name);
			view.setTag(hotel);
			// ImageLoader.getInstance().displayImage(hotel.poster, ((ImageView)
			// view.findViewById(R.id.iv_img)));
			mList.add(view);
		}
		if (upcomingHotelList != null && !upcomingHotelList.isEmpty()){
			mList.add(fromUpcomingViews(upcomingHotelList));
		}
		return mList;
	}

	public View fromUpcomingViews(List<Hotel> hotelList) {
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		for (int i = 0; i < hotelList.size(); i++) {
			Hotel hotel = hotelList.get(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put("date", DateTimeUtils.friendly_time(hotel.open_date));
			map.put("name", hotel.name);
			lists.add(map);
		}
		View view = getActivity().getLayoutInflater().inflate(R.layout.hotel_book_list_upcoming, null);
		ListView listView = (ListView) view.findViewById(R.id.list_view);
		SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), lists, R.layout.hotel_book_upcoming_list_item,
				new String[]{
						"date", "name"}, new int[]{R.id.tv_date, R.id.tv_name});
		View hint_view = getActivity().getLayoutInflater().inflate(R.layout.hotel_book_upcoming_more_hint, null);
		listView.addFooterView(hint_view);
		listView.setAdapter(simpleAdapter);

		return view;
	}
}
