package com.zmax.app.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.ui.base.BaseActivity;
import com.zmax.app.utils.MDate;

/**
 * Created with IntelliJ IDEA. User: iron Date: 6/11/13 Time: 7:04 PM
 */
public class HotelDatePickActivity extends BaseActivity {

	public static final String TAG = "DateChoiceDialog";

	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM 月 yyyy");

	private String dateNumFormatter;

	private LayoutInflater inflater;

	private DateTextAdapter adapter;

	private ListView listView;
	private TextView title, monthText, tv_date_time;

	private View next, previous;

	private int startSel, days = 0;
	private Map<Integer, Integer> selections = new HashMap<Integer, Integer>();

	private static final int maxBookDays = 90;

	private int selDays = 1, startPos, endPos, scrollLines = 0, delta,
			touchStart;
	private int startDay, endDay;
	private float dpi;

	private int currentMonth;

	public int getDelta() {
		return delta;
	}

	public int getSelDays() {
		return selDays;
	}

	/*
	 * public DateChoiceDialog(Context context) { this(context,
	 * android.R.style.Theme_DeviceDefault_Light_Dialog); }
	 * 
	 * public DateChoiceDialog(Context context, int theme) { super(context,
	 * theme); initial(); }
	 */
	private void initial() {
		inflater = LayoutInflater.from(this);
		dateNumFormatter = getString(R.string.date_sel_text);
		adapter = new DateTextAdapter();
		setContentView(R.layout.hotel_date_pick);
		title = (TextView) findViewById(R.id.tv_title);
		monthText = (TextView) findViewById(R.id.date_month);
		tv_date_time = (TextView) findViewById(R.id.tv_date_time);
		(previous = findViewById(R.id.previous))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						skipMonth(false);
					}
				});
		(next = findViewById(R.id.next))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						skipMonth(true);
					}
				});
		listView = (ListView) findViewById(R.id.date_grid);
		listView.setAdapter(adapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initial();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		// setCanceledOnTouchOutside(true);
		dpi = metrics.density;
		listView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				int x = (int) (event.getX() / (v.getMeasuredWidth() / 7f));
				int y = (int) (event.getY() / (50f * dpi));
				// 范围限定
				if (x < 0)
					x = 0;
				if (x > 6)
					x = 6;
				if (y < 0)
					y = 0;
				if (y > 5)
					y = 5;

				switch (action) {
				case MotionEvent.ACTION_DOWN:
					touchStart = x + 7 * (y + scrollLines);
					if (x + 7 * (y + scrollLines) >= startDay
							& x + 7 * (y + scrollLines) <= endDay) {
						days = 0;
						startSel = x + 7 * (y + scrollLines);
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if (x + 7 * (y + scrollLines) >= startDay
							& x + 7 * (y + scrollLines) <= endDay) {
						if (touchStart < startDay)
							startSel = startDay;
						if (touchStart > endDay)
							startSel = endDay;
						days = x + 7 * (y + scrollLines) - startSel;
						if (days >= 0) {
							if (days > 30) {
								days = 30;
								setSelDayText(days);
								adapter.setStatus(startSel, days);
							} else {
								if (days >= 0) {
									setSelDayText(days);
									adapter.setStatus(startSel, days);
								}
								if (days != 0 & (x == 6 & y == 5))
									skip2Month(adapter.getCalendar(
											startSel + days)
											.get(Calendar.MONTH) + 1);
							}
						} else {
							if (days < -30) {
								days = -30;
								setSelDayText(-days);
								adapter.setStatus(startSel + days, -days);
							} else {
								if (days <= 0 & days > -30) {
									setSelDayText(-days);
									adapter.setStatus(startSel + days, -days);
								}
								if (days != 0 & (x == 0 & y == 0)) {
									if (adapter.getCalendar(startSel + days)
											.get(Calendar.MONTH) + 1 == currentMonth) {
										skip2Month(adapter.getCalendar(
												startSel + days).get(
												Calendar.MONTH));
									} else
										skip2Month(adapter.getCalendar(
												startSel + days).get(
												Calendar.MONTH) + 1);
								}
							}
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					if (days == 0) {
						days = 1;
						if (startSel < startDay)
							startSel = startDay;
						adapter.setStatus(startSel, 1);
						setSelDayText(1);
					}
					if (days > 0) {
						delta = startSel - startDay;
						selDays = days;
					} else {
						delta = startSel + days - startDay;
						selDays = -days;
					}
					break;
				}
				return true;
			}
		});

		findViewById(R.id.btn_share).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						tv_date_time.setText( "住"+getSelDays()+"晚" +"sel:"+ startSel );
						// Toast.makeText(DateChoiceDialog.this, "", toa)
					}
				});

		findViewById(R.id.btn_back).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		MDate date = new MDate();
		date.setDate(calendar);
		// 设置日期为当天状态
		setDateStatus(date);
	}

	private void setMonth(Calendar month) {
		monthText.setText(dateFormat.format(month.getTime()));
	}

	public void setDateStatus(MDate mDate) {
		// 设置时间状态
		// selDays = PreferenceManager.getDates().getDays();
		selDays = mDate.getDays();
		setSelDayText(selDays);
		// 系统当前时间
		Calendar current = Calendar.getInstance();
		current.setTime(new Date());
		Calendar[] cs = makeDates(current);
		parseSelection(cs);
		adapter.setDateTexts(cs);
		// delta = PreferenceManager.getDates().getDelt();
		delta = mDate.getDelt();
		adapter.setStatus(startPos + delta, selDays);
		// 获取起始日期
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, mDate.getDate().get(Calendar.YEAR));
		c.set(Calendar.MONTH, mDate.getDate().get(Calendar.MONTH));
		c.set(Calendar.DAY_OF_MONTH, mDate.getDate().get(Calendar.DAY_OF_MONTH));
		c.roll(Calendar.DAY_OF_YEAR, mDate.getDelt());
		setMonth(c);
		// 获取选择起始点所在的月份..
		currentMonth = c.get(Calendar.MONTH) + 1;
		previous.setEnabled(true);
		next.setEnabled(true);
		if (isStart())
			previous.setEnabled(false);
		if (isEnd())
			next.setEnabled(false);
		// 非空判定
		if (selections.get(currentMonth) != null)
			listView.setSelection(selections.get(currentMonth));
		// 起始的位置
		scrollLines = selections.get(currentMonth);
	}

	private void skipMonth(boolean isDown) {
		next.setEnabled(true);
		previous.setEnabled(true);
		int sels;
		if (isDown) {
			currentMonth++;
			if (currentMonth > 12)
				currentMonth = 1;
			sels = selections.get(currentMonth);
		} else {
			currentMonth--;
			if (currentMonth < 1)
				currentMonth = 12;
			sels = selections.get(currentMonth);
		}
		if (isEnd()) {
			next.setEnabled(false);
		}
		if (isStart()) {
			previous.setEnabled(false);
		}
		scrollLines = sels;
		setMonth(adapter.getItem(sels));
		adapter.notifyDataSetChanged();
		listView.setSelection(sels);
	}

	private void skip2Month(int month) {
		if (month < 1)
			month = 12;
		if (month > 12)
			month = 1;
		next.setEnabled(true);
		previous.setEnabled(true);
		currentMonth = month;
		if (isEnd()) {
			next.setEnabled(false);
		}
		if (isStart()) {
			previous.setEnabled(false);
		}
		int sels = selections.get(month);
		scrollLines = sels;
		setMonth(adapter.getItem(sels));
		adapter.notifyDataSetChanged();
		listView.setSelection(sels);
	}

	private void setSelDayText(int days) {
		String topTitle = String.format(dateNumFormatter, days + "");
		title.setText(topTitle);
	}

	private Calendar[] makeDates(Calendar date) {
		List<Calendar> calendarList = new ArrayList<Calendar>();
		// 找到这个月的第一天..
		Calendar temp = Calendar.getInstance();
		temp.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), 1);
		startDay = startPos = temp.get(Calendar.DAY_OF_WEEK)
				+ date.get(Calendar.DAY_OF_MONTH) - 2;
		// 向前填满一个月
		for (int i = startPos; i >= 0; i--) {
			Calendar c = Calendar.getInstance();
			c.setTime(date.getTime());
			c.roll(Calendar.DAY_OF_YEAR, -i);
			calendarList.add(c);
		}

		// int tempDay = 0;
		// 往后+90天
		for (int i = 1; i <= maxBookDays; i++) {
			Calendar c = Calendar.getInstance();
			c.setTime(date.getTime());
			c.roll(Calendar.DAY_OF_YEAR, i);
			// 如果需要进入新的年份
			calendarList.add(c);
			if (c.get(Calendar.MONTH) == 11
					&& c.get(Calendar.DAY_OF_MONTH) == 31) {
				// tempDay = i;
				c.roll(Calendar.YEAR, 1);
				break;
			}
		}

		// Calendar newYear = Calendar.getInstance();
		// newYear.set(date.get(Calendar.YEAR) + 1, 0, 1);
		// for (int i = 0; i <= maxBookDays - tempDay; i++) {
		// Calendar c = (Calendar) newYear.clone();
		// c.roll(Calendar.DAY_OF_YEAR, i);
		// calendarList.add(c);
		// }

		endDay = endPos = calendarList.size() - 1;
		Calendar end = calendarList.get(endPos);
		int week = end.get(Calendar.WEEK_OF_MONTH);
		// 追加天数..
		for (int i = 1; i < (7 - week) * 7
				+ (7 - end.get(Calendar.DAY_OF_WEEK)); i++) {
			Calendar c = Calendar.getInstance();
			c.setTime(end.getTime());
			c.roll(Calendar.DAY_OF_YEAR, i);
			calendarList.add(c);
		}

		return calendarList.toArray(new Calendar[calendarList.size()]);
	}

	// 计算各个月的头一天所在的行号并存储
	private void parseSelection(Calendar[] dates) {
		for (int i = 0; i <= endPos; i++) {
			if (dates[i].get(Calendar.DAY_OF_MONTH) == 1) {
				// Utils.log("month = " + (dates[i].get(Calendar.MONTH) + 1) +
				// " sel = " + i / 7);
				selections.put(dates[i].get(Calendar.MONTH) + 1, i / 7);
			}
		}
	}

	private boolean isStart() {
		int temp = currentMonth - 1;
		if (temp == 0)
			temp = 12;
		return selections.get(temp) == null;
	}

	private boolean isEnd() {
		int temp = currentMonth + 1;
		if (temp > 12)
			temp = 1;
		return selections.get(temp) == null;
	}

	private class DateTextAdapter extends BaseAdapter {

		private int startPos, endPos;

		private Calendar[] dateTexts;
		private boolean[] dateStatus;

		public DateTextAdapter() {
			dateTexts = new Calendar[0];
		}

		public void setStatus(int start, int len) {
			startPos = start;
			endPos = startPos + len;
			dateStatus = new boolean[dateTexts.length];
			for (int i = 0; i <= len; i++) {
				dateStatus[start + i] = true;
			}
			notifyDataSetChanged();
		}

		public void setDateTexts(Calendar[] dates) {
			dateTexts = dates;
			dateStatus = new boolean[dateTexts.length];
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return dateTexts.length % 7 == 0 ? dateTexts.length / 7 + 1
					: dateTexts.length / 7;
		}

		public Calendar getCalendar(int pos) {
			if (pos < dateTexts.length)
				return dateTexts[pos];
			else
				return null;
		}

		@Override
		public Calendar getItem(int position) {
			if (position * 7 + 6 < dateTexts.length)
				return dateTexts[position * 7 + 6];
			else
				return dateTexts[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = inflater.inflate(R.layout.hotel_item_date_text, null,
						false);
			}
			ViewGroup textGroup = (ViewGroup) convertView
					.findViewById(R.id.date_text_cnt);
			ViewGroup tagGrop = (ViewGroup) convertView
					.findViewById(R.id.date_tag_cnt);
			for (int i = 0; i < 7; i++) {
				CheckedTextView text = (CheckedTextView) textGroup
						.getChildAt(i);
				// 如果当前的位置小于起始点
				if (dateTexts.length - 1 > position * 7 + i) {
					text.setText(dateTexts[position * 7 + i]
							.get(Calendar.DAY_OF_MONTH) + "");
					if (position * 7 + i < startDay
							|| position * 7 + i > endDay) {
						text.setEnabled(false);
						text.setTextColor(getResources().getColor(
								R.color.unable_date_color));
					} else {
						text.setEnabled(true);
						if (dateTexts[position * 7 + i].get(Calendar.MONTH) == currentMonth - 1) {
							text.setTextColor(getResources().getColor(
									R.color.current_date_color));
						} else {
							text.setTextColor(getResources().getColor(
									R.color.overtime_date_color));
						}
					}
					text.setChecked(dateStatus[position * 7 + i]);
				}

				if (position * 7 + i == startPos) {
					tagGrop.getChildAt(i).setBackgroundResource(
							R.drawable.date_tag_start);
					tagGrop.getChildAt(i).setVisibility(View.VISIBLE);
				} else if (position * 7 + i == endPos) {
					tagGrop.getChildAt(i).setBackgroundResource(
							R.drawable.date_tag_end);
					tagGrop.getChildAt(i).setVisibility(View.VISIBLE);
				} else
					tagGrop.getChildAt(i).setVisibility(View.INVISIBLE);

			}
			return convertView;
		}

	}

}
