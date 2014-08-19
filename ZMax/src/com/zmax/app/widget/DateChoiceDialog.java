package com.zmax.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import com.zmax.app.R;
import com.zmax.app.utils.MDate;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: iron
 * Date: 6/11/13
 * Time: 7:04 PM
 */
public class DateChoiceDialog extends Dialog {

	public static final String TAG = "DateChoiceDialog";
	private static final int DATE_SIZE = 18;
	private static final int FESTIVAL_SIZE = 12;
	private static final int maxBookDays = 90;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM 月 yyyy");
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private String dateNumFormatter;
	private LayoutInflater inflater;
	private DateTextAdapter adapter;
	private ListView listView;
	private TextView title, monthText, warning;
	private View next, previous;
	private LinearLayout.LayoutParams layoutParams;
	private View layout;
	private int startSel;
	private Map<Integer, Integer> selections = new HashMap<Integer, Integer>();
	private Map<String, String[]> festival;
	private int selDays = 1, startPos, endPos, scrollLines = 0, delta;
	private int startDay, endDay;
	private float dpi;
	private boolean second = true;

	private int currentMonth;

	public DateChoiceDialog(Context context) {
		this(context, R.style.AppDialog_Bottom);
	}

	public DateChoiceDialog(Context context, int theme) {
		super(context, theme);
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		setCanceledOnTouchOutside(true);
		dpi = metrics.density;
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.x = 0;
		params.y = -1000;
		params.gravity = 80;
		getWindow().setAttributes(params);
		View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_date_sel, null);
		ViewGroup.LayoutParams conLay = new ViewGroup.LayoutParams(metrics.widthPixels,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		setContentView(contentView, conLay);
		festival = PreferenceManager.getFestivals();
		layoutParams = new LinearLayout.LayoutParams(metrics.widthPixels / 7, ViewGroup.LayoutParams.MATCH_PARENT);
		initial();
	}

	public int getDelta() {
		return delta;
	}

	public int getSelDays() {
		return selDays;
	}

	private void initial() {
		inflater = LayoutInflater.from(getContext());
		dateNumFormatter = getContext().getString(R.string.date_sel_text);
		adapter = new DateTextAdapter();
		title = (TextView) findViewById(R.id.date_sel_title);
		monthText = (TextView) findViewById(R.id.date_month);
		layout = findViewById(R.id.date_warning_layout);
		warning = (TextView) findViewById(R.id.date_warning_text);
		(previous = findViewById(R.id.previous)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				skipMonth(false);
			}
		});
		(next = findViewById(R.id.next)).setOnClickListener(new View.OnClickListener() {
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
		listView.setOnTouchListener(new View.OnTouchListener() {
			private float oldx;
			private float oldy;
			private boolean isMove = false;
			private boolean tag = false;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				int x = (int) (event.getX() / (v.getMeasuredWidth() / 7f));
				int y = (int) (event.getY() / (50f * dpi));
				// 范围限定
				if (x < 0) x = 0;
				if (x > 6) x = 6;
				if (y < 0) y = 0;
				if (y > 5) y = 5;
				int pos = x + 7 * (y + scrollLines);

				switch (action) {
					case MotionEvent.ACTION_DOWN:
						isMove = false;
						oldx = event.getX();
						oldy = event.getY();
						if (pos >= startDay && pos <= endDay + 1){
							startSel = x + 7 * (y + scrollLines);
						}
						break;
					case MotionEvent.ACTION_MOVE:
						// move暂时什么也不干
						if (!isMove){
							if (Math.abs(event.getX() - oldx) > 20 || Math.abs(event.getY() - oldy) > 20)
								isMove = true;
						}
						break;
					case MotionEvent.ACTION_UP:
						if (pos == startSel && !isMove){
							// 如果都选中了
							if (second){
								if (pos == endDay + 1){
									tag = false;
								} else {
									warning.setVisibility(View.GONE);
									warning.setText("无效时间");
									tag = true;
									second = false;
									startPos = pos;
									delta = startPos - startDay;
									endPos = -1;
									selDays = 0;
								}
							}
							if (tag && startPos > 0){
								if (pos > startPos && pos - startPos <= 30){
									warning.setVisibility(View.GONE);
									endPos = pos;
									selDays = endPos - startPos;
									second = true;
								} else {
									startPos = pos;
									delta = startPos - startDay;
								}
							}
							setSelDayText(selDays);
							adapter.setStatus(startPos, selDays);
						}
						break;
				}
				return true;
			}
		});

		findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (second)
					PreferenceManager.setDates(PreferenceManager.getMainDates(), getDelta(), getSelDays());
				dismiss();
			}
		});

		findViewById(R.id.date_warning_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 设置日期为当前状态
		setDateStatus(PreferenceManager.getMainDates());
		warning.setText("无效时间");
		layout.setVisibility(View.VISIBLE);
	}

	private void setMonth(Calendar month) {
		monthText.setText(dateFormat.format(month.getTime()));
	}

	public void setDateStatus(MDate mDate) {
		second = true;
		// 设置时间状态
		selDays = mDate.getDays();
		setSelDayText(selDays);
		// 系统当前时间
		Calendar current = Calendar.getInstance();
		current.setTime(new Date());
		Calendar[] cs = makeDates(current);
		parseSelection(cs);
		adapter.setDateTexts(cs);
		delta = mDate.getDelt();
		adapter.setStatus(startPos + delta, selDays);
		// 获取起始日期
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, mDate.getDate()
				.get(Calendar.YEAR));
		c.set(Calendar.MONTH, mDate.getDate()
				.get(Calendar.MONTH));
		c.set(Calendar.DAY_OF_MONTH
				, mDate.getDate()
				.get(Calendar.DAY_OF_MONTH));
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
		if (isDown){
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
		if (isEnd()){
			next.setEnabled(false);
		}
		if (isStart()){
			previous.setEnabled(false);
		}
		scrollLines = sels;
		setMonth(adapter.getItem(sels));
		adapter.notifyDataSetChanged();
		listView.setSelection(sels);
	}

//    private void skip2Month(int month) {
//        if (month < 1)
//            month = 12;
//        if (month > 12)
//            month = 1;
//        next.setEnabled(true);
//        previous.setEnabled(true);
//        currentMonth = month;
//        if (isEnd()) {
//            next.setEnabled(false);
//        }
//        if (isStart()) {
//            previous.setEnabled(false);
//        }
//        int sels = selections.get(month);
//        scrollLines = sels;
//        setMonth(adapter.getItem(sels));
//        adapter.notifyDataSetChanged();
//        listView.setSelection(sels);
//    }

	private void setSelDayText(int days) {

		title.setText(String.format("住 %s 晚",selDays));

	}

	private Calendar[] makeDates(Calendar date) {
		List<Calendar> calendarList = new ArrayList<Calendar>();
		// 找到这个月的第一天..
		Calendar temp = Calendar.getInstance();
		temp.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), 1);
		startDay = startPos = temp.get(Calendar.DAY_OF_WEEK) + date.get(Calendar.DAY_OF_MONTH) - 2;
		// 向前填满一个月
		for (int i = startPos; i >= 0; i--) {
			Calendar c = Calendar.getInstance();
			c.setTime(date.getTime());
			c.roll(Calendar.DAY_OF_YEAR, -i);
			calendarList.add(c);
		}

		// 往后+89天+当天正好90天
		for (int i = 1; i <= maxBookDays - 1; i++) {
			Calendar c = Calendar.getInstance();
			c.setTime(date.getTime());
			c.roll(Calendar.DAY_OF_YEAR, i);
			// 如果需要进入新的年份
			calendarList.add(c);
			if (c.get(Calendar.MONTH) == 11 && c.get(Calendar.DAY_OF_MONTH) == 31){
				c.roll(Calendar.YEAR, 1);
				break;
			}
		}

		endDay = endPos = calendarList.size() - 1;
		Calendar end = calendarList.get(endPos);
		int week = end.get(Calendar.WEEK_OF_MONTH);
		// 追加天数..   不足最后一个月 (或者不择可滚动的天数)
		for (int i = 1; i < (7 - week) * 7 + (7 - end.get(Calendar.DAY_OF_WEEK)); i++) {
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
			if (dates[i].get(Calendar.DAY_OF_MONTH) == 1){
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
			return dateTexts.length % 7 == 0 ? dateTexts.length / 7 + 1 : dateTexts.length / 7;
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
			if (null == convertView){
				convertView = inflater.inflate(R.layout.item_date_text, null, false);
			}
			ViewGroup textGroup = (ViewGroup) convertView.findViewById(R.id.date_text_cnt);
			ViewGroup extraText = (ViewGroup) convertView.findViewById(R.id.extra_layout);
			ViewGroup viewGroup = (ViewGroup) convertView.findViewById(R.id.extra_view_layout);
			ViewGroup bgGroup = (ViewGroup) convertView.findViewById(R.id.extra_bg_layout);
			for (int i = 0; i < 7; i++) {
				CheckedTextView text = (CheckedTextView) textGroup.getChildAt(i);
				text.setLayoutParams(layoutParams);
				// 如果当前的位置小于起始点
				if (dateTexts.length - 1 > position * 7 + i){
					String date = format.format(dateTexts[position * 7 + i].getTime());
					if (festival == null || festival.get(date) == null){
						text.setTextSize(TypedValue.COMPLEX_UNIT_SP, DATE_SIZE);
						text.setText(dateTexts[position * 7 + i].get(Calendar.DAY_OF_MONTH) + "");
					} else {
						text.setTextSize(TypedValue.COMPLEX_UNIT_SP, FESTIVAL_SIZE);
						text.setText(festival.get(date)[0]);
					}
 				if (position * 7 + i < startDay || position * 7 + i > endDay){
						text.setEnabled(false);
						text.setTextColor(getContext().getResources().getColor(R.color.unable_date_color));
					} else {
						text.setEnabled(true);
						if (dateTexts[position * 7 + i].get(Calendar.MONTH) == currentMonth - 1){
							text.setTextColor(getContext().getResources().getColor(R.color.current_date_color));
						} else {
							text.setTextColor(getContext().getResources().getColor(R.color.overtime_date_color));
						}
					}
					text.setChecked(dateStatus[position * 7 + i]);
				}
				((TextView) extraText.getChildAt(i)).setText("");
				viewGroup.getChildAt(i).setBackgroundResource(0);
				((ImageView) viewGroup.getChildAt(i)).setImageResource(0);
				((ImageView) bgGroup.getChildAt(i)).setImageResource(0);
				if (position * 7 + i == startPos){
					text.setTextSize(DATE_SIZE);
					text.setText(dateTexts[position * 7 + i].get(Calendar.DAY_OF_MONTH) + "");
					((ImageView) viewGroup.getChildAt(i)).setImageResource(R.drawable.date_tag_start);
					if (second)
						((ImageView) bgGroup.getChildAt(i)).setImageResource(R.drawable.date_tag_start);
					((TextView) textGroup.getChildAt(i)).setTextColor(Color.WHITE);
					((TextView) extraText.getChildAt(i)).setText("入住");
				} else if (position * 7 + i == endPos){
					text.setTextSize(DATE_SIZE);
					text.setText(dateTexts[position * 7 + i].get(Calendar.DAY_OF_MONTH) + "");
					((ImageView) viewGroup.getChildAt(i)).setImageResource(R.drawable.date_tag_end);
					((ImageView) bgGroup.getChildAt(i)).setImageResource(R.drawable.date_tag_end);
					((TextView) textGroup.getChildAt(i)).setTextColor(Color.WHITE);
					((TextView) extraText.getChildAt(i)).setText("离店");
				} else if (position * 7 + i < endPos && position * 7 + i > startPos){
					((ImageView) viewGroup.getChildAt(i)).setImageResource(R.drawable.date_sel_tag);
				} else {
					((ImageView) viewGroup.getChildAt(i)).setImageResource(0);
				}
			}
			return convertView;
		}
	}
}
